package map;

/**
 * Diese Klasse speichert alle notwendigen Informationen um ein Feld zu rendern.
 */
public class CTile
{
	/**
	 * Eine Referenz auf das im Editor geladene Tileset.
	 */
	private CTileset tileset;
	/**
	 * Die Nummer Ausschnittes des Tilesets mit dem dieses Feld belegt ist.
	 * Beginnt bei 0, ein Wert von -1 steht für ein leeres Feld.
	 */
	private int tileNum;
	// -----------------------------------------------\\
	
	/**
	 * Konstruktor der die übergebene Tilesetreferenz speichert und tileNum mit einem Standardwert belegt.
	 * @param newSet Referenz auf das zum rendern zu verwendende Tileset
	 */
	public CTile(CTileset newSet)
	{
		tileset= newSet;
		tileNum=-1;
	}

	/**
	 * Methode die es ermöglicht das Tileset zu ändern ohne ein neues Tile Objekt zu erstellen.
	 * @param newSet Referenz auf das <b>neue</b> zum rendern zu verwendende Tileset
	 */
	public void setTileset(CTileset newSet)
	{
		tileset = newSet;
	}

	
	/**
	 * Setzt den übergebenen Wert für die tileNum
	 * @param newTileNum der neue Wert für tileNum
	 */
	public void setTileNum(int newTileNum)
	{
		tileNum = newTileNum;
	}

	/**
	 * Die Set Methode für die Nummer des Tilesetausschnittes den dieses Objekt speichert
	 * @return liefert die momentan für dieses Tile gesetzte tileNum zurück
	 */
	public int getTileNum()
	{
		return tileNum;
	}
	
	/**
	 * Falls tileNum einen Wert größer -1 hält wird die Renderfunktion des Tilesets aufgerufen.
	 *  x, y und tileNum werden als Parameter übergeben um an der Stelle (x,y) auf dem Canvas den der tileNum entsprechenden Ausschnitt des Tilesets zu rendern.
	 * @param x der horizontale Wert an dem der der tileNum entsprechende Ausschnitt des Tilesets gerendert werden soll.
	 * @param y der vertikale Wert an dem der der tileNum entsprechende Ausschnitt des Tilesets gerendert werden soll.
	 */
	public void render(int x, int y)
	{
		if(tileNum > -1)
		{
			tileset.render(x, y, tileNum);
		}
	}
}
