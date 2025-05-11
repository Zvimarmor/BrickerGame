package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;
import bricker.gameobjects.FallingHeart;
import bricker.gameobjects.HeartsPanel;
import bricker.main.Constants;

/**
 * Strategy that create a falling heart when a brick is destroyed.
 * If the heart collides with the player's paddle, an extra life is added
 * (provided the number of lives is below the allowed maximum).
 * This strategy uses the FallingHeart class to define the behavior
 * of the falling object and updates the obj HeartsPanel when collected.
 */
public class LifeRestorationStrategy implements CollisionStrategy {

	/** Collection managing all game objects in the scene. */
	private final GameObjectCollection gameObjects;

	/** Counter tracking the number of remaining bricks. */
	private final Counter bricksCounter;

	/** Asset loader for reading heart image. */
	private final ImageReader imageReader;

	/** Reference to the heart panel responsible for managing life display. */
	private final HeartsPanel heartsPanel;

	/**
	 * Constructs the LifeRestorationStrategy.
	 *
	 * @param gameObjects   GameObjectCollection for adding/removing game elements.
	 * @param bricksCounter Counter used to track remaining bricks.
	 * @param imageReader   Used to load the heart image asset.
	 * @param heartsPanel   HeartsPanel that manages player lives UI and logic.
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

	/**
	 * Called when a brick using this strategy is hit.
	 * The method removes the brick, spawns a falling heart
	 * centered at the brick's position, and registers it in the game.
	 * @param brick The brick GameObject being collided with.
	 * @param collider The object that hit the brick (e.g., ball).
	 */
	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		// Remove the brick and update the remaining bricks counter
		if (gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS)) {
			bricksCounter.decrement();
		}

		// Create the falling heart with the same dimensions as hearts in the panel
		Renderable heartImage = imageReader.readImage(Constants.HEART_IMAGE_PATH, true);
		Vector2 heartSize = new Vector2(Constants.objectWidth, Constants.objectHeight);
		Vector2 heartPos = brick.getCenter().subtract(heartSize.mult(Constants.HeartPosPositionDiscounting));

		// Spawn a heart that falls and can restore one life upon paddle collision
		GameObject fallingHeart = new FallingHeart(heartPos, heartImage, gameObjects, heartsPanel);
		gameObjects.addGameObject(fallingHeart, Layer.DEFAULT);
	}
}
