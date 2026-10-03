package org.quickshop.tools.localization.model;

import java.util.List;

/**
 * Represents an index of localization data within a repository.
 *
 * This record encapsulates information about a specific repository and branch,
 * alongside a summary of the localization statistics and a list of detailed entries.
 * It is a core structure used to represent the state and details of localized keys and their translations.
 *
 * @param repository The name of the repository containing the localization data.
 * @param branch The name of the branch from which the localization data is sourced.
 * @param summary A summary of localization statistics, such as the number of translations,
 *                indexed keys, and warnings.
 * @param entries A list of individual entries representing specific translation keys,
 *                their translations, usages, documentation, and associated warnings.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public record Index(String repository, String branch, Summary summary, List<Entry> entries) {

}
