<a href="./LICENSE.md">
<img src="./images/public-domain.png" alt="Public Domain"
align="right"/>
</a>

# Rich CLI

[![build](https://github.com/binkley/rich-cli/workflows/build/badge.svg)](https://github.com/binkley/rich-cli/actions)
[![issues](https://img.shields.io/github/issues/binkley/rich-cli.svg)](https://github.com/binkley/rich-cli/issues/)
[![Public Domain](https://img.shields.io/badge/license-Public%20Domain-blue.svg)](http://unlicense.org/)

"Rich CLI" is an integration of "command-line interface" (CLI) toolings for
JVM languages (Java, Kotlin, et al) providing a good user experience in a
terminal program.

## Build

```shell
$ ./mvnw clean verify
$ ./run demo -d arg1 arg2
```

---

## TOC

* [Integrated libraries](#integrated-libraries)
* [Language support](#language-support)
* [API](#api)
* [Versions](#versions)

---

## Integrated libraries

- [JLine](https://github.com/jline/jline3)
- [Jansi](https://github.com/fusesource/jansi)
- [Picocli](https://github.com/remkop/picocli)

---

## Language support

The code is written in Kotlin. Tests are these languages to demonstrate native
support:

- [Java](./src/test/java/hm/binkley/cli/JavaMainTest.java)
- [Kotlin](./src/test/kotlin/hm/binkley/cli/KotlinMainTest.kt)

---

## API

### `RichAPI`

Note that the name of your program for `Terminal` and `LineReader` is read
from the `name` value of the `@Command` annotation on your options class.

A `RichCLI` is also a:

- `AnsiRenderStream` (STDOUT based on Jansi)
- `Terminal` (a JLine type)
- `LineReader` (a JLine type)

`RichCLI` also provides:

#### Properties

**NB** &mdash; In Java these properties are standard getters.

- `ansi` direct access to methods on `Ansi`
- `err` STDERR, also an `AnsiRenderStream` based on Jansi

#### Java

```java
class YourMain {
    public static void main(final String... args) {
        final var cli = new RichCLI<>(new YourOptions(), args);
        // Use your CLI ...
    }
}
```

#### Kotlin

```kotlin
fun main() {
    val cli = RichCLI(YourOptions(), *args)
    // Use your CLI ...
}
```

### `AnsiRenderStream`

An `AnsiRenderStream` is also a:

- `PrintStream` (like STDOUT and STDERR)

`AnsiRenderStream` also provides:

#### Methods

- `print(String)` -- an override which parses Jansi-rendered text (see the
  original
  [Jansi-1.18 documentation](https://github.com/fusesource/jansi/blob/jansi-project-1.18/jansi/src/main/java/org/fusesource/jansi/AnsiRenderer.java)
  for syntax details)
- `println(String, Object...)` -- a convenience to format and print lines

---

## Versions

* 0-SNAPSHOT -- Unpublished
