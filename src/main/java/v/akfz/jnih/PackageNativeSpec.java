package v.akfz.jnih;

import v.akfz.jnih.model.NativeLibrarySet;
import org.gradle.api.Action;

public class PackageNativeSpec {
	private final NativeLibrarySet jar;
	private final NativeLibrarySet run;

	public PackageNativeSpec(NativeLibrarySet jar, NativeLibrarySet run) {
		this.jar = jar;
		this.run = run;
	}

	public NativeLibrarySet getJar() {
		return jar;
	}

	public NativeLibrarySet getRun() {
		return run;
	}

	public void jar(Action<? super NativeLibrarySet> action) {
		action.execute(jar);
	}

	public void run(Action<? super NativeLibrarySet> action) {
		action.execute(run);
	}
}