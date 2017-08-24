package map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Die Tileset Klasse wird genutzt, um eine Bilddatei(ein Tileset) zu laden,<br>
 * und diese entweder Ausschnittweise oder komplett zu rendern
 *
 */
public class CTileset
{
	/**
	 * die geladene Bilddatei
	 */
	private Image img;
	/**
	 * Der GraphicsContext auf dem gerendert werden soll
	 */
	private GraphicsContext surface;
	/**
	 * Die Höhe eines Bildausschnittes
	 */
	private int frameHeight;
	/**
	 * Die Breite eines Bildausschnittes
	 */
	private int frameWidth;
	/**
	 * Die Höhe eines Bildausschnittes wenn er gerendert wird
	 */
	private int scaleHeight;
	/**
	 * Die Weite eines bildausschnittes beim rendern
	 */
	private int scaleWidth;
	// -----------------------------------------------\\
	/**
	 * Konstruktor für die Tileset Klasse
	 * @param path Pfad zur Bilddatei die geladen werden soll
	 * @param surface der GraphicsContext auf dem gerendert werden soll
	 * @param frameHeight Höhe eines Ausschnittes auf der Bilddatei
	 * @param frameWidth Weite eines Ausschnittes auf der Bilddatei
	 * @param scaleHeight Höhe des gerenderten Ausschnitts
	 * @param scaleWidth Weite des gerenderten Ausschnitts
	 */
	public CTileset(String path, GraphicsContext surface, int frameHeight, int frameWidth, int scaleHeight,
			int scaleWidth)
	{
		this.img = new Image(path);
		this.surface = surface;

		this.frameHeight = frameHeight;
		this.frameWidth = frameWidth;

		this.scaleHeight = scaleHeight;
		this.scaleWidth = scaleWidth;
	}

	/**
	 * Getter Methode für die Höhe eines Ausschnittes auf der Bilddatei
	 * @return die Höhe eines Ausschnittes auf der Bilddatei
	 */
	public int getFrameHeight()
	{
		return frameHeight;
	}

	/**
	 * Getter Methode für die Weite eines Ausschnittes auf der Bilddatei
	 * @return die Weite eines Ausschnittes auf der Bilddatei
	 */
	public int getFrameWidth()
	{
		return frameWidth;
	}

	/**
	 * Getter für die Gesamtweite der geladenen Bilddatei
	 * @return die Weite der geladenen Bilddatei(px)
	 */
	public int getImgWidth()
	{
		return (int) img.getWidth();
	}

	/**
	 * Render-Methode für das Tileset, rendert einen Ausschnitt
	 * @param x X Position an der gerendert werden soll
	 * @param y Y Position an der gerendert werden soll
	 * @param frameNum Nummer des Ausschnitts der gerendert werden soll, beginnt bei 0.
	 */
	public void render(int x, int y, int frameNum)
	{
		int col = (int) ((frameNum) % (img.getWidth() / frameWidth));
		int row = (int) ((frameNum) / ( img.getWidth()/ frameWidth));

	
		surface.drawImage(img, col*frameWidth, row*frameHeight, frameWidth - 1, frameHeight - 1, x, y, scaleWidth, scaleHeight);
	}

	/**
	 * Render-Methode die die gesamte Bilddatei rendert
	 * @param x X Position Renders
	 * @param y Y Position des Renders
	 */
	public void renderComplete(int x, int y)
	{
		surface.drawImage(img, x, y);
	}

}
