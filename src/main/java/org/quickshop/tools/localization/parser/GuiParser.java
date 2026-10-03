package org.quickshop.tools.localization.parser;

import org.quickshop.tools.localization.model.Usage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A parser for GUI configuration files in YAML format that extracts and organizes localization key usages.
 *
 * This class processes YAML configuration files, identifies localization keys prefixed with "lang:",
 * and collects usage information in a structured format. The extracted data includes details like
 * file paths, line numbers, and YAML path segments associated with the localization keys,
 * enabling better management and tracking of localization references.
 *
 * The class is immutable and cannot be extended.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public final class GuiParser {
    
    public Map<String, List<Usage>> parse(final Path guiFile) throws IOException {
        
        final Map<String, List<Usage>> result = new LinkedHashMap<>();
        scan(YamlSupport.load(guiFile), "", guiFile, result);
        return result;
    }
    
    private void scan(final Object value, final String yamlPath, final Path file, final Map<String, List<Usage>> result) throws IOException {
        
        if (value instanceof final Map<?, ?> map) {
            
            for (final Map.Entry<?, ?> entry : map.entrySet()) {
                
                final String child = (yamlPath.isEmpty())? String.valueOf(entry.getKey()) : yamlPath + "." + entry.getKey();
                scan(entry.getValue(), child, file, result);
            }
            return;
        }
        
        if (value instanceof final List<?> list) {
            
            for (int i = 0; i < list.size(); i++) {
                
                scan(list.get(i), yamlPath + "[" + i + "]", file, result);
            }
            return;
        }
        
        if (!(value instanceof final String text) || !text.startsWith("lang:")) return;
        
        final String key = text.substring("lang:".length()).trim();
        result.computeIfAbsent(key, ignored -> new ArrayList<>()).add(new Usage("gui",
                                                                                relative(file),
                                                                                findLine(file, text),
                                                                                null,
                                                                                null,
                                                                                firstPathSegment(yamlPath),
                                                                                yamlPath,
                                                                                lastPathSegment(yamlPath),
                                                                                text));
    }
    
    private int findLine(final Path file, final String exactValue) throws IOException {
        
        final List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            
            if (lines.get(i).contains(exactValue)) {

                return i + 1;
            }
        }
        return -1;
    }
    
    private String firstPathSegment(final String path) {
        
        final int dot = path.indexOf('.');
        final int bracket = path.indexOf('[');
        int end = (dot < 0)? path.length() : dot;
        if (bracket >= 0) {

            end = Math.min(end, bracket);
        }

        return path.substring(0, end);
    }
    
    private String lastPathSegment(final String path) {
        
        final String normalized = path.replaceAll("\\[\\d+]$", "");
        final int dot = normalized.lastIndexOf('.');
        return (dot < 0)? normalized : normalized.substring(dot + 1);
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
