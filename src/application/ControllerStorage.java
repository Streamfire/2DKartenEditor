package application;

/**
 * Singleton Klasse zur Kommunikation zwischen dem EditorWindowController<br>
 * und den anderen Controllerklassen.
 *
 */
public class ControllerStorage
{
	//Data for new Map  Creation
	/**
	 * Höhe einer neuen Karte
	 */
	public  int Height;
	/**
	 * Breite einer neuen Karte
	 */
	public  int Width;
	/**
	 * Ebenen einer neuen Karte
	 */
	public  int Layers;
	/**
	 * Pfad zum Tileset der neun Karte
	 */
	public  String Path;
	/**
	 * Boolean der bei true dafür sorgt dass der EditorWindowController eine neue Karte erstellt.
	 */
	public  boolean newdata;
	//Data for saving the map
	/**
	 * Der Pfad zum speichern der Karte
	 */
	public String savePath;
	/**
	 * Boolean der bei true dafür sorgt dass der EditorWindowController die Karte speichert.
	 */
	public boolean saveNext;
	//Data for loading a map
	/**
	 * Der Pfad zum Speichern der Karte
	 */
	public String loadPath;
	/**
	 * Boolean der bei true dafür sorgt dass der EditorWindowController die Karte vom Pfad lädt.
	 */
	public boolean loadNext;
	
	/**
	 * Referenz auf die Instanz des ControllerStorage
	 */
	private static ControllerStorage instance=null;
	// -----------------------------------------------\\
	/**
	 * Konstruktor der die Storagedaten mit Standardwerten belegt.
	 */
	public ControllerStorage()
	{
		  Height=5;
		  Width=7;
		  Layers=3;
		  Path="Tileset.png";
		  newdata=false;
		  saveNext=false;
		  loadNext=false;
	}
	
	/**
	 * Gibt die bereits vorhandene oder eine neu erstellte Instanz zurück.
	 * @return Die Instanz der ControllerStorage Klasse
	 */
	public static ControllerStorage getInstance() {
        
		if (instance == null) {
            instance = new ControllerStorage();
        }
        return instance;
    }
}
