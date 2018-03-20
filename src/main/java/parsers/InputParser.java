package parsers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public final class InputParser {

    private static final String SEPARATOR = " ";

    private InputParser() {
        //private constructor to prevent instantiation
    }

    public static String[] getLine(Stream<String> stream, int line) {
        if (stream == null) {
            return null;
        }

        Optional<String> firstLine = stream.skip(line).findFirst();
        String stringLine = firstLine.orElse(SEPARATOR);
        return stringLine.trim().split(SEPARATOR);
    }

    public static List<String> getMazeLines(Stream<String> stream, int line) {
        if (stream == null) {
            return null;
        }

        Stream<String> maze = stream.skip(line);
        return maze.collect(Collectors.toList());
    }
}
