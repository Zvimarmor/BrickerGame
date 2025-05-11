package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.main.Constants;

/**
 * A visual heart that falls from a destroyed brick.
 * If collected by the main paddle, it restores one life (if below maximum).
 * Otherwise, it is removed when it leaves the screen bottom.
 */
public class FallingHeart extends GameObject {
    private final GameObjectCollection gameObjects;
    private final HeartsPanel heartsPanel;

    /**
     * Constructs a FallingHeart object of the same size as panel hearts.
     *
     * @param topLeftCorner Position where the heart appears.
     * @param renderable    Image representing the heart.
     * @param gameObjects   Collection for adding/removing game objects.
     * @param heartsPanel   The hearts UI panel to update upon pickup.
     */
    public FallingHeart(Vector2 topLeftCorner,
                        Renderable renderable,
                        GameObjectCollection gameObjects,
                        HeartsPanel heartsPanel) {
        super(topLeftCorner,
                new Vector2(Constants.objectWidth, Constants.objectHeight),
                renderable);
        this.gameObjects = gameObjects;
        this.heartsPanel = heartsPanel;
        setTag(Constants.FALLING_HEART_TAG);
    }

    /**
     * Only the main paddle can collect the heart.
     *
     * @param other The other GameObject.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return Constants.MAIN_PADDLE_TAG.equals(other.getTag());
    }

    /**
     * On collision with paddle, grants a life if not at max, then removes itself.
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (heartsPanel.getLifeNum() < Constants.MAX_LIFE_NUM) {
            heartsPanel.addHeart(gameObjects);
        }
        gameObjects.removeGameObject(this);
    }

    /**
     * Moves the heart down each frame and removes it if it exits the bottom.
     *
     * @param deltaTime Time since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Descend at constant speed
        setTopLeftCorner(getTopLeftCorner().add(
                new Vector2(0, Constants.FALLING_HEART_SPEED * deltaTime)));

        // Remove when off-screen
        if (getTopLeftCorner().y() > Constants.windowDimensions.y()) {
            gameObjects.removeGameObject(this);
        }
    }
}
