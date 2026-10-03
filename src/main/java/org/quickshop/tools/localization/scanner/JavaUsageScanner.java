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

  private static final Pattern TEXT_OF_CALL = Pattern.compile("(?:\\.text\\(\\)|\\btext\\(\\))\\s*\\.of\\s*\\(\\s*[^,]+,\\s*\\\"([^\\\"]+)\\\"", Pattern.DOTALL);

  public Map<String, List<Usage>> scan(final Path repositoryRoot) throws IOException {

    final Map<String, List<Usage>> result = new LinkedHashMap<>();
    try (final Stream<Path> stream = Files.walk(repositoryRoot)) {

      for (final Path file : stream.filter(p -> p.toString().endsWith(".java"))
              .filter(p -> !p.toString().contains("/build/"))
              .filter(p -> !p.toString().contains("\\build\\"))
              .toList()) {

        final String source = Files.readString(file, StandardCharsets.UTF_8);
        final Matcher matcher = TEXT_OF_CALL.matcher(source);
        while (matcher.find()) {

          final String key = matcher.group(1);
          final String className = file.getFileName().toString().replaceFirst("\\.java$", "");
          result.computeIfAbsent(key, ignored -> new ArrayList<>()).add(new Usage("java",
                                                                                  relative(file),
                                                                                  lineNumber(source, matcher.start(1)),
                                                                                  className,
                                                                                  null,
                                                                                  null,
                                                                                  null,
                                                                                  null,
                                                                                  null));
        }
      }
    }
    return result;
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

  private String relative(final Path file) {

    final Path cwd = Path.of("").toAbsolutePath().normalize();
    final Path absolute = file.toAbsolutePath().normalize();
    try {

      return cwd.relativize(absolute).toString().replace('\\', '/');
    } catch (final IllegalArgumentException ignored) {

      return absolute.toString().replace('\\', '/');
    }
  }
}
