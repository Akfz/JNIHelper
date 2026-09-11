# JNI Helper

Gradle plugin for working with Java JNI libraries.

## Features

* Generate JNI headers from `native` Java methods
* Package native libraries into JAR
* Prepare native libraries for local run
* Set `java.library.path` for `JavaExec`
* Basic OS and architecture detection

## Installation

```groovy
plugins {
    id 'java'
    id 'io.github.Akfz.jnih' version '1.0.0'
}
```
Use any published version of the plugin.

## Usage

Configure native libraries in `build.gradle`:

```groovy
jni {
    packageNative {
        jar.libraries.from('libmytest.so')
        run.libraries.from('libmytest.so')
    }
}
```

`jar.libraries` are copied into the JAR resources.

`run.libraries` are copied to a directory used when running the application.

## JNI Headers

The plugin automatically adds JNI header generation to `compileJava`.

For example:

```java
package v.akfz;

public class NativeTest {
    public native int add(int a, int b);
}
```

Run:

```bash
./gradlew compileJava
```

The header will be generated in:

```text
build/generated/jni-headers/v_akfz_NativeTest.h
```

The generated header can then be used in C/C++ code:

```cpp
#include "v_akfz_NativeTest.h"

JNIEXPORT jint JNICALL
Java_v_akfz_NativeTest_add(
    JNIEnv* env,
    jobject obj,
    jint a,
    jint b
) {
    return 42;
}
```

## Native Libraries

Supported library types:

```text
.so
.dll
.dylib
```

Example:

```groovy
jni {
    packageNative {
        jar.libraries.from('libmytest.so')
        run.libraries.from('libmytest.so')
    }
}
```

When using `JavaExec`, the plugin automatically sets `java.library.path` to the packaged native library directory.

## Tasks

Generate JNI headers:

```bash
./gradlew generateJniHeaders
```

Package native libraries:

```bash
./gradlew packageNative
```

Show current platform information:

```bash
./gradlew nativeInfo
```

## Example

Minimal `build.gradle`:

```groovy
plugins {
    id 'java'
    id 'io.github.Akfz.jnih' version '1.0.0'
}

repositories {
    mavenCentral()
}

jni {
    packageNative {
        jar.libraries.from('libmytest.so')
        run.libraries.from('libmytest.so')
    }
}
```