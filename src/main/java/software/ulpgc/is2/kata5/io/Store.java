package software.ulpgc.is2.kata5.io;

import software.ulpgc.is2.kata5.model.Movie;

import java.util.stream.Stream;

public interface Store {
    Stream<Movie> movies();
}
