package v.akfz.jnih.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class GenerateJniHeadersTask extends DefaultTask {

	@InputFiles
	@Optional
	public abstract ConfigurableFileCollection getClasspath();

	@OutputDirectory
	public abstract DirectoryProperty getOutputDir();

	@TaskAction
	public void generate() {
		File outputDir = getOutputDir().getAsFile().get();
		outputDir.mkdirs();

		List<File> inputFiles = new ArrayList<>();
		for (File file : getClasspath()) {
			if (file.isDirectory()) {
				collectClassFiles(file, inputFiles);
			} else if (file.getName().endsWith(".class")) {
				inputFiles.add(file);
			}
		}

		if (inputFiles.isEmpty()) {
			getLogger().warn("Classes for JNI header generation not found");
			return;
		}

		List<String> command = new ArrayList<>();
		command.add("javac");
		command.add("-h");
		command.add(outputDir.getAbsolutePath());
		command.add("-d");
		command.add(getProject().getLayout().getBuildDirectory().dir("tmp/jni-classes").get().getAsFile().getAbsolutePath());

		for (File f : inputFiles) {
			command.add(f.getAbsolutePath());
		}

		getLogger().lifecycle("Generating JNI headers...");

		try {
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectErrorStream(true);
			Process process = pb.start();

			String output = new String(process.getInputStream().readAllBytes());
			int exitCode = process.waitFor();

			if (exitCode == 0) {
				getLogger().lifecycle("Headers generated in: " + outputDir);
			} else {
				getLogger().error("Generation error:\n" + output);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to execute javac", e);
		}
	}

	private void collectClassFiles(File dir, List<File> result) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					collectClassFiles(file, result);
				} else if (file.getName().endsWith(".class")) {
					result.add(file);
				}
			}
		}
	}
}