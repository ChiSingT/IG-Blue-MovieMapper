import java.io.Reader;
import java.util.Arrays;
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

  private static final List<String> allR = Arrays
      .asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

  /**
   * This is the entry point of the Movie Mapper application. The program will start in its base
   * mode, initialize a few variables and the Backend, print out a welcome message, top three movies
   * based on the selected ratings and the genres.
   *
   * @param args program arguments
   */
  public static void main(String[] args) {
    new FrontEnd().run(args);
  }

  public void run(String[] args) {
    // initialize back end
    backEnd = new Backend(args);

    // initializing scanner variable
    scanner = new Scanner(System.in);

    enterBaseMode();
  }

  public void run(Backend backend) {
    // initialize back end
    backEnd = backend;

    // initializing scanner variable
    scanner = new Scanner(System.in);

    enterBaseMode();
  }

  public void enterBaseMode() {
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
    System.out.println("============================");
    System.out.println("Entered Genre Selection Mode");
    System.out.println("============================");

    // genre selection mode program loop
    boolean inMode = true;
    while (inMode) {

      // print selected genres
      printGenres();
      int numMovies = backEnd.getNumberOfMovies();
      System.out.println("There are " + numMovies + " movies that match your current selection.");

      // prompt command
      System.out.println("Enter 'x' to return to base mode.");
      System.out.println("Enter a number select/deselect genres.");

      // record input
      String input = scanner.nextLine();

      // check and run command
      if (input.equals("x")) {
        inMode = false;
      } else if (checkInt(input)) {
        if (Integer.parseInt(input) - 1 < 0
            || Integer.parseInt(input) - 1 >= backEnd.getAllGenres().size()) {
          System.out.println("Invalid input.");
          continue;
        }
        String genre = backEnd.getAllGenres().get(Integer.parseInt(input) - 1);
        if (genreSelected(genre)) {
          backEnd.removeGenre(genre);
          System.out.println(genre + " deselected.");
        } else {
          backEnd.addGenre(genre);
          System.out.println(genre + " selected.");
        }
      } else {
        System.out.println("Invalid input.");
      }
    }

    System.out.println("===========================");
    System.out.println("Exited Genre Selection Mode");
    System.out.println("===========================");

  }

  /**
   * This method contains all of the Rating Selection Mode. This method has a while loop that will
   * print all ratings with an indication of whether they are selected or not, and then prints out a
   * list of commands, then a prompt for the user to enter a command. 'x' will exit this mode and
   * return to the base mode while a number will select/deselect a rating.
   */
  public static void enterRatingsMode() {
    System.out.println("=============================");
    System.out.println("Entered Rating Selection Mode");
    System.out.println("=============================");

    // rating selection mode program loop
    boolean inMode = true;
    while (inMode) {
      // print selected ratings
      printRatings();
      int numMovies = backEnd.getNumberOfMovies();
      System.out.println("There are " + numMovies + " movies that match your current selection.");

      // prompt command
      System.out.println("Enter 'x' to return to base mode.");
      System.out.println("Enter a number select/deselect ratings.");

      // record input
      String input = scanner.nextLine();

      // check and run command
      if (input.equals("x")) {
        inMode = false;
      } else if (checkInt(input)) {
        int num = Integer.parseInt(input);
        if (num < 0 || num > 10) {
          System.out.println("Invalid input.");
          continue;
        }
        String rating = allR.get(Integer.parseInt(input));
        if (ratingSelected(rating)) {
          backEnd.removeAvgRating(rating);
          System.out.println(rating + " deselected.");
        } else {
          backEnd.addAvgRating(rating);
          System.out.println(rating + " selected.");
        }
      } else {
        System.out.println("Invalid input.");
      }
    }

    System.out.println("============================");
    System.out.println("Exited Rating Selection Mode");
    System.out.println("============================");

  }

  /**
   * This is a helper method that checks whether a String is an integer in String form.
   *
   * @param in input String to be checked
   * @return true if parameter is a number in String form, false otherwise
   */
  private static boolean checkInt(String in) {
    try {
      Integer.parseInt(in);
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
    table[0][0] = "No.";
    table[0][1] = "Title";
    table[0][2] = "Year";
    table[0][3] = "Director";
    table[0][4] = "Rating";
    table[0][5] = "Genre";
    table[0][6] = "Description";

    int numMovies = movies.size();

    // format movie information into a table of Strings
    for (int i = 0; i < numMovies; i++) {
      table[i + 1][0] = (index + i + 1) + ".";
      table[i + 1][1] = movies.get(i).getTitle();
      table[i + 1][2] = movies.get(i).getYear().toString();
      table[i + 1][3] = movies.get(i).getDirector();
      table[i + 1][4] = movies.get(i).getAvgVote().toString();
      table[i + 1][5] = String.join(", ", movies.get(i).getGenres());
      table[i + 1][6] = movies.get(i).getDescription();
    }

    // find the longest string length of each column, and gives enough space to print a proper table
    int[] maxLength = new int[table[0].length];
    for (int i = 0; i < numMovies + 1; i++) {
      for (int j = 0; j < maxLength.length; j++) {
        if (table[i][j].length() > maxLength[j]) {
          maxLength[j] = table[i][j].length();
        }
      }
    }

    // create format string
    String format = "";
    for (int j : maxLength) {
      format += "%-" + (2 + j) + "s";
    }

    // print in table format
    for (int i = 0; i < numMovies + 1; i++) {
      System.out.format(format + "%n", (Object[]) table[i]);
    }

  }

  /**
   * This helper method prints all genres with a "[x] " in front of any genre that is selected and a
   * "[ ] " in front of a genre that is not selected. This number also prints out a number before
   * each genre to help users identify which number to enter to select/deselect which genre.
   */
  private static void printGenres() {

    System.out.println("Selected genres have an '[x]' in front, and the rest will only have '[ ]'");
    List<String> selectedG = backEnd.getGenres();
    List<String> allG = backEnd.getAllGenres();

    for (int i = 0; i < allG.size(); i++) {
      String genre = allG.get(i);
      System.out.print((i + 1) + ". [");
      if (selectedG.contains(genre)) {
        System.out.println("x] " + genre);
      } else {
        System.out.println(" ] " + genre);
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
        System.out.println("x] " + rating);
      } else {
        System.out.println(" ] " + rating);
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