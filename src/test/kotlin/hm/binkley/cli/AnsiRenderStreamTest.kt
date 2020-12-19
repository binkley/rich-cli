package hm.binkley.cli

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

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

        assertEquals("stuff\n", out.toString(), "Wrong output")
    }

    @Test
    fun `should render foreground with ASCII escape codes`() {
        val out = ByteArrayOutputStream()
        val ansi = AnsiRenderStream(out)
        ansi.println("@|fg_red %s|@", "stuff")

        assertEquals("\u001B[31mstuff\u001B[m\n", out.toString(), "Wrong output")
    }

    @Test
    fun `should render background with ASCII escape codes`() {
        val out = ByteArrayOutputStream()
        val ansi = AnsiRenderStream(out)
        ansi.println("@|bg_red %s|@", "stuff")

        assertEquals("\u001B[41mstuff\u001B[m\n", out.toString(), "Wrong output")
    }
}
