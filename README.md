# A11yScanner
A Java command line tool that checks for accessibility defects in android applications by parsing source code and allows the user to have a good visualization of the results.

To run the tool-
Run the main method in \src\main\java\rit\se\SWEN789\process\Tool.java and pass the absolute path of the parent folder which has android projects as command line argument.

Accessibility Reports:
After running A11yScanner on an Android app, it generates a report in HTML, which shows the issues found in the folders that was analyzed. Each issue comes with all the necessary information to be located, such as file name and line number, and with information about how to fix the issues based on the accessibility issue type. Moreover, the report contains a graph showing the project statistics, which can be changed to two different views: bar or pie.
