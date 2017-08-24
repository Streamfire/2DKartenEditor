package map;

import javafx.scene.canvas.GraphicsContext;

/**
 * Klasse die alle Informationen über eine Karte hält und in der Lage ist<br>
 * diese Informationen grafisch auszugeben, in einen BitString umzuwandeln<br>
 * oder aus einem Bitstring auszulesen.
 */
public class CMap
{

	/**
	 * Das Tileset dass zum rendern der Karte benutzt wird
	 */
	private CTileset tileset;
	/**
	 * Das Tileset das zum rendern der Solidität eines Feldes benutzt wird
	 */
	private CTileset soliditySet;

	/**
	 * 3 Dimensionales Array, welches die Tiles der Karte beinhaltet.<br>
	 * [Kartenebene][X Wert][Y Wert]
	 */
	private CTile[][][] tiles;// Layer,X,Y
	/**
	 * Die Anzahl der Ebenen / Tiles in X Richtung/ Tiles in Y Richtung der momentan geladenen Karte
	 */
	private int layercount, height, width;
	/**
	 * 2 Dimensionales Array in dem vermerkt wird ob es sich beim Feld [X][Y] um ein solides Feld(true) handelt oder nicht(false)
	 * der Sinn dieses Wertes ist es, festzulegen, ob Spieler/NPCs/etc. sich auf diesem Feld befinden/ es betreten können
	 */
	private boolean[][] solidLayer;// X,Y

	/**
	 * interner Vermerk welche Kartenebenen momentan angesehen werden sollen und entsprechend gerendert werden müssen
	 * [Layer]
	 */
	private boolean[] visible;//Layer
	/**
	 * interner Vermerk darüber ob die Werte des solidLayer Arrays angezeigt werden sollen(true) oder nicht(false)
	 */
	private boolean solidityVisible;

	/**
	 * Die Größe(x*x) in der die Ausschnitte aus dem Tileset auf dem MapCanvas gerendert werden sollen.
	 * momentan nicht implementiert 
	 */
	private int scalesize;
	/**
	 * Die Größe des Ausschnittes der vom Tileset auf die Karte übernommen werden soll
	 */
	@SuppressWarnings("unused")
	private int fHeight, fWidth;

