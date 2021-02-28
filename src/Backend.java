import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.io.FileReader;
import java.io.Reader;

// --== CS400 File Header Information ==--
// Author: Bryanna Plaisir
// Email: plaisir@wisc.edu
// Notes: code for Backend MovieMapper
//
public class Backend implements BackendInterface {
    private List<MovieInterface> listOfMovies;;
    private LinkedList<genreInfo>[] genres;
    private LinkedList<ratingInfo>[] avgRatings;
    private int capacity = 10;
    private int size = 0;
    private int depth = 0;


    /*
     * class that stores genre as key and the MovieInterface object that has the associated genre as
     * one of its genres
     */
    public class genreInfo {
        private String key;
        private MovieInterface value;

        /*
         * constructor to set key and value
         */
        public genreInfo(String key, MovieInterface value) {
            this.key = key;
            this.value = value;
        }


        /*
         * getter method to retrieve key
         */
        public String getKey() {
            return this.key;
        }

        /*
         * getter method to retrieve Movie with said genre
         */
        public MovieInterface getValue() {
            return this.value;
        }
    }
    /*
     * class that stores rating as key and the MovieInterface object that has the associated rating
     * 
     * @author bryannaplaisir
     */
    public class ratingInfo {
        private String key;
        private MovieInterface value;
        private int size;
        private int capacity = 10;

        /*
         * constructor to set key and value
         */
        public ratingInfo(String key, MovieInterface value) {
            this.key = key;
            this.value = value;
        }

        /*
         * getter method to retrieve key
         */
        public String getKey() {
            return this.key;
        }

        /*
         * getter method to retrieve Movie with said rating
         */
        public MovieInterface getValue() {
            return this.value;
        }
    }


    /**
     * This constructor instantiates a backend with the command line arguments passed to the app when
     * started by the user. The arguments are expected to contain the path to the data file that the
     * backend will read in.
     *
     * @param args list of command line arguments passed to front end
     */
    public Backend(String[] args) {
        MovieDataReaderInterface movies = new MovieDataReaderDummy();
        Reader reader = null;

        try {
            reader = new FileReader(args[0]);
            listOfMovies = (List<MovieInterface>) movies.readDataSet(reader);
        } catch (Exception e) {
            // System.out.println("error trying to load data to readDataSet method");
        }

        this.genres = (LinkedList<genreInfo>[]) new LinkedList[capacity];
        this.avgRatings = (LinkedList<ratingInfo>[]) new LinkedList[capacity];
    }

    /**
     * A constructor that will create a backend from data that can be read in with a reader object.
     *
     * @param r A reader that contains the data in CSV format.
     */
    public Backend(Reader r) {
        MovieDataReaderInterface movies = new MovieDataReaderDummy();

        try {
            listOfMovies = (List<MovieInterface>) movies.readDataSet(r);
        } catch (Exception e) {
            System.out.println("error trying to load data to readDataSet method");
        }
        genres = (LinkedList<genreInfo>[]) new LinkedList[capacity];
        avgRatings = (LinkedList<ratingInfo>[]) new LinkedList[capacity];
    }

    /**
     * Method to add a genre that the user selected. It will output but not store the genres passed to
     * it.
     */
    @Override
    public void addGenre(String genre) {
        genreInfo newGenre = null;
        boolean added = false;


        if (genre == null) {
            return;
        }

        if (containsGenre(genre)) {
            return;
        }

        // checks hashtable size
        double loadfactor = (double) this.size / this.capacity;
        if (loadfactor >= .85) {
            reHashGenres();
        }

        // finds hashcode and hashcode index for genre
        int hashCode = genre.hashCode();
        int index = Math.abs(hashCode) % capacity;

        if (depth == 0) {
            this.genres[index] = new LinkedList<genreInfo>();
        }

        // iterates through list of movieNames and finds each movie with the
        // associated genre. Creates a new genreInfo object with key as the genre and value
        // as the movie and adds this object to the hashcode index of the genre
        for (int i = 0; i < listOfMovies.size(); i++) {
            if (listOfMovies.get(i).getGenres().contains(genre)) {
                newGenre = new genreInfo(genre, listOfMovies.get(i));
                genres[index].add(depth, newGenre);
                depth++;
                added = true;
            }

        }
        if (added == true) {
            this.size++;
        } else {
            newGenre = new genreInfo(genre, null);
            genres[index].add(depth, newGenre);
            this.size++;
        }

        // rechecks the size of the hashtable
        loadfactor = ((double) this.size) / (double) this.capacity;
        if (loadfactor >= .85) {
            reHashGenres();
            }
           
        this.depth = 0;
    }

