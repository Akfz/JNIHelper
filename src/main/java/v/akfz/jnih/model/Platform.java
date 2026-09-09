package v.akfz.jnih.model;

public final class Platform {

	private Platform() {}

	public enum Os {
		LINUX("linux"),
		WINDOWS("windows"),
		MACOS("macos");

		private final String id;
		Os(String id) { this.id = id; }
		public String getId() { return id; }
	}

	public enum Arch {
		X86_64("x86_64"),
		ARM64("arm64"),
		X86("x86");

		private final String id;
		Arch(String id) { this.id = id; }
		public String getId() { return id; }
	}

	public static Os getCurrentOs() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) return Os.WINDOWS;
		if (os.contains("mac")) return Os.MACOS;
		if (os.contains("nux") || os.contains("nix")) return Os.LINUX;
		throw new UnsupportedOperationException("Неизвестная ОС: " + os);
	}

	public static Arch getCurrentArch() {
		String arch = System.getProperty("os.arch").toLowerCase();
		if (arch.contains("aarch64") || arch.contains("arm64")) return Arch.ARM64;
		if (arch.contains("64")) return Arch.X86_64;
		return Arch.X86;
	}

	public static String getFolderName(Os os, Arch arch) {
		return os.getId() + "-" + arch.getId();
	}

	public static String getCurrentFolder() {
		return getFolderName(getCurrentOs(), getCurrentArch());
	}

	public static Os detectFromExtension(String ext) {
		return switch (ext.toLowerCase()) {
			case ".so" -> Os.LINUX;
			case ".dll" -> Os.WINDOWS;
			case ".dylib" -> Os.MACOS;
			default -> throw new IllegalArgumentException("Unknown extension: " + ext);
		};
	}
}