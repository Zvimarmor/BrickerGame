package brick_strategies;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;
import gameobjects.HeartsPanel;
import main.Constants;

/**
 * A collision strategy that spawns a falling heart when a brick is destroyed.
 * If the heart is caught by the paddle, an extra life is added (up to the max).
 */
public class LifeRestorationStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjects;
	private final Counter bricksCounter;
	private final ImageReader imageReader;
	private final HeartsPanel heartsPanel;

	/**
	 * Constructs a LifeRestorationStrategy.
	 *
	 * @param gameObjects   Game object collection to manipulate.
	 * @param bricksCounter Counter for remaining bricks.
	 * @param imageReader   Used to read the heart image.
	 * @param heartsPanel   Panel to manage life display and values.
	 */
	public LifeRestorationStrategy(GameObjectCollection gameObjects,
								   Counter bricksCounter,
								   ImageReader imageReader,
								   HeartsPanel heartsPanel) {
		this.gameObjects = gameObjects;
		this.bricksCounter = bricksCounter;
		this.imageReader = imageReader;
		this.heartsPanel = heartsPanel;
	}

	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		// Remove the brick and decrement the counter
		if (gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS)) {
			bricksCounter.decrement();
		}

		// Load the heart image
		Renderable heartImage = imageReader.readImage(Constants.HEART_IMAGE_PATH, true);
		Vector2 heartSize = new Vector2(Constants.objectWidth, Constants.objectHeight);
		Vector2 heartPos = brick.getCenter().subtract(heartSize.mult(0.5f));

		// Create the falling heart as an anonymous inner GameObject
		GameObject fallingHeart = new GameObject(heartPos, heartSize, heartImage) {
			@Override
			public boolean shouldCollideWith(GameObject other) {
				// Only collide with the main paddle
				return Constants.MAIN_PADDLE_TAG.equals(other.getTag());
			}

			@Override
			public void onCollisionEnter(GameObject other, Collision collision) {
				if (Constants.MAIN_PADDLE_TAG.equals(other.getTag())) {
					if (heartsPanel.getLifeNum() < Constants.MAX_LIFE_NUM) {
						heartsPanel.addHeart(gameObjects);
					}
					gameObjects.removeGameObject(this);
				}
			}

			@Override
			public void update(float deltaTime) {
				super.update(deltaTime);
				// Move downward
				setTopLeftCorner(getTopLeftCorner().add(
						new Vector2(0, Constants.FALLING_HEART_SPEED * deltaTime)));
				// Remove if out of screen
				if (getTopLeftCorner().y() > Constants.windowDimensions.y()) {
					gameObjects.removeGameObject(this);
				}
			}
		};

		fallingHeart.setTag(Constants.FALLING_HEART_TAG);
		gameObjects.addGameObject(fallingHeart, Layer.DEFAULT);
	}
}
