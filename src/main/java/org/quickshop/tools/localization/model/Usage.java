package org.quickshop.tools.localization.model;

/**
 * Represents the usage of a localization key within a project.
 *
 * This record captures detailed information about where and how a specific localization
 * key is utilized, including references in code, configuration settings, and GUI elements.
 * It facilitates better understanding and tracing of localization dependencies and usage scenarios.
 *
 * @param type Specifies the type of usage (e.g., GUI, Java, Configuration).
 * @param file The file path where the usage is located.
 * @param line The line number in the file where the usage occurs.
 * @param className The name of the class where the usage is found, if applicable.
 * @param method The name of the method where the usage is found, if applicable.
 * @param gui The GUI element or file associated with the usage, if applicable.
 * @param configPath The configuration path or reference associated with the usage, if applicable.
 * @param property The property name associated with the usage, if applicable.
 * @param reference Additional references or notes about the usage context.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public record Usage(String type, String file, int line, String className, String method, String gui, String configPath, String property, String reference) {

}
