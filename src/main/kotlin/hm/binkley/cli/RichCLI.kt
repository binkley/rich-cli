package hm.binkley.cli

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.impl.completer.NullCompleter
import org.jline.terminal.MouseEvent
import org.jline.terminal.Size
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.jline.widget.AutosuggestionWidgets
import picocli.CommandLine
import picocli.CommandLine.Command
import kotlin.reflect.full.findAnnotation
import kotlin.system.exitProcess

@SuppressFBWarnings("DM_EXIT")
class RichCLI<T : Any>(
    // TODO: Pass in "name" used by all integrations
    val options: T, // picocli needs a union type: not (yet) a Kotlin thing
    /** @todo A custom terminal *does not* acquire the common name */
    private val terminal: Terminal = namedTerminal(commandNameOf(options)),
    completer: Completer = NullCompleter.INSTANCE,
    private val lineReader: LineReader = completedLineReader(
        completer = completer,
        terminal = terminal),
    vararg args: String,
) : AnsiRenderStream(terminal.output()),
    Terminal by terminal,
    LineReader by lineReader {

    init {
        CommandLine(options).apply {
            val code = execute(*args)
            // TODO: How to tie this to @Command settings for exit codes?
            when {
                isUsageHelpRequested || isVersionHelpRequested ->
                    exitProcess(0)
                0 != code -> exitProcess(code)
            }
        }

        AnsiConsole.systemInstall()
        AutosuggestionWidgets(lineReader).enable()
    }

    // Convenience ctors for Java

    constructor(
        options: T,
        vararg args: String,
    ) : this(
        options = options,
        // Unneeded parameter to ensure ctor signature is unique
        terminal = namedTerminal(commandNameOf(options)),
        args = args,
    )

    val err get() = AnsiRenderStream(System.err)

    /**
     * For more control of terminal output.  This would be a delegated
     * interface, however [Ansi] is a concrete class.  Note that this _does
     * not print_: it formats.
     *
     * @todo Contrast jansi & jline, possibly picking just jline: consider
     *       rendering
     */
    val ansi get() = ansi()

    // Conflicts between Terminal and LineReader
    override fun close() = terminal.close()
    override fun flush() = terminal.flush()
    override fun readMouseEvent(): MouseEvent = terminal.readMouseEvent()
}

/** @todo This is a hack, and broken */
fun Terminal.isTty() = Size(0, 0) != size

private fun namedTerminal(name: String) = TerminalBuilder.builder()
    .name(name)
    .build()

private fun completedLineReader(
    completer: Completer,
    terminal: Terminal,
) = LineReaderBuilder.builder()
    // TODO: See https://github.com/jline/jline3/issues/631
    .appName(terminal.name)
    .completer(completer)
    .terminal(terminal)
    .build()

private fun commandNameOf(any: Any) =
    any::class.findAnnotation<Command>()!!.name
