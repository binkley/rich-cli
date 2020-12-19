package hm.binkley.cli

import lombok.Data
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters

private const val name = "test.shell"

fun main(vararg args: String) {
    RichCLI(
        name = name,
        options = TestOptions(),
        args = args,
    )
}

@Command(description = ["Math shell"],
    mixinStandardHelpOptions = true,
    name = name,
    version = ["0-SNAPSHOT"])
@Data
class TestOptions : Runnable {
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
