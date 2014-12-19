package menus.filemenuitems;

import gui.Controller;
import javafx.scene.control.MenuItem;
import menus.FileMenu;

public class ConsumeDriverFiles extends MenuItem {
	public ConsumeDriverFiles(FileMenu menu, Controller control){
		super("Consume Driver Files");
		this.setDisable(true);
		this.setOnAction(event->control.consumeDriverFiles());
	}
}
