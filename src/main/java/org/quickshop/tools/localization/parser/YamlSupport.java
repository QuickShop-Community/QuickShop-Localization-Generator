package org.quickshop.tools.localization.parser;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A utility class for loading YAML files.
 *
 * This class provides a method for loading the contents of a YAML file
 * into a Java object using a provided file path. The method reads the
 * file with UTF-8 encoding and parses its content using SnakeYAML.
 *
 * The class is immutable and cannot be instantiated.
 *
 * Thread safety is not guaranteed; external synchronization may be needed
 * for concurrent usage.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
final class YamlSupport {
    
    private YamlSupport() {}
    
    static Object load(final Path file) throws IOException {
        
        try (final Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            
            return new Yaml().load(reader);
        }
    }
}
