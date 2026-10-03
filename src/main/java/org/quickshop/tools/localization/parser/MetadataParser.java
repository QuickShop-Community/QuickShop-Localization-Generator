package org.quickshop.tools.localization.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A parser for extracting metadata from YAML files.
 *
 * This class provides functionality to parse a YAML file located at a given file path
 * and return its content as a map. The keys and values in the YAML file are converted
 * to string-object pairs and stored in a LinkedHashMap while maintaining their order.
 *
 * If the file does not exist or cannot be parsed into a map structure, the method will
 * return an empty map. This ensures safe handling of nonexistent files or invalid data.
 *
 * The class is immutable and cannot be extended.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public final class MetadataParser {
    
    public Map<String, Object> parse(final Path file) throws IOException {
        
        if (!Files.exists(file)) {
            
            return Map.of();
        }
        
        final Object loaded = YamlSupport.load(file);
        if (!(loaded instanceof final Map<?, ?> map)) {
            
            return Map.of();
        }
        
        final Map<String, Object> result = new LinkedHashMap<>();
        map.forEach((key, value) -> result.put(String.valueOf(key), value));
        return result;
    }
}
