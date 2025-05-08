package gameobjects;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import main.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HeartsPanel extends GameObject {
    private static final float PADDING = 5f;
    private static final float HEART_ASPECT_RATIO = 1f;
    private static final float HEART_SCALE = 0.7f;

    private final List<GameObject> heartObjects = new ArrayList<>();
    private final GameObject textObject;
    private final TextRenderable textRenderable;
    private final Vector2 panelTopLeft;
    private final Vector2 panelSize;
    private final Renderable heartImage;
    private int lifeNum;

    public HeartsPanel(ImageReader imageReader, int initialLives) {
        super(Constants.panelTopLeft, Constants.panelSize,
                imageReader.readImage("assets/HeartsPanel.png", true));

        this.panelTopLeft = Constants.panelTopLeft;
        this.panelSize = Constants.panelSize;
        this.lifeNum = initialLives;
        this.heartImage = imageReader.readImage("assets/heart.png", true);

        float totalWidth = panelSize.x() - 2 * PADDING;
        float objectWidth = totalWidth / (Constants.MAX_LIFE_NUM + 1);
        float objectHeight = panelSize.y() - 2 * PADDING;
        Vector2 objectSize = new Vector2(objectWidth, objectHeight);
        Vector2 textPos = new Vector2(panelTopLeft.x() + PADDING, panelTopLeft.y() + PADDING);

        // text
        textRenderable = new TextRenderable(Integer.toString(lifeNum));
        updateTextColor();
        textObject = new GameObject(textPos, objectSize, textRenderable);

        // hearts
        createHearts(objectWidth, textPos);
    }

    private void createHearts(float objectWidth, Vector2 textPos) {
        heartObjects.clear();

        float heartWidth = objectWidth * HEART_SCALE;
        float heartHeight = heartWidth / HEART_ASPECT_RATIO;

        float yCenter = panelTopLeft.y() + panelSize.y() / 2f;
        float heartTop = yCenter - heartHeight / 2f;

        for (int i = 0; i < lifeNum; i++) {
            float xPos = textPos.x() + objectWidth + i * objectWidth;
            Vector2 heartPos = new Vector2(xPos, heartTop);
            Vector2 heartSize = new Vector2(heartWidth, heartHeight);

            GameObject heart = new GameObject(heartPos, heartSize, heartImage);
            heartObjects.add(heart);
        }
    }

    private void updateTextColor() {
        if (lifeNum >= 3)
            textRenderable.setColor(Color.GREEN);
        else if (lifeNum == 2)
            textRenderable.setColor(Color.YELLOW);
        else
            textRenderable.setColor(Color.RED);
    }

    public int getLifeNum() {
        return lifeNum;
    }

    public GameObject getTextObject() {
        return textObject;
    }

    public List<GameObject> getHeartObjects() {
        return new ArrayList<>(heartObjects);
    }

    // shitssss
//    private void updateText() {
//        textRenderable.setString(Integer.toString(lifeNum));
//        updateTextColor();

//    }
//    public void addHeart() {
//        if (lifeNum < Constants.MAX_LIFE_NUM) {
//            lifeNum++;
//            recreateHearts();
//        }

//    }
//    public void removeHeart() {
//        if (lifeNum > 0) {
//            lifeNum--;
//            recreateHearts();
//        }

//    }
//    public void resetHearts(int newLifeNum) {
//        lifeNum = Math.min(newLifeNum, Constants.MAX_LIFE_NUM);
//        recreateHearts();

//    }
//    private void recreateHearts() {
//        float totalWidth = panelSize.x() - 2 * PADDING;
//        float objectWidth = totalWidth / (Constants.MAX_LIFE_NUM + 1);
//        Vector2 textPos = new Vector2(panelTopLeft.x() + PADDING, panelTopLeft.y() + PADDING);
//        createHearts(objectWidth, textPos);

//        updateText();
//    }
}
