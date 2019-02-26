package rit.se.SWEN789.vo;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rit.se.SWEN789.astparser.vo.ClassBean;

public class ProjectVO {
	private String name;
	private String version;
	private Path path;
	private List<ClassBean> activityClasses = new ArrayList<>();
	private List<Path> layoutFolders;
	//add instance variable to hold the result after parsing
	private List<DefectVO> defects = new ArrayList<>();
	private List<XMLFileVO> xmlFiles = new ArrayList<>();
	private Map<String, Map<String, String>> values = new HashMap<>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public List<Path> getLayoutFolders() {
		return layoutFolders;
	}
	public void setLayoutFolders(List<Path> layoutFolders) {
		this.layoutFolders = layoutFolders;
	}
	public List<DefectVO> getDefects() {
		return defects;
	}
	public void setDefects(List<DefectVO> defects) {
		this.defects = defects;
	}
	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}
	public List<XMLFileVO> getXmlFiles() {
		return xmlFiles;
	}
	public void setXmlFiles(List<XMLFileVO> xmlFiles) {
		this.xmlFiles = xmlFiles;
	}
	public List<ClassBean> getActivityClasses() {
		return activityClasses;
	}
	public void setActivityClasses(List<ClassBean> activityClasses) {
		this.activityClasses = activityClasses;
	}
	public Map<String, Map<String, String>> getValues() {
		return values;
	}
	public void setValues(Map<String, Map<String, String>> values) {
		this.values = values;
	}
}
