package org.quickshop.tools.localization.cli;

import org.quickshop.tools.localization.model.Config;

import java.nio.file.Path;

public record Cli(String command, Config config, boolean help) {

  public static Cli parse(final String[] args) {

    String command = "generate";
    int index = 0;

    if (args.length > 0 && !args[0].startsWith("--")) {

      command = args[0]; index = 1;
    }

    if (!command.equals("generate") && !command.equals("validate")) {

      throw new IllegalArgumentException("Unknown command: " + command);
    }

    final Path cwd = Path.of("").toAbsolutePath().normalize();
    Path repository = cwd.resolve("../QuickShop-Hikari").normalize();
    String messagesArg = "quickshop-bukkit/src/main/resources/lang/messages.yml";
    String guiArg = "quickshop-bukkit/src/main/resources/gui.yml";
    Path metadata = cwd.resolve("localization-docs/metadata.yml");
    Path output = cwd.resolve("build/generated/localization");
    String branch = "hikari";
    String repositoryName = "QuickShop-Community/QuickShop-Hikari";
    boolean help = false;

    while (index < args.length) {

      final String option = args[index++];
      if (option.equals("--help") || option.equals("-h")) {

        help = true;
        continue;
      }

      if (index >= args.length) {

        throw new IllegalArgumentException("Missing value for " + option);
      }

      final String value = args[index++];
      switch (option) {
        case "--repository" -> repository = Path.of(value).toAbsolutePath().normalize();
        case "--messages" -> messagesArg = value;
        case "--gui" -> guiArg = value;
        case "--metadata" -> metadata = Path.of(value).toAbsolutePath().normalize();
        case "--output" -> output = Path.of(value).toAbsolutePath().normalize();
        case "--branch" -> branch = value;
        case "--repository-name" -> repositoryName = value;
        default -> throw new IllegalArgumentException("Unknown option: " + option);
      }
    }

    return new Cli(command,
                   new Config(repository,
                              resolveFrom(repository, messagesArg),
                              resolveFrom(repository, guiArg),
                              metadata,
                              output,
                              branch,
                              repositoryName
                   ),
                   help);
  }

  private static Path resolveFrom(final Path root, final String value) {

    final Path path = Path.of(value);
    return (path.isAbsolute())? path.normalize() : root.resolve(path).normalize();
  }
}
