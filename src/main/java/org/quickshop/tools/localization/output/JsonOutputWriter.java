package org.quickshop.tools.localization.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.quickshop.tools.localization.model.Index;
import org.quickshop.tools.localization.model.Report;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A utility class responsible for writing JSON output files for localization data.
 *
 * This class generates and writes two JSON files to the specified output directory:
 * 1. `localization.json`: A representation of the {@link Index} object, which contains
 *    detailed localization data such as repository information, branch, statistics, and entries.
 * 2. `localization-report.json`: A representation of the {@link Report} object, which provides
 *    a summary of localization statistics and a list of warnings about potential issues.
 *
 * The JSON output is formatted for readability with pretty printing enabled, and
 * HTML characters are not escaped in the output.
 *
 * This class ensures that the output directory exists, creating it if necessary, and
 * writes the JSON files using UTF-8 encoding.
 *
 * The main responsibilities of this class are:
 * - Serializing localization data and associated report information into JSON.
 * - Handling file I/O for writing JSON data to the disk.
 *
 * It utilizes the Gson library for JSON serialization, with the configuration of:
 * - Pretty printing for better readability.
 * - Disabled HTML escaping to preserve characters as-is in the JSON output.
 *
 * The method {@link #write(Path, Index, Report)} is the entry point for generating
 * the JSON files, taking as input the output directory, an {@link Index} object, and
 * a {@link Report} object.
 *
 * @author creatorfromhell
 * @since 0.1.0
 */
public class JsonOutputWriter {

  private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

  public void write(final Path outputDirectory, final Index index, final Report report) throws IOException {

    Files.createDirectories(outputDirectory);
    Files.writeString(outputDirectory.resolve("localization.json"), gson.toJson(index), StandardCharsets.UTF_8);
    Files.writeString(outputDirectory.resolve("localization-report.json"), gson.toJson(report), StandardCharsets.UTF_8);
  }
}
