package hm.binkley.cli

import com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class KotlinMainTest {
    @Test
    fun `should show help and exit`() {
        val code = catchSystemExit {
            main("-h")
        }

        assertEquals(0, code, "Did not exit normally")
    }

    @Test
    fun `should show version and exit`() {
        val code = catchSystemExit {
            main("-V")
        }

        assertEquals(0, code, "Did not exit normally")
    }

    @Test
    fun `should complain for unknown option`() {
        val code = catchSystemExit {
            main("-?")
        }

        assertEquals(2, code, "Did not exit abnormally")
    }
}
