package v.akfz.jnih;

import org.gradle.api.Project;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import v.akfz.jnih.model.NativeLibrarySet;

import javax.inject.Inject;

public abstract class JniHelperExtension {

	private final NativeLibrarySet jar;
	private final NativeLibrarySet run;
	private final DirectoryProperty headerOutputDir;

	@Inject
	public JniHelperExtension(Project project, ObjectFactory objects) {
		this.jar = objects.newInstance(NativeLibrarySet.class);
		this.run = objects.newInstance(NativeLibrarySet.class);
		this.headerOutputDir = objects.directoryProperty()
				.convention(project.getLayout().getBuildDirectory().dir("generated/jni-headers"));
	}

	public NativeLibrarySet getJar() {
		return jar;
	}

	public NativeLibrarySet getRun() {
		return run;
	}

	public DirectoryProperty getHeaderOutputDir() {
		return headerOutputDir;
	}

	public void packageNative(org.gradle.api.Action<PackageNativeSpec> action) {
		PackageNativeSpec spec = new PackageNativeSpec(jar, run);
		action.execute(spec);
	}
}