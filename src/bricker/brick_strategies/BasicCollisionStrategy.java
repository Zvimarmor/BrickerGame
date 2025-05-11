package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * A basic strategy that simply removes the brick upon collision.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjectCollection;
	private final Counter bricksCounter;

	/**
	 * Constructs a basic brick collision strategy.
	 */
	public BasicCollisionStrategy(GameObjectCollection gameObjectCollection, Counter bricksCounter) {
		this.gameObjectCollection = gameObjectCollection;
		this.bricksCounter = bricksCounter;
	}

	@Override
	public void onCollision(GameObject object1, GameObject object2) {
		if (gameObjectCollection.removeGameObject(object1, Layer.STATIC_OBJECTS)) {
			bricksCounter.decrement();
		}
	}
}
