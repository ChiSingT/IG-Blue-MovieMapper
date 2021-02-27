import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import HashTableMap.Node;



public class Backend<KeyType, ValueType> implements BackendInterface {
    private List<MovieInterface> movieNames;
    private LinkedList<genreInfo>[] genres;
    private LinkedList<ratingInfo>[] avgRatings;
    private int capacity = 10;
    private int size = 0;
    private int depth = 0;

    // do i need to specify the type passed into each of these classes
    public class genreInfo<KeyType, ValueType> {
        private KeyType key;
        private ValueType value;

        public genreInfo(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }

        public KeyType getKey() {
            return this.key;
        }

        public ValueType getValue() {
            return this.value;
        }
    }
    public class ratingInfo<KeyType, ValueType> {
        private KeyType key;
        private ValueType value;
        private int size;
        private int capacity = 10;


        public ratingInfo(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }

        public KeyType getKey() {
            return this.key;
        }

        public ValueType getValue() {
            return this.value;
        }
    }



    public Backend(File data) {
        MovieReaderInterface movies = new MovieReaderInterface();
        movieNames = (List<MovieInterface>) movies.readDataSet(data);
        genres = (LinkedList<genreInfo>[]) new LinkedList[capacity];
        avgRatings = (LinkedList<ratingInfo>[]) new LinkedList[capacity];

    }

    @Override
    public void addGenre(String genre) {
        genreInfo<String, MovieInterface> newGenre = null;
        if (genre == null) {

            return;
        }
        if (containsGenre(genre)) {
            return;
        }

        double loadfactor = (double) this.size / this.capacity;
        if (loadfactor >= .85) {
            reHashGenres();
        }


        int hashCode = genre.hashCode();
        int index = Math.abs(hashCode) % capacity;

        for (int i = 0; i < movieNames.size(); i++) {
            if (movieNames.get(i).getGenres().contains(genre)) {
                newGenre = new genreInfo<String, MovieInterface>(genre, movieNames.get(i));
                genres[index].add(depth, newGenre);
                depth++;
            }

        }

        loadfactor = ((double) this.size) / (double) this.capacity;
        if (loadfactor >= .85) {
            reHashRatings();
        }

        this.depth = 0;
        this.size++;
    }

    @Override
    public void addAvgRating(String rating) {
        ratingInfo<String, MovieInterface> newRating = null;
        double value = Double.parseDouble(rating);
        
        if (rating == null) {

            return;
        }
        if(value < 0 || value > 10) {
            return;          
        }
        if (containsRating(rating)) {
            return;
        }
        double loadfactor = ((double) this.size) / (double) this.capacity;

        if (loadfactor >= .85) {
            reHashRatings();
        }

        int hashCode = rating.hashCode();
        int index = Math.abs(hashCode) % capacity;
        int rate = Integer.parseInt(rating);

        for (int i = 0; i < movieNames.size(); i++) {
            if ((int) (float) movieNames.get(i).getAvgVote() == rate) {
                newRating = new ratingInfo<String, MovieInterface>(String.valueOf(rate), movieNames.get(i));
                avgRatings[index].add(depth, newRating);
                depth++;
            }

        }
        loadfactor = ((double) this.size) / (double) this.capacity;
        if (loadfactor >= .85) {
            reHashRatings();
        }

        this.depth = 0;
        this.size++;
    }

