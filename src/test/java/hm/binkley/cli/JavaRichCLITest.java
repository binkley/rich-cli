package hm.binkley.cli;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Testing is a struggle for an integration library.  The goals: 1. Do not
 * test library functionality directly 2. Only use mocks as a last resort
 */
class JavaRichCLITest {
    private static RichCLI<TestOptions> testRichCLI(final String... args) {
        return new RichCLI<>(new TestOptions(), args);
    }

    @Test
    void shouldConstructWithNoArgs() {
        try (var cli = testRichCLI()) {
            final String[] expected = {};
            assertArrayEquals(expected, cli.getOptions().getArgs(),
                    "Wrong arguments");
        }
    }

    @Test
    void shouldConstructWithArgs() {
        try (var cli = testRichCLI("-d", "arg1", "arg2")) {
            final String[] expected = {"arg1", "arg2"};
            assertArrayEquals(expected, cli.getOptions().getArgs(),
                    "Wrong arguments");
        }
    }
}
