package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class BasicCollisionStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjectCollection;
	private final Counter bricksCounter;
	
	public BasicCollisionStrategy(GameObjectCollection gameObjectCollection, Counter bricksCounter) {
		this.gameObjectCollection = gameObjectCollection;
		this.bricksCounter = bricksCounter;
	}
	@Override
	public void onCollision(GameObject object1, GameObject object2) {

		gameObjectCollection.removeGameObject(object1, Layer.STATIC_OBJECTS);
		bricksCounter.decrement();
	}
}
