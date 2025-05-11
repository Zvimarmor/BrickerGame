package brick_strategies;

import danogl.gui.rendering.Renderable;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.util.Counter;
import gameobjects.Ball;
import main.Constants;

/**
 * A strategy that activates turbo mode on the ball upon brick collision.
 * The ball moves faster and changes color temporarily.
 */
public class TurboModeStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjects;
	private final Counter bricksCounter;
	private static ImageReader imageReader;

	private static boolean turboActive = false;
	private static int turboCollisionStart = -1;

	public TurboModeStrategy(GameObjectCollection gameObjects, Counter bricksNum, ImageReader imageReader) {
		this.gameObjects = gameObjects;
		this.bricksCounter = bricksNum;
		TurboModeStrategy.imageReader = imageReader;
	}

	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		if (gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS))
			bricksCounter.decrement();

		if (!collider.getTag().equals(Constants.MAIN_BALL_TAG) || turboActive)
			return;

		Ball ball = (Ball) collider;
		turboActive = true;
		turboCollisionStart = ball.getCollisionCounter();
		ball.setVelocity(ball.getVelocity().mult(Constants.TURBO_FACTOR));
		ball.renderer().setRenderable(imageReader.readImage(Constants.RED_BALL_IMAGE_PATH, true));

		ball.addComponent(deltaTime -> {
			if (!turboActive) return;
			if (ball.getCollisionCounter() - turboCollisionStart >= Constants.TURBO_COLLISION_LIMIT) {
				ball.setVelocity(ball.getVelocity().mult(1f / Constants.TURBO_FACTOR));
				ball.renderer().setRenderable(imageReader.readImage(Constants.BALL_IMAGE_PATH, true));
				turboActive = false;
			}
		});
	}
}
