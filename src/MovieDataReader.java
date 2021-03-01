// --== CS400 File Header Information ==--
// Name: Kristopher Navar
// Email: knavar@wisc.edu
// Team: IG Blue
// Role: Data Wrangler
// TA: Sid
// Lecturer: Dahl
// Notes to Grader: 

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

/**
 * The MovieDataReader class extends the MovieDataReaderInterface. This class
 * contains a readDataSet method which works to read the inputed file. A more
 * in-depth description is below.
 * 
 * @author kristophernavar
 *
 */
public class MovieDataReader implements MovieDataReaderInterface{


    /**
     * The readDataSet method works to read an inputed file and return a list of
     * movies with only the following properties: title, year, genres, directors,
     * description, and average vote. The method also ensures that the number of 
     * columns for each movie is equal to the number of columns of the header line.
     * Also, the method will throw an IOException if there is a problem reading 
     * the file and will throw a FileNotFoundException if the inputed file is 
     * not found.
     */
    @Override
    public List<MovieInterface> readDataSet(Reader inputFileReader)
        throws FileNotFoundException, IOException, DataFormatException {
        
        // initialize variables
        BufferedReader reader = new BufferedReader(inputFileReader);        
        List<MovieInterface> movieList = new LinkedList<MovieInterface>();
        String title;
        Integer year;
        List<String> genres;
        String director;
        String description;
        Float avgVote;
        
        String line = "";
        String seperatedBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        
                
        
        
        try {
            // skips first line of file
            reader.readLine();
            // parse file
            while ((line = reader.readLine()) != null){
               
                String[] eachLine = line.split(seperatedBy, - 1);                  
                Movie eachMovie = new Movie();
                
                // ensures that each line has equal number of columns as header line
                if (eachLine.length != 13) {
                    throw new DataFormatException("ERROR: Line contains incorrect number of columns.");
                }
                              
                // finds specified properties in data set
                title = eachLine[0];
                year = Integer.parseInt(eachLine[2]);
                genres = Arrays.asList(eachLine[3].split(","));
                director = eachLine[7];
                description = eachLine[11];
                avgVote = Float.parseFloat(eachLine[12]);
                
                // deletes quotes from genres and accounts for extra spaces in list
                if (genres.size() != 1) { 
                    for (int i = 0; i < genres.size(); ++i) {
                        StringBuilder sb = new StringBuilder(genres.get(i));
                        sb.deleteCharAt(0);
                        genres.set(i, sb.toString());
                    }
                    
                    // deletes last quote from last element
                    String lastGenre = genres.get(genres.size() - 1);
                    StringBuilder sb2 = new StringBuilder(lastGenre);
                       
                    sb2.deleteCharAt(lastGenre.length() - 1);               
                    lastGenre = sb2.toString();                    
                    genres.set(genres.size() - 1, lastGenre);
                                                    
                }
                
                // deletes the quotes from movies with multiple directors
                if (director.contains("\"")) {
                    director = director.replace("\"", "");
                    
                }
                                     
                // sets each property
                eachMovie.setTitle(title);
                eachMovie.setYear(year);
                eachMovie.setGenres(genres);
                eachMovie.setDirector(director);
                eachMovie.setDescription(description);
                eachMovie.setAvgVote(avgVote);
                
                // adds each movie to the list of movies
                movieList.add(eachMovie);
                    
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File was not found.");
        } catch (IOException e) {
            System.out.println("ERROR: Something went wrong reading the file.");
        }
            
        reader.close();
        return movieList;
    }
    
    

}
