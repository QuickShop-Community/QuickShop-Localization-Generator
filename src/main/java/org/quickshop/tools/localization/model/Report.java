package org.quickshop.tools.localization.model;

import java.util.List;

/**
 * Represents a report containing a summary of localization statistics
 * and a list of warnings related to potential issues.
 *
 * This record is used to encapsulate detailed information about the state
 * of localization, including an aggregated summary and warnings for specific
 * keys, providing insights into translation quality, usage, and errors.
 *
 * @param summary A summary of localization data, including counts of translations,
 *                indexed keys, GUI and Java references, keys with placeholders,
 *                documented keys, and warnings.
 * @param warnings A list of warnings identifying potential issues, such as
 *                 missing translations or other localization anomalies.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public record Report(Summary summary, List<Warning> warnings) {

}
