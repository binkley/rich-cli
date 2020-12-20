package hm.binkley.cli

import lombok.Data
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import kotlin.reflect.full.findAnnotation

const val NAME = "test.shell"

@Command(description = ["Test shell"],
    mixinStandardHelpOptions = true,
    name = NAME,
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
