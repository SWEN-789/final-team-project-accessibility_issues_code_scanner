package A11yUI;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<ArrayList<String>> csvResults = CSVParser.parseFile(args[0]);

        for (ArrayList<String> c: csvResults) {
            System.out.println("Issue Type: " + c.get(1));
            System.out.println("Element Type: " + c.get(3));
            System.out.println("Line Number: " + c.get(4));
            System.out.println("Comment: " + c.get(6));
            System.out.println("=========================================\n");
        }
    }
}
