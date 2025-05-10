package brick_strategies;

import danogl.gui.rendering.Renderable;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.util.Counter;

public class TurboModeStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjects;
	private final Counter bricksCounter;
	private static ImageReader imageReader;


	public TurboModeStrategy(GameObjectCollection gameObjects, Counter bricksNum, ImageReader imageReader) {
		this.gameObjects = gameObjects;
		this.bricksCounter = bricksNum;
		this.imageReader = imageReader;
	}

	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		if (gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS)) {
			bricksCounter.decrement();
		}

		// only if the main ball hit thr brick
		if(collider.getTag().equals("Main_ball")) {
			collider.setTag("Turbo_ball");

			collider.setVelocity(collider.getVelocity().mult(1.4f));

			// change the ball to red
			Renderable redBallImage = imageReader.readImage("assets/redball.png", true);
			collider.renderer().setRenderable(redBallImage);
		}

	}

}
