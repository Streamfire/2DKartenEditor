package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Controller Klasse für das Fenster zum Laden einer Karte.
 *
 */
public class LoadFileController
{
	
	/**
	 * Pfad des Tilesets, welches genutzt werden soll.
	 */
	@FXML
	private TextField ldPath;
	// -----------------------------------------------\\
	/**
	 * Setzt den Inhalt des Textfeldes gleich dem letzten Speicherort.
	 */
	public void initialize()
	{
		ldPath.setText(ControllerStorage.getInstance().savePath);
	}
	
	
	/**
	 * Reagiert auf den Laden Button.<br>
	 * Gibt den Pfad von dem geladen werden soll weiter an den StorageController.<br>
	 * Setzt loadNext auf true, sodass beim nächsten Update <br>
	 * die Karte vom gegebenen Pfad geladen wird.
	 */
	@FXML
	public void LoadbtnClicked()
	{
		if(!ldPath.getText().isEmpty())
		{
		ControllerStorage.getInstance().loadPath=ldPath.getText(); 
		ControllerStorage.getInstance().loadNext=true;
		ldPath.getScene().getWindow().hide();
		}
	}
	
	/**
	 * Reagiert auf den Abbrechen Button.<br>
	 * stellt sicher dass beim nächsten Update keine Map geladen wird und <br>
	 * schließt das Fenster. 
	 */
	@FXML
	public void CancelbtnClicked()
	{
		ControllerStorage.getInstance().saveNext=false;
		ldPath.getScene().getWindow().hide();
	}
}
