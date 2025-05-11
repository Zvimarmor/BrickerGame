// === TurboModeStrategy.java ===
package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.util.Counter;
import bricker.gameobjects.Ball;
import bricker.main.Constants;

/**
 * Activates turbo mode on the main ball when a brick is hit.
 * Increases ball speed by a constant factor and changes its color to red.
 * Turbo lasts for a fixed number of collisions, then reverts to normal.
 * Prevents re-activation while active.</p>
 */
public class TurboModeStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjects;
	private final Counter bricksCounter;
	private static ImageReader imageReader;

	private static boolean turboActive = false;
	private static int turboCollisionStart = 0;

	/**
	 * Constructs the turbo mode strategy.
	 *
	 * @param gameObjects  Collection managing game objects.
	 * @param bricksNum    Counter tracking remaining bricks.
	 * @param imageReader  Loads ball images.
	 */
	public TurboModeStrategy(GameObjectCollection gameObjects, Counter bricksNum, ImageReader imageReader) {
		this.gameObjects = gameObjects;
		this.bricksCounter = bricksNum;
		TurboModeStrategy.imageReader = imageReader;
	}

	/**
	 * Applies turbo mode behavior when the brick is hit.
	 * Speeds up the main ball, changes its appearance, and attaches a timed component
	 * to revert changes after a certain number of collisions.
	 *
	 * @param brick    The brick being hit.
	 * @param collider The object that collided with the brick (should be the main ball).
	 */
	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		// Remove brick and decrement
		if (gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS))
			bricksCounter.decrement();

		// Only react to main ball and if not already turbo
		if (!collider.getTag().equals(Constants.MAIN_BALL_TAG) || turboActive)
			return;

		Ball ball = (Ball) collider;
		turboActive = true;
		turboCollisionStart = ball.getCollisionCounter();

		// Increase velocity and change appearance
		ball.setVelocity(ball.getVelocity().mult(Constants.TURBO_FACTOR));
		ball.renderer().setRenderable(
				imageReader.readImage(Constants.RED_BALL_IMAGE_PATH, true));

		// Add component to revert after limit
		ball.addComponent(deltaTime -> {
			if (!turboActive) return;
			if (ball.getCollisionCounter() - turboCollisionStart > Constants.TURBO_COLLISION_LIMIT) {
				ball.setVelocity(ball.getVelocity().mult(1f / Constants.TURBO_FACTOR));
				ball.renderer().setRenderable(
						imageReader.readImage(Constants.BALL_IMAGE_PATH, true));
				turboActive = false;
			}
		});
	}
}
