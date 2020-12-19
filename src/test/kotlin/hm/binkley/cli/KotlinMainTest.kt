package hm.binkley.cli

import com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit
import org.jline.reader.LineReader
import org.jline.terminal.Terminal
import org.jline.widget.Widgets
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Testing is a struggle for an integration library.  The goals: 1. Do not
 * test library functionality directly 2. Only use mocks as a last resort
 */
internal class KotlinMainTest {
    @Suppress("USELESS_IS_CHECK")
    @Test
    fun `should support types`() {
        val cli = RichCLI(
            "java.test",
            TestOptions()
        )

        assertTrue(cli is AnsiRenderStream)
        assertTrue(cli is LineReader)
        assertTrue(cli is Terminal)
    }

    @Test
    fun `should construct`() {
        val cli = RichCLI(
            "java.test",
            TestOptions(),
            "-d", "arg1", "arg2"
        )

        val expected = arrayOf("arg1", "arg2")
        assertArrayEquals(expected, cli.options.args, "Wrong arguments")
    }

    @Test
    fun `should have fish completion`() {
        val widget = object : Widgets(RichCLI(
            "java.test",
            TestOptions()
        )) {}

        assertTrue(widget.existsWidget("_autosuggest-forward-char"),
            "No Fish behavior")
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
}
