package org.quickshop.tools.localization.model;

/**
 * Represents the status of a localization entry, summarizing its translation presence,
 * usage across various contexts, documentation status, and placeholder details.
 *
 * @param hasTranslation Indicates whether a translation exists for the corresponding localization key.
 * @param used Indicates whether the localization key is utilized within the project.
 * @param hasGuiUsage Specifies if the key is referenced within GUI elements or files.
 * @param hasJavaUsage Specifies if the key is referenced within Java code.
 * @param documented Indicates whether the localization key is accompanied by documentation.
 * @param placeholderCount The number of placeholders present in the translation associated with the key.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public record Status(boolean hasTranslation, boolean used, boolean hasGuiUsage, boolean hasJavaUsage, boolean documented, int placeholderCount) {

}
