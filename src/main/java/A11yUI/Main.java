package A11yUI;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *  Main Class to generate the HTML report
 */
public class Main {
    public static void main(String[] args) {

        // args[0] needs to be the full file path to the CSV file outputted by A11yScanner on your system
        ArrayList<ArrayList<String>> csvResults = CSVParser.parseFile(args[0]);
        HashMap<String, ArrayList<ArrayList<String>>> issueMap = new HashMap<>();

        for (ArrayList<String> c: csvResults) { //Loop through each row of the results and format it as a Map
            if (issueMap.containsKey(c.get(1))) { //If the key (issue type) exists, add it to the ArrayList containing other issues of that type
                ArrayList<ArrayList<String>> typedIssues = issueMap.get(c.get(1));
                typedIssues.add(c);
                issueMap.put(c.get(1), typedIssues);
            } else { //If it doesn't exist, create a new key in the Map and add this issue as its first value
                ArrayList<ArrayList<String>> typedIssues = new ArrayList<>();
                typedIssues.add(c);
                issueMap.put(c.get(1), typedIssues);
            }
        }

        // Create the HTML Generator Object, passing in the map of issues, the link to the source folder in github (formatted to view the code), and the project title
        HTMLGenerator htmlGen = new HTMLGenerator(issueMap, "https://github.com/Telegram-FOSS-Team/Telegram-FOSS/blob/master/TMessagesProj/", "Telegram");
        String fileData = htmlGen.generatePage();

        try { //Generate the report file and attempt to write it to a file named A11y_report.html
            FileOutputStream fos = new FileOutputStream("A11y_report.html");
            fos.write(fileData.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not Found");
        } catch (Exception e) {
            System.out.println("General Exception");
        }
    }
}
