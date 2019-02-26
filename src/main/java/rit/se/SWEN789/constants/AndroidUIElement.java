package rit.se.SWEN789.constants;

public enum AndroidUIElement {
	
	IMAGE_BUTTON("ImageButton"),IMAGE_VIEW("ImageView"),EDIT_TEXT("EditText"),TEXT_VIEW("TextView"),
	BUTTON("Button"), TOGGLE_BUTTON("ToggleButton"), SWITCH("Switch"), CHECK_BOX("CheckBox"),
	RADIO_GROUP("RadioGroup"), RADIO_BUTTON("RadioButton"), SPINNER("Spinner"),SCROLL_VIEW("ScrollView"),
	TOAST("Toast"),LIST_VIEW("ListView");
	
	private String name;

	private AndroidUIElement(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
