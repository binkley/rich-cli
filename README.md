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

## Usage

*TODO: FILL IN TEXT ONCE PUBLISHED TO BINTRAY*

---

## TOC

* [Integrated libraries](#integrated-libraries)
* [API](#api)
* [Versions](#versions)

---

## Integrated libraries

- [JLine](https://github.com/jline/jline3)
- [Jansi](https://github.com/fusesource/jansi)
- [Picocli](https://github.com/remkop/picocli)

---

## API

### Java

### Kotlin

```kotlin
    val cli = RichCLI(
        name = name,
        options = YourOptions(), // See picocli documentation
        args = args, // Passed from `main(args)`
    )
```

---

## Versions

* 0-SNAPSHOT -- Unpublished
