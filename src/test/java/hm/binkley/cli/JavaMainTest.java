package hm.binkley.cli;

import org.junit.jupiter.api.Test;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static hm.binkley.cli.TestMainKt.main;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing is a struggle for an integration library.  The goals: 1. Do not
 * test library functionality directly 2. Only use mocks as a last resort
 */
class JavaMainTest {
    @Test
    void shouldConstruct() {
        final var cli = new RichCLI<>(
                "java.test",
                new TestOptions(),
                "-d", "arg1", "arg2");

        final String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, cli.getOptions().getArgs(),
                "Wrong arguments");
    }

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
