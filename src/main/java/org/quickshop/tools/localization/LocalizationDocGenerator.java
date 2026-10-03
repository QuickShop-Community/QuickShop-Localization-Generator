package org.quickshop.tools.localization;

import org.quickshop.tools.localization.cli.Cli;
import org.quickshop.tools.localization.model.*;
import org.quickshop.tools.localization.output.JsonOutputWriter;
import org.quickshop.tools.localization.parser.GuiParser;
import org.quickshop.tools.localization.parser.MetadataParser;
import org.quickshop.tools.localization.parser.TranslationParser;
import org.quickshop.tools.localization.scanner.JavaUsageScanner;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for generating localization documentation for the QuickShop project
 * by parsing and analyzing translation and usage data from various sources such as messages,
 * GUI, Java code, and metadata files. It also provides validation and reporting functionalities
 * to ensure proper localization coverage and correctness.
 *
 * The main features include:
 * - Parsing translations (messages.yml), GUI usage (gui.yml), and Java code references.
 * - Extracting placeholders from translations for completeness checks.
 * - Cross-referencing translation keys with their usage and documentation.
 * - Generating a comprehensive summary and warnings for missing elements or discrepancies.
 * - Outputting localization data and validation reports in the prescribed format.
 *
 * This class is not meant to be instantiated and is designed to be used through its static methods.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public final class LocalizationDocGenerator {

  private static final Pattern PLACEHOLDER = Pattern.compile("\\{(\\d+)}");
  private LocalizationDocGenerator() {}

  public static void main(final String[] args) throws Exception {

    final Cli cli = Cli.parse(args);
    if (cli.help()) {

      printHelp();
      return;
    }

    final Config config = cli.config();
    validateInputs(config);

    final Map<String, Translation> translations = new TranslationParser().parse(config.messagesFile());
    final Map<String, List<Usage>> guiUsages = new GuiParser().parse(config.guiFile());
    final Map<String, List<Usage>> javaUsages = new JavaUsageScanner().scan(config.repositoryRoot());
    final Map<String, Object> metadata = new MetadataParser().parse(config.metadataFile());

    final Set<String> allKeys = new LinkedHashSet<>(translations.keySet());

    allKeys.addAll(guiUsages.keySet()); allKeys.addAll(javaUsages.keySet());

    final List<Entry> entries = new ArrayList<>();
    final List<Warning> warnings = new ArrayList<>();
    for (final String key : allKeys.stream().sorted().toList()) {

      final Translation translation = translations.get(key);
      final List<Usage> usages = new ArrayList<>();

      usages.addAll(guiUsages.getOrDefault(key, List.of())); usages.addAll(javaUsages.getOrDefault(key, List.of()));
      usages.sort(Comparator.comparing(Usage::type).thenComparing(Usage::file).thenComparingInt(Usage::line));

      final List<String> placeholders = (translation == null)? List.of() : extractPlaceholders(translation.raw());

      final Object documentation = metadata.get(key);
      final List<Warning> entryWarnings = new ArrayList<>();

      if (translation == null) {

        entryWarnings.add(new Warning("MISSING_TRANSLATION", key, "The key is referenced by QuickShop but is missing from messages.yml."));
      }

      if (usages.isEmpty() && translation != null && !key.startsWith("_") && !key.contains("._")) {

        entryWarnings.add(new Warning("NO_DETECTED_USAGE", key, "No gui.yml or static Java text().of(...) usage was detected."));
      }

      if (documentation == null) {

        entryWarnings.add(new Warning("MISSING_DOCUMENTATION", key, "No metadata entry exists for this localization key."));
      }

      warnings.addAll(entryWarnings);

      entries.add(new Entry(key,
                            translation,
                            placeholders,
                            usages,
                            documentation,
                            new Status(translation != null,
                                       !usages.isEmpty(),
                                       guiUsages.containsKey(key),
                                       javaUsages.containsKey(key),
                                       documentation != null,
                                       placeholders.size()
                            ),
                            entryWarnings));
    }

    final Summary summary = new Summary(translations.size(),
                                        entries.size(),
                                        guiUsages.values().stream().mapToInt(List::size).sum(),
                                        javaUsages.values().stream().mapToInt(List::size).sum(),
                                        entries.stream().filter(e -> !e.placeholders().isEmpty()).count(),
                                        entries.stream().filter(e -> e.documentation() != null).count(),
                                        warnings.size());
    new JsonOutputWriter().write(config.outputDirectory(),
                                 new Index(config.repositoryName(),
                                           config.branch(),
                                           summary,
                                           entries
                                 ), new Report(summary, warnings));

    printSummary(config, summary);

    if (cli.command().equals("validate")) {

      final long errors = warnings.stream().filter(Warning::isError).count();
      if (errors > 0) {

        System.err.println("Validation failed with " + errors + " error(s).");
        System.exit(2);
      }
      System.out.println("Validation passed.");
    }
  }

  private static List<String> extractPlaceholders(final String text) {

    final Set<String> result = new LinkedHashSet<>();
    final Matcher matcher = PLACEHOLDER.matcher(text);
    while (matcher.find()) {

      result.add(matcher.group(1));
    }
    return result.stream().sorted(Comparator.comparingInt(Integer::parseInt)).toList();
  }

  private static void validateInputs(final Config config) {

    if (!Files.isDirectory(config.repositoryRoot())) {

      throw new IllegalArgumentException("Repository directory does not exist: " + config.repositoryRoot());
    }

    if (!Files.isRegularFile(config.messagesFile())) {

      throw new IllegalArgumentException("messages.yml does not exist: " + config.messagesFile());
    }

    if (!Files.isRegularFile(config.guiFile())) {

      throw new IllegalArgumentException("gui.yml does not exist: " + config.guiFile());
    }
  }

  private static void printSummary(final Config config, final Summary summary) {

    System.out.println("QuickShop Localization Index");
    System.out.println("Translations: " + summary.translations()); System.out.println("Indexed keys: " + summary.indexedKeys());
    System.out.println("GUI references: " + summary.guiReferences()); System.out.println("Java references: " + summary.javaReferences());
    System.out.println("Keys with placeholders: " + summary.keysWithPlaceholders()); System.out.println("Documented keys: " + summary.documentedKeys());
    System.out.println("Warnings: " + summary.warnings()); System.out.println("Generated: " + config.outputDirectory().toAbsolutePath());
  }

  private static void printHelp() {

    System.out.println("""
                QuickShop Localization Generator

                Usage:
                  java -jar quickshop-localization-generator-all.jar [generate|validate] [options]

                Options:
                  --repository <path>       QuickShop-Hikari checkout (default: ../QuickShop-Hikari)
                  --messages <path>         messages.yml path; relative paths resolve from repository
                  --gui <path>              gui.yml path; relative paths resolve from repository
                  --metadata <path>         optional metadata.yml; relative paths resolve from generator cwd
                  --output <path>           output directory (default: build/generated/localization)
                  --branch <name>           source branch label used in output (default: hikari)
                  --repository-name <name>  repository label (default: QuickShop-Community/QuickShop-Hikari)
                  --help                    show this help
                """);
  }
}
