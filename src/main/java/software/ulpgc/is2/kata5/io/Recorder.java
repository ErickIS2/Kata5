package software.ulpgc.is2.kata5.io;

import software.ulpgc.is2.kata5.model.Movie;

import java.util.stream.Stream;

public interface Recorder {
    void record(Stream<Movie> movies);
}

