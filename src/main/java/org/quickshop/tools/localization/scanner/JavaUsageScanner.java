package org.quickshop.tools.localization.scanner;

import org.quickshop.tools.localization.model.Usage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A utility class that scans Java source code files for specific patterns within a given repository
 * and collects information about their occurrences.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public final class JavaUsageScanner {

  private static final Pattern TEXT_OF_CALL = Pattern.compile("(?:\\.text\\(\\)|\\btext\\(\\))\\s*\\.of\\s*\\(\\s*(?:[^,]+,\\s*)?\\\"([^\\\"]+)\\\"", Pattern.DOTALL);
  private static final Pattern TEXT_OF_LIST_CALL = Pattern.compile("(?:\\.text\\(\\)|\\btext\\(\\)|\\btext)\\s*\\.ofList\\s*\\(\\s*(?:[^,]+,\\s*)?\\\"([^\\\"]+)\\\"", Pattern.DOTALL);
  private static final Pattern DISCORD_EMBED_MESSAGE_CALL = Pattern.compile("\\bgetEmbedMessage\\s*\\(\\s*\\\"([^\\\"]+)\\\"", Pattern.DOTALL);
  private static final Pattern TEXT_MANAGER_OF_CALL = Pattern.compile("\\.getTextManager\\(\\)\\s*\\.of\\s*\\(\\s*[^,]+,\\s*\\\"([^\\\"]+)\\\"", Pattern.DOTALL);
  private static final Pattern SHOP_CHECK_RESULT_FAIL_CALL = Pattern.compile("\\bShopCheckResult\\s*\\.\\s*fail\\s*\\(\\s*\\\"([^\\\"]+)\\\"", Pattern.DOTALL);
  private static final Pattern GUI_MESSAGE_CALL = Pattern.compile("\\bguiMessage\\s*\\(\\s*\\\"([^\\\"]+)\\\"", Pattern.DOTALL);
  private static final Pattern TAB_COMPLETE_HINT_CALL = Pattern.compile("\\btabCompleteHint\\s*\\(\\s*[^,]+,\\s*\\\"([^\\\"]+)\\\"", Pattern.DOTALL);

  private static final String GUI_MESSAGE_PREFIX = "gui.messages.";
  private static final String DISCORD_MESSAGE_PREFIX = "addon.discord.discord-messages.";

  public Map<String, List<Usage>> scan(final Path repositoryRoot) throws IOException {

    final Map<String, List<Usage>> result = new LinkedHashMap<>();
    try (final Stream<Path> stream = Files.walk(repositoryRoot)) {

      for (final Path file : stream.filter(p -> p.toString().endsWith(".java"))
              .filter(p -> !p.toString().contains("/build/"))
              .filter(p -> !p.toString().contains("\\build\\"))
              .toList()) {

        final String source = Files.readString(file, StandardCharsets.UTF_8);

        scanPattern(result, repositoryRoot, file, source, TEXT_OF_CALL, null);
        scanPattern(result, repositoryRoot, file, source, TEXT_OF_LIST_CALL, null);
        scanPattern(result, repositoryRoot, file, source, TEXT_MANAGER_OF_CALL, null);
        scanPattern(result, repositoryRoot, file, source, GUI_MESSAGE_CALL, GUI_MESSAGE_PREFIX);
        scanPattern(result, repositoryRoot, file, source, SHOP_CHECK_RESULT_FAIL_CALL, null);
        scanPattern(result, repositoryRoot, file, source, TAB_COMPLETE_HINT_CALL, null);
        scanPattern(result, repositoryRoot, file, source, DISCORD_EMBED_MESSAGE_CALL, DISCORD_MESSAGE_PREFIX);
      }
    }
    return result;
  }

  private void scanPattern(final Map<String, List<Usage>> result,
                           final Path repositoryRoot,
                           final Path file,
                           final String source,
                           final Pattern pattern,
                           final String prefix) {

    final Matcher matcher = pattern.matcher(source);
    while (matcher.find()) {

      final String key = ((prefix == null)? "" : prefix) + matcher.group(1);
      final String className = file.getFileName().toString().replaceFirst("\\.java$", "");
      final Usage usage = new Usage("java",
                                    relative(repositoryRoot, file),
                                    lineNumber(source, matcher.start(1)),
                                    className,
                                    null,
                                    null,
                                    null,
                                    null,
                                    matcher.group());

      final List<Usage> usages = result.computeIfAbsent(key, ignored -> new ArrayList<>());
      if (usages.stream().noneMatch(existing -> existing.file().equals(usage.file())
                                                && existing.line() == usage.line()
                                                && existing.reference().equals(usage.reference()))) {

        usages.add(usage);
      }
    }
  }

  private int lineNumber(final String source, final int offset) {

    int line = 1;
    for (int i = 0; i < offset; i++) {

      if (source.charAt(i) == '\n') {

        line++;
      }
    }
    return line;
  }

  private String relative(final Path repositoryRoot, final Path file) {

    final Path root = repositoryRoot.toAbsolutePath().normalize();
    final Path absolute = file.toAbsolutePath().normalize();
    try {

      return root.relativize(absolute).toString().replace('\\', '/');
    } catch (final IllegalArgumentException ignored) {

      return absolute.toString().replace('\\', '/');
    }
  }
}