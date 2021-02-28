import java.util.List;

public class Movie implements MovieInterface {

    String title;
    Integer year;
    List<String> genres;
    String director;
    String description;
    Float avgVote;
    

    @Override
    public String getTitle() {
        return this.title;
    }


    @Override
    public Integer getYear() {
        return this.year;
    }


    @Override
    public List<String> getGenres() {
        return this.genres;
    }


    @Override
    public String getDirector() {
        return this.director;
    }


    @Override
    public String getDescription() {
        return this.description;
    }


    @Override
    public Float getAvgVote() {
        return this.avgVote;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public void setYear(Integer newYear) {
        this.year = newYear;
    }

    public void setGenres(List<String> newGenres) {
        this.genres = newGenres;
    }

    public void setDirector(String newDirector) {
        this.director = newDirector;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setAvgVote(Float newAvgVote) {
        this.avgVote = newAvgVote;
    }


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
