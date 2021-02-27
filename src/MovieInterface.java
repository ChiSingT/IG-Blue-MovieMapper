import java.util.List;

public interface MovieInterface extends Comparable<MovieInterface> {

  String getTitle();

  Integer getYear();

  List<String> getGenres();

  String getDirector();

  String getDescription();

  Float getAvgVote();

  // from super interface Comparable
  int compareTo(MovieInterface otherMovie);

}
