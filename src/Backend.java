import java.util.ArrayList;
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
    private List<MovieInterface> listOfMovies;
    private List<MovieInterface> MovieObjects;
    private LinkedList<genreInfo>[] genres;
    private LinkedList<ratingInfo>[] avgRatings;
    private int ratingcapacity = 10;
    private int genresize = 0;
    private int ratingsize = 0;
    private int genrecapacity = 10;
    private int depth = 0;


    /*
     * class that stores genre as key and the MovieInterface object that has the associated genres
     * as one of its genres
     */
    public class genreInfo {
        private String key;
        private List<MovieInterface> value;

        /*
         * constructor to set key and value
         */
        public genreInfo(String key, List<MovieInterface> value) {
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
         * getter method to retrieve list of movies with said genre
         */
        public List<MovieInterface> getValue() {
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
        private List<MovieInterface> value;
        private int size;
        private int capacity = 10;

        /*
         * constructor to set key and value
         */
        public ratingInfo(String key, List<MovieInterface> value) {
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
        public List<MovieInterface> getValue() {
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
        MovieDataReader movies = new MovieDataReader();
        Reader reader = null;
        this.MovieObjects = new ArrayList<MovieInterface>();

        try {
            
                reader = new FileReader(args[0]);
            this.MovieObjects = movies.readDataSet(reader);
        } catch (Exception e) {
        }

        this.genres = (LinkedList<genreInfo>[]) new LinkedList[genrecapacity];
        this.avgRatings = (LinkedList<ratingInfo>[]) new LinkedList[ratingcapacity];
        this.listOfMovies = new ArrayList();
    }

    /**
     * A constructor that will create a backend from data that can be read in with a reader object.
     *
     * @param r A reader that contains the data in CSV format.
     */
    public Backend(Reader r) {
        MovieDataReader movies = new MovieDataReader();
        this.MovieObjects = new ArrayList();

        try {
            this.MovieObjects = movies.readDataSet(r);
        } catch (Exception e) {
        }
        genres = (LinkedList<genreInfo>[]) new LinkedList[genrecapacity];
        avgRatings = (LinkedList<ratingInfo>[]) new LinkedList[ratingcapacity];
        this.listOfMovies = new ArrayList();
    }

    /**
     * Method to add a genre that the user selected. It will output but not store the genres passed to
     * it.
     */
    @Override
    public void addGenre(String genre) {
        List<MovieInterface> add = new ArrayList<MovieInterface>();
        genreInfo newGenre = null;


        if (genre == null) {
            return;
        }

        // checks hashtable size
        double loadfactor = (double) this.genresize / this.genrecapacity;
        if (loadfactor >= .85) {
            reHashGenres();
        }
        
        if (containsGenre(genre)) {
            return;
        }

        // finds hashcode and hashcode index for genre
        int hashCode = genre.hashCode();
        int index = Math.abs(hashCode) % genrecapacity;

        if (depth == 0) {
            this.genres[index] = new LinkedList<genreInfo>();
        }

        // iterates through list of movieNames and finds each movie with the
        // associated genre. Creates a new genreInfo object with key as the genre and value
        // as the movie and adds this object to the hashcode index of the genre
        for (int i = 0; i < MovieObjects.size(); i++) {
            if (MovieObjects.get(i).getGenres().contains(genre)) {
                if(!this.listOfMovies.contains(MovieObjects.get(i))){
                    this.listOfMovies.add(MovieObjects.get(i));
                }
                add.add(MovieObjects.get(i));
            }

        }
        newGenre = new genreInfo(genre, add);
        genres[index].add(depth, newGenre);
        updateListMovies();
        this.depth = 0;
        this.genresize++;

    }

    /**
     * Method to add a rating that the user selected. 
     */
    @Override
    public void addAvgRating(String rating) {
        List<MovieInterface> add = new ArrayList<MovieInterface>();
        ratingInfo newRating = null;

        if (rating == null) {

            return;
        }
        double value = Double.parseDouble(rating);
        int rate = (int) value;
        if (rate < 0 || rate > 10) {
            return;
        }
        double loadfactor = ((double) this.ratingsize) / (double) this.ratingcapacity;

        if (loadfactor >= .85) {
            reHashRatings();
        }

        
        // checks to see if the list of avgRatings already contains the rating passed through method
        if (containsRating(String.valueOf(rate))) {
            return;
        }
     


        // finds hashcode and hashcode index for rating
        int hashCode = String.valueOf(rate).hashCode();
        int index = Math.abs(hashCode) % ratingcapacity;

        if (depth == 0) {
            this.avgRatings[index] = new LinkedList<ratingInfo>();
        }
        // converts rating to an integer to check against MovieInterface avgRatings


        // iterates through list of movieNames and finds each movie with the
        // associated rating. Creates a new ratingInfo object with key as the rating and value
        // as the movie and adds this object to the hashcode index of the rating
        for (int i = 0; i < MovieObjects.size(); i++) {
            if ((int) (float) MovieObjects.get(i).getAvgVote() == rate) {
                add.add(MovieObjects.get(i));
            }

        }

        newRating = new ratingInfo(String.valueOf(rate), add);
        avgRatings[index].add(depth, newRating);
        this.depth = 0;
        this.ratingsize++;
        updateListMovies();

        // rechecks hashtable size
        loadfactor = ((double) this.ratingsize) / (double) this.ratingcapacity;
        if (loadfactor >= .85) {
            reHashRatings();
        }

    }
    /**
     * method to update listOfMovies once genres has been added or rating has been removed
     */
    private void updateListMovies() {

        for (int i = 0; i < this.genres.length; i++) {
            if (genres[i] != null) {
                for (int k = 0; k < this.genres[i].size(); k++) {
                    for (int j = 0; j < this.listOfMovies.size(); j++) {
                        if (!listOfMovies.get(j).getGenres()
                            .contains(this.genres[i].get(k).getKey())) {
                            listOfMovies.remove(listOfMovies.get(j));
                        }
                    }
                }
            }
        }

    }

    /*
     * Method to increase the size of the hashtable once loadfactor has reached 85%
     */
    private void reHashGenres() {
        this.depth = 0;
        int newcap = 2 * this.genrecapacity;
        this.genrecapacity = newcap;
        LinkedList<genreInfo>[] newHash = this.genres;
        this.genres = (LinkedList<genreInfo>[]) new LinkedList[newcap];


        for (int i = 0; i < newHash.length; i++) {
            if (newHash[i] != null) {
                for (int j = 0; j < newHash[i].size(); j++) {
                    genreInfo node = newHash[i].get(j);
                    int hashcode = node.getKey().hashCode();
                    int index2 = Math.abs(hashcode) % this.genrecapacity;

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
        int newcap = 2 * this.ratingcapacity;
        this.ratingcapacity = newcap;
        LinkedList<ratingInfo>[] newHash = this.avgRatings;
        this.avgRatings = (LinkedList<ratingInfo>[]) new LinkedList[newcap];

        for (int i = 0; i < newHash.length; i++) {
            if (newHash[i] != null) {
                for (int j = 0; j < newHash[i].size(); j++) {
                    ratingInfo node = newHash[i].get(j);
                    int hashcode = node.getKey().hashCode();
                    int index2 = Math.abs(hashcode) % this.ratingcapacity;

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
        int index = Math.abs(hashCode) % this.genrecapacity;


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
        int index = Math.abs(hashCode) % this.ratingcapacity;


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
        List<MovieInterface> movies = new ArrayList<MovieInterface>();
        // determines hashcode and hashindex for genre

        if (genre == null) {
            return;
        }
        int hashCode = genre.hashCode();
        int index = Math.abs(hashCode) % this.genrecapacity;

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
                    movies = genres[index].get(i).getValue();
                    genres[index].remove(i);


                    if (genres[index].size() == 0) {
                        genres[index] = null;
                    }
                    this.genresize--;
                    
                    for(int o = 0; o < movies.size(); o++) {
                        if(listOfMovies.contains(movies.get(o))) {
                            listOfMovies.remove(movies.get(o));
                    }
                   }
                    addtoListMovies();

                    return;
                }
            }
        }
    }
    private void addtoListMovies() {
       List<String> allGenres = getGenres(); 
       List<String> movieGenres;
       int num = 0;
        
            for(int j = 0; j < MovieObjects.size(); j++) {
                movieGenres = MovieObjects.get(j).getGenres();
                for(int i = 0; i < allGenres.size(); i++) {
                    if(movieGenres.contains(allGenres.get(i))) {
                        num += 1;
                    }
                    
            }
                if(num == allGenres.size()) {
                    listOfMovies.add(MovieObjects.get(j));
                    num = 0;
                }
            
        }
    }

    /**
     * Method to remove a rating that the user selected. It will output but not remove the rating
     * passed to it from the backend object.
     */
    @Override
    public void removeAvgRating(String rating) {
        List<MovieInterface> movies = new ArrayList<MovieInterface>();
        if (rating == null) {
            return;
        }
        // determines hashcode and hashindex for rating
        int hashCode = rating.hashCode();
        int index = Math.abs(hashCode) % this.ratingcapacity;

        // checks if rating to remove is within the list of ratings
        if (!containsRating(rating)) {
            return;
        }

        // removes each movie associated with the rating and sets hashcode index of this rating to
        // null to remove rating
        if (avgRatings[index] != null) {
            for (int i = 0; i < avgRatings[index].size(); i++) {
                if (avgRatings[index].get(i).getKey().equals(rating)) {
                    movies = avgRatings[index].get(i).getValue();
                    avgRatings[index].remove(i);


                    if (avgRatings[index].size() == 0) {
                        avgRatings[index] = null;
                    }
                    this.ratingsize--;

                    for (int k = 0; k < listOfMovies.size(); k++) {
                        if ((int) (float) listOfMovies.get(k).getAvgVote() == Integer
                            .valueOf(rating)) {
                            listOfMovies.remove(listOfMovies.get(k));
                        }
                    }

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
                for (int j = 0; j < this.genres[i].size(); j++) {
                    listGenres.add(this.genres[i].get(j).getKey().toString());
                }
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
                    listRatings.add(this.avgRatings[i].get(j).getKey().toString());
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

        for (int i = 0; i < MovieObjects.size(); i++) {
            List<String> listGenres = MovieObjects.get(i).getGenres();

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
     */
    @Override
    public List<MovieInterface> getThreeMovies(int startingIndex) {
        List<MovieInterface> movieList = new ArrayList<MovieInterface>();

        
        for (int i = startingIndex; i < listOfMovies.size(); i++) {
            if( i >= startingIndex + 3) {
                break;
            }
            else {
            movieList.add(listOfMovies.get(i));
            }
        }
        
        for(int i = 0; i < movieList.size(); i++) {
        }

        for (int i = 0; i < movieList.size(); i++) {
            for (int j = i + 1; j < movieList.size(); j++) {
                if (movieList.get(i).getAvgVote() < movieList.get(j).getAvgVote()) {
                    MovieInterface swap = movieList.get(j);
                    movieList.set(j, movieList.get(i));
                    movieList.set(i, swap);
                }
            }
        }
        return movieList;

    }
}
