package hm.binkley.cli

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import picocli.CommandLine.Help.Ansi
import picocli.CommandLine.Help.Ansi.AUTO
import java.io.OutputStream
import java.io.PrintStream

@SuppressFBWarnings("DM_DEFAULT_ENCODING")
open class AnsiRenderStream(
    out: OutputStream,
    autoFlush: Boolean = false,
    private val ansi: Ansi = AUTO,
) : PrintStream(out, autoFlush) {
    override fun print(x: String?) = super.print(when (x) {
        null -> null
        else -> ansi.string(x)
    })

    fun println(format: String, vararg args: Any?) =
        println(format.format(*args))
}
