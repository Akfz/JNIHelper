package v.akfz.jnih.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Deprecated(forRemoval = true)
public abstract class GenerateJniHeadersTask extends DefaultTask {

	@InputFiles
	public abstract ConfigurableFileCollection getSources();

	@OutputDirectory
	public abstract DirectoryProperty getOutputDir();

	@TaskAction
	public void generate() {
		File outputDir = getOutputDir().getAsFile().get();
		outputDir.mkdirs();

		List<String> command = new ArrayList<>();
		command.add("javac");
		command.add("-h");
		command.add(outputDir.getAbsolutePath());

		command.add("-d");
		command.add(
				getProject()
						.getLayout()
						.getBuildDirectory()
						.dir("tmp/jni-classes")
						.get()
						.getAsFile()
						.getAbsolutePath()
		);

		for (File source : getSources()) {
			command.add(source.getAbsolutePath());
		}

		try {
			Process process = new ProcessBuilder(command)
					.redirectErrorStream(true)
					.start();

			String output = new String(process.getInputStream().readAllBytes());
			int exitCode = process.waitFor();

			if (exitCode != 0) {
				throw new RuntimeException(
						"JNI header generation failed:\n" + output
				);
			}

			getLogger().lifecycle(
					"JNI headers generated in: " + outputDir
			);

		} catch (Exception e) {
			throw new RuntimeException("Failed to execute javac", e);
		}
	}
}