    /**
     * Method to add a rating that the user selected. 
     */
    @Override
    public void addAvgRating(String rating) {
        ratingInfo newRating = null;
        boolean added = false;

        if (rating == null) {

            return;
        }
        double value = Double.parseDouble(rating);
        int rate = (int) value;
        if (rate < 0 || rate > 10) {
            return;
        }
        // checks to see if the list of avgRatings already contains the rating passed through method
        if (containsRating(String.valueOf(rate))) {
            return;
        }
        double loadfactor = ((double) this.size) / (double) this.capacity;

        if (loadfactor >= .85) {
            reHashRatings();
        }

        // finds hashcode and hashcode index for rating
        int hashCode = String.valueOf(rate).hashCode();
        int index = Math.abs(hashCode) % capacity;

        if (depth == 0) {
            this.avgRatings[index] = new LinkedList<ratingInfo>();
        }
        // converts rating to an integer to check against MovieInterface avgRatings


        // iterates through list of movieNames and finds each movie with the
        // associated rating. Creates a new ratingInfo object with key as the rating and value
        // as the movie and adds this object to the hashcode index of the rating
        for (int i = 0; i < listOfMovies.size(); i++) {
            if ((int) (float) listOfMovies.get(i).getAvgVote() == rate) {
                newRating = new ratingInfo(String.valueOf(rate), listOfMovies.get(i));
                avgRatings[index].add(depth, newRating);
                added = true;
                depth++;
            }

        }
        // rechecks hashtable size
        loadfactor = ((double) this.size) / (double) this.capacity;
        if (loadfactor >= .85) {
            reHashRatings();
        }

        if (added == true) {
            this.size++;
        } else {
            newRating = new ratingInfo(String.valueOf(rate), null);
            avgRatings[index].add(depth, newRating);
        }

        this.depth = 0;
    }

    /*
     * Method to increase the size of the hashtable once loadfactor has reached 85%
     */
    private void reHashGenres() {
        this.depth = 0;
        int newcap = 2 * this.capacity;
        this.capacity = newcap;
        LinkedList<genreInfo>[] newHash = this.genres;
        this.genres = (LinkedList<genreInfo>[]) new LinkedList[newcap];


        for (int i = 0; i < newHash.length; i++) {
            if (newHash[i] != null) {
                for (int j = 0; j < newHash[i].size(); j++) {
                    genreInfo node = newHash[i].get(j);
                    int hashcode = node.getKey().hashCode();
                    int index2 = Math.abs(hashcode) % this.capacity;

                    if (genres[index2] == null) {
                        genres[index2] = new LinkedList<genreInfo>();
                        genres[index2].add(depth, node);
                        continue;
                    } else {
                        for (int k = 0; k < genres[index2].size(); k++) {
                            if (genres[index2].get(k) != null) {
                                depth++;

                            }
                        }
                        genres[index2].add(depth, node);
                        depth = 0;
                    }
                }
            }
        }
    }

    /*
     * Method to increase the size of the hashtable once loadfactor has reached 85%
     */
    private void reHashRatings() {
        this.depth = 0;
        int newcap = 2 * this.capacity;
        this.capacity = newcap;
        LinkedList<ratingInfo>[] newHash = this.avgRatings;
        this.avgRatings = (LinkedList<ratingInfo>[]) new LinkedList[newcap];

        for (int i = 0; i < newHash.length; i++) {
            if (newHash[i] != null) {
                for (int j = 0; j < newHash[i].size(); j++) {
                    ratingInfo node = newHash[i].get(j);
                    int hashcode = node.getKey().hashCode();
                    int index2 = Math.abs(hashcode) % this.capacity;

                    if (avgRatings[index2] == null) {
                        avgRatings[index2] = new LinkedList<ratingInfo>();
                        avgRatings[index2].add(depth, node);
                        continue;
                    } else {
                        for (int k = 0; k < avgRatings[index2].size(); k++) {
                            if (avgRatings[index2].get(k) != null) {
                                depth++;

                            }
                        }
                        avgRatings[index2].add(depth, node);
                        depth = 0;
                    }
                }
            }
        }

    }

    /**
     * checks to see if the genre that is to be added to the list of genres is already within the list
     * @param key genre that is to be added to list of genres
     * @return True is genre list contains the genre. False otherwise
     */
    public boolean containsGenre(String key) {
        int hashCode = key.hashCode();
        int index = Math.abs(hashCode) % this.capacity;


        if (this.genres[index] == null) {
            return false;
        }
        for (int i = 0; i < genres[index].size(); i++) {
            String getKey = (String) this.genres[index].get(i).getKey();
            if (getKey.equals(key)) {
                return true;
            } else {
                depth++;
            }
        }

        return false;
    }

