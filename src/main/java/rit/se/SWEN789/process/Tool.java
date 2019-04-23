package rit.se.SWEN789.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import A11yUI.CSVParser;
import A11yUI.HTMLGenerator;
import rit.se.SWEN789.outputgenerator.CSVOutputGenerator;
import rit.se.SWEN789.parser.LayoutParser;
import rit.se.SWEN789.util.A11yCheckerUtil;
import rit.se.SWEN789.vo.ProjectVO;
import rit.se.SWEN789.vo.XMLFileVO;
import rit.se.SWEN789.astparser.process.FolderToJavaProjectConverter;
import rit.se.SWEN789.astparser.vo.ClassBean;

public class Tool {

	public Path sourceFilesPath;
	public String githubRepoURL = "";
	public String projectName = "Android Project";

	public LayoutParser layoutParser = new LayoutParser();

	public List<ProjectVO> projects = new ArrayList<>();

	public Tool(String sourceFilesPath) {
		super();
		this.sourceFilesPath = Paths.get(sourceFilesPath);
	}

	public void run() throws Exception {
		try (DirectoryStream<Path> entries = Files.newDirectoryStream(sourceFilesPath)) {
			for (Path entry : entries) {
				if (Files.isDirectory(entry)) {
					// each entry is an app
					// fetch details about the app
					ProjectVO project = new ProjectVO();
					project.setPath(entry);
					project.setName(entry.getFileName().toString());
					System.out.println("Inside app " + project.getName());
					projects.add(project);

					// find java src folder
					List<Path> javaFolder = A11yCheckerUtil.findAllDescendantsByPattern(entry, ".*main.*java");
					if (javaFolder != null && !javaFolder.isEmpty()) {
						List<ClassBean> activityClasses = FolderToJavaProjectConverter
								.extractActivityClasses(javaFolder.get(0).toAbsolutePath().toString());
						project.setActivityClasses(activityClasses);
					} else {
						//throw exception
					//	throw new Exception("Cannot find Java src folder!"); //commenting this for now 
					}

					// find layout folder.
					List<Path> layoutFolders = A11yCheckerUtil.findAllDescendantsByPattern(entry, ".*res.*layout.{0,1}"); //pattern for compiled apps - .*main.*res.*layout.*
					if (layoutFolders != null && !layoutFolders.isEmpty()) {
						project.setLayoutFolders(layoutFolders);
					} else {
						//throw new Exception("Cannot find layout folder!");
					}

					// find values folder
					List<Path> valuesFolders = A11yCheckerUtil.findAllDescendantsByPattern(entry, ".*res.*values.{0,1}"); //pattern for compiled apps - .*main.*res.*values.{0,1}
					if (valuesFolders != null && !valuesFolders.isEmpty()) {
						// project.setLayoutFolders(layoutFolders);
						for (Path folder : valuesFolders) {
							try (DirectoryStream<Path> files = Files.newDirectoryStream(folder, "*.xml")) {
								for (Path file : files) {
									extractTagValues(project, file);
								}
							}
						}
					}else{
//						throw new Exception("Cannot find layout folder!");
					}

					// Mapping Activity classes to XML layouts
					/*
					 * for (ClassBean activity : project.getActivityClasses()) {
					 * for (MethodBean method : activity.getMethods()) { if
					 * (method.getName().equals("onCreate")) { String
					 * methodContent = method.getTextContent(); Matcher m =
					 * Pattern.compile("setContentView\\(([^)]+)\\)").matcher(
					 * methodContent); if (m.find()) { String methodValue =
					 * m.group().trim(); methodValue =
					 * methodValue.substring(methodValue.lastIndexOf(".") + 1,
					 * methodValue.length() - 1);
					 * System.out.println(methodValue); Path xml =
					 * findFile(project.getLayoutFolder(),methodValue);
					 * XMLFileVO xmlFileVO = layoutParser.parse(xml);
					 * xmlFileVO.setActivityClass(activity);
					 * project.getXmlFiles().add(xmlFileVO); } break; } } }
					 */

					// parsing xml layout files
					System.out.println("Parsing layouts.....");
					for (Path layoutPath : project.getLayoutFolders()) {
						try (DirectoryStream<Path> layoutEntries = Files.newDirectoryStream(layoutPath, "*.xml")) {
							for (Path layoutEntry : layoutEntries) {
								XMLFileVO xml = layoutParser.parse(layoutEntry);
								if(xml!=null){
									project.getXmlFiles().add(xml);
								}
							}
						}
					}
					
					// Applying rules
					Validator.validate(project);
				}
			}
			
			// Generating output csv file
			generateOutputResults();
			
		} catch (IOException | CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void generateOutputResults() {
		
		// generate CSV
		String reportName = "report-" + System.currentTimeMillis(); 
		String reportCSVAbsPath = reportName + ".csv";
		
		CSVOutputGenerator output = new CSVOutputGenerator(Paths.get(sourceFilesPath.toString(), reportCSVAbsPath));
		output.write(projects);
		
		// generate HTML
        ArrayList<ArrayList<String>> csvResults = CSVParser.parseFile(Paths.get(sourceFilesPath.toString(), reportCSVAbsPath).toString());
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
        HTMLGenerator htmlGen = new HTMLGenerator(issueMap, githubRepoURL, projectName);
        String fileData = htmlGen.generatePage();

		String reportHTMLAbsPath = reportName + ".html";
        try { //Generate the report file and attempt to write it to a file named A11y_report.html
            FileOutputStream fos = new FileOutputStream(Paths.get(sourceFilesPath.toString(), reportHTMLAbsPath).toString());
            fos.write(fileData.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not Found");
        } catch (Exception e) {
            System.out.println("General Exception");
        }
	}
	
	private void extractTagValues(ProjectVO project, Path value) {
		if (value.getFileName().toString().equals("dimens.xml")
				|| value.getFileName().toString().equals("strings.xml")) {
			XMLFileVO xml = layoutParser.parse(value);
			Map<String, String> valuesMap = new HashMap<>();
			NodeList resourcesList = xml.getDocument().getElementsByTagName("resources");
			Element resources = (Element) resourcesList.item(0);
			NodeList children = resources.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				if (!(children.item(i) instanceof Element))
					continue;
				Element child = (Element) children.item(i);
				String attr = child.getAttribute("name");
				System.out.println(attr);
				valuesMap.put(attr, child.getFirstChild()!=null?child.getFirstChild().getTextContent():"");
			}
			project.getValues().put(value.getFileName().toString(), valuesMap);
		}
	}

	public static void main(String[] args) {
		
		if (args[0] != null) {
			Tool tool = new Tool(args[0]);
			
			if (args.length > 1) { tool.projectName = args[1]; }
			if (args.length > 2) { tool.githubRepoURL = args[2]; }
			try {
				tool.run();
				System.out.println("Parsing Complete.");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// throw exception
		}
	}
}
