package rs.helpkit.util.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Tyler Sedlar
 * @since 8/6/2017
 */
public class TSVListing {

    public final List<String[]> lines = new ArrayList<>();

    public static TSVListing load(String file) {
        TSVListing mapping = new TSVListing();
        try {
            Files.lines(Paths.get(file)).forEach(line -> mapping.lines.add(line.split("\t")));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to parse tsv: " + file);
        }
        return mapping;
    }
}