package v.akfz.jnih;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.compile.JavaCompile;
import v.akfz.jnih.model.Platform;
import v.akfz.jnih.tasks.NativeInfoTask;
import v.akfz.jnih.tasks.PackageNativeTask;

import java.io.File;

public class JniHelperPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		JniHelperExtension extension = project.getExtensions()
				.create("jni", JniHelperExtension.class, project);

		project.getTasks().withType(JavaCompile.class).configureEach(task -> {
			task.getOptions().getCompilerArgs().add("-h");
			task.getOptions().getCompilerArgs().add(
					extension.getHeaderOutputDir()
							.get().getAsFile().getAbsolutePath()
			);
		});

		project.getTasks().register("packageNative", PackageNativeTask.class, task -> {
			task.setGroup("jni");
			task.setDescription("Packages native libraries");

			task.getJarLibraries().from(extension.getJar().getLibraries());
			task.getRunLibraries().from(extension.getRun().getLibraries());

			task.getJarOutputDir().set(
					project.getLayout().getBuildDirectory()
							.dir("resources/main/native")
			);

			task.getRunOutputDir().set(
					project.getLayout().getBuildDirectory()
							.dir("native-run")
			);
		});

		project.getTasks().named("processResources").configure(task ->
				task.dependsOn("packageNative")
		);

		project.getTasks().register("nativeInfo", NativeInfoTask.class, task -> {
			task.setGroup("jni");
			task.setDescription("Shows information about native libraries");
		});

		project.getTasks().withType(JavaExec.class).configureEach(task -> {
			task.dependsOn("packageNative");

			task.doFirst(t -> {
				File runDir = project.getLayout().getBuildDirectory()
						.dir("native-run")
						.get()
						.getAsFile();

				String currentFolder = Platform.getCurrentFolder();
				File libDir = new File(runDir, currentFolder);

				if (libDir.exists()) {
					task.systemProperty(
							"java.library.path",
							libDir.getAbsolutePath()
					);
				}
			});
		});

		project.getLogger().lifecycle(
				"JNI Helper Plugin applied successfully."
		);
	}
}