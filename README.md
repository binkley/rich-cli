<a href="./LICENSE.md">
<img src="./images/public-domain.png" alt="Public Domain"
align="right"/>
</a>

# Rich CLI

[![build](https://github.com/binkley/rich-cli/workflows/build/badge.svg)](https://github.com/binkley/rich-cli/actions)
[![issues](https://img.shields.io/github/issues/binkley/rich-cli.svg)](https://github.com/binkley/rich-cli/issues/)
[![Public Domain](https://img.shields.io/badge/license-Public%20Domain-blue.svg)](http://unlicense.org/)

"Rich CLI" is an integration of "command-line interface" (CLI) libraries for
JVM languages (Java, Kotlin, et al), providing a good programmer experience
for terminal programs.

## Build

```shell
$ ./mvnw clean verify
$ ./run demo -d arg1 arg2
```

---

## TOC

* [Integrations](#integrations)
* [JVM language support](#jvm-language-support)
* [API](#api)
* [Versions](#versions)
* [Further reading](#further-reading)
* [TODO](#todo)

---

## Integrations

- [JLine](https://github.com/jline/jline3)
- [Jansi](https://github.com/fusesource/jansi)
- [Picocli](https://github.com/remkop/picocli)

---

## JVM language support

The code is written in Kotlin, however this library works with any JVM
language. Tests check these languages:

- [Java](./src/test/java/hm/binkley/cli/JavaRichCLITest.java) -- validates
  Java usage
- [Kotlin](./src/test/kotlin/hm/binkley/cli/KotlinRichCLITest.kt) -- tests
  functionality

---

## API

### `RichAPI`

Note that the name of your program for `Terminal` and `LineReader` is read
from the `name` value of the `@Command` annotation on your options class.

A `RichCLI` is also:

- An `AnsiRenderStream` (STDOUT based on Jansi)
- A `Terminal` (a JLine type)
- A `LineReader` (a JLine type)

As well `RichCLI` provides:

#### Properties

- `ansi` direct access to methods on `Ansi`
- `err` STDERR, also an `AnsiRenderStream` based on Jansi

**NB** &mdash; In Java these properties are getters.

#### Methods

- `isTty()` -- checks if the terminal is attached to a console

#### Java example

```java
class YourMain {
    public static void main(final String... args) {
        final var cli = new RichCLI<>(new YourOptions(), args);

        // Use your CLI ...
    }
}
```

#### Kotlin example

```kotlin
fun main() {
    val cli = RichCLI(YourOptions(), *args)

    // Use your CLI ...
}
```

### `AnsiRenderStream`

An `AnsiRenderStream` is also:

- A `PrintStream` (like STDOUT and STDERR)

As well `AnsiRenderStream` provides:

#### Methods

- `print(String)` -- an override which parses Jansi-rendered text (see the
  original
  [Jansi-1.18 documentation](https://github.com/fusesource/jansi/blob/jansi-project-1.18/jansi/src/main/java/org/fusesource/jansi/AnsiRenderer.java)
  for syntax details) and prints to
  [STDOUT](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#out)
- `println(String, Object...)` -- a convenience method to
  [format](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html#format(java.lang.String,java.lang.Object...))
  and prints lines to
  [STDOUT](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#out)

---

## Versions

* 0-SNAPSHOT -- Unpublished

---

## Further reading

* [_Command Line Interface Guidelines_](https://clig.dev/)
* [GNU option
  standards](https://www.gnu.org/prep/standards/html_node/Option-Table.html)

---

## TODO

* Address method `RichCLI.isTty()` -- neither JLine nor the JDK handle this
  well