	// -----------------------------------------------\\

	
	/**
	 * Konstruktor der Map Klasse. Lädt sowohl das verwendete Tileset als auch das Soliditätstileset,<br> 
	 * erstellt und initialisiert das CTileArray in dem die Kartendaten gespeichert werden. 
	 * @param height die Höhe der Karte in Feldern gemessen
	 * @param width die Weite der Karte in Feldern gemessen
	 * @param layercount die Zahl der Ebenen der Karte(beginnt bei 0, d.h. 10 Ebenen = Ebene0-Ebene9)
	 * @param gc der GraphicsContext der genutzt werden soll um die Karte anzuzeigen("rendern")
	 * @param tilesetPath der (absolute) Pfad zum Tileset das zum Rendern genutzt werden soll
	 * @param frameHeight die Höhe eines Feldes auf dem Tileset(vorzugsweise 32px)
	 * @param frameWidth die Weite eines Feldes auf dem Tileset(vorzugsweise 32px)
	 * @param fillTile die Nummer des Tiles im Tileset mit dem die unterste Ebene gefüllt werden soll
	 */
	public CMap(int height, int width, int layercount, GraphicsContext gc, String tilesetPath, int frameHeight,
			int frameWidth, int fillTile)
	{

		tiles = new CTile[layercount][height][width];
		this.layercount = layercount;
		this.height = height;
		this.width = width;
		this.scalesize = 32;//numbers larger than the resolution of the tileset may(read: will) result in scaleing errors

		this.fHeight = frameHeight;
		this.fWidth = frameWidth;

		/*
		 * setting the ScaleHeight/-Width as scalesize means every tileset will
		 * be displayed at scalesize by scalesize pixels that makes it easier to
		 * calculate the position of every Tile on the canvas 
		 */

		tileset = new CTileset(tilesetPath, gc, frameHeight, frameWidth, scalesize, scalesize);

		/*
		 * Set ALL Tiles to be empty
		 */
		for (int n = 0; n < layercount; n++)
		{

			for (int i = 0; i < height; i++)
			{
				for (int j = 0; j < width; j++)
				{
					tiles[n][i][j] = new CTile(tileset);
					tiles[n][i][j].setTileNum(-1);
				}
			}
		}

		/*
		 * filling the lowest Layer with the Filltile
		 */
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				tiles[0][i][j].setTileNum(fillTile);
			}
		}

		soliditySet = new CTileset("soliditySet.png", gc, frameHeight, frameWidth, scalesize, scalesize);
		/*
		 * setting up the solidity Layer as 'All non-solid'
		 */
		solidLayer = new boolean[height][width];
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				solidLayer[i][j] = false;
			}
		}

		visible = new boolean[layercount];
		for (int j = 0; j < layercount; j++)
		{
			visible[j] = false;
		}
		solidityVisible = false;
	}

	/**
	 * Setter Methode für das visible Array dass genutzt wird um auszuwählen welche Kartenebenen gerendert werden sollen
	 * @param layer die Ebene deren visible Wert geändert werden soll
	 * @param newState der neue Wert der dem visible Attribut der Ebene layer zugewiesen werden soll
	 */
	public void setVisible(int layer, boolean newState)
	{
		visible[layer] = newState;
	}

	/**
	 * Setter Methode für die Tilenummer eines beliebigen Tiles im tiles Array
	 * @param layer die Kartenebene auf der sich das zu verändernde Feld befindet
	 * @param x der X wert (in Feldern) des zu ändernden Feldes
	 * @param y der Y Wert (in Feldern) des zu ändernden Feldes
	 * @param TileNr die neue Tilenummer des Tiles, welches das angegebene Feld repräsentiert
	 */
	public void setTile(int layer, int x, int y, int TileNr)
	{
		tiles[layer][x][y].setTileNum(TileNr);
	}

	/**Setter Methode für die Tilenummer eines beliebigen Tiles im tiles Array <br>
	 * für den Fall dass nur die Daten eines Mouseevents gegeben sind und daraus das gewünschte Feld zu ermitteln ist,<br>
	 * oder keine direkte Tilenummer sondern 2 seperate Nummern, welche X und Y repräsentieren gegeben sind.
	 * @param layer die Kartenebene auf der sich das zu verändernde Feld befindet
	 * @param h der Y Wert des Mouseevents
	 * @param w der X Wert des Mousevents
	 * @param TileX der X Wert(in Feldern) der gewünschten neuen Tilenummer
	 * @param TileY der Y Wert(in Feldern) der gewünschten neuen Tilenummer
	 */
	public void setTile(int layer, int h, int w, int TileX, int TileY)
	{
		tiles[layer][w / scalesize][h / scalesize].setTileNum(TileY * (tileset.getImgWidth() / fWidth) + TileX);
	}

	/**
	 * Umschaltende Methode um den Wert des solidLayer Arrays für ein bestimmtes Feld umzuschalten.<br>
	 * die Angabe des Feldes erfolgt dabei über die x,y Koordinaten eines Mouseevents.
	 * @param h Y Wert eines Mouseevents
	 * @param w X Wer eines Mouseevents
	 */
	public void toggleSolidity(int h, int w)
	{
		solidLayer[w / scalesize][h / scalesize] = !solidLayer[w / scalesize][h / scalesize];
	}

	/**
	 * Methode um festzulegen ob die Werte aus dem solidLayer Array gerendert werden sollen oder nicht.
	 * true= rendern, false = nicht rendern
	 * @param newState der neue Wert der Entscheidervariable 
	 */
	public void setSolidityVisible(boolean newState)
	{
		solidityVisible = newState;
	}

	/**
	 * Getter Methode für die Zahl der Ebenen
	 * @return Zahl der Kartenebenen(als Integer)
	 */
	public int getLayerCount()
	{
		return layercount;
	}

	/**
	 * Getter Methode für die Zahl der Felder in Y Richtung
	 * @return  die Zahl der Felder in Y Richtung(als Integer)
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 *Getter Methode für die Zahl der Felder in X Richtung
	 * @return  die Zahl der Felder in X Richtung(als Integer)
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 *Getter Methode für Größe in der Felder gerendert werden sollen
	 * @return Rendergröße der Felder (als Integer)
	 */
	public int getScalesize()
	{
		return scalesize;
	}

	/**
	 * Methode die den String aus 1 und 0 generiert, welcher zum speichern der Karte genutzt wird
	 * @return ein String aus 0 und 1 der alle notwendigen Daten enthält um die Karte wiederherzustellen
	 */
	public String getSaveString()
	{
		String returnString = new String();

		returnString = force8Bit("0");
		returnString += force8Bit(Integer.toBinaryString(layercount));
		returnString += force8Bit(Integer.toBinaryString(width));
		returnString += force8Bit(Integer.toBinaryString(height));

		// converting the solidity Layer to a binary String
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if (solidLayer[i][j] == true)
				{
					returnString += force8Bit("1");
				} else
				{
					returnString += force8Bit("0");
				}
			}
		}

		// converting the Tiles to 8 Bit binary String
		for (int n = 0; n < layercount; n++)
		{
			for (int i = 0; i < height; i++)
			{
				for (int j = 0; j < width; j++)
				{
					returnString += force8Bit(Integer.toBinaryString(tiles[n][i][j].getTileNum()));
				}
			}
		}
		return returnString;
	}

	/**
	 * Methode zum Laden von gespeicherten Karten
	 * @param saveString ein String aus 1 und 0 der die Daten einer Karte enthält
	 */
	public void loadSaveString(String saveString)
	{

		saveString = saveString.substring(32);// cutting of the first 4 bytes
		String solidityString = saveString.substring(0, (height * width * 8));
		String dataString = saveString.substring((height * width * 8),
				(height * width * layercount * 8) + (height * width * 8));

		// converting the solidityString to the solidity Layer
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if (Integer.parseInt(solidityString.substring(0, 8), 2) == 1)
				{
					solidLayer[i][j] = true;
				} else
				{
					solidLayer[i][j] = false;
				}
				solidityString = solidityString.substring(8);
			}
		}

		// converting the data String into the layers
		for (int n = 0; n < layercount; n++)
		{
			for (int i = 0; i < height; i++)
			{
				for (int j = 0; j < width; j++)
				{
					if(Integer.parseInt(dataString.substring(0, 8), 2)==255)
					tiles[n][i][j].setTileNum(-1);
					else
					tiles[n][i][j].setTileNum(Integer.parseInt(dataString.substring(0, 8), 2));	
						
					dataString=dataString.substring(8);
				}
			}
		}
	}

	/**
	 * Hilfsmethode die dazu dient, einen String aus 0 und 1 auf exakt 8 Stellen zu erweitern/trimmen 
	 * @param binaryString der zu trimmende/erweiternde String
	 * @return der erweiterte/ getrimmte String
	 */
	public static String force8Bit(String binaryString)
	{
		String s = new String("00000000");
		s += binaryString;
		s = s.substring(s.length() - 8);
		return s;
	}

	/**
	 * Render Methode für die Karte, rendert die Karte, <br>
	 * dabei liegt die linke obere Ecke des Feldes [0][0] an der <br>
	 * durch x und y festgelegten Position
	 * @param x X Position der Karte
	 * @param y Y Position der Karte
	 */
	public void render(int x, int y)
	{
		for (int n = 0; n < layercount; n++)
		{
			if (visible[n] == true)
			{
				for (int i = 0; i < height; i++)
				{
					for (int j = 0; j < width; j++)
					{
						tiles[n][i][j].render(j * scalesize, i * scalesize);
					}
				}
			}
		}
	}

	/**
	 * Render Methode für das solidLayer Array, rendert das Array falls solidityVisible == true, <br>
	 * dabei liegt die linke obere Ecke des Feldes [0][0] an der <br>
	 * durch x und y festgelegten Position
	 * @param x X Position der Karte
	 * @param y Y Position der Karte
	 */
	public void renderSolidity(int x, int y)
	{
		if (solidityVisible == true)
		{
			for (int i = 0; i < height; i++)
			{
				for (int j = 0; j < width; j++)
				{
					if (solidLayer[i][j] == false)
					{
						soliditySet.render((j * scalesize) + x, (i * scalesize) + y, 0);
					} else
					{
						soliditySet.render((j * scalesize) + x, (i * scalesize) + y, 1);
					}
				}
			}
		}
	}

}
