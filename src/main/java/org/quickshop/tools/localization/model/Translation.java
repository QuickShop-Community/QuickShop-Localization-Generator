package org.quickshop.tools.localization.model;

/**
 * Represents a translation entry with various formats and metadata about its value type.
 *
 * This record is used as a core structure in the localization system to encapsulate
 * the raw and plain text of a translation, along with its type. It helps in managing
 * and processing translations effectively by providing clarity on the format of the
 * translation text and its intended usage.
 *
 * @param raw The raw text of the translation, potentially including placeholders or formatting details.
 * @param plain The plain text of the translation, with placeholders or formatting removed.
 * @param valueType Metadata indicating the type of value represented by the translation, such as text, number, or date.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public record Translation(String raw, String plain, String valueType) {

}
