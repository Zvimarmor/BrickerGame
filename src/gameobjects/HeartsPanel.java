
package gameobjects;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import main.Constants;

import java.awt.*;
import java.util.ArrayList;

public class HeartsPanel extends GameObject {
    // Design parts
    private static final float HEART_ASPECT_RATIO = 1f;
    private static final float HEART_SCALE = 0.8f;

    private final ArrayList<GameObject> heartObjects = new ArrayList<>();
    private final GameObject textObject;
    private final TextRenderable textRenderable;
    private Vector2 objectSize = new Vector2(Constants.objectWidth, Constants.objectHeight);
    private Vector2 textPos = new Vector2(Constants.panelTopLeft.x() + Constants.PADDING, Constants.panelTopLeft.y() + Constants.PADDING+ Constants.PADDING);
    private float heartWidth = Constants.objectWidth * HEART_SCALE;
    private float heartHeight = heartWidth / HEART_ASPECT_RATIO;


    private final Renderable heartImage;

    public HeartsPanel(ImageReader imageReader, int initialLives, GameObjectCollection game) {
        super(Constants.panelTopLeft, Constants.panelSize,null);
        this.heartImage = imageReader.readImage("assets/heart.png", true);

        //add the panel to the game objects
        game.addGameObject(this, Layer.UI);

        // text
        textRenderable = new TextRenderable(Integer.toString(initialLives));
        updateTextColor();
        textObject = new GameObject(textPos, new Vector2(objectSize.x()*0.7f,objectSize.y()*0.7f), textRenderable);
        game.addGameObject(textObject, Layer.UI);


        // add 3 hearts to the array list
        createHearts(initialLives, game);
    }

    private void createHearts(int numberOfHeartsToCreate,GameObjectCollection game) {
        float yCenter = Constants.panelTopLeft.y() + Constants.panelSize.y() / 2f;
        float heartTop = yCenter - heartHeight / 2f;
        //remove the old objects and update UI
        for (GameObject heartObject : heartObjects) {
            game.removeGameObject(heartObject, Layer.UI);
        }
        heartObjects.clear();
        //recreate the hearts
        for (int i = 0; i < numberOfHeartsToCreate; i++) {
            float xPos = textPos.x() + heartWidth + i * (heartWidth+Constants.PADDING);
            Vector2 heartPos = new Vector2(xPos, heartTop);
            Vector2 heartSize = new Vector2(heartWidth, heartHeight);
            heartObjects.add(new GameObject(heartPos, heartSize, heartImage));
            game.addGameObject(heartObjects.get(i), Layer.UI);
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

    public int getLifeNum() {
        return heartObjects.size();
    }

    public void addHeart(GameObjectCollection game) {
        if (heartObjects.size() >= Constants.MAX_LIFE_NUM) {
            return;
        }
        createHearts(heartObjects.size() + 1,game);
    }

    public void removeHeart(GameObjectCollection game) {
        createHearts(heartObjects.size() - 1,game);
    }
}
