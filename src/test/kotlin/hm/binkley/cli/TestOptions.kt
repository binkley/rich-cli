package hm.binkley.cli

import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters

const val NAME = "test.shell"

@Command(description = ["Test shell"],
    mixinStandardHelpOptions = true,
    name = NAME,
    version = ["0-SNAPSHOT"])
internal class TestOptions : Runnable {
    override fun run() {}

    @Option(
        description = ["Enable debug output."],
        names = ["--debug", "-d"],
    )
    var debug = false

    @Parameters(
        description = ["Optional rest of the command line."],
        paramLabel = "ARGS",
    )
    var args = arrayOf<String>()
}
