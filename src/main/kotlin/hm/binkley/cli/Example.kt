@file:Suppress("unused")

package hm.binkley.cli

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import hm.binkley.cli.Commands.CliCommands
import lombok.Generated
import org.fusesource.jansi.AnsiConsole
import org.jline.console.CmdLine
import org.jline.console.SystemRegistry
import org.jline.console.impl.Builtins
import org.jline.console.impl.Builtins.Command.TTOP
import org.jline.console.impl.SystemRegistryImpl
import org.jline.keymap.KeyMap
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader.LIST_MAX
import org.jline.reader.LineReaderBuilder
import org.jline.reader.MaskingCallback
import org.jline.reader.Parser
import org.jline.reader.Reference
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.DefaultParser
import org.jline.terminal.TerminalBuilder
import org.jline.widget.TailTipWidgets
import org.jline.widget.TailTipWidgets.TipType.COMPLETER
import picocli.CommandLine
import picocli.CommandLine.ArgGroup
import picocli.CommandLine.Command
import picocli.CommandLine.HelpCommand
import picocli.CommandLine.Option
import picocli.CommandLine.ParentCommand
import picocli.shell.jline3.PicocliCommands
import picocli.shell.jline3.PicocliCommands.ClearScreen
import picocli.shell.jline3.PicocliCommands.PicocliCommandsFactory
import java.io.PrintWriter
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

/** See https://github.com/remkop/picocli/tree/master/picocli-shell-jline3 */
@Generated
fun main() {
    AnsiConsole.systemInstall()
    try {
        val workDir = Supplier {
            Paths.get(System.getProperty("user.dir"))
        }
        // set up JLine built-in commands
        val builtins = Builtins(workDir, null, null)
        builtins.rename(TTOP, "top")
        builtins.alias("zle", "widget")
        builtins.alias("bindkey", "keymap")
        // set up picocli commands
        val commands = CliCommands()
        val factory = PicocliCommandsFactory()
        // Or, if you have your own factory, you can chain them like this:
        // MyCustomFactory customFactory = createCustomFactory(); // your application custom factory
        // PicocliCommandsFactory factory = new PicocliCommandsFactory(customFactory); // chain the factories
        val cmd = CommandLine(commands, factory)
        val picocliCommands = PicocliCommands(cmd)
        val parser: Parser = DefaultParser()
        TerminalBuilder.builder().build().use { terminal ->
            val systemRegistry: SystemRegistry = SystemRegistryImpl(
                parser,
                terminal,
                workDir,
                null,
            )
            systemRegistry.setCommandRegistries(builtins, picocliCommands)
            systemRegistry.register("help", picocliCommands)
            val reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(systemRegistry.completer())
                .parser(parser)
                // max tab completion candidates
                .variable(LIST_MAX, 50)
                .build()
            builtins.setLineReader(reader)
            factory.setTerminal(terminal)
            val widgets = TailTipWidgets(reader, { line: CmdLine? ->
                systemRegistry.commandDescription(line)
            }, 5, COMPLETER)
            widgets.enable()
            val keyMap = reader.keyMaps["main"]!!
            keyMap.bind(Reference("tailtip-toggle"), KeyMap.alt("s"))
            val prompt = "prompt> "
            val rightPrompt: String? = null

            // start the shell and process input until the user quits with Ctrl-D
            var line: String?
            while (true) {
                try {
                    systemRegistry.cleanUp()
                    line = reader.readLine(prompt,
                        rightPrompt,
                        null as MaskingCallback?,
                        null)
                    systemRegistry.execute(line)
                } catch (e: UserInterruptException) {
                    // Ignore
                } catch (e: EndOfFileException) {
                    return
                } catch (e: Exception) {
                    systemRegistry.trace(e)
                }
            }
        }
    } catch (t: Throwable) {
        t.printStackTrace()
    } finally {
        AnsiConsole.systemUninstall()
    }
}

/**
 * Example that demonstrates how to build an interactive shell with JLine3 and
 * picocli. This example requires JLine 3.16+ and picocli 4.4+.
 *
 * The built-in `PicocliCommands.ClearScreen` command was introduced in
 * picocli 4.6.
 */
@Generated
class Commands {
    /** Top-level command that just prints help. */
    @Command(name = "",
        description = [
            "Example interactive shell with completion and autosuggestions." +
                    " Hit @|magenta <TAB>|@ to see available commands.",
            "Hit @|magenta ALT-S|@ to toggle tailtips.",
            "",
        ],
        footer = ["", "Press Ctrl-D to exit."],
        subcommands = [MyCommand::class, ClearScreen::class, HelpCommand::class])
    @Generated
    class CliCommands : Runnable {
        var out: PrintWriter? = null
        override fun run() {
            out!!.println(CommandLine(this).usageMessage)
        }
    }

    /** A command with some options to demonstrate completion. */
    @Command(name = "cmd",
        mixinStandardHelpOptions = true,
        version = ["1.0"],
        description = [
            "Command with some options to demonstrate TAB-completion.",
            " (Note that enum values also get completed.)",
        ],
        subcommands = [Nested::class, HelpCommand::class])
    @Generated
    @SuppressFBWarnings("EI_EXPOSE_REP", "EI_EXPOSE_REP2")
    class MyCommand : Runnable {
        @Option(names = ["-v", "--verbose"],
            description = [
                "Specify multiple -v options to increase verbosity.",
                "For example, `-v -v -v` or `-vvv`",
            ])
        var verbosity = booleanArrayOf()

        @ArgGroup(exclusive = false)
        var myDuration = MyDuration()

        @ParentCommand
        var parent: CliCommands? = null
        override fun run() {
            if (verbosity.isNotEmpty()) {
                parent!!.out!!.printf("Hi there. You asked for %d %s.%n",
                    myDuration.amount, myDuration.unit)
            } else {
                parent!!.out!!.println("hi!")
            }
        }
    }

    @Command(name = "nested",
        mixinStandardHelpOptions = true,
        subcommands = [HelpCommand::class],
        description = ["Hosts more sub-subcommands"])
    @Generated
    class Nested : Runnable {
        override fun run() {
            println("I'm a nested subcommand. I don't do much, but I have sub-subcommands!")
        }

        @Command(mixinStandardHelpOptions = true,
            subcommands = [HelpCommand::class],
            description = ["Multiplies two numbers."])
        fun multiply(
            @Option(names = ["-l", "--left"], required = true) left: Int,
            @Option(names = ["-r", "--right"], required = true) right: Int,
        ) {
            System.out.printf("%d * %d = %d%n", left, right, left * right)
        }

        @Command(mixinStandardHelpOptions = true,
            subcommands = [HelpCommand::class],
            description = ["Adds two numbers."])
        fun add(
            @Option(names = ["-l", "--left"], required = true) left: Int,
            @Option(names = ["-r", "--right"], required = true) right: Int,
        ) {
            System.out.printf("%d + %d = %d%n", left, right, left + right)
        }

        @Command(mixinStandardHelpOptions = true,
            subcommands = [HelpCommand::class],
            description = ["Subtracts two numbers."])
        fun subtract(
            @Option(names = ["-l", "--left"], required = true) left: Int,
            @Option(names = ["-r", "--right"], required = true) right: Int,
        ) {
            System.out.printf("%d - %d = %d%n", left, right, left - right)
        }
    }
}

@Generated
class MyDuration {
    @Option(names = ["-d", "--duration"],
        description = ["The duration quantity."],
        required = true)
    var amount = 0

    @Option(names = ["-u", "--timeUnit"],
        description = ["The duration time unit."],
        required = true)
    var unit: TimeUnit? = null
}
