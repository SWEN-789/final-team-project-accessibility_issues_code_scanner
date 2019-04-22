package A11yUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to generate an HTML report based on an inputted list of accessibility issues
 */
public class HTMLGenerator {
    //Class Attributes
    private HashMap<String, ArrayList<ArrayList<String>>> errorMap;
    private String githubLink;
    private String projectTitle;

    /**
     * Parameterized constructor for the HTML Generator
     * @param _errorMap A Map containing the issues detected by the A11yScanner
     * @param _githubLink The link formatted to show code (i.e. with the /blob/ in it) to the desired github folder
     * @param _projectTitle The title of the project (for display purposes)
     */
    public HTMLGenerator(HashMap<String, ArrayList<ArrayList<String>>> _errorMap, String _githubLink, String _projectTitle) {
        errorMap = _errorMap;
        githubLink = _githubLink;
        projectTitle = _projectTitle;
    }

    /**
     * Functional method of the class to generate a formatted HTML string that is written to file
     * @return htmlString A string containing the html for the entire page
     */
    public String generatePage() {
        //Start the page with the page head, title, and opening of the container
        String htmlString = generateHead();
        htmlString += generateTitle();
        htmlString += startSectionContainer();

        for (Map.Entry<String, ArrayList<ArrayList<String>>> section : errorMap.entrySet()) { //Loop through each section of issues and create a collapsible div for each issue
        	if (section.getValue().size() >= 2) {
                if(section.getValue().get(1).size() > 0) {
                    htmlString += generateSection(section.getKey(), section.getValue());
                }
        	}
        }

        htmlString += "     </div>\n"; //Close the error sections


        //htmlString += generateStatistics(); //Generate the statistics div (displayed top right)
        htmlString += generateChart(); //Gemerate the data visualizations (displayed top right)

        //Close all open divs (full page containers, body, and html tags)
        htmlString +=   "   </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        return htmlString;
    }

    /**
     * Generate the <head> tag information for the report
     * @return headString The string containing the HTML page header
     */
    public String generateHead() {
        return "<!DOCTYPE html>\n" +
                "<html lang='en'>\n" +
                "<head>\n" +
                "   <title>" + projectTitle + "</title>\n" +
                "   <meta charse ='utf-8' />\n" +
                "   <script src='https://code.jquery.com/jquery-3.3.1.slim.min.js' integrity='sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo' crossorigin='anonymous'></script>\n" + //jQuery CDN link
                "   <script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js' integrity='sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49' crossorigin='anonymous'></script>\n" + //Popper JS CDN (needed for bootstrap)
                "   <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>\n" + //Bootstrap CS CDN
                "   <script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js' integrity='sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa' crossorigin='anonymous'></script>\n" + //Bootstrap JS CDN
                "   <script type='text/javascript' src='https://canvasjs.com/assets/script/jquery.canvasjs.min.js'></script>" + //JS for Graph-based data visualization
                "   <style>\n" + //Custom page styling
                "       .collapseBtn { margin-bottom: 15px; margin-left: 10px;}\n" +
                "       #statsSection { border: 1px solid black; border-radius: 30px;}\n" +
                "       body {width: 90%; margin: 0 auto; background-color: #E6E6E6;}\n" +
                "       .container-fluid {background-color: white; padding: 25px; border-radius: 0 0 30px 30px;}\n" +
                "       #chartContainer {width: 425px; height: 425px; float: right; margin-right: 35px;}\n" +
                "       .canvasjs-chart-credit{ display: none !important;}\n" +
                "       canvas {border-bottom-left-radius: 100px !important;}\n" +
                "       .hidden {display: none;}\n" +
                "           .buttonDiv {text-align: center; widht: 500px;}\n" +
                "   </style>\n" +
                "</head>\n" +
                "<body>\n";
    }

    /**
     * A method to generate the display title for the page
     * @return titleString The <h1> String showing the title
     */
    public String generateTitle() {
        return "<h1 style='text-align:center; text-decoration: underline; background-color: white; margin-bottom: 0px; padding-bottom: 15px; border-radius: 30px 30px 0 0;'>" + projectTitle + " Accessibility Report</h1>\n";
    }

