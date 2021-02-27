import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FrontEnd {

  public static Scanner scanner;
  public static BackendInterface backEnd;

  private static List<String> allR;

  public static void main(String[] args) {

    // making a list of all ratings in String for convenience
    allR = new ArrayList<>();
    for (int i = 0; i < 11; i++) {
      allR.add(String.valueOf(i));
    }

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

    boolean running = true;
    int index = 0;
    while (running) {
      // print top three movies
      printMovies(backEnd.getThreeMovies(index), index);

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
        index = Integer.parseInt(input) - 1;
      } else {
        System.out.println("Invalid input.");
      }
    }
  }

  public static void enterGenreMode() {
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

  public static void enterRatingsMode() {
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

  private static boolean checkInt(String in) {
    try {
      int test = Integer.parseInt(in);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private static void printMovies(List<MovieInterface> movies, int index) {
    String[][] table = new String[4][7];
    table[0][0] = "";
    table[0][1] = "Title";
    table[0][2] = "Year";
    table[0][3] = "Genre";
    table[0][4] = "Director";
    table[0][5] = "Description";
    table[0][6] = "Avg. Vote";

    for (int i = 1; i < 4; i++) {
      table[i][0] = (index + i) + ".";
      table[i][1] = movies.get(i).getTitle();
      table[i][2] = movies.get(i).getYear().toString();
      table[i][3] = movies.get(i).getDirector();
      table[i][4] = movies.get(i).getGenres().toString();
      table[i][5] = movies.get(i).getDescription();
      table[i][6] = movies.get(i).getAvgVote().toString();
    }

    for (String[] row : table) {
      System.out.format("%-15s%-15s%-15s%-15s%-15s%-15s%-15s%n", row);
    }

  }

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

  private static boolean genreSelected(String genre) {
    return backEnd.getGenres().contains(genre);
  }

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

  private static boolean ratingSelected(String rating) {
    return backEnd.getAvgRatings().contains(rating);
  }

}