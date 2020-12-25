package hm.binkley.cli

import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class DemoKtTest {
    @Disabled("TODO: Find out where STDOUT is going; this works for other tests than main")
    @Test
    fun `should demo with defaults`() {
        val out = tapSystemOutNormalized {
            main()
        }.trim()

        out shouldContain "DEBUG? -> false"
        out shouldContain "ARGS? -> []"
    }

    @Disabled("TODO: Find out where STDOUT is going; this works for other tests than main")
    @Test
    fun `should demo with command line`() {
        val out = tapSystemOutNormalized {
            main("-d", "arg1", "arg2")
        }.trim()

        out shouldContain "DEBUG? -> true"
        out shouldContain "ARGS? -> [arg1, arg2]"
    }
}
