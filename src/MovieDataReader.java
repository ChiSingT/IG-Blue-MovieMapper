import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.DataFormatException;

public class MovieDataReader implements MovieDataReaderInterface {


    @Override
    public List<MovieInterface> readDataSet(Reader inputFileReader)
        throws FileNotFoundException, IOException, DataFormatException {

        // initialize variables
        BufferedReader reader = new BufferedReader(inputFileReader);
        List<MovieInterface> movieList = new LinkedList<MovieInterface>();

        String line = "";
        String seperatedBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

        String title;
        Integer year;
        List<String> genres;
        String director;
        String description;
        Float avgVote;

        // skips first line of file
        reader.readLine();
        // parse file
        while ((line = reader.readLine()) != null) {
            String[] eachLine = line.split(seperatedBy, -1);
            Movie eachMovie = new Movie();

            // remove this later, it is for testing purposes only
            //System.out.println(eachLine[3]);

            title = eachLine[0];
            year = Integer.parseInt(eachLine[2]);
            genres = Arrays.asList(eachLine[3].split(","));
            director = eachLine[7];
            description = eachLine[9];
            avgVote = Float.parseFloat(eachLine[12]);

            eachMovie.setTitle(title);
            eachMovie.setYear(year);
            eachMovie.setGenres(genres);
            eachMovie.setDirector(director);
            eachMovie.setDescription(description);
            eachMovie.setAvgVote(avgVote);

            movieList.add(eachMovie);

        }
        reader.close();
        return movieList;
    }


    // for testing purposes
    public static void main(String[] args) throws IOException, DataFormatException {
        BufferedReader br = new BufferedReader(new StringReader(
            "title,original_title,year,genre,duration,country,language,director,writer,production_company,actors,description,avg_vote\n"
                + "The Source of Shadows,The Source of Shadows,2020,Horror,83,USA,English,\"Ryan Bury, Jennifer Bonior\",\"Jennifer Bonior, Trevor Botkin\",Four Thieves Productions,\"Ashleigh Allard, Tom Bonington, Eliane Gagnon, Marissa Kaye Grinestaff, Jenna Heffernan, Joshua Hummel, Janice Kingsley, Chris Labasbas, Jared Laufree, Dominic Lee, Vic May, Sienna Mazzone, Lizzie Mounter, Grace Mumm, Ashley Otis\",\"A series of stories woven together by one of our most primal fears, the fear of the unknown.\",3.5\n"
                + "The Insurrection,The Insurrection,2020,Action,90,USA,English,Rene Perez,Rene Perez,,\"Michael Paré, Wilma Elles, Joseph Camilleri, Rebecca Tarabocchia, Jeanine Harrington, Malorie Glavan, Danner Boyd, Michael Cendejas, Woody Clendenen, Keely Dervin, Aaron Harvey, Tony Jackson, Michael Jarrod, Angelina Karo, Bernie Kelly\",The director of the largest media company wants to expose how left-wing powers use film to control populations.,2.9\n"
                + "Valley Girl,Valley Girl,2020,\"Comedy, Musical, Romance\",102,USA,English,Rachel Lee Goldenberg,\"Amy Talkington, Andrew Lane\",Sneak Preview Productions,\"Jessica Rothe, Josh Whitehouse, Jessie Ennis, Ashleigh Murray, Chloe Bennet, Logan Paul, Mae Whitman, Mario Revolori, Rob Huebel, Judy Greer, Alex Lewis, Alex MacNicoll, Danny Ramirez, Andrew Kai, Allyn Rachel\",\"Set to a new wave '80s soundtrack, a pair of young lovers from different backgrounds defy their parents and friends to stay together. A musical adaptation of the 1983 film.\",5.4\n"
        ));
        System.out.print(new StringReader(
            "title,original_title,year,genre,duration,country,language,director,writer,production_company,actors,description,avg_vote\n"
                + "The Source of Shadows,The Source of Shadows,2020,Horror,83,USA,English,\"Ryan Bury, Jennifer Bonior\",\"Jennifer Bonior, Trevor Botkin\",Four Thieves Productions,\"Ashleigh Allard, Tom Bonington, Eliane Gagnon, Marissa Kaye Grinestaff, Jenna Heffernan, Joshua Hummel, Janice Kingsley, Chris Labasbas, Jared Laufree, Dominic Lee, Vic May, Sienna Mazzone, Lizzie Mounter, Grace Mumm, Ashley Otis\",\"A series of stories woven together by one of our most primal fears, the fear of the unknown.\",3.5\n"
                + "The Insurrection,The Insurrection,2020,Action,90,USA,English,Rene Perez,Rene Perez,,\"Michael Paré, Wilma Elles, Joseph Camilleri, Rebecca Tarabocchia, Jeanine Harrington, Malorie Glavan, Danner Boyd, Michael Cendejas, Woody Clendenen, Keely Dervin, Aaron Harvey, Tony Jackson, Michael Jarrod, Angelina Karo, Bernie Kelly\",The director of the largest media company wants to expose how left-wing powers use film to control populations.,2.9\n"
                + "Valley Girl,Valley Girl,2020,\"Comedy, Musical, Romance\",102,USA,English,Rachel Lee Goldenberg,\"Amy Talkington, Andrew Lane\",Sneak Preview Productions,\"Jessica Rothe, Josh Whitehouse, Jessie Ennis, Ashleigh Murray, Chloe Bennet, Logan Paul, Mae Whitman, Mario Revolori, Rob Huebel, Judy Greer, Alex Lewis, Alex MacNicoll, Danny Ramirez, Andrew Kai, Allyn Rachel\",\"Set to a new wave '80s soundtrack, a pair of young lovers from different backgrounds defy their parents and friends to stay together. A musical adaptation of the 1983 film.\",5.4\n"
        ));
        MovieDataReader m = new MovieDataReader();

        List<MovieInterface> movie = m.readDataSet(br);
        for (int i = 0; i < movie.size(); ++i) {
            System.out.println(movie.get(i).getGenres());
        }


    }


}
