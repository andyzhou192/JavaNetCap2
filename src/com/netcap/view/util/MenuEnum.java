package com.netcap.view.util;

public enum MenuEnum {
	START("start", "Start"), 
	STOP("stop", "Stop"), 
	SAVE("save", "Save"), 
	SAVEAS("saveAs", "SaveAs"), 
	EXIT("exit", "Exit"), 
	SETTING("setting", "Setting"),
	GENEJAVA("generatorJava", "Generator to Java Script");
	
	private String command;
	private String description;
	
	private MenuEnum(String command, String description){
		this.command = command;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	public static MenuEnum getMenuEnum(String command){
		for (MenuEnum e : MenuEnum.values()) {
            if(e.getCommand().equals(command))
            	return e;
        }
		return null;
	}
}
