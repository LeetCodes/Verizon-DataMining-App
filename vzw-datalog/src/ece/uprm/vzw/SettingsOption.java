package ece.uprm.vzw;

public class SettingsOption{
	private String name;
	private String description;
	private boolean hasCheckBox;
	private boolean checkBoxActive;
	
	public SettingsOption(String name, String description, boolean hasCheckBox,
			boolean checkBoxActive) {
		super();
		this.name = name;
		this.description = description;
		this.hasCheckBox = hasCheckBox;
		this.checkBoxActive = checkBoxActive;
	}

	public SettingsOption(String name, String description, boolean hasCheckBox) {
		super();
		this.name = name;
		this.description = description;
		this.hasCheckBox = hasCheckBox;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isHasCheckBox() {
		return hasCheckBox;
	}

	public boolean isCheckBoxActive() {
		return checkBoxActive;
	}
	
	
	

	

	
}
