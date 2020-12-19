package hm.binkley.cli;

import org.junit.jupiter.api.Test;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static hm.binkley.cli.TestMainKt.main;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JavaMainTest {
    @Test
    void shouldShowHelpAndExit() throws Exception {
        final int code = catchSystemExit(() -> {
            main("-h");
        });

        assertEquals(0, code, "Did not exit normally");
    }

    @Test
    void shouldShowVersionAndExit() throws Exception {
        final int code = catchSystemExit(() -> {
            main("-V");
        });

        assertEquals(0, code, "Did not exit normally");
    }

    @Test
    void shouldComplainForUnknownOption() throws Exception {
        final int code = catchSystemExit(() -> {
            main("-?");
        });

        assertEquals(2, code, "Did not exit abnormally");
    }
}
