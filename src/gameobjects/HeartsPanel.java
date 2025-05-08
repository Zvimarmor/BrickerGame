package gameobjects;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * A visual panel that displays the player's remaining lives using hearts and a colored number.
 */
public class HeartsPanel {
	private static final float PANEL_WIDTH = 200;
	private static final float PANEL_HEIGHT = 30;

	private final List<GameObject> currentHeartsObjects;
	private final Vector2 panelTopLeft;
	private final int maxLives;
	private final Renderable heartImage;
	private final GameObject textObject;
	private final List<GameObject> gameObjectRegistry;

	/**
	 * Constructs a HeartsPanel with a given number of lives.
	 *
	 * @param gameObjectRegistry List to simulate game object collection.
	 * @param topLeft Top-left corner of the panel.
	 * @param initialLives Initial number of lives.
	 * @param maxLives Maximum number of lives.
	 * @param imageReader ImageReader for loading heart image.
	 */
	public HeartsPanel(List<GameObject> gameObjectRegistry,
					   Vector2 topLeft,
					   int initialLives,
					   int maxLives,
					   ImageReader imageReader) {
		this.panelTopLeft = topLeft;
		this.maxLives = maxLives;
		this.currentHeartsObjects = new ArrayList<>();
		this.gameObjectRegistry = gameObjectRegistry;

		this.heartImage = imageReader.readImage("assets/heart.png", true);

		float objectWidth = PANEL_WIDTH / (maxLives + 1);

		TextRenderable textRenderable = new TextRenderable(Integer.toString(initialLives));
		if (initialLives >= 3) textRenderable.setColor(Color.GREEN);
		else if (initialLives == 2) textRenderable.setColor(Color.YELLOW);
		else textRenderable.setColor(Color.RED);

		this.textObject = new GameObject(
				topLeft,
				new Vector2(objectWidth, PANEL_HEIGHT),
				textRenderable
		);
		gameObjectRegistry.add(textObject);
		currentHeartsObjects.add(textObject);

		for (int i = 0; i < initialLives; i++) {
			float xPos = topLeft.x() + objectWidth + i * objectWidth;
			GameObject heart = new GameObject(
					new Vector2(xPos, topLeft.y()),
					new Vector2(objectWidth, PANEL_HEIGHT),
					heartImage
			);
			gameObjectRegistry.add(heart);
			currentHeartsObjects.add(heart);
		}
	}

	/**
	 * Updates the panel to reflect a new number of lives.
	 *
	 * @param lives Current number of lives.
	 */
	public void update(int lives) {
		for (GameObject obj : currentHeartsObjects) {
			gameObjectRegistry.remove(obj);
		}
		currentHeartsObjects.clear();

		float objectWidth = PANEL_WIDTH / (maxLives + 1);

		TextRenderable textRenderable = new TextRenderable(Integer.toString(lives));
		if (lives >= 3) textRenderable.setColor(Color.GREEN);
		else if (lives == 2) textRenderable.setColor(Color.YELLOW);
		else textRenderable.setColor(Color.RED);

		GameObject text = new GameObject(
				panelTopLeft,
				new Vector2(objectWidth, PANEL_HEIGHT),
				textRenderable
		);
		gameObjectRegistry.add(text);
		currentHeartsObjects.add(text);

		for (int i = 0; i < lives; i++) {
			float xPos = panelTopLeft.x() + objectWidth + i * objectWidth;
			GameObject heart = new GameObject(
					new Vector2(xPos, panelTopLeft.y()),
					new Vector2(objectWidth, PANEL_HEIGHT),
					heartImage
			);
			gameObjectRegistry.add(heart);
			currentHeartsObjects.add(heart);
		}
	}
}
