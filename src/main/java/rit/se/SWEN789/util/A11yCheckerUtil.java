package rit.se.SWEN789.util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import rit.se.SWEN789.constants.AccessibilityDefect;
import rit.se.SWEN789.vo.DefectVO;
import rit.se.SWEN789.vo.XMLFileVO;

public class A11yCheckerUtil {
	
	public static List<Path> findAllDescendantsByPattern(Path root, String pattern) throws IOException {
		List<Path> paths = new ArrayList<>();
		Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult postVisitDirectory(Path path, IOException ex) {
				if (Files.isDirectory(path) && path.toString().matches(pattern)) {
					System.out.println(path);
					paths.add(path);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path path, IOException exc) {
				return FileVisitResult.CONTINUE;
			}
		});
		return paths;
	}
	
	public static void createDefect(XMLFileVO xmlFile,Element element, AccessibilityDefect defectType, String attr, String value) {
		DefectVO defect = new DefectVO();
		defect.setActivityName(xmlFile.getName());
		defect.setElementName(element.getTagName());
		defect.setFileVO(xmlFile);
		defect.setLineNumber(element.getUserData("lineNumber").toString());
		defect.setType(defectType.getName());
		defect.setComment(attr + ":" + value);
		xmlFile.getDefects().add(defect);
	}
	
	public static Path findFolder(Path rootPath, String folderToFind) throws IOException {
		Path folder = null;
		try (DirectoryStream<Path> directories = Files.newDirectoryStream(rootPath)) {
			for (Path directory : directories) {
				if (Files.isDirectory(directory)) {
					if (directory.getFileName().toString().toLowerCase().equals(folderToFind)) {
						folder = directory;
					} else {
						folder = findFolder(directory, folderToFind);
					}
				}
				if (folder != null) {
					System.out.println(folderToFind + " folder found");
					break;
				}
			}
		}
		return folder;
	}


	public static Path findFile(Path rootPath, String fileToFind) throws IOException {
		Path file = null;
		try (DirectoryStream<Path> directories = Files.newDirectoryStream(rootPath)) {
			for (Path directory : directories) {
				if (Files.isRegularFile(directory)) {
					if (directory.getFileName().toString().toLowerCase().equals(fileToFind)) {
						file = directory;
						break;
					}
				}
			}
		}
		return file;
	}

	public static String getParentElementsWidthOrHeight(Node element, String attr){
		if(element==null)
			return null;
		if(element.getNodeType() == Node.ELEMENT_NODE){
			Element parent = (Element) element.getParentNode();
			String attrValue = parent.getAttribute(attr);
			if("wrap_content".equals(attrValue)){
				return "48dp";
			}else if("match_parent".equals(attrValue)){
				return getParentElementsWidthOrHeight(parent, attr);
			}else{
				return attrValue;
			}
		}else{
			return getParentElementsWidthOrHeight(element.getParentNode(), attr);
		}	
	}
	
}
