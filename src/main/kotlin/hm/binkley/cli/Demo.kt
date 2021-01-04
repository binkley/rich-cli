package hm.binkley.cli

import lombok.Generated
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters

@Generated
fun main(vararg args: String): Unit =
    with(RichCLI(TestOptions(), *args)) {
        println("TTY? -> @|bold,red %b|@", isAnsi())
        println("DEBUG? -> ${options.debug}")
        println("ARGS -> ${options.args.toList()}")
    }

@Command(description = ["Demo shell"],
    mixinStandardHelpOptions = true,
    name = "demo.shell",
    version = ["0-SNAPSHOT"])
@Generated
private class TestOptions : Runnable {
    override fun run() {}

    @Option(
        description = ["Enable @|bold,red debug|@ output."],
        names = ["--debug", "-d"],
    )
    var debug = false

    @Parameters(
        description = ["Optional rest of the command line."],
        paramLabel = "ARGS",
    )
    var args = arrayOf<String>()
}
