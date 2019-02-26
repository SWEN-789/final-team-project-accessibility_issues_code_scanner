package rit.se.SWEN789.rules;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import rit.se.SWEN789.constants.AccessibilityDefect;
import rit.se.SWEN789.constants.AndroidUIElement;
import rit.se.SWEN789.util.A11yCheckerUtil;
import rit.se.SWEN789.vo.ProjectVO;
import rit.se.SWEN789.vo.XMLFileVO;

public class TextSizeRule {

	public final static double MIN_SIZE = 12;// https://stackoverflow.com/questions/5611411/what-is-the-default-text-size-on-android

	@SuppressWarnings("serial")
	public static List<String> elements = new ArrayList<String>() {
		{
			add(AndroidUIElement.BUTTON.getName());
			add(AndroidUIElement.CHECK_BOX.getName());
			add(AndroidUIElement.EDIT_TEXT.getName());
			add(AndroidUIElement.RADIO_BUTTON.getName());
			add(AndroidUIElement.SWITCH.getName());
			add(AndroidUIElement.TOGGLE_BUTTON.getName());
			add(AndroidUIElement.TEXT_VIEW.getName());
			// More UI elements to be added
		}
	};
	
	public static void applyRule(Element element, XMLFileVO xmlFile, ProjectVO project) {
		String elementName = element.getTagName();

		if (elementName.contains(".")) {
			elementName = elementName.substring(elementName.lastIndexOf('.') + 1);
		}
		System.out.println("Element Name : " + elementName);
		if (!elements.contains(elementName)) {
			return;
		}
		String textSize = element.getAttribute("android:textSize");
		if(textSize!=null && !textSize.isEmpty()){
			if(textSize.contains("@dimen")){
				textSize = project.getValues().get("dimens.xml").get(textSize.substring(textSize.indexOf("/")+1));
			}
			String formattedTextSize = textSize.replaceAll("[^\\d.]", "");
			double value = "".equals(formattedTextSize.trim())?0:Double.parseDouble(formattedTextSize);
			if (value < MIN_SIZE) {
				A11yCheckerUtil.createDefect(xmlFile,element,AccessibilityDefect.SMALL_TEXT_SIZE ,"android:textSize", textSize);
			}
		}
	}
}
