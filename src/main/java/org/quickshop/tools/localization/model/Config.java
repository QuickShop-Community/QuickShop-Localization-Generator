package org.quickshop.tools.localization.model;

import java.nio.file.Path;

/**
 * Represents the configuration settings used by the application.
 * This record contains essential paths and metadata required to
 * operate on various resources such as messages, GUI files,
 * metadata, and output directory, as well as repository details.
 *
 * @param repositoryRoot The root directory of the repository.
 * @param messagesFile The path to the messages file.
 * @param guiFile The path to the GUI file.
 * @param metadataFile The path to the metadata file.
 * @param outputDirectory The directory where output files will be generated.
 * @param branch The name of the branch being used in the repository.
 * @param repositoryName The full name of the repository, typically including the owner and repository name.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public record Config(Path repositoryRoot, Path messagesFile, Path guiFile, Path metadataFile, Path outputDirectory, String branch, String repositoryName) {

}
