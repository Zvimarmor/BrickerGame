// === PaddleDuplicatorStrategy.java ===
package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.Paddle;
import bricker.main.Constants;

/**
 * Collision strategy that create a duplicate paddle when a brick is hit by a ball.
 * The duplicate paddle appears at screen center, listens to user input,
 * and removes itself after a fixed number of ball collisions.
 * Ensures only one duplicate is active at a time by tracking global state.
 */
public class PaddleDuplicatorStrategy implements CollisionStrategy {
	// Maximum number of hits before duplicate paddle disappears
	private static final int MAX_HITS = Constants.MAX_HITS_PADDLE_DUPLICATOR;

	/** Tracks whether a duplicate paddle is already present */
	public static boolean isHitPaddle = false;

	// Shared dependencies
	private static ImageReader imageReader;
	private static UserInputListener inputListener;
	private static Vector2 windowDimensions;
	private static Vector2 paddleDimensions;
	private static GameObjectCollection gameObjects;

	// Counter tracking remaining bricks
	private final Counter bricksCounter;

	/**
	 * Constructs the PaddleDuplicatorStrategy with required game hooks.
	 *
	 * @param gameObjects   Collection managing game objects in the scene.
	 * @param bricksCounter Counter tracking how many bricks remain.
	 * @param imageReader   Used to load the paddle image.
	 * @param inputListener Listens to player keyboard input.
	 */
	public PaddleDuplicatorStrategy(GameObjectCollection gameObjects,
									Counter bricksCounter,
									ImageReader imageReader,
									UserInputListener inputListener) {
		PaddleDuplicatorStrategy.gameObjects = gameObjects;
		this.bricksCounter = bricksCounter;
		PaddleDuplicatorStrategy.imageReader = imageReader;
		PaddleDuplicatorStrategy.inputListener = inputListener;
		// Cache dimensions for paddle placement
		windowDimensions = Constants.windowDimensions;
		paddleDimensions = Constants.paddleDimensions;
	}

	/**
	 * Executes the strategy's logic when a brick is hit.
	 * Spawns a temporary duplicate paddle, none is already active.
	 *
	 * @param brick The brick being collided with.
	 * @param collider The object that hit the brick.
	 */
	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		// Remove the brick and decrement counter
		if (gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS)) {
			bricksCounter.decrement();
		}
		// If a duplicate already exists, skip
		if (isHitPaddle) return;

		// Load paddle image and compute spawn position
		Renderable paddleImg = imageReader.readImage(Constants.PADDLE_IMAGE_PATH, true);
		Vector2 pos = new Vector2(
				windowDimensions.x() / 2f - paddleDimensions.x() / 2f,
				windowDimensions.y() / 2f
		);
		// Create and add duplicate paddle
		GameObject duplicatePaddle = new HitCountingPaddle(
				pos, paddleDimensions, paddleImg, inputListener);
		gameObjects.addGameObject(duplicatePaddle, Layer.DEFAULT);
		isHitPaddle = true;
	}

	/**
	 * Inner Paddle subclass that counts hits and self-removes after MAX_HITS collisions.
	 */
	private static class HitCountingPaddle extends Paddle {
		private int hitCount = 0;

		/**
		 * Constructs a hit-counting paddle.
		 *
		 * @param topLeftCorner Top-left position.
		 * @param dimensions    Paddle size.
		 * @param renderable    Visual representation.
		 * @param inputListener User keyboard listener.
		 */
		public HitCountingPaddle(Vector2 topLeftCorner,
								 Vector2 dimensions,
								 Renderable renderable,
								 UserInputListener inputListener) {
			super(topLeftCorner, dimensions, renderable, inputListener);
		}

		/**
		 * Determines whether this duplicate paddle should collide with the given object.
		 *
		 * @param other The other object involved in the potential collision.
		 * @return true if the object is a main or mini ball, false otherwise.
		 */
		@Override
		public boolean shouldCollideWith(GameObject other) {
			// Only respond to collisions with main or mini balls
			String tag = other.getTag();
			return Constants.MAIN_BALL_TAG.equals(tag)
					|| Constants.MINI_BALL_TAG.equals(tag);
		}

		/**
		 * Handles collision behavior for the duplicate paddle.
		 * Increments collision count and removes the paddle when the hit limit is reached.
		 *
		 * @param other The object that collided with the paddle.
		 * @param collision The collision data.
		 */
		@Override
		public void onCollisionEnter(GameObject other, Collision collision) {
			super.onCollisionEnter(other, collision);
			hitCount++;
			// After reaching hit limit, remove duplicate and reset state
			if (hitCount >= MAX_HITS) {
				gameObjects.removeGameObject(this);
				isHitPaddle = false;
			}
		}
	}
}