    private void reHashGenres() {
        int newcap = 2 * this.capacity;
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

    private void reHashRatings() {
        int newcap = 2 * this.capacity;
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

    public boolean containsGenre(String key) {
        int hashCode = key.hashCode();
        int index = Math.abs(hashCode) % this.capacity;


        if (this.genres[index] == null) {
            return false;
        }
        for (int i = 0; i < genres[index].size(); i++) {
            String getKey = (String)this.genres[index].get(i).getKey();
            if (getKey.equals(key)) {
                return true;
            } else {
                depth++;
            }
        }

        return false;
    }

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

    @Override
    public void removeGenre(String genre) {
        int hashCode = genre.hashCode();
        int index = Math.abs(hashCode) % this.capacity;

        if (!containsGenre(genre)) {
            System.out.println("UHOH! this genre is not in the list!");
            return;
        }
        // if I'm removing a genre have to remove the linked movies to the genre as well.
        if (genres[index] != null) {
            for (int i = 0; i < genres[index].size(); i++) {
                if (genres[index].get(i).getKey() == genre) {
                    genres[index].remove(i);


                    if (i == 0) {
                        genres[index] = null;
                    }
                    this.size--;
                }
            }
        }
    }

    @Override
    public void removeAvgRating(String rating) {
        int hashCode = rating.hashCode();
        int index = Math.abs(hashCode) % this.capacity;

        if (!containsRating(rating)) {
            System.out.println("UHOH! this ratingis not in the list!");
            return;
        }
        if (avgRatings[index] != null) {
            for (int i = 0; i < avgRatings[index].size(); i++) {
                if (avgRatings[index].get(i).getKey() == rating) {
                    avgRatings[index].remove(i);


                    if (i == 0) {
                        avgRatings[index] = null;
                    }
                    this.size--;
                }
            }
        }
    }

    @Override
    public List<String> getGenres() {
        List<String> listGenres = new ArrayList<String>();

        for (int i = 0; i < this.genres.length; i++) {
            listGenres.add(this.genres[i].get(0).getKey().toString());
        }
        return listGenres;
    }

    @Override
    public List<String> getAvgRatings() {
        List<String> listRatings = new ArrayList<String>();

        for (int i = 0; i < this.avgRatings.length; i++) {
            listRatings.add(this.avgRatings[i].get(0).getKey().toString());
        }
        return listRatings;
    }

    @Override
    public int getNumberOfMovies() {
        return movieNames.size();
    }

    @Override
    public List<MovieInterface> getThreeMovies(int startingIndex) {
        MovieInterface movie1 = null;
        MovieInterface movie2 = null;
        MovieInterface movie3 = null;


        if (startingIndex + 2 < movieNames.size()) {
            movie1 = movieNames.get(startingIndex);
            movie2 = movieNames.get(startingIndex + 1);
            movie3 = movieNames.get(startingIndex + 2);
        } else if (startingIndex + 1 < movieNames.size()) {
            movie1 = movieNames.get(startingIndex);
            movie2 = movieNames.get(startingIndex + 1);
        } else if (startingIndex < movieNames.size()) {
            movie1 = movieNames.get(startingIndex);
        }
        List<MovieInterface> movieList = new ArrayList<MovieInterface>();



        if (movie1 == null) {
            return movieList;
        }

        if (movie2 == null) {
            movieList.add(movie1);
            return movieList;
        }
        if (movie3 == null) {
            if (movie1.getAvgVote() >= movie2.getAvgVote()) {
                movieList.add(movie1);
                movieList.add(movie2);
                return movieList;
            } else {
                movieList.add(movie2);
                movieList.add(movie1);
                return movieList;
            }
        }

        if (movie1.getAvgVote() > movie2.getAvgVote() > movie3.getAvgVote()) {
            movieList.add(movie1);
            movieList.add(movie2);
            movieList.add(movie3);
            return movieList;
        } else if (movie2.getAvgVote() > movie1.getAvgVote() > movie3.getAvgVote()) {
            movieList.add(movie2);
            movieList.add(movie1);
            movieList.add(movie3);
            return movieList;

        } else if ((movie3.getAvgVote() > movie2.getAvgVote() > movie1.getAvgVote()) {
            movieList.add(movie3);
            movieList.add(movie2);
            movieList.add(movie1);
            return movieList;
        }

        return movieList;

    }

    @Override
    public List<String> getAllGenres() {
        List<String> allGenres = new ArrayList<String>();

        for (int i = 0; i < movieNames.size(); i++) {
            List<String> listGenres = movieNames.get(i).getGenres();

            for (int j = 0; j < listGenres.size(); j++) {
                if (!allGenres.contains(listGenres.get(j))) {
                    allGenres.add(listGenres.get(j));
                }
            }
        }

        return allGenres;
    }



}