    /**
     * Opens the main container for the page, creates one column for the accessibility issues (another column used for the project stats)
     * @return sectionString The opening to the Accessibility Issues section
     */
    public String startSectionContainer() {
        return "<div class='container-fluid'>\n" +
                "   <div class='row'>\n" +
                "       <div class='col-md-7 col-lg-7'>\n";
    }

    /**
     * Generate each specific section for the different types of accessibility issues
     * @param sectionTitle The type of issue (Map key)
     * @param section The list of issues of that type (Map value)
     * @return returnString The formatted HTML for an issue type
     */
    public String generateSection(String sectionTitle, ArrayList<ArrayList<String>> section) {
        String strippedTitle = sectionTitle.replaceAll("\\s","-"); //Format the Title as an ID (to allow for collapsible divs

        //Creates the container and list for the issues
        String returnString =   "           <div class='section'>\n" +
                "               <div style='display: inline-block'>" +
                "                   <span style='text-decoration: underline' class='h2'>" + sectionTitle + "</span>\n" +
                "                   <span>\n" +
                "                       <button type='button' class='btn btn-default collapseBtn' type='button' data-toggle='collapse' data-target='#div_" + strippedTitle + "'>Collapse Section</button>\n" +
                "                   </span>\n" +
                "               </div>\n" +
                "               <div class='issueContainer collapse in' id='div_" + strippedTitle + "'>\n" +
//                "                   <ol>\n";
        		"           	<table class=\"table\">\n" + 
        		"             		<thead class=\"thead-light\">\n" + 
        		"               		<tr>\n" + 
        		"                 			<th scope=\"col\">#</th>\n" + 
        		"                 			<th scope=\"col\">Element</th>\n" + 
        		"                 			<th scope=\"col\">Comment</th>\n" + 
        		"                 			<th scope=\"col\">Location</th>\n" + 
        		"               		</tr>\n" + 
        		"             		</thead>\n" + 
        		"             		<tbody>\n" + 
        		"";

        int identifier = 0;
        for(ArrayList<String> issue : section) { //Add each issue to the report
//            returnString += generateIssue(issue);
            returnString += generateIssueTable(++identifier, issue);
        }

        //Close the containers and return the string
        returnString +=    
//        		"               </ol>\n" +
                "               	</tbody>\n" + 
                "           	</table>" +
                "           </div>\n" +
                "        </div>\n";

        return returnString;
    }


    /**
     * Generates a formatted HTML <li> tag for each issue
     * @param issue A specific accessibility issue
     * @return liString The formatted <li> HTML for an issue
     */
    public String generateIssue(ArrayList<String> issue) {
        return "                <li>" + generateLink(issue) + " comment: " + issue.get(6) + "</li>\n";
    }

    /**
     * Generates a formatted HTML <tr> tag for each issue
     * @param issue A specific accessibility issue
     * @return trString The formatted <tr> HTML for an issue
     */
    public String generateIssueTable(int id, ArrayList<String> issue) {
		return "               			<tr>\n" + 
				"                 			<th scope=\"row\">" + id + "</th>\n" + 
				"                 			<td>"+ issue.get(3) +"</td>\n" + 
				"                 			<td>"+ issue.get(6) +"</td>\n" + 
				"                 			<td>"+ generateLink(issue) +"</td>\n" + 
				"               		</tr>";
    	
    }
    
    /**
     * Generates a formatted HTML <a> tag for each issue (allows linking to the specific line in the GitHub repo)
     * @param issue A specific accessibility issue
     * @return anchorString The formatted <a> tag
     */
    public String generateLink(ArrayList<String> issue) {
        if (githubLink != null && githubLink.length() > 0) {
            String anchorLink = githubLink + issue.get(2);

            return "<a target='_blank' href='" + anchorLink + "#L" + issue.get(4) + "'>" + issue.get(2) + "(line " + issue.get(4) + ")</a>";
        } else {
            return "<span>" + issue.get(2) + " (line " + issue.get(4) + ")</span>";
        }
    }

