package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import map.CMap;
import map.CTileset;

/**
 * Die Hauptkontroller Klasse EditorWindowController ist zuständig für <br>
 * die Verwaltung und das editieren der Karte, die Verwaltung des Auswahlfeldes,<br>
 * das erstellen neuer Fenster, das Speichern/Laden von Kartendaten und <br>
 * für die Auswahl aller Optionen die das Editorfenster beinhaltet. 
 *
 */
public class EditorWindowController
{

	/**
	 * Die Instanz der Karte, welche bearbeitet wird
	 */
	private CMap map;
	/**
	 * Das Tileset, welches zur Auswahl des neu zu setzenden Tiles benutzt wird
	 */
	private CTileset selectionTileset;
	/**
	 * Das Ausgewählte Tile in X Richtung
	 */
	private int selectedTileX;
	/**
	 * Das Ausgewählte Tile in Y Richtung
	 */
	private int selectedTileY;

	/**
	 * Zwischenwert für die Zahl der Kartenebenen
	 */
	private String LayerNumber;

	/**
	 * Canvas für die zu bearbeitende Karte
	 */
	@FXML
	private Canvas mapCanvas;
	/**
	 * GraphicsContext des Canvas für die zu bearbeitende Karte
	 */
	private GraphicsContext gcMapCanvas;
	/**
	 * Canvas für das Tile Auswahlmenue
	 */
	@FXML
	private Canvas tilesetCanvas;
	/**
	 * GraphicsContext des Canvas für das Tile Auswahlmenue
	 */
	private GraphicsContext gcTilesetCanvas;
	/**
	 * Der MenueButton für die Auswahl der zu zeigenden Kartenebenen
	 */
	@FXML
	private MenuButton LayerMenueBtn;
	/**
	 * das Array von MenueItems mit dem das Ebenen-Ansichts-Auswahlmenue belegt wird
	 */
	private RadioMenuItem LayerMenuItems[];
	/**
	 * Das Auswahlmenue mit dem festgelegt wird welche Kartenebene bearbeitet wird
	 */
	@FXML
	private ChoiceBox<String> LayerEditChoiceBox;
	/**
	 * Checkbox zur Entscheidung ob die Soliditätswerte der Felder gezeigt werden sollen oder nicht
	 */
	@FXML
	private CheckBox checkBoxShowSolidity;
	/**
	 * Checkbox zur Entscheidung ob <b>alle</b> Kartenebenen angezeigt werden sollen
	 */
	@FXML
	private CheckBox checkBoxForceShowAllLayers;
	/**
	 * Die Horizontale Scrollbar für Tile Auswahlmenue
	 */
	@FXML
	private ScrollBar tilesetScrollbar;
	// -----------------------------------------------\\
	/**
	 * Initialisierungsmethode, setzt Standardwerte für eine Karte der Größe 5H,7W,3E.<br>
	 * Belegt alle Auswahlmenues mit den notwendigen Auswahlmöglichkeiten.
	 * Updatet den Controller.
	 */
	public void initialize()
	{
		gcMapCanvas = mapCanvas.getGraphicsContext2D();
		gcTilesetCanvas = tilesetCanvas.getGraphicsContext2D();
		selectedTileX = 0;
		selectedTileY = 0;
		LayerNumber = new String();

		map = new CMap(5, 7, 3, gcMapCanvas, ControllerStorage.getInstance().Path, 32, 32, 0);
		selectionTileset = new CTileset(ControllerStorage.getInstance().Path, gcTilesetCanvas, 32, 32, 32, 32);
		tilesetScrollbar.setMax((selectionTileset.getImgWidth() - tilesetCanvas.getWidth()) / 2);

		/*
		 * creating the ChoiceBox options determines which layer of the map is
		 * currently being edited
		 */
		ObservableList<String> layerList = FXCollections.observableArrayList();
		layerList.add("SolidityLayer");
		for (int i = 0; i < map.getLayerCount(); i++)
		{
			layerList.add("Layer " + i);
		}
		LayerEditChoiceBox.setItems(layerList);
		LayerEditChoiceBox.setValue("SolidityLayer");
		/*
		 * creating the RadioMenu items for the LayerMenu
		 */
		LayerMenuItems = new RadioMenuItem[map.getLayerCount()];
		for (int i = 0; i < map.getLayerCount(); i++)
		{
			LayerMenuItems[i] = new RadioMenuItem("Layer " + i);
			LayerMenueBtn.getItems().add(LayerMenuItems[i]);

			final int ii = i;
			LayerMenuItems[i].setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent e)
				{
					if (LayerMenuItems[ii].isSelected())
					{
						map.setVisible(ii, true);
						update();
					} else
					{
						map.setVisible(ii, false);
						update();
					}
				}// handle
			});// new Eventhandler
		} // LayerMenuItem Creation
		update();
	}// Initialize

	/**
	 * Methode zur Reaktion auf Wertänderungen der checkBoxShowSolidity.<br>
	 * Ruft die CMap Methode zum aendern des Sichtbarkeitswertes auf und gibt dieser<br>
	 * den entsprechenden Wert weiter, updatet dannach den Controller.
	 */
	@FXML
	public void SolidityToggled()
	{
		map.setSolidityVisible(checkBoxShowSolidity.isSelected());
		update();
	}

	/**
	 * Methode zur Reaktion auf Wertänderungen der checkBoxForceShowAllLayers.<br>
	 * Ruft die CMap Methode zum aendern des Sichtbarkeitswertes auf und gibt dieser<br>
	 * den entsprechenden Wert weiter, updatet dannach den Controller.
	 */
	@FXML
	public void AllLayersToggled()
	{
		for (int i = 0; i < map.getLayerCount(); i++)
		{
			map.setVisible(i, checkBoxForceShowAllLayers.isSelected());
		}
		update();
	}

	/**
	 * Reagiert auf die Auswahl von "New" im "File" Menue.<br>
	 * Erzeugt ein neues Fenster auf Vorlage von "NewFile.fxml".
	 * Updatet den Controller.
	 */
	@FXML
	public void FileMenueNewClicked()
	{
		Stage FileNewStage = new Stage();
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("NewFile.fxml"));
			AnchorPane root = loader.load();
			Scene scene = new Scene(root);
			FileNewStage.setScene(scene);
			FileNewStage.show();

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		update();
	}

	/**
	 * Reagiert auf die Auswahl von "Save" im "File" Menue.<br>
	 * Erzeugt ein neues Fenster auf Vorlage von "SaveFile.fxml".
	 * Updatet den Controller.
	 */
	@FXML
	public void SaveMenueClicked()
	{
		Stage SaveMenueStage = new Stage();
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("SaveFile.fxml"));
			AnchorPane root = loader.load();
			Scene scene = new Scene(root);
			SaveMenueStage.setScene(scene);
			SaveMenueStage.show();

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		update();
	}

	/**
	 * Reagiert auf die Auswahl von "Load" im "File" Menue.<br>
	 * Erzeugt ein neues Fenster auf Vorlage von "LoadFile.fxml".
	 * Updatet den Controller.
	 */
	@FXML
	public void LoadMenueClicked()
	{
		Stage LoadMenueStage = new Stage();
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("LoadFile.fxml"));
			AnchorPane root = loader.load();
			Scene scene = new Scene(root);
			LoadMenueStage.setScene(scene);
			LoadMenueStage.show();

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Reagiert auf die Auswahl von "Close" im "File" Menue.<br>
	 * Schließt das Editor Fenster.
	 */
	@FXML
	public void CloseClicked()
	{
		mapCanvas.getScene().getWindow().hide();
	}

	/**
	 * Reagiert auf Klicks auf dem Kartencanvas.<br>
	 * Prüft den Wert der LayerEditchoicebox um festzustellen,<br>
	 * Welche Kartenebene bearbeitet wird und übergibt anschließend alle <br>
	 * notwendigen Werte an die CMap Instanz.
	 * @param event das vom Klick erzeugte Mouseevent
	 */
	@FXML
	public void MapCanvasClicked(MouseEvent event)
	{	
		try
		{
			if (LayerEditChoiceBox.getValue() == "SolidityLayer")
				map.toggleSolidity((int) event.getX(), (int) event.getY());
			else
				LayerNumber = LayerEditChoiceBox.getValue().substring(6);

		} catch (ArrayIndexOutOfBoundsException e)
		{
		}

		try
		{
			map.setTile(Integer.parseInt(LayerNumber), (int) event.getX(), (int) event.getY(), selectedTileX,
					selectedTileY);
		} catch (Exception e)
		{

		}

		update();

	}

	/**
	 * Reagiert auf Klicks auf dem Auswahlcanvas.<br>
	 * Setzt die Werte für selectedTileX und selectedTileY<br>
	 * entsprechend des MouseEvents. 
	 * @param event das vom Klick erzeugte MouseEvent 
	 */
	@FXML
	public void SelectionCanvasClicked(MouseEvent event)
	{
		selectedTileX = (int) (((tilesetScrollbar.getValue() * 2) + event.getX()) / 32);
		selectedTileY = (int) (event.getY() / 32);
		update();
	}

	/**
	 * Bereinigt beide Canvas, ruft sowohl die Karten Rendermethode als auch die <br>
	 * des Auswahl-Tileset auf.<br>
	 * Es wird geprüft on im StorageController newdata, saveNext oder loadNext auf true stehen,<br>
	 * ist das der Fall wird die entsprechend geforderte Methode aufgerufen.
	 */
	@FXML
	public void update()
	{
		gcTilesetCanvas.clearRect(0, 0, tilesetCanvas.getWidth(), tilesetCanvas.getHeight());
		selectionTileset.renderComplete(-((int) tilesetScrollbar.getValue() * 2), 0);
		gcMapCanvas.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
		map.render(0, 0);
		map.renderSolidity(0, 0);
		if (ControllerStorage.getInstance().newdata == true)
		{
			newMap();
		}
		if (ControllerStorage.getInstance().saveNext == true)
		{
			saveMap();
		}
		if (ControllerStorage.getInstance().loadNext == true)
		{
			loadMap();
		}
	}

	/**
	 * Laedt eine Karte aus dem im StorageController angegebenen Pfad.<br>
	 * Liest zuerst alle Daten ein und wandelt sie in einen String aus 0 und 1 um.<br>
	 * aus den Daten der ersten 4 Byte werden die Parameter für eine neue Karte entnommen,<br>
	 * dannach wird der String an die neu erstellte CMap Instanz uebergeben. 
	 */
	private void loadMap()
	{

		File source = new File(ControllerStorage.getInstance().loadPath);
		FileInputStream fileIn;
		String s = "";
		try
		{
			fileIn = new FileInputStream(source);
			int i = fileIn.read();
			do
			{
				s += CMap.force8Bit(Integer.toBinaryString(i));
				i = fileIn.read();
			} while (i != -1);
			fileIn.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		
		int layer= Integer.parseInt(s.substring(8, 16), 2);//layer
		int width =Integer.parseInt(s.substring(16, 24),2);//weite
		int height=Integer.parseInt(s.substring(24, 32),2);//hoehe
		
		map=new CMap(height,width,layer,gcMapCanvas, ControllerStorage.getInstance().Path, 32, 32, 0);
		
		/*
		 * creating the ChoiceBox options determines which layer of the map is
		 * currently being edited
		 */
		ObservableList<String> layerList = FXCollections.observableArrayList();
		layerList.add("SolidityLayer");
		for (int i = 0; i < map.getLayerCount(); i++)
		{
			layerList.add("Layer " + i);
		}
		LayerEditChoiceBox.setItems(layerList);
		LayerEditChoiceBox.setValue("SolidityLayer");
		/*
		 * creating the RadioMenu items for the LayerMenu
		 */
		LayerMenuItems = new RadioMenuItem[map.getLayerCount()];
		for (int i = 0; i < map.getLayerCount(); i++)
		{
			LayerMenuItems[i] = new RadioMenuItem("Layer " + i);
			LayerMenueBtn.getItems().add(LayerMenuItems[i]);

			final int ii = i;
			LayerMenuItems[i].setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent e)
				{
					if (LayerMenuItems[ii].isSelected())
					{
						map.setVisible(ii, true);
						update();
					} else
					{
						map.setVisible(ii, false);
						update();
					}
				}// handle
			});// new Eventhandler
		} // LayerMenuItem Creation
		
		
		map.loadSaveString(s);
		ControllerStorage.getInstance().loadNext = false;
		update();
	}

	/**
	 * Schreibt die Kartendaten als Bytes in eine Datei, deren Pfad <br>
	 * im StorageController angegeben wurde, existiert diese nicht, wird sie erstellt.
	 */
	public void saveMap()
	{
		File target = new File(ControllerStorage.getInstance().savePath);
		FileOutputStream fileOut;

		try
		{
			fileOut = new FileOutputStream(target);

			char[] chsaveString = map.getSaveString().toCharArray();
			String s = new String();
			for (int i = 0; i < map.getSaveString().length(); i++)
			{
				s += chsaveString[i];
				if ((i + 1) % 8 == 0)
				{
					fileOut.write(Integer.valueOf(s, 2));
					s = "";
				}
			}
			fileOut.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ControllerStorage.getInstance().saveNext = false;
	}

	/**
	 * Erzeugt eine neue Karte.<br>
	 * Übernimmt dabei die Parameter für Hoehe,Weite,Ebenenzahl,etc.
	 * aus dem StorageController Singleton.<br>
	 * Belegt die Auswahlmenues mit den nötigen Optionen.
	 */
	public void newMap()
	{
		LayerNumber = new String();

		map = new CMap(ControllerStorage.getInstance().Height, ControllerStorage.getInstance().Width,
				ControllerStorage.getInstance().Layers, gcMapCanvas, ControllerStorage.getInstance().Path, 32, 32, 0);
		selectionTileset = new CTileset(ControllerStorage.getInstance().Path, gcTilesetCanvas, 32, 32, 32, 32);
		tilesetScrollbar.setMax((selectionTileset.getImgWidth() - tilesetCanvas.getWidth()) / 2);

		/*
		 * creating the ChoiceBox options determines which layer of the map is
		 * currently being edited
		 */
		ObservableList<String> layerList = FXCollections.observableArrayList();
		layerList.add("SolidityLayer");
		for (int i = 0; i < map.getLayerCount(); i++)
		{
			layerList.add("Layer " + i);
		}
		LayerEditChoiceBox.setItems(layerList);
		LayerEditChoiceBox.setValue("SolidityLayer");
		/*
		 * creating the RadioMenu items for the LayerMenu
		 */
		LayerMenuItems = new RadioMenuItem[map.getLayerCount()];
		for (int i = 0; i < map.getLayerCount(); i++)
		{
			LayerMenuItems[i] = new RadioMenuItem("Layer " + i);
			LayerMenueBtn.getItems().add(LayerMenuItems[i]);

			final int ii = i;
			LayerMenuItems[i].setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent e)
				{
					if (LayerMenuItems[ii].isSelected())
					{
						map.setVisible(ii, true);
					} else
					{
						map.setVisible(ii, false);
					}
				}// handle
			});// new Eventhandler
		} // LayerMenuItem Creation
		ControllerStorage.getInstance().newdata = false;
		map.setSolidityVisible(checkBoxShowSolidity.isSelected());
		update();
	}

}
