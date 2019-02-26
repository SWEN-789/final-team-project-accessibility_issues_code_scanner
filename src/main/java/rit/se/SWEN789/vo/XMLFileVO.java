package rit.se.SWEN789.vo;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import rit.se.SWEN789.astparser.vo.ClassBean;

public class XMLFileVO {
	private String name;
//	public String location;
	private Path filePath;
	private Document document;
//	public ProjectVO project;
	private List<DefectVO> defects = new ArrayList<>();
	private ClassBean activityClass;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Path getFilePath() {
		return filePath;
	}
	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}
	//	public ProjectVO getProject() {
//		return project;
//	}
//	public void setProject(ProjectVO project) {
//		this.project = project;
//	}
	public List<DefectVO> getDefects() {
		return defects;
	}
	public void setDefects(List<DefectVO> defects) {
		this.defects = defects;
	}
	public ClassBean getActivityClass() {
		return activityClass;
	}
	public void setActivityClass(ClassBean activityClass) {
		this.activityClass = activityClass;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
}
