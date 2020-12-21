package hm.binkley.cli

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

private const val ESC = "\u001B"

internal class AnsiRenderStreamTest {
    @Test
    fun `should render null`() {
        val out = ByteArrayOutputStream()
        val ansi = AnsiRenderStream(out)
        ansi.println(null as String?)

        assertEquals("null\n", out.toString(), "Wrong output")
    }

    @Test
    fun `should render plain text`() {
        val out = ByteArrayOutputStream()
        val ansi = AnsiRenderStream(out)
        ansi.println("stuff")

        assertEquals(
            "%s\n".format("stuff"),
            out.toString(),
            "Wrong output")
    }

    @Test
    fun `should render ignoring unmatched open token`() {
        val out = ByteArrayOutputStream()
        val ansi = AnsiRenderStream(out)
        ansi.println("@|stuff")

        assertEquals(
            "%s\n".format("@|stuff"),
            out.toString(),
            "Wrong output")
    }

    @Test
    fun `should render ignoring unmatched close token`() {
        val out = ByteArrayOutputStream()
        val ansi = AnsiRenderStream(out)
        ansi.println("stuff|@")

        assertEquals(
            "%s\n".format("stuff|@"),
            out.toString(),
            "Wrong output")
    }

    @Test
    fun `should render ignoring inapplicable tokens`() {
        val out = ByteArrayOutputStream()
        val ansi = AnsiRenderStream(out)
        ansi.println("@|stuff|@")

        assertEquals(
            "%s\n".format("@|stuff|@"),
            out.toString(),
            "Wrong output")
    }

    @Test
    fun `should render foreground with ASCII escape codes`() {
        val out = ByteArrayOutputStream()
        val ansi = AnsiRenderStream(out)
        ansi.println("@|fg_red %s|@", "stuff")

        assertEquals(
            "${ascii(31)}%s${ascii(0)}\n".format("stuff"),
            out.toString(),
            "Wrong output")
    }

    @Test
    fun `should render background with ASCII escape codes`() {
        val out = ByteArrayOutputStream()
        val ansi = AnsiRenderStream(out)
        ansi.println("@|bg_red %s|@", "stuff")

        assertEquals(
            "${ascii(41)}%s${ascii(0)}\n".format("stuff"),
            out.toString(),
            "Wrong output")
    }

    @Test
    fun `should render with ASCII attribute escape codes`() {
        val out = ByteArrayOutputStream()
        val ansi = AnsiRenderStream(out)
        ansi.println("@|bold %s|@", "stuff")

        assertEquals(
            "${ascii(1)}%s${ascii(0)}\n".format("stuff"),
            out.toString(),
            "Wrong output")
    }

    @Test
    fun `should render with multiple ASCII escape codes`() {
        val out = ByteArrayOutputStream()
        val ansi = AnsiRenderStream(out)
        ansi.println("@|bold,blue %s|@", "stuff")

        assertEquals(
            "${ascii(1, 34)}%s${ascii(0)}\n".format("stuff"),
            out.toString(),
            "Wrong output")
    }

    @Test
    fun `should render ASCII escape codes multiple times`() {
        val out = ByteArrayOutputStream()
        val ansi = AnsiRenderStream(out)
        ansi.println("@|bold %s|@ @|green %s|@", "stuff", "other stuff")

        assertEquals(
            "${ascii(1)}%s${ascii(0)} ${ascii(32)}%s${ascii(0)}\n"
                .format("stuff", "other stuff"),
            out.toString(),
            "Wrong output")
    }
}

private fun ascii(vararg codes: Int) =
    "${ESC}[%sm".format(codes.map {
        when (it) {
            0 -> "" // 0 is reset; Jansi chooses missing value, also valid
            else -> it
        }
    }.joinToString(";"))
