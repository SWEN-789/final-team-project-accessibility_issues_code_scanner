package rit.se.SWEN789.rules;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import java.awt.Color;

import rit.se.SWEN789.constants.AccessibilityDefect;
import rit.se.SWEN789.constants.AndroidUIElement;
import rit.se.SWEN789.util.A11yCheckerUtil;
import rit.se.SWEN789.vo.ProjectVO;
import rit.se.SWEN789.vo.XMLFileVO;


public class ContrastRule {
	//https://www.w3.org/TR/mobile-accessibility-mapping/#contrast	
	public static final double MIN_CONTRAST_RATIO = 4.5; 
	
	//Basically any element with color
	@SuppressWarnings("serial")
	public static List<String> elements = new ArrayList<String>() {
		{
			add(AndroidUIElement.BUTTON.getName());
			add(AndroidUIElement.IMAGE_BUTTON.getName());
			add(AndroidUIElement.CHECK_BOX.getName());
			add(AndroidUIElement.EDIT_TEXT.getName());
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
		
		// Get hex colors for text and backgrounds
		String textColor = element.getAttribute("android:textColor");
		String backColor = element.getAttribute("android:background");
		
		
		// Find the relative luminance of both colors, use them to find the contrast ratio
		//We currently skip the color names, must be handled in the future
		if (textColor != null && !textColor.isEmpty() && !textColor.matches(".*[color].*") && backColor != null && !backColor.isEmpty()) {
			// https://www.w3.org/TR/2008/REC-WCAG20-20081211/#relativeluminancedef
			// L = 0.2126 * R + 0.7152 * G + 0.0722 * B
			// where R = R_8bit/255, G = G_8bit/255, B = B_8bit/255
			
			double textR = Integer.parseInt(textColor.substring(1, 3), 16) / 255;
			double textG = Integer.parseInt(textColor.substring(3, 5), 16) / 255;
			double textB = Integer.parseInt(textColor.substring(5, 7), 16) / 255;
			
			double backR = Integer.parseInt(textColor.substring(1, 3), 16) / 255;
			double backG = Integer.parseInt(textColor.substring(3, 5), 16) / 255;
			double backB = Integer.parseInt(textColor.substring(5, 7), 16) / 255;
			
			double textLuminance = 0.2126 * textR + 0.7152 * textG + 0.0722 * textB;
			double backLuminance = 0.2126 * backR + 0.7152 * backG + 0.0722 * backB;
			
			//https://www.w3.org/TR/WCAG/#contrast-ratiodef
			double contrast = (backLuminance + .05) / (textLuminance + .05);
			
			if (contrast < MIN_CONTRAST_RATIO) {
				System.out.println("Low Contrast defect found!");
				A11yCheckerUtil.createDefect(xmlFile, element, AccessibilityDefect.LOW_CONTRAST,
						"Contrast", "" + contrast);
			}
		}
	}	
}
