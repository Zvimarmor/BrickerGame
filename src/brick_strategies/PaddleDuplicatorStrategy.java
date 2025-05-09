// PaddleDuplicatorStrategy.java
package brick_strategies;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.gui.ImageReader;
import danogl.util.Vector2;
import danogl.util.Counter;
import danogl.collisions.Layer;
import gameobjects.Paddle;
import main.Constants;

/**
 * A collision strategy that spawns a single duplicate paddle when a brick is destroyed.
 * Uses static initialization to supply required external dependencies,
 * thereby limiting constructor parameters to four.
 */
public class PaddleDuplicatorStrategy implements CollisionStrategy {
	private static final int MAX_HITS = Constants.MAX_HITS_PADDLE_DUPLICATOR;
	public static boolean isHitPaddle = false;

	// Static dependencies, initialized once in constructor
	private static ImageReader imageReader;
	private static UserInputListener inputListener;
	private static Vector2 windowDimensions;
	private static Vector2 paddleDimensions;
	private static GameObjectCollection gameObjects;

	private final Counter bricksCounter;
	private HitCountingPaddle duplicatePaddle;

	/**
	 * Constructs the strategy with minimal parameters.
	 *
	 * @param gameObjects   GameObjectCollection
	 * @param bricksCounter Counter of remaining bricks
	 * @param imageReader   ImageReader for assets
	 * @param inputListener UserInputListener for paddle control
	 */
	public PaddleDuplicatorStrategy(GameObjectCollection gameObjects,
									Counter bricksCounter,
									ImageReader imageReader,
									UserInputListener inputListener) {
		// initialize static dependencies properly
		this.gameObjects = gameObjects;
		this.bricksCounter = bricksCounter;
		this.imageReader = imageReader;
		this.inputListener = inputListener;
		this.windowDimensions = Constants.windowDimensions;
		this.paddleDimensions = Constants.paddleDimensions;
	}

	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		if (gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS)) {
			bricksCounter.decrement();
		}

		if (isHitPaddle){
			return;
		}

		// create duplicate paddle at center X, half Y
		Renderable paddleImg = imageReader.readImage(Constants.PADDLE_IMAGE_PATH, true);
		Vector2 pos = new Vector2(
				windowDimensions.x() / 2f - paddleDimensions.x() / 2f,
				windowDimensions.y() / 2f
		);
		duplicatePaddle = new HitCountingPaddle(
				pos,
				paddleDimensions,
				paddleImg,
				inputListener
		);
		gameObjects.addGameObject(duplicatePaddle, Layer.DEFAULT);
		isHitPaddle = true;
	}

	/**
	 * Internal paddle subclass that counts hits and self-removes after limit.
	 */
	private static class HitCountingPaddle extends Paddle {
		private int hitCount = 0;

		public HitCountingPaddle(Vector2 topLeftCorner,
								 Vector2 dimensions,
								 Renderable renderable,
								 UserInputListener inputListener) {
			super(topLeftCorner, dimensions, renderable, inputListener);
		}

		@Override
		public void onCollisionEnter(GameObject other, Collision collision) {
			//todo: problem when the new paddle hit the borders-it counts as a hit
			super.onCollisionEnter(other, collision);
			hitCount++;
			if (hitCount >= MAX_HITS) {
				gameObjects.removeGameObject(this);
				isHitPaddle = false;
			}
		}
	}
}
