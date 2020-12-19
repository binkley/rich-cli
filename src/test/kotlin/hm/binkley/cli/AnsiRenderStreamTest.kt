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
}

private fun ascii(vararg codes: Int) = codes.map {
    when (it) {
        // 0 is valid for reset, however Jansi chooses a missing value, also valid
        0 -> ""
        else -> it.toString()
    }
}.map {
    "$ESC[${it}m"
}.joinToString(";")
