import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// --== CS400 File Header Information ==--
// Author: Jimmy Li
// Email: xli2272@wisc.edu
// Notes: This is the source file for code developed
//        for the front end of the Movie Mapper project.
public class FrontEnd {

  public static Scanner scanner;
  public static BackendInterface backEnd;

  private static List<String> allR;

  /**
   * This is the entry point of the Movie Mapper application. The program will start in its base
   * mode, initialize a few variables and the Backend, print out a welcome message, top three movies
   * based on the selected ratings and the genres.
   *
   * @param args program arguments
   */
  public static void main(String[] args) {

    // making a list of all ratings in String for convenience
    allR = new ArrayList<>();
    for (int i = 0; i < 11; i++) {
      allR.add(String.valueOf(i));
    }

    // initializing scanner variable
    scanner = new Scanner(System.in);

    // initialize back end
    backEnd = new Backend(args);

    // print welcome message
    System.out.println("Welcome to Movie Mapper!");
    System.out.println("A movie browsing application.");

    // select all ratings
    for (String rating : allR) {
      backEnd.addAvgRating(rating);
    }

    // program loop
    boolean running = true;
    int browsingIndex = 0;
    while (running) {
      // print top three movies
      printMovies(backEnd.getThreeMovies(browsingIndex), browsingIndex);

      // list commands
      System.out.println("Enter 'x' to exit the program.");
      System.out.println("Enter 'g' to enter Genre Selection Mode.");
      System.out.println("Enter 'r' to enter Rating Selection Mode.");
      System.out.println("Enter a number to browse through movies.");

      // prompt for a command
      System.out.println("Please enter a command:");

      // record input
      String input = scanner.nextLine();
      scanner.nextLine();

      // check and run command
      if (input.equals("x")) {
        running = false;
      } else if (input.equals("g")) {
        enterGenreMode();
      } else if (input.equals("r")) {
        enterRatingsMode();
      } else if (checkInt(input)) {
        browsingIndex = Integer.parseInt(input) - 1;
      } else {
        System.out.println("Invalid input.");
      }
    }
  }

  /**
   * This method contains all of the Genre Selection Mode. This method has a while loop that will
   * print all genres with an indication of whether they are selected or not, and then prints out a
   * list of commands, then a prompt for the user to enter a command. 'x' will exit this mode and
   * return to the base mode while a number will select/deselect a genre.
   */
  public static void enterGenreMode() {
    // genre selection mode program loop
    boolean inMode = true;
    while (inMode) {
      // print selected genres
      printGenres();
      int numMovies = backEnd.getNumberOfMovies();
      System.out.println("There are " + numMovies + " movies that match your selection.");

      // prompt command
      System.out.println("Enter 'x' to return to base mode.");
      System.out.println("Enter a number select/deselect genres.");

      // record input
      String input = scanner.nextLine();
      scanner.nextLine();

      // check and run command
      if (input.equals("x")) {
        inMode = false;
      } else if (checkInt(input)) {
        String genre = backEnd.getAllGenres().get(Integer.parseInt(input));
        if (genreSelected(genre)) {
          backEnd.removeGenre(genre);
        } else {
          backEnd.addGenre(genre);
        }
      } else {
        System.out.println("Invalid input.");
      }
    }
  }

  /**
   * This method contains all of the Rating Selection Mode. This method has a while loop that will
   * print all ratings with an indication of whether they are selected or not, and then prints out a
   * list of commands, then a prompt for the user to enter a command. 'x' will exit this mode and
   * return to the base mode while a number will select/deselect a rating.
   */
  public static void enterRatingsMode() {
    // rating selection mode program loop
    boolean inMode = true;
    while (inMode) {
      // print selected ratings
      printRatings();
      int numMovies = backEnd.getNumberOfMovies();
      System.out.println("There are " + numMovies + " movies that match your selection.");

      // prompt command
      System.out.println("Enter 'x' to return to base mode.");
      System.out.println("Enter a number select/deselect ratings.");

      // record input
      String input = scanner.nextLine();
      scanner.nextLine();

      // check and run command
      if (input.equals("x")) {
        inMode = false;
      } else if (checkInt(input)) {
        int num = Integer.parseInt(input);
        if (num < 0 || num > 10) {
          System.out.println("Invalid input.");
          continue;
        }
        String rating = backEnd.getAllGenres().get(Integer.parseInt(input));
        if (ratingSelected(rating)) {
          backEnd.removeAvgRating(rating);
        } else {
          backEnd.addAvgRating(rating);
        }
      } else {
        System.out.println("Invalid input.");
      }
    }
  }

  /**
   * This is a helper method that checks whether a String is an integer in String form.
   *
   * @param in input String to be checked
   * @return true if parameter is a number in String form, false otherwise
   */
  private static boolean checkInt(String in) {
    try {
      int test = Integer.parseInt(in);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  /**
   * This helper method formats three movies taken from the Backend and puts them in a table, then
   * prints them.
   *
   * @param movies movies to be printed
   * @param index  rank of the first movie
   */
  private static void printMovies(List<MovieInterface> movies, int index) {
    // table header that shows what each column of the table is
    String[][] table = new String[4][7];
    table[0][0] = "";
    table[0][1] = "Title";
    table[0][2] = "Year";
    table[0][3] = "Genre";
    table[0][4] = "Director";
    table[0][5] = "Description";
    table[0][6] = "Avg. Vote";

    // format movie information into a table of Strings
    for (int i = 1; i < 4; i++) {
      table[i][0] = (index + i) + ".";
      table[i][1] = movies.get(i).getTitle();
      table[i][2] = movies.get(i).getYear().toString();
      table[i][3] = movies.get(i).getDirector();
      table[i][4] = movies.get(i).getGenres().toString();
      table[i][5] = movies.get(i).getDescription();
      table[i][6] = movies.get(i).getAvgVote().toString();
    }

    // print in table format
    for (String[] row : table) {
      System.out.format("%-15s%-15s%-15s%-15s%-15s%-15s%-15s%n", row);
    }

  }

  /**
   * This helper method prints all genres with a "[x] " in front of any genre that is selected and a
   * "[ ] " in front of a genre that is not selected.
   */
  private static void printGenres() {
    List<String> selectedG = backEnd.getGenres();
    List<String> allG = backEnd.getAllGenres();

    for (String genre : allG) {
      System.out.print("[");
      if (selectedG.contains(genre)) {
        System.out.print("x] " + genre);
      } else {
        System.out.print(" ] " + genre);
      }
    }
  }

  /**
   * This helper method checks whether or not a genre is selected.
   *
   * @param genre input genre to be checked
   * @return true if selected, false otherwise
   */
  private static boolean genreSelected(String genre) {
    return backEnd.getGenres().contains(genre);
  }

  /**
   * This helper method prints all ratings with a "[x] " in front of any rating that is selected and
   * a "[ ] " in front of a rating that is not selected.
   */
  private static void printRatings() {
    List<String> selectedR = backEnd.getAvgRatings();

    for (String rating : allR) {
      System.out.print("[");
      if (selectedR.contains(rating)) {
        System.out.print("x] " + rating);
      } else {
        System.out.print(" ] " + rating);
      }
    }
  }

  /**
   * This helper method checks whether or not a rating is selected.
   *
   * @param rating input rating to be checked
   * @return true if selected, false otherwise
   */
  private static boolean ratingSelected(String rating) {
    return backEnd.getAvgRatings().contains(rating);
  }

}