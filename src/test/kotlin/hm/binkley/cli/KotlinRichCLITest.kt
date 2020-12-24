package hm.binkley.cli

import com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit
import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrNormalized
import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized
import io.kotest.assertions.withClue
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.fusesource.jansi.Ansi
import org.jline.reader.LineReader
import org.jline.terminal.Terminal
import org.jline.widget.Widgets
import org.junit.jupiter.api.Test

/**
 * Testing is a struggle for an integration library.  The goals: 1. Do not
 * test library functionality directly 2. Only use mocks as a last resort
 */
internal class KotlinMainTest {
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
    fun `should construct with no args`() = with(testRichCLI()) {
        withClue("Wrong arguments") {
            options.args shouldBe arrayOf()
        }
    }

    @Test
    fun `should construct with args`() =
        with(testRichCLI("-d", "arg1", "arg2")) {
            withClue("Wrong arguments") {
                options.args shouldBe arrayOf("arg1", "arg2")
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
}

private fun testRichCLI(vararg args: String) = RichCLI(TestOptions(), *args)
