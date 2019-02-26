package rit.se.SWEN789.rules;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import rit.se.SWEN789.constants.AccessibilityDefect;
import rit.se.SWEN789.constants.AndroidUIElement;
import rit.se.SWEN789.util.A11yCheckerUtil;
import rit.se.SWEN789.vo.ProjectVO;
import rit.se.SWEN789.vo.XMLFileVO;

public class ContentDescriptionRule {
	
	@SuppressWarnings("serial")
	public static List<String> elements = new ArrayList<String>(){
		{
			add(AndroidUIElement.EDIT_TEXT.getName());
			add(AndroidUIElement.IMAGE_BUTTON.getName());
			add(AndroidUIElement.IMAGE_VIEW.getName());
			add(AndroidUIElement.LIST_VIEW.getName());
			// More UI elements to be added
		}
	};
	
	public static void applyRule(Element element, XMLFileVO xmlFile, ProjectVO project){
		String elementName = element.getTagName();

		if(elementName.contains(".")){
			elementName = elementName.substring(elementName.lastIndexOf('.') + 1);
		}
		System.out.println("Element Name : "+elementName);
		if(!elements.contains(elementName)){
			return;
		}
		
		String contentDesc = element.getAttribute("android:contentDescription");
		
		if(contentDesc==null || contentDesc.trim().length()==0){
			System.out.println("Defect detected!");
			
			String id = element.getAttribute("android:id");
			if(id!=null && id.contains("/")){
				id = id.substring(id.lastIndexOf("/"));
			}
			
			//check if the attribute is changed from Java class
//			ClassBean activity = xmlFile.getActivityClass();
//			String activityContent = activity.getTextContent();

			A11yCheckerUtil.createDefect(xmlFile, element, AccessibilityDefect.NO_CONTENT_DESCRIPTION, "android:contentDescription", null);
			
		}
	}
}
