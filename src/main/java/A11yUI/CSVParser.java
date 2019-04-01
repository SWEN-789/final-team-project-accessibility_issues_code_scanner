package A11yUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class to handle parsing in a CSV file and casting its information to an ArrayList of ArrayLists
 */
public class CSVParser {
    //Define default separator as , and default quote as "
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    /**
     * Method that accepts a CSV file, parses it line by line, and returns a nested ArrayList containing the results
     * @param fileName The full path of the CSV file
     * @return lines A Nested ArrayList containing the CSV information (null if an exception occurs)
     */
    public static ArrayList<ArrayList<String>> parseFile(String fileName) {
        try { // Try opening the file, scanning the results in line by line, and returning the results
            ArrayList<ArrayList<String>> lines = new ArrayList<>();
            Scanner scanner = new Scanner(new File(fileName));

            while (scanner.hasNext()) { // Loop through each line and add it to the ArrayList
                ArrayList<String> line = parseLine(scanner.nextLine());
                lines.add(line);
            }
            scanner.close();

            return lines;
        } catch (FileNotFoundException fnfe) { // Handle Bad Filenames
            System.out.println("Error reading file: " + fileName + " is not a valid file");
            return null;
        } catch (Exception e) { //Handle General Errors
            System.out.println("An unspecified error occured.");
            return null;
        }

    }


    /**
     * Overloaded method to read in a line from a CSV. Uses default separator and quotes
     * @param csvLine The string result of the scanner.nextLine() call on the CSV file
     * @return result An ArrayList of strings containing each result from that line
     */
    private static ArrayList<String> parseLine(String csvLine) {
        return parseLine(csvLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    /**
     * Overloaded method to read in a line from a CSV. Uses default quotes and custom separator
     * @param csvLine The string result of the scanner.nextLine() call on the CSV file
     * @param separators A custom defined separation character
     * @return result An AraryList of strings containing each result from that line
     */
    private static ArrayList<String> parseLine(String csvLine, char separators) {
        return parseLine(csvLine, separators, DEFAULT_QUOTE);
    }

    /**
     * Overloaded method to read in a line from a CSV. Uses custom separator and custom quotes
     * @param csvLine The string result of the scanner.nextLine() call on the CSV file
     * @param separators A custom defined separation character
     * @param customQuote A custom defined quote character
     * @return result An AraryList of strings containing each result from that line
     */
    private static ArrayList<String> parseLine(String csvLine, char separators, char customQuote) {
        ArrayList<String> result = new ArrayList<>();

        // If empty, just return the line
        if (csvLine == null || csvLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') { // Handle empty custom quote
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') { // Handle empty separator
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = csvLine.toCharArray();

        for (char ch : chars) {
            if(inQuotes) {
                startCollectChar = true;

                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else { //Allows " in custom quotes
                    if (ch == '\"') {
                        if(!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }
                }
            } else {
                if (ch == customQuote) {
                    inQuotes = true;

                    if(chars[0] != '"' && customQuote == '\"') { //Allow "" in empty quote enclosed
                        curVal.append('"');
                    }

                    if(startCollectChar) { //Catches double quotes
                        curVal.append('"');
                    }
                } else if (ch == separators) {
                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;
                } else if (ch == '\r') { //Ignore LF characters
                    continue;
                } else if (ch == '\n') { //Newline so we break (end of the line)
                    break;
                } else {
                    curVal.append(ch);
                }
            }
        }

        result.add(curVal.toString());
        return result;
    }
}
