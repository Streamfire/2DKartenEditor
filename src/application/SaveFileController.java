package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Controller Klasse für das Fenster zum speichern der Karte
 *
 */
public class SaveFileController
{
	/**
	 * Das Textfeld für den Pfad der Datei in die gespeichert werden soll
	 */
	@FXML
	private TextField sfPath;
	// -----------------------------------------------\\
	/**
	 * Initialisierungsfunktion des Controllers
	 * Im Falle dessen, dass eine geladene Karte benutzt wird wird das Textfeld mit dem Pfad<br>
	 * der geladenen Karte vorbelegt
	 */
	public void initialize()
	{
		sfPath.setText(ControllerStorage.getInstance().savePath);
	}
	
	/**
	 * Reagiert auf den Speichern-Button.<br>
	 * Für den Fall, dass das Textfeld nicht leer ist wird an den Storage Controller <br>
	 * der Pfad weitergegeben und saveNext auf true gesetzt um beim nächsten update des <br>
	 * Editorfensters die Karte zu speichern. Schließt anschließend das Fenster.
	 */
	@FXML
	public void SavebtnClicked()
	{
		if(!sfPath.getText().isEmpty())
		{
			ControllerStorage.getInstance().savePath=sfPath.getText(); 
		
		ControllerStorage.getInstance().saveNext=true;
	
		sfPath.getScene().getWindow().hide();
		}
	}
	
	/**
	 * Reagiert auf den Abbrechen Button.<br>
	 * stellt sicher dass beim nächsten Update nicht gespeichert wird und <br>
	 * schließt das Fenster. 
	 */
	@FXML
	public void CancelbtnClicked()
	{
		ControllerStorage.getInstance().saveNext=false;
		sfPath.getScene().getWindow().hide();
	}
}
