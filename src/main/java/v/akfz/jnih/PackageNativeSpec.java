package v.akfz.jnih;

import v.akfz.jnih.model.NativeLibrarySet;

public class PackageNativeSpec {

	private final NativeLibrarySet jar;
	private final NativeLibrarySet run;

	public PackageNativeSpec(NativeLibrarySet jar, NativeLibrarySet run) {
		this.jar = jar;
		this.run = run;
	}

	public void jar(org.gradle.api.Action<NativeLibrarySet> action) {
		action.execute(jar);
	}

	public void run(org.gradle.api.Action<NativeLibrarySet> action) {
		action.execute(run);
	}
}