    /**
     * checks to see if the rating that is to be added to the list of avgRatings is already within the list
     * @param key rating that is to be added to list of ratings
     * @return True is rating list contains the rating. False otherwise
     */
    public boolean containsRating(String key) {
        int hashCode = key.hashCode();
        int index = Math.abs(hashCode) % this.capacity;


        if (this.avgRatings[index] == null) {
            return false;
        }
        for (int i = 0; i < avgRatings[index].size(); i++) {
            String getKey = (String) this.avgRatings[index].get(i).getKey();
            if (getKey.equals(key)) {
                return true;
            } else {
                depth++;
            }
        }

        return false;
    }

    /**
     * Method to remove a genre that the user selected. 
     */
    @Override
    public void removeGenre(String genre) {
        // determines hashcode and hashindex for genre
        
        if(genre == null) {
            return;
        }
        int hashCode = genre.hashCode();
        int index = Math.abs(hashCode) % this.capacity;

        // checks to see if the genre is within the list of genres
        if (!containsGenre(genre)) {
            return;
        }

        // removes each movie associated with the genre and sets hashcode index of this genre to
        // null to
        // remove genre
        if (genres[index] != null) {
            for (int i = 0; i < genres[index].size(); i++) {
                if (genres[index].get(i).getKey().equals(genre)) {
                    genres[index].remove(i);


                    if (genres[index].size() == 0) {
                        genres[index] = null;
                    }
                    this.size--;
                    return;
                }
            }
        }
    }

    /**
     * Method to remove a rating that the user selected. It will output but not remove the rating
     * passed to it from the backend object.
     */
    @Override
    public void removeAvgRating(String rating) {
        if(rating == null) {
            return;
        }
        // determines hashcode and hashindex for rating
        int hashCode = rating.hashCode();
        int index = Math.abs(hashCode) % this.capacity;

        // checks if rating to remove is within the list of ratings
        if (!containsRating(rating)) {
            return;
        }

        // removes each movie associated with the rating and sets hashcode index of this rating to
        // null to remove rating
        if (avgRatings[index] != null) {
            for (int i = 0; i < avgRatings[index].size(); i++) {
                if (avgRatings[index].get(i).getKey().equals(rating)) {
                    avgRatings[index].remove(i);


                    if (avgRatings[index].size() == 0) {
                        avgRatings[index] = null;
                    }
                    this.size--;
                    return;
                }
            }
        }
    }

    /**
     * Return the genres added to this backend object.
     */
    @Override
    public List<String> getGenres() {
        List<String> listGenres = new ArrayList<String>();

        for (int i = 0; i < this.genres.length; i++) {
            if (this.genres[i] != null) {
                listGenres.add(this.genres[i].get(0).getKey().toString());
            }
        }
        return listGenres;
    }

    /**
     * Return the ratings added to this backend object. 
     */
    @Override
    public List<String> getAvgRatings() {
        List<String> listRatings = new ArrayList<String>();

        for (int i = 0; i < this.avgRatings.length; i++) {
            if (this.avgRatings[i] != null) {
                for (int j = 0; j < this.avgRatings[i].size(); j++) {
                    if (this.avgRatings[i].get(j) != null) {
                        listRatings.add(this.avgRatings[i].get(j).getKey().toString());
                    }
                }
            }
        }
        return listRatings;
    }

    /**
     * Returns the number of movies. 
     */
    @Override
    public int getNumberOfMovies() {
        return listOfMovies.size();
    }

    /**
     * Returns all genres in the dataset.
     */
    @Override
    public List<String> getAllGenres() {
        List<String> allGenres = new ArrayList<String>();

        for (int i = 0; i < listOfMovies.size(); i++) {
            List<String> listGenres = listOfMovies.get(i).getGenres();

            for (int j = 0; j < listGenres.size(); j++) {
                if (!allGenres.contains(listGenres.get(j))) {
                    allGenres.add(listGenres.get(j));
                }
            }
        }

        return allGenres;
    }

    /**
     * Returns the movies that match the ratings and genres. 
     * Ask about this method in Office hoURS
     */
    @Override
    public List<MovieInterface> getThreeMovies(int startingIndex) {
        List<MovieInterface> movieList = new ArrayList<MovieInterface>();

        for (int i = startingIndex; i < listOfMovies.size(); i++) {
            movieList.add(listOfMovies.get(i));
        }

        for (int i = 0; i < movieList.size(); i++) {
            for (int j = 1; j < movieList.size(); j++)
                if (movieList.get(i).getAvgVote() < movieList.get(j).getAvgVote()) {
                    MovieInterface swap = movieList.get(j);
                    movieList.set(j, movieList.get(i));
                    movieList.set(i, swap);
                }

        }
        return movieList;

    }
}
