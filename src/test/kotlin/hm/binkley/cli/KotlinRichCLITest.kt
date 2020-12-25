package hm.binkley.cli

import com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit
import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrNormalized
import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized
import io.kotest.assertions.withClue
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.fusesource.jansi.Ansi
import org.jline.reader.Candidate
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.ParsedLine
import org.jline.terminal.MouseEvent
import org.jline.terminal.MouseEvent.Button.Button1
import org.jline.terminal.MouseEvent.Modifier
import org.jline.terminal.MouseEvent.Type.Pressed
import org.jline.terminal.Size
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.jline.terminal.impl.DumbTerminal
import org.jline.widget.Widgets
import org.junit.jupiter.api.Test
import java.lang.System.`in`
import java.lang.System.out
import java.util.EnumSet

/**
 * Testing is a struggle for an integration library.  The goals: 1. Do not
 * test library functionality directly 2. Only use mocks as a last resort
 */
internal class KotlinMainTest {
    @Test
    fun `should construct with no args`() = with(testRichCLI()) {
        withClue("Wrong arguments") {
            options.args shouldBe arrayOf()
        }
    }

    @Test
    fun `should construct with args`() =
        with(testRichCLI("-d", "arg1", "arg2")) {
            withClue("Wrong arguments") {
                options.debug.shouldBeTrue()
                options.args shouldBe arrayOf("arg1", "arg2")
            }
        }

    @Test
    fun `should construct with custom everything`() {
        // TODO: Silly to provide a Completer parameter when it is only used
        //       as convenience in the default lineReader parameter value
        val completer: (LineReader, ParsedLine, MutableList<Candidate>) -> Unit =
            { _, _, _ -> }
        val terminal = TerminalBuilder.builder()
            .dumb(true)
            .build()
        val lineReader = LineReaderBuilder.builder()
            .appName(terminal.name)
            .completer(completer)
            .terminal(terminal)
            .build()
        val options = TestOptions()

        // Test that:
        // 1. Compiles
        // 2. Does not throw
        RichCLI(
            completer = completer,
            lineReader = lineReader,
            options = options,
            terminal = terminal,
            // See https://youtrack.jetbrains.com/issue/KT-17691
            args = arrayOf("-d", "arg1", "arg2"),
        )
    }

    @Suppress("USELESS_IS_CHECK")
    @Test
    fun `should support types`() = with(testRichCLI()) {
        withClue("Wrong types") {
            (this is AnsiRenderStream).shouldBeTrue()
            (this is LineReader).shouldBeTrue()
            (this is Terminal).shouldBeTrue()
        }
    }

    @Suppress("USELESS_IS_CHECK")
    @Test
    fun `should have an ANSI error stream`() = with(testRichCLI()) {
        withClue("Wrong type for err property") {
            (err is AnsiRenderStream).shouldBeTrue()
        }
    }

    @Suppress("USELESS_IS_CHECK")
    @Test
    fun `should have an ANSI formatter`() = with(testRichCLI()) {
        withClue("Wrong type for ansi property") {
            (ansi is Ansi).shouldBeTrue()
        }
    }

    @Test
    fun `should delegate flush correctly`() {
        val testTerminal = object : DumbTerminal(`in`, out) {
            var flushed = false

            override fun flush() {
                flushed = true
            }
        }

        with(RichCLI(
            options = TestOptions(),
            terminal = testTerminal,
        )) {
            flush()

            withClue("Did not delegate") {
                testTerminal.flushed shouldBe true
            }
        }
    }

    @Test
    fun `should delegate mouse event correctly`() {
        val testTerminal = object : DumbTerminal(`in`, out) {
            var readMouse = false

            override fun readMouseEvent() = MouseEvent(
                Pressed,
                Button1,
                EnumSet.noneOf(Modifier::class.java),
                0,
                0).also {
                readMouse = true
            }
        }

        with(RichCLI(
            options = TestOptions(),
            terminal = testTerminal,
        )) {
            readMouseEvent()

            withClue("Did not delegate") {
                testTerminal.readMouse shouldBe true
            }
        }
    }

    @Test
    fun `should show help`() {
        var code = -1
        val out = tapSystemOutNormalized {
            code = catchSystemExit {
                testRichCLI("-h")
            }
        }.trim()

        withClue("Did not exit normally") {
            code shouldBe 0
        }
        withClue("No sample usage") {
            out shouldContain "Usage: %s [-dhV] [ARGS...]".format(NAME)
        }
        withClue("No help text") {
            out shouldContain Regex("-d, --debug  *Enable debug output.")
        }
    }

    @Test
    fun `should show version`() {
        var code = -1
        val out = tapSystemOutNormalized {
            code = catchSystemExit {
                testRichCLI("-V")
            }
        }.trim()

        withClue("Did not exit normally") {
            code shouldBe 0
        }
        withClue("Wrong version") {
            out shouldContain "0-SNAPSHOT"
        }
    }

    @Test
    fun `should complain for unknown option`() {
        var code = -1
        val err = tapSystemErrNormalized {
            code = catchSystemExit {
                testRichCLI("-?")
            }
        }.trim()

        withClue("Did not exit normally") {
            code shouldBe 2
        }
        withClue("No bad flag complaint") {
            err shouldContain "Unknown option: '-?'"
        }
        withClue("No sample usage") {
            err shouldContain "Usage: %s [-dhV] [ARGS...]".format(NAME)
        }
        withClue("No help text") {
            err shouldContain Regex("-d, --debug +Enable debug output.")
        }
    }

    @Test
    fun `should have fish completion`() = with(testRichCLI()) {
        val widget = object : Widgets(this) {}

        withClue("No Fish behavior") {
            // Sadly, no constant from JLine for this
            widget.existsWidget("_autosuggest-forward-char").shouldBeTrue()
        }
    }

    @Test // TODO: The production code relies on a *hack*
    fun `should know if attached to a console`() {
        with(RichCLI(
            options = TestOptions(),
            terminal = TerminalBuilder.builder()
                .size(Size(1, 1)) // TODO: Hack
                .system(false)
                .build(),
        )) {
            withClue("Not attached to a console") {
                isTty().shouldBeTrue()
            }
        }
    }

    @Test // TODO: The production code relies on a *hack*
    fun `should know if not attached to a console`() {
        with(RichCLI(
            options = TestOptions(),
            terminal = TerminalBuilder.builder()
                .size(Size(0, 0)) // TODO: Hack
                .system(false)
                .build(),
        )) {
            withClue("Not unattached to a console") {
                isTty().shouldBeFalse()
            }
        }
    }
}

private fun testRichCLI(vararg args: String) = RichCLI(TestOptions(), *args)
