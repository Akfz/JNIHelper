package v.akfz.jnih.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.*;
import v.akfz.jnih.model.Platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public abstract class PackageNativeTask extends DefaultTask {

	@InputFiles
	@Optional
	public abstract ConfigurableFileCollection getJarLibraries();

	@InputFiles
	@Optional
	public abstract ConfigurableFileCollection getRunLibraries();

	@OutputDirectory
	public abstract DirectoryProperty getJarOutputDir();

	@OutputDirectory
	public abstract DirectoryProperty getRunOutputDir();

	@TaskAction
	public void packageNative() throws IOException {
		boolean hasJar = !getJarLibraries().isEmpty();
		boolean hasRun = !getRunLibraries().isEmpty();

		if (!hasJar && !hasRun) {
			getLogger().lifecycle("packageNative skipped (no libraries configured)");
			return;
		}

		if (hasJar) {
			packageForJar();
		}

		if (hasRun) {
			packageForRun();
		}
	}

	private void packageForJar() throws IOException {
		File outputDir = getJarOutputDir().getAsFile().get();
		getLogger().lifecycle("Packaging libraries for JAR...");

		for (File lib : getJarLibraries()) {
			if (!lib.exists()) {
				getLogger().warn("Library not found: " + lib);
				continue;
			}

			String ext = getExtension(lib.getName());
			Platform.Os os = Platform.detectFromExtension(ext);
			String folder = Platform.getFolderName(os, Platform.Arch.X86_64);

			File targetDir = new File(outputDir, folder);
			targetDir.mkdirs();

			File targetFile = new File(targetDir, lib.getName());
			Files.copy(lib.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

			getLogger().lifecycle("  " + folder + "/" + lib.getName());
		}
	}

	private void packageForRun() throws IOException {
		File outputDir = getRunOutputDir().getAsFile().get();
		String currentFolder = Platform.getCurrentFolder();

		getLogger().lifecycle("Packaging libraries for run (" + currentFolder + ")...");

		for (File lib : getRunLibraries()) {
			if (!lib.exists()) {
				getLogger().warn("Library not found: " + lib);
				continue;
			}

			String ext = getExtension(lib.getName());
			Platform.Os libOs = Platform.detectFromExtension(ext);
			Platform.Os currentOs = Platform.getCurrentOs();

			if (libOs != currentOs) {
				getLogger().lifecycle("  Skipped (not current OS): " + lib.getName());
				continue;
			}

			File targetDir = new File(outputDir, currentFolder);
			targetDir.mkdirs();

			File targetFile = new File(targetDir, lib.getName());
			Files.copy(lib.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

			if (currentOs != Platform.Os.WINDOWS) {
				targetFile.setExecutable(true);
			}

			getLogger().lifecycle("  " + currentFolder + "/" + lib.getName());
		}
	}

	private String getExtension(String filename) {
		int dot = filename.lastIndexOf('.');
		return dot > 0 ? filename.substring(dot) : "";
	}
}