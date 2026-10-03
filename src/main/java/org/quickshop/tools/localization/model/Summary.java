package org.quickshop.tools.localization.model;

/**
 * Represents a summary of localization statistics for a project.
 *
 * This record encapsulates various aggregated metrics that provide
 * an overview of the localization state, including counts of translations,
 * indexed keys, references in GUI and Java code, keys with placeholders,
 * documented keys, and warnings.
 *
 * @param translations The total number of translations present.
 * @param indexedKeys The total number of unique localization keys indexed.
 * @param guiReferences The number of references to localization keys within GUI files.
 * @param javaReferences The number of references to localization keys within Java code.
 * @param keysWithPlaceholders The count of localization keys containing placeholders.
 * @param documentedKeys The number of localization keys accompanied by documentation.
 * @param warnings The total count of warnings associated with localization issues.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public record Summary(int translations, int indexedKeys, int guiReferences, int javaReferences, long keysWithPlaceholders, long documentedKeys, int warnings) {

}
