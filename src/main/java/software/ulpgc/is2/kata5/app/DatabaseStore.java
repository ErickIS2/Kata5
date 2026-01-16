package software.ulpgc.is2.kata5.app;

import software.ulpgc.is2.kata5.io.Store;
import software.ulpgc.is2.kata5.model.Movie;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

public class DatabaseStore implements Store {
    private final Connection connection;

    public DatabaseStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Stream<Movie> movies() {
        try{
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM movies");
            return Stream.generate(() -> nextMovie(rs))
                    .takeWhile(Objects::nonNull)
                    .onClose(() -> close(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Movie nextMovie(ResultSet rs) {
        try{
            if(!rs.next()) return null;
            String title = rs.getString("title");
            int year = rs.getInt("year");
            int duration = rs.getInt("duration");
            return new Movie(title, year, duration);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void close(ResultSet rs) {
        try{
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
