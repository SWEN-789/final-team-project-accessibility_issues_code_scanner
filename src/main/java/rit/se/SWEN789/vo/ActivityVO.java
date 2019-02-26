package rit.se.SWEN789.vo;

import java.util.List;

import rit.se.SWEN789.astparser.vo.ClassBean;

public class ActivityVO {

	private String activityName;
	private ClassBean activityClass;
	private List<XMLFileVO> layouts;
	
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public ClassBean getActivityClass() {
		return activityClass;
	}
	public void setActivityClass(ClassBean activityClass) {
		this.activityClass = activityClass;
	}
	public List<XMLFileVO> getLayouts() {
		return layouts;
	}
	public void setLayouts(List<XMLFileVO> layouts) {
		this.layouts = layouts;
	}
	
}
