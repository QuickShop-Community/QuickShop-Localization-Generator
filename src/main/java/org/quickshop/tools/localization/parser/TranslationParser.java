package org.quickshop.tools.localization.parser;

import org.quickshop.tools.localization.model.Translation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A utility class for parsing and flattening YAML-based translation files into a Map structure.
 *
 * This class is designed to process localization files, extracting and transforming their contents
 * into a flat key-value structure where each key represents a unique path to a translation entry
 * and its associated metadata. The class also strips formatting tags based on MiniMessage syntax
 * when handling translation values.
 *
 * This class is immutable and thread-safe when used in a read-only fashion.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public final class TranslationParser {
    
    private static final Pattern MINI_MESSAGE_TAG = Pattern.compile("</?[^>]+>");
    
    public Map<String, Translation> parse(final Path file) throws IOException {
        
        final Map<String, Translation> result = new LinkedHashMap<>();
        flatten("", YamlSupport.load(file), result);
        return result;
    }
    
    private void flatten(final String path, final Object value, final Map<String, Translation> result) {
        
        if (value instanceof final Map<?, ?> map) {
            
            for (final Map.Entry<?, ?> entry : map.entrySet()) {
                
                final String child = (path.isEmpty())? String.valueOf(entry.getKey()) : path + "." + entry.getKey();
                flatten(child, entry.getValue(), result);
            }
            return;
        }
        
        if (value instanceof final Collection<?> collection) {
            
            final List<String> lines = collection.stream().map(String::valueOf).toList();
            final String raw = String.join("\n", lines);
            result.put(path, new Translation(raw, stripMiniMessage(raw), "list"));
            return;
        }
        
        final String raw = value == null? "" : String.valueOf(value);
        result.put(path, new Translation(raw, stripMiniMessage(raw), "scalar"));
    }
    
    private String stripMiniMessage(final String text) {
        
        return MINI_MESSAGE_TAG.matcher(text).replaceAll("");
    }
}
