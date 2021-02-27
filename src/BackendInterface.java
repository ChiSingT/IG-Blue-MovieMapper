import java.util.List;

public interface BackendInterface {

  void addGenre(String genre);

  void addAvgRating(String rating);

  void removeGenre(String genre);

  void removeAvgRating(String rating);

  List<String> getGenres();

  List<String> getAvgRatings();

  int getNumberOfMovies();

  List<MovieInterface> getThreeMovies(int startingIndex);

  List<String> getAllGenres();

}
