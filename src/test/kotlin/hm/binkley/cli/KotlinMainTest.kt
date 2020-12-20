package hm.binkley.cli

import com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit
import org.fusesource.jansi.Ansi
import org.jline.reader.LineReader
import org.jline.terminal.Terminal
import org.jline.widget.Widgets
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.PrintStream

/**
 * Testing is a struggle for an integration library.  The goals: 1. Do not
 * test library functionality directly 2. Only use mocks as a last resort
 */
internal class KotlinMainTest {
    @Suppress("USELESS_IS_CHECK")
    @Test
    fun `should support types`() = with(testRichCLI()) {
        assertTrue(this is AnsiRenderStream)
        assertTrue(this is LineReader)
        assertTrue(this is Terminal)
    }

    @Test
    fun `should construct with no args`() = with(testRichCLI()) {
        assertArrayEquals(
            arrayOf<String>(),
            options.args,
            "Wrong arguments",
        )
    }

    @Test
    fun `should construct with args`() =
        with(testRichCLI("-d", "arg1", "arg2")) {
            assertArrayEquals(
                arrayOf("arg1", "arg2"),
                options.args,
                "Wrong arguments",
            )
        }

    @Test
    fun `should show help and exit`() {
        val code = catchSystemExit {
            main("-h")
        }

        assertEquals(0, code, "Did not exit normally")
    }

    @Test
    fun `should show version and exit`() {
        val code = catchSystemExit {
            main("-V")
        }

        assertEquals(0, code, "Did not exit normally")
    }

    @Test
    fun `should complain for unknown option`() {
        val code = catchSystemExit {
            main("-?")
        }

        assertEquals(2, code, "Did not exit abnormally")
    }

    @Suppress("USELESS_IS_CHECK")
    @Test
    fun `should have an error stream`() = with(testRichCLI()) {
        assertTrue(err is PrintStream)
    }

    @Suppress("USELESS_IS_CHECK")
    @Test
    fun `should have an ANSI formatter`() = with(testRichCLI()) {
        assertTrue(ansi is Ansi)
    }

    @Test
    fun `should have fish completion`() = with(testRichCLI()) {
        val widget = object : Widgets(this) {}

        assertTrue(widget.existsWidget("_autosuggest-forward-char"),
            "No Fish behavior")
    }
}

private fun testRichCLI(vararg args: String) = RichCLI(
    "java.test",
    TestOptions(),
    *args)
