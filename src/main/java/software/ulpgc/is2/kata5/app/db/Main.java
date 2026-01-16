package software.ulpgc.is2.kata5.app.db;

import software.ulpgc.is2.kata5.app.*;
import software.ulpgc.is2.kata5.io.Store;
import software.ulpgc.is2.kata5.model.Movie;
import software.ulpgc.is2.kata5.tasks.HistogramBuilder;
import software.ulpgc.is2.kata5.viewmodel.Histogram;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Stream;

public class Main {
    private static final String DATABASE = "movies.db";

    public static void main(String[] args) {
        boolean notCreated = !new File(DATABASE).exists();

        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE)){
            connection.setAutoCommit(false);

            createTableIfNotExists(connection);
            DatabaseRecorder recorder = new DatabaseRecorder(connection);

            if(notCreated) {
                System.out.println("Importing movies to " + DATABASE);
                Stream<Movie> movies = new RemoteStore(MovieDeserializer::fromTsv)
                        .movies()
                        .filter(m -> m.year() >= 1900)
                        .filter(m -> m.year() <=2026);
                recorder.record(movies);
                System.out.println("Imported movies to " + DATABASE);
            }

            Store store = new DatabaseStore(connection);
            Stream<Movie> movies = store.movies();

            Histogram histogram = HistogramBuilder.with(movies).
                    title("Películas por año")
                    .x("Año")
                    .legend("Nº de películas").build(Movie::year);
            Desktop desktop = Desktop.with(store);
            desktop.display(histogram);
            desktop.setVisible(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createTableIfNotExists(Connection connection) throws SQLException {
        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS movies (" +
                        "title TEXT, duration INTEGER, year INTEGER)"
        );
    }
}
