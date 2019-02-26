package rit.se.SWEN789.constants;

public enum AccessibilityDefect {

	NO_CONTENT_DESCRIPTION("No Content Description"), UNTOUCHABLE_TARGET("Untouchable Target"),SMALL_TEXT_SIZE("Small Text Size"), 
	LOW_CONTRAST("Low Contrast"), SMALL_TOUCH_SPACING("Small Touch Spacing"), UNCLEAR_ACTIONABLE_ELEMENT("Clear Actionable Element");
	
	private String name;

	private AccessibilityDefect(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
