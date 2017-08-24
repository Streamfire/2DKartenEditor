package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controller Klasse für das Fenster zum erstellen einer neuen Karte.
 *
 */
public class NewFileController
{
	/**
	 * Höhe der neuen Karte(in Feldern)
	 */
	@FXML
	private TextField fnHeight;
	/**
	 * Breite der neuen Karte(in Feldern)
	 */
	@FXML
	private TextField fnWidth;
	/**
	 * Zahl der Ebenen der neuen Karte
	 */
	@FXML
	private TextField fnLayers;
	/**
	 * Pfad zum Tileset
	 */
	@FXML
	private TextField fnTilesetPath;
	/**
	 * Der Erstellen Button
	 */
	@FXML
	private Button fnCreateBtn;
	// -----------------------------------------------\\
	

	/**
	 * Reagiert auf den Erstellen Button.<br>
	 * Gibt die eingegebenen Daten an den StorageController weiter.<br>
	 * Ist kein Pfad angegeben von dem ein Tileset geladen werden soll, ein Standardpfad gesetzt.<br>
	 * newdata wird auf true gesetzt um dem EditorWindowController mitzuteilen,<br>
	 * dass beim nächsten Update eine neue Map erstellt werden soll.
	 */
	@FXML
	public void CreateClicked()
	{
		
		ControllerStorage.getInstance().Height = Integer.valueOf(fnHeight.getText());
		ControllerStorage.getInstance().Width = Integer.valueOf(fnWidth.getText());
		ControllerStorage.getInstance().Layers = Integer.valueOf(fnLayers.getText());

		ControllerStorage.getInstance().Path = "Tileset.png";
		if (!fnTilesetPath.getText().isEmpty())
			ControllerStorage.getInstance().Path ="file:///"+ fnTilesetPath.getText();

		ControllerStorage.getInstance().newdata = true;
		fnCreateBtn.getScene().getWindow().hide();
	}

	/**
	 * Reagiert auf den Abbrechen Button.<br>
	 * stellt sicher dass beim nächsten Update keine neue Map erstellt wird und <br>
	 * schließt das Fenster. 
	 */
	@FXML
	public void CancelClicked()
	{
		ControllerStorage.getInstance().newdata = false;
		fnCreateBtn.getScene().getWindow().hide();
	
	}

}
