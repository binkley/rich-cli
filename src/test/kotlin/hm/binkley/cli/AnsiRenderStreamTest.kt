package hm.binkley.cli

import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import picocli.CommandLine.Help.Ansi.ON
import java.io.ByteArrayOutputStream
import java.lang.System.lineSeparator

internal class AnsiRenderStreamTest {
    private val out = ByteArrayOutputStream()
    private val ansi = AnsiRenderStream(out = out, ansi = ON)

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
    fun `should render with ASCII attribute escape codes`() {
        ansi.println("@|bold %s|@", "stuff")

        assertAnsi("${ascii(1)}%s${ascii(21, 0)}"
            .format("stuff"))
    }

    private fun assertAnsi(expected: String) = withClue("Wrong output") {
        out.toString() shouldBe "%s${lineSeparator()}".format(expected)
    }
}

private const val ESC = "\u001B"

private fun ascii(vararg codes: Int) =
    codes.joinToString("") {
        "${ESC}[${it}m"
    }
