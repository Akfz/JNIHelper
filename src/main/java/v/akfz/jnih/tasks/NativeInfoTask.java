package v.akfz.jnih.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import v.akfz.jnih.JniHelperExtension;
import v.akfz.jnih.model.Platform;

public abstract class NativeInfoTask extends DefaultTask {

	@TaskAction
	public void showInfo() {
		getLogger().lifecycle("");
		getLogger().lifecycle("Native Library Info");
		getLogger().lifecycle("----------------------------------------");
		getLogger().lifecycle("OS:             " + Platform.getCurrentOs().getId());
		getLogger().lifecycle("Architecture:   " + Platform.getCurrentArch().getId());
		getLogger().lifecycle("JVM bitness:    " + System.getProperty("sun.arch.data.model") + "-bit");
		getLogger().lifecycle("");
		getLogger().lifecycle("Configuration:");

		JniHelperExtension extension = getProject().getExtensions().getByType(JniHelperExtension.class);
		getLogger().lifecycle("  Header output:  " + extension.getHeaderOutputDir().getAsFile().get());
		getLogger().lifecycle("");
	}
}