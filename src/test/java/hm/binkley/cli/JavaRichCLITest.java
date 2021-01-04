package hm.binkley.cli;

import org.jline.reader.Completer;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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

    @Test
    void shouldConstructWithCustomEverything() throws IOException {
        final var options = new TestOptions();
        final Completer completer = (reader, line, candidates) -> {
        };
        final var terminal = TerminalBuilder.builder()
                .dumb(true)
                .build();
        final var lineReader = LineReaderBuilder.builder()
                .appName(terminal.getName())
                .completer(completer)
                .terminal(terminal)
                .build();

        // Test that:
        // 1. Compiles
        // 2. Does not throw
        new RichCLI<>(
                options,
                terminal,
                completer,
                lineReader,
                "-d", "arg1", "arg2");
    }
}
