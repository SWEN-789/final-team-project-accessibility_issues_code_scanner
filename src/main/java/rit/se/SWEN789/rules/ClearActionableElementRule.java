package rit.se.SWEN789.rules;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import rit.se.SWEN789.constants.AccessibilityDefect;
import rit.se.SWEN789.constants.AndroidUIElement;
import rit.se.SWEN789.util.A11yCheckerUtil;
import rit.se.SWEN789.vo.ProjectVO;
import rit.se.SWEN789.vo.XMLFileVO;

public class ClearActionableElementRule {
	
	public final static double MIN_ELEVATION = 2;

	
	public static List<String> elements = new ArrayList<String>() {
		{
			add(AndroidUIElement.BUTTON.getName());
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
		
		boolean hasElevation = false;
		boolean hasColorOffset = false;
	
		// Does the element have elevation?
		// https://developer.android.com/training/material/shadows-clipping.html
		String elevation = element.getAttribute("android:elevation");
		
		elevation = project.getValues().get("dimens.xml").get(elevation.substring(elevation.indexOf("/") + 1));
		elevation = elevation!=null?elevation:"2dp"; //setting default value if there is no entry in dimen.xml, workaround
		
		String formattedElevation = elevation.replaceAll("[^\\d.]", "");
		System.out.println(formattedElevation);
		double elevation_value = "".equals(formattedElevation.trim()) ? 0 : Double.parseDouble(formattedElevation);
		
		if (elevation_value > 2)
			hasElevation = true;
		
		String textColor = element.getAttribute("android:textColor");
		String backColor = element.getAttribute("android:background");
		if (textColor != backColor)
			hasColorOffset = true;
		
	}		
}
