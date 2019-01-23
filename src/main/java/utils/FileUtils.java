package utils;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    public static List<Path> listAllInDirectory(String path, Optional<String> includeRegex, Optional<String> excludeRegex) throws IOException {
        Stream<Path> allFiles = Files.walk(Paths.get(path)).filter(Files::isRegularFile);
        if (includeRegex.isPresent() && !includeRegex.get().isEmpty()) {
            allFiles = allFiles.filter(p -> p.toString().matches(includeRegex.get()));
        }
        if (excludeRegex.isPresent() && !excludeRegex.get().isEmpty()) {
            allFiles = allFiles.filter(p -> !p.toString().matches(excludeRegex.get()));
        }
        return allFiles.collect(Collectors.toList());
    }

}

