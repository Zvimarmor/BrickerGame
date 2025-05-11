package gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import main.Constants;

import java.awt.*;
import java.util.ArrayList;

/**
 * A UI component that displays remaining lives using heart icons and a number.
 */
public class HeartsPanel extends GameObject {
    private static final float HEART_ASPECT_RATIO = 1f;
    private static final float HEART_SCALE = 0.8f;

    private final ArrayList<GameObject> heartObjects = new ArrayList<>();
    private final GameObject textObject;
    private final TextRenderable textRenderable;

    private final Vector2 objectSize = new Vector2(Constants.objectWidth, Constants.objectHeight);
    private final Vector2 textPos = new Vector2(
            Constants.panelTopLeft.x() + Constants.PADDING,
            Constants.panelTopLeft.y() + 2 * Constants.PADDING);

    private final float heartWidth = Constants.objectWidth * HEART_SCALE;
    private final float heartHeight = heartWidth / HEART_ASPECT_RATIO;

    private final Renderable heartImage;

    /**
     * Constructs the heart panel with initial number of lives.
     *
     * @param imageReader  Used to load the heart image.
     * @param initialLives Initial number of lives.
     * @param game         Game object collection to add the panel into.
     */
    public HeartsPanel(ImageReader imageReader, int initialLives, GameObjectCollection game) {
        super(Constants.panelTopLeft, Constants.panelSize, null);
        this.heartImage = imageReader.readImage(Constants.HEART_IMAGE_PATH, true);

        game.addGameObject(this, Layer.UI);

        // Set up the numeric text
        textRenderable = new TextRenderable(Integer.toString(initialLives));
        updateTextColor();
        textObject = new GameObject(
                textPos,
                new Vector2(objectSize.x() * 0.7f, objectSize.y() * 0.7f),
                textRenderable);
        game.addGameObject(textObject, Layer.UI);

        createHearts(initialLives, game);
    }

    private void createHearts(int numberOfHeartsToCreate, GameObjectCollection game) {
        float yCenter = Constants.panelTopLeft.y() + Constants.panelSize.y() / 2f;
        float heartTop = yCenter - heartHeight / 2f;

        // Remove old hearts
        for (GameObject heartObject : heartObjects) {
            game.removeGameObject(heartObject, Layer.UI);
        }
        heartObjects.clear();

        // Create new hearts
        for (int i = 0; i < numberOfHeartsToCreate; i++) {
            float xPos = textPos.x() + heartWidth + i * (heartWidth + Constants.PADDING);
            Vector2 heartPos = new Vector2(xPos, heartTop);
            Vector2 heartSize = new Vector2(heartWidth, heartHeight);
            GameObject heart = new GameObject(heartPos, heartSize, heartImage);
            heartObjects.add(heart);
            game.addGameObject(heart, Layer.UI);
        }

        textRenderable.setString(Integer.toString(numberOfHeartsToCreate));
        updateTextColor();
    }

    private void updateTextColor() {
        if (heartObjects.size() >= 3)
            textRenderable.setColor(Color.GREEN);
        else if (heartObjects.size() == 2)
            textRenderable.setColor(Color.YELLOW);
        else
            textRenderable.setColor(Color.RED);
    }

    /**
     * @return Current number of lives.
     */
    public int getLifeNum() {
        return heartObjects.size();
    }

    /**
     * Adds one life (if not exceeding max).
     */
    public void addHeart(GameObjectCollection game) {
        if (heartObjects.size() >= Constants.MAX_LIFE_NUM) return;
        createHearts(heartObjects.size() + 1, game);
    }

    /**
     * Removes one life.
     */
    public void removeHeart(GameObjectCollection game) {
        createHearts(heartObjects.size() - 1, game);
    }
}
