package gameobjects;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class HeartsPanel {
	private int numHearts;
	private final List<GameObject> heartObjects = new ArrayList<>();
	private final GameObject textObject;
	private final TextRenderable textRenderable;
	private final Renderable heartImage;
	private final Vector2 topLeftCorner;
	private final Vector2 panelSize;

	public HeartsPanel(Vector2 topLeftCorner, Vector2 panelSize, int initialHearts, ImageReader imageReader) {
		this.topLeftCorner = topLeftCorner;
		this.panelSize = panelSize;
		this.numHearts = initialHearts;
		this.heartImage = imageReader.readImage("assets/heart.png", true);

		float totalWidth = panelSize.x();
		float totalHeight = panelSize.y();
		float objectWidth = totalWidth / (numHearts + 1);  // מקום לטקסט + לבבות

		// יצירת הטקסט
		textRenderable = new TextRenderable(Integer.toString(numHearts));
		textRenderable.setColor(Color.GREEN);

		textObject = new GameObject(
				new Vector2(topLeftCorner.x(), topLeftCorner.y()),
				new Vector2(objectWidth, totalHeight),
				textRenderable
		);

		// יצירת לבבות
		for (int i = 0; i < numHearts; i++) {
			float xPos = topLeftCorner.x() + objectWidth + i * objectWidth;
			GameObject heart = new GameObject(
					new Vector2(xPos, topLeftCorner.y()),
					new Vector2(objectWidth, totalHeight),
					heartImage
			);
			heartObjects.add(heart);
		}
	}

	public void addHeart() {
		float totalWidth = panelSize.x();
		float totalHeight = panelSize.y();
		float objectWidth = totalWidth / (numHearts + 2); // מוסיפים אחד

		float xPos = topLeftCorner.x() + objectWidth + numHearts * objectWidth;

		GameObject newHeart = new GameObject(
				new Vector2(xPos, topLeftCorner.y()),
				new Vector2(objectWidth, totalHeight),
				heartImage
		);
		heartObjects.add(newHeart);
		numHearts++;
		updateText();
	}

	public void decreaseHeart() {
		if (!heartObjects.isEmpty()) {
			GameObject removed = heartObjects.remove(heartObjects.size() - 1);
			numHearts--;
			updateText();
		}
	}

	public void reset(int newHearts) {
		heartObjects.clear();
		numHearts = newHearts;

		float totalWidth = panelSize.x();
		float totalHeight = panelSize.y();
		float objectWidth = totalWidth / (numHearts + 1);

		for (int i = 0; i < numHearts; i++) {
			float xPos = topLeftCorner.x() + objectWidth + i * objectWidth;
			GameObject heart = new GameObject(
					new Vector2(xPos, topLeftCorner.y()),
					new Vector2(objectWidth, totalHeight),
					heartImage
			);
			heartObjects.add(heart);
		}
		updateText();
	}

	public int getHearts() {
		return numHearts;
	}

	public GameObject getTextObject() {
		return textObject;
	}

	public List<GameObject> getHeartObjects() {
		return heartObjects;
	}

	private void updateText() {
		textRenderable.setString(Integer.toString(numHearts));

		if (numHearts >= 3) {
			textRenderable.setColor(Color.GREEN);
		} else if (numHearts == 2) {
			textRenderable.setColor(Color.YELLOW);
		} else {
			textRenderable.setColor(Color.RED);
		}
	}
}