    /**
     * Function to generate data visualizations for the project
     * Generates a bar chart and a pie chart with the option to toggle display
     * @return chartString HTML for the data visualization
     */
    public String generateChart() {
        String labelString = "";

        for (Map.Entry<String, ArrayList<ArrayList<String>>> section : errorMap.entrySet()) { //Loop through the entries in the map and count the list of issues
        	if (!section.getKey().equals("")) {
        		labelString +=     "                       { label: '" + section.getKey() + "', y: " + section.getValue().size() + " },\n";
        	}
        }

        String returnString =   "<div id='chartContainer' class='col-md-5 col-lg-5' style='width: 500px; height: 500px'>\n" +
                "   <div id='pieChartContainer' style='width: 500px; height: 500px'></div>\n" +
                "   <div id='barChartContainer' class='hidden' style='width: 500px; height: 500px;'></div>\n" +
                "   <div class='buttonDiv'>\n" +
                "       <button onclick='switchChart()' id='switchChart' type='button' class='btn btn-primary'>Switch Chart</button>\n" +
                "   </div>\n" +
                "   <script type='text/javascript'>\n" +
                "       function switchChart() {\n" +
                "           $('#pieChartContainer').toggleClass('hidden');\n" +
                "           $('#barChartContainer').toggleClass('hidden')\n" +
                "       }\n" +
                "       window.onload = function() {\n" +
                "           var pieOptions = {\n" +
                "               title: {\n" +
                "                   text: '" + projectTitle + "'\n" +
                "               },\n" +
                "               data: [{\n" +
                "                   type: 'pie',\n" +
                "                   startAngle: 45,\n" +
                "                   legendText: '{label}',\n" +
                "                   indexLabel: '{label} ({y})',\n" +
                "                   yValueFormatString: '#,##0.#'%'',\n" +
                "                   dataPoints: [\n" +
                labelString +
                "                   ]\n" +
                "               }]\n" +
                "           };\n\n" +
                "           var barOptions = {\n" +
                "               title: {\n" +
                "                   text: '" + projectTitle + "'\n" +
                "               },\n" +
                "               data: [{\n" +
                "                   type: 'column',\n" +
                "                   dataPoints: [\n" +
                labelString +
                "                   ]\n" +
                "               }]\n" +
                "           };\n" +
                "           $('#pieChartContainer').CanvasJSChart(pieOptions);\n" +
                "           $('#barChartContainer').CanvasJSChart(barOptions);\n" +
                "       }\n" +
                "   </script>\n" +
                "</div>";
        return returnString;
    }

    /**
     * Generates formatted HTML to display overall project statistics about the types of accessibility issues
     * @return htmlString The formatted HTML string showing statistics about the project
     */
    public String generateStatistics() {
        //Maintain a reference of the highest count to identify which accessibility issue is most common
        int highestCount = 0;
        String biggestSection = "";

        for (Map.Entry<String, ArrayList<ArrayList<String>>> section : errorMap.entrySet()) { //Loop through the entries in the map and count the list of issues
            if (section.getValue().size() > highestCount) { //If the current list is larger than the identified map, set it as the highest
                highestCount = section.getValue().size();
                biggestSection = section.getKey();
            }
        }

        //Format and return the HTML string
        String htmlString =    "        <div id='statsSection' class='col-md-3 col-lg-3'>\n" +
                "           <h1 style='text-decoration: underline; text-align: center'>Project Statistics</h1>\n" +
                "           <p><strong>Most common issue type: </strong>" + biggestSection + "</p>\n" +
                "           <p><strong>Section Error Count: </strong>" + Integer.toString(highestCount) + "</p>\n" +
                "           <br />\n" +
                "           <p><strong>Count of issue types: </strong>" + errorMap.size() +"</p>\n" +
                "       </div>\n";

        return htmlString;
    }
}
