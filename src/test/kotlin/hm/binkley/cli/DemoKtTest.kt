package hm.binkley.cli

import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class DemoKtTest {
    @Disabled("TODO: Find out where STDOUT is going; this works for other tests than main")
    @Test
    fun `should demo`() {
        val out = tapSystemOutNormalized {
            main("arg1", "arg2")
        }.trim()

        out shouldContain "DEBUG? -> false"
        out shouldContain "ARGS? -> [args1, arg2]"
    }
}
