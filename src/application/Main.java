package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;




/**
 * Entry Klasse für das Programm.<br>
 * Erzeugt das Hauptfenster aus der EditorWindow.fxml Ressource.
 */
public class Main extends Application
{
	
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("EditorWindow.fxml"));
			SplitPane root = loader.load();

			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Eine nicht wirklich notwendige Mainmethode
	 */
	public static void main(String[] args)
	{
		launch(args);
	}
}
