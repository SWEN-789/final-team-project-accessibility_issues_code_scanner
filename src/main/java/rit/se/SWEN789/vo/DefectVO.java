package rit.se.SWEN789.vo;

public class DefectVO {
	
	private XMLFileVO fileVO;
	
	private String lineNumber;
	private String type;
	private String elementName;
//	private String description;
	private String activityName;
	private String comment;
	
	public XMLFileVO getFileVO() {
		return fileVO;
	}
	public void setFileVO(XMLFileVO fileVO) {
		this.fileVO = fileVO;
	}
	public String getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
