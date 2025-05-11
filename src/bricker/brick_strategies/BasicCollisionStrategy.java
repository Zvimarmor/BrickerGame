package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * A basic strategy that removes the brick from the game upon collision.
 * Decrements the shared bricks counter.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjectCollection;
	private final Counter bricksCounter;

	/**
	 * Constructs a basic collision strategy.
	 *
	 * @param gameObjectCollection Collection managing game objects.
	 * @param bricksCounter        Counter tracking remaining bricks.
	 */
	public BasicCollisionStrategy(GameObjectCollection gameObjectCollection, Counter bricksCounter) {
		this.gameObjectCollection = gameObjectCollection;
		this.bricksCounter = bricksCounter;
	}

	/**
	 * Called when a brick collides with another object.
	 * Removes the brick and decrements the counter if removal succeeds.
	 *
	 * @param brick The brick being collided.
	 * @param collider The other game object involved in the collision.
	 */
	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		if (gameObjectCollection.removeGameObject(brick, Layer.STATIC_OBJECTS)) {
			bricksCounter.decrement();
		}
	}
}
