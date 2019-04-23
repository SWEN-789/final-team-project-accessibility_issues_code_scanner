# A11yUI
An update to the Java command line tool A11yScanner that generates an HTML-based report of any accessibility defects detected in scanned Android Applications.

# Running the Tool
Run the main method in \src\main\java\rit\se\SWEN789\process\Tool.java passing in the following parameters as command line arguments:
- absolute path of the parent folder
- Application Name (optional - for display purposes)
- Link to the github repository (optional - for formatting the issues as links to the specific line of code)

Upon a successful run of A11yScanner, the A11yUI module  will automatically generate an HTML report named A11y_Report.html.  You can view this report in your local browser.  If the Application name is not supplied, the generated HTML report will not show a title for the Page or Graphs.  If the link to the github repository is not included, the generated HTML report will show the issues as simple text rather than as links.

# General Functionality
After running A11yScanner on an Android app it will generate a report in HTML, which shows the issues found in the source code that was analyzed. Each issue comes with all the necessary information to be located, such as file name and line number, and with information about how to fix the issues based on the accessibility issue type. Moreover, the report contains a graph showing the project statistics, which can be changed to two different formats: bar chart or pie chart.
