package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.gui.ImageReader;
import danogl.util.Vector2;
import danogl.util.Counter;
import danogl.collisions.Layer;
import bricker.gameobjects.Paddle;
import bricker.main.Constants;

/**
 * A collision strategy that spawns a duplicate paddle when a brick is destroyed.
 * The duplicate disappears after a limited number of hits.
 */
public class PaddleDuplicatorStrategy implements CollisionStrategy {
	private static final int MAX_HITS = Constants.MAX_HITS_PADDLE_DUPLICATOR;
	public static boolean isHitPaddle = false;

	private static ImageReader imageReader;
	private static UserInputListener inputListener;
	private static Vector2 windowDimensions;
	private static Vector2 paddleDimensions;
	private static GameObjectCollection gameObjects;

	private final Counter bricksCounter;

	/**
	 * Constructs the strategy with necessary game dependencies.
	 */
	public PaddleDuplicatorStrategy(GameObjectCollection gameObjects,
									Counter bricksCounter,
									ImageReader imageReader,
									UserInputListener inputListener) {
		PaddleDuplicatorStrategy.gameObjects = gameObjects;
		this.bricksCounter = bricksCounter;
		PaddleDuplicatorStrategy.imageReader = imageReader;
		PaddleDuplicatorStrategy.inputListener = inputListener;
		windowDimensions = Constants.windowDimensions;
		paddleDimensions = Constants.paddleDimensions;
	}

	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		if (gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS))
			bricksCounter.decrement();

		if (isHitPaddle) return;

		Renderable paddleImg = imageReader.readImage(Constants.PADDLE_IMAGE_PATH, true);
		Vector2 pos = new Vector2(
				windowDimensions.x() / 2f - paddleDimensions.x() / 2f,
				windowDimensions.y() / 2f
		);
		GameObject duplicatePaddle = new HitCountingPaddle(pos, paddleDimensions, paddleImg, inputListener);
		gameObjects.addGameObject(duplicatePaddle, Layer.DEFAULT);
		isHitPaddle = true;
	}

	private static class HitCountingPaddle extends Paddle {
		private int hitCount = 0;

		public HitCountingPaddle(Vector2 topLeftCorner, Vector2 dimensions,
								 Renderable renderable, UserInputListener inputListener) {
			super(topLeftCorner, dimensions, renderable, inputListener);
		}

		@Override
		public boolean shouldCollideWith(GameObject other) {
			String tag = other.getTag();
			return Constants.MAIN_BALL_TAG.equals(tag) || Constants.MINI_BALL_TAG.equals(tag);
		}

		@Override
		public void onCollisionEnter(GameObject other, Collision collision) {
			super.onCollisionEnter(other, collision);
			hitCount++;
			if (hitCount >= MAX_HITS) {
				gameObjects.removeGameObject(this);
				isHitPaddle = false;
			}
		}
	}
}
