package rit.se.SWEN789.rules;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import rit.se.SWEN789.constants.AccessibilityDefect;
import rit.se.SWEN789.constants.AndroidUIElement;
import rit.se.SWEN789.util.A11yCheckerUtil;
import rit.se.SWEN789.vo.ProjectVO;
import rit.se.SWEN789.vo.XMLFileVO;

public class TouchTargetSpacingRule {

	public final static double MIN_PADDING = 8;

	@SuppressWarnings("serial")
	public static List<String> elements = new ArrayList<String>() {
		{
			add(AndroidUIElement.BUTTON.getName());
			add(AndroidUIElement.IMAGE_BUTTON.getName());
			add(AndroidUIElement.CHECK_BOX.getName());
			add(AndroidUIElement.EDIT_TEXT.getName());
			add(AndroidUIElement.RADIO_BUTTON.getName());
			add(AndroidUIElement.RADIO_GROUP.getName());
			add(AndroidUIElement.SPINNER.getName());
			add(AndroidUIElement.SWITCH.getName());
			add(AndroidUIElement.TOGGLE_BUTTON.getName());
			add(AndroidUIElement.LIST_VIEW.getName());
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
		
		
		String padding = element.getAttribute("android:padding");
		
		padding = project.getValues().get("dimens.xml").get(padding.substring(padding.indexOf("/") + 1));
		padding = padding!=null?padding:"8dp";//setting default value if there is no entry in dimen.xml, workaround
		
		String formattedPadding = padding.replaceAll("[^\\d.]", "");
		System.out.println(formattedPadding);
		double value = "".equals(formattedPadding.trim()) ? 0 : Double.parseDouble(formattedPadding);
		if (value < MIN_PADDING) {
			A11yCheckerUtil.createDefect(xmlFile, element, AccessibilityDefect.SMALL_TOUCH_SPACING,
					"android:padding", padding);
		}
	}		
}
