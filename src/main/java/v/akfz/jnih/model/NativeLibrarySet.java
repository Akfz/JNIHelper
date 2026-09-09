package v.akfz.jnih.model;

import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.model.ObjectFactory;
import javax.inject.Inject;

public abstract class NativeLibrarySet {

	private final ConfigurableFileCollection libraries;

	@Inject
	public NativeLibrarySet(ObjectFactory objects) {
		this.libraries = objects.fileCollection();
	}

	public ConfigurableFileCollection getLibraries() {
		return libraries;
	}

	public boolean isEmpty() {
		return libraries.isEmpty();
	}
}