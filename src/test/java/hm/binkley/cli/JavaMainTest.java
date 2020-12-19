package hm.binkley.cli;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Testing is a struggle for an integration library.  The goals: 1. Do not
 * test library functionality directly 2. Only use mocks as a last resort
 */
class JavaMainTest {
    @Test
    void shouldConstructWithNoArgs() {
        try (var cli = new RichCLI<>(
                "java.test",
                new TestOptions())) {
            final String[] expected = {};
            assertArrayEquals(expected, cli.getOptions().getArgs(),
                    "Wrong arguments");
        }
    }

    @Test
    void shouldConstructWithArgs() {
        try (var cli = new RichCLI<>(
                "java.test",
                new TestOptions(),
                "-d", "arg1", "arg2")) {
            final String[] expected = {"arg1", "arg2"};
            assertArrayEquals(expected, cli.getOptions().getArgs(),
                    "Wrong arguments");
        }
    }
}
