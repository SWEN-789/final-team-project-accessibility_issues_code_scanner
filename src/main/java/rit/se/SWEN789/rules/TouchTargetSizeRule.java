package rit.se.SWEN789.rules;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import rit.se.SWEN789.constants.AccessibilityDefect;
import rit.se.SWEN789.constants.AndroidUIElement;
import rit.se.SWEN789.util.A11yCheckerUtil;
import rit.se.SWEN789.vo.ProjectVO;
import rit.se.SWEN789.vo.XMLFileVO;

public class TouchTargetSizeRule {

	public final static double MIN_SIZE = 48;

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
			//More UI elements to be added
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
		String width = element.getAttribute("android:layout_width");
		String height = element.getAttribute("android:layout_height");
		String minWidth = element.getAttribute("android:minWidth");
		String minHeight = element.getAttribute("android:minHeight");
		
		//wrap_content can be ignored as UI touch elements minHeight and minWidth of 48dp even if the content size is less
		//TO DO handle match_parent value
		if (width != null && !width.trim().isEmpty() && !"wrap_content".equals(width)
				&& !"match_parent".equals(width) && !"fill_parent".equals(width)) {
			if (width.contains("@dimen")) {
				width = project.getValues().get("dimens.xml").get(width.substring(width.indexOf("/") + 1));
				width = width!=null?width:"48dp";//setting default value if there is no entry in dimen.xml, workaround
			}
			String formattedWidth = width.replaceAll("[^\\d.]", "");
			System.out.println(formattedWidth);
			double value = "".equals(formattedWidth.trim())?0:Double.parseDouble(formattedWidth);
			if (value < MIN_SIZE) {
				A11yCheckerUtil.createDefect(xmlFile, element, AccessibilityDefect.UNTOUCHABLE_TARGET,
						"android:layout_width", width);
			}
		}

		if (height != null && !height.trim().isEmpty() && !"wrap_content".equals(height)
				&& !"match_parent".equals(height) && !"fill_parent".equals(height)) {
			System.out.println(height);
			if (height.contains("@dimen")) {
				height = project.getValues().get("dimens.xml").get(height.substring(height.indexOf("/") + 1));
				height = height!=null?height:"48dp";//setting default value if there is no entry in dimen.xml, workaround
			}
			String formattedHeight = height.replaceAll("[^\\d.]", "");
			System.out.println(formattedHeight);
			double value = "".equals(formattedHeight.trim())?0:Double.parseDouble(formattedHeight);
			if (value < MIN_SIZE) {
				A11yCheckerUtil.createDefect(xmlFile, element, AccessibilityDefect.UNTOUCHABLE_TARGET,
						"android:layout_height", height);
			}
		}
		
		if(minWidth!=null && !minWidth.trim().isEmpty()){
			if (minWidth.contains("@dimen")) {
				minWidth = project.getValues().get("dimens.xml").get(minWidth.substring(minWidth.indexOf("/") + 1));
			}
			String formattedMinWidth = minWidth.replaceAll("[^\\d.]", "");
			double value = "".equals(formattedMinWidth.trim())?0:Double.parseDouble(formattedMinWidth);
			if (value < MIN_SIZE) {
				A11yCheckerUtil.createDefect(xmlFile, element, AccessibilityDefect.UNTOUCHABLE_TARGET,
						"android:minWidth", minWidth);
			}
		}
		
		if(minHeight!=null && !minHeight.trim().isEmpty()){
			if (minHeight.contains("@dimen")) {
				minHeight = project.getValues().get("dimens.xml").get(minHeight.substring(minHeight.indexOf("/") + 1));
			}
			String formattedMinHeight = minHeight.replaceAll("[^\\d.]", "");
			double value = "".equals(formattedMinHeight.trim())?0:Double.parseDouble(formattedMinHeight);
			if (value < MIN_SIZE) {
				A11yCheckerUtil.createDefect(xmlFile, element, AccessibilityDefect.UNTOUCHABLE_TARGET,
						"android:minHeight", minHeight);
			}
		}
	}
}
