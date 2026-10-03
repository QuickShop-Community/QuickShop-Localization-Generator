package org.quickshop.tools.localization.model;

import java.util.List;

/**
 * Represents a single entry in the localization system.
 *
 * This record is a central part of the system, capturing details about
 * a specific translation key, its associated translation, placeholders,
 * usages, documentation, status, and any warnings to account for potential
 * issues or inconsistencies.
 *
 * @param key The unique key identifying this entry in the localization system.
 * @param translation The translation associated with this key, including raw text, plain text, and value type information.
 * @param placeholders A list of placeholder variable names used in the translation text.
 * @param usages A list of details about where and how this key is utilized within the project, such as in GUI or Java code.
 * @param documentation Additional documentation or metadata related to this key.
 * @param status The status of this entry, summarizing information such as translation presence, usage status, and placeholder count.
 * @param warnings A list of warnings related to the entry, describing potential issues such as missing translations or usage problems.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public record Entry(String key, Translation translation, List<String> placeholders, List<Usage> usages, Object documentation, Status status, List<Warning> warnings) {

}
