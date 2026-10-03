package org.quickshop.tools.localization.model;

/**
 * Represents a warning associated with a localization entry.
 *
 * This record is used to capture details about potential issues or inconsistencies in the localization process.
 * Each warning contains information about its type, the specific key it pertains to, and an associated message
 * describing the issue.
 *
 * @param type The type of the warning, indicating the category or nature of the problem (e.g., MISSING_TRANSLATION).
 * @param key The specific localization key associated with this warning.
 * @param message A message explaining the details of the warning.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public record Warning(String type, String key, String message) {
    
    public boolean isError() {
        
        return type.equals("MISSING_TRANSLATION");
    }
}
