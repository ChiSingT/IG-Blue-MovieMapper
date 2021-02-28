// --== CS400 File Header Information ==--
// Name: Kristopher Navar
// Email: knavar@wisc.edu
// Team: IG Blue
// Role: Data Wrangler
// TA: Sid
// Lecturer: Dahl
// Notes to Grader: 

import java.util.List;

/**
 * The Movie class extends the MovieInterface. This class contains methods that simply set different
 * properties to their corresponding property and other methods that allow the retrieval of these
 * properties.
 *
 * @author kristophernavar
 */
public class Movie implements MovieInterface {

    // initialize variables
    String title;
    Integer year;
    List<String> genres;
    String director;
    String description;
    Float avgVote;


    /**
     * This method returns the title of the movie
     * @return The title of the movie
     */
    @Override
    public String getTitle() {
        return this.title;
    }

    /**
     * This method returns the year of the movie
     * @return The year of the movie
     */
    @Override
    public Integer getYear() {
        return this.year;
    }

    /**
     * This method returns the genres of the movie
     * @return The genres of the movie
     */
    @Override
    public List<String> getGenres() {
        return this.genres;
    }

    /**
     * This method returns the directors of the movie
     * @return The directors of the movie
     */
    @Override
    public String getDirector() {
        return this.director;
    }

    /**
     * This method returns the description of the movie
     * @return The description of the movie
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * This method returns the average vote of the movie
     * @return The average vote of the movie
     */
    @Override
    public Float getAvgVote() {
        return this.avgVote;
    }

    /**
     * This method sets the title of the movie
     * @param newTitle The inputed title to set
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     * This method sets the year of the movie
     * @param newYear The inputed year to set
     */
    public void setYear(Integer newYear) {
        this.year = newYear;
    }

    /**
     * This method sets the genres of the movie
     * @param newGenres The inputed genres to set
     */
    public void setGenres(List<String> newGenres) {
        this.genres = newGenres;
    }

    /**
     * This method sets the director of the movie
     * @param newDirector The inputed director to set
     */
    public void setDirector(String newDirector) {
        this.director = newDirector;
    }

    /**
     * This method sets the description of the movie
     * @param newDescription The description title to set
     */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * This method sets the average vote of the movie
     * @param newAvgVote The average vote to set
     */
    public void setAvgVote(Float newAvgVote) {
        this.avgVote = newAvgVote;
    }

    /**
     * This method compares the average votes of movies. 
     * @return +1 if average vote of movie is less than the average vote of the
     * inputed movie, -1 if reversed, 0 if movie title is the same as inputed
     * movie.
     */
    @Override
    public int compareTo(MovieInterface otherMovie) {
        if (this.getTitle().equals(otherMovie.getTitle())) {
            return 0;
            // sort by rating
        } else if (this.getAvgVote() < otherMovie.getAvgVote()) {
            return +1;
        } else {
            return -1;
        }

    }

}
