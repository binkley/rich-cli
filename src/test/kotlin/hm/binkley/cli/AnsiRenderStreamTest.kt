package hm.binkley.cli

import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.lang.System.lineSeparator

internal class AnsiRenderStreamTest {
    private val out = ByteArrayOutputStream()
    private val ansi = AnsiRenderStream(out)

    @Test
    fun `should render null`() {
        ansi.println(null as String?)

        assertAnsi("null")
    }

    @Test
    fun `should render plain text`() {
        ansi.println("stuff")

        assertAnsi("stuff")
    }

    @Test
    fun `should render ignoring unmatched open token`() {
        ansi.println("@|stuff")

        assertAnsi("@|stuff")
    }

    @Test
    fun `should render ignoring unmatched close token`() {
        ansi.println("stuff|@")

        assertAnsi("stuff|@")
    }

    @Test
    fun `should render ignoring inapplicable tokens`() {
        ansi.println("@|stuff|@")

        assertAnsi("@|stuff|@")
    }

    @Test
    fun `should render foreground with ASCII escape codes`() {
        ansi.println("@|fg_red %s|@", "stuff")

        assertAnsi("${ascii(31)}%s${ascii(0)}"
            .format("stuff"))
    }

    @Test
    fun `should render background with ASCII escape codes`() {
        ansi.println("@|bg_red %s|@", "stuff")

        assertAnsi("${ascii(41)}%s${ascii(0)}"
            .format("stuff"))
    }

    @Test
    fun `should render with ASCII attribute escape codes`() {
        ansi.println("@|bold %s|@", "stuff")

        assertAnsi("${ascii(1)}%s${ascii(0)}"
            .format("stuff"))
    }

    @Test
    fun `should render with multiple ASCII escape codes`() {
        ansi.println("@|bold,blue %s|@", "stuff")

        assertAnsi("${ascii(1, 34)}%s${ascii(0)}"
            .format("stuff"))
    }

    @Test
    fun `should render ASCII escape codes multiple times`() {
        ansi.println("@|bold %s|@ @|green %s|@", "stuff", "other stuff")

        assertAnsi("${ascii(1)}%s${ascii(0)} ${ascii(32)}%s${ascii(0)}"
            .format("stuff", "other stuff"))
    }

    private fun assertAnsi(expected: String) = withClue("Wrong output") {
        out.toString() shouldBe "%s${lineSeparator()}".format(expected)
    }
}

private const val ESC = "\u001B"

private fun ascii(vararg codes: Int) =
    "${ESC}[%sm".format(codes.map {
        when (it) {
            0 -> "" // 0 is reset; Jansi chooses missing value, also valid
            else -> it
        }
    }.joinToString(";"))
