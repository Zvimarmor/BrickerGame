package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import gameobjects.Ball;

public class BasicCollisionStrategy implements CollisionStrategy {
	private GameObjectCollection gameObjectCollection;
	
	public BasicCollisionStrategy(GameObjectCollection gameObjectCollection) {
		this.gameObjectCollection = gameObjectCollection;
	}
	@Override
	public void onCollision(GameObject object1, GameObject object2) {
		gameObjectCollection.removeGameObject(object1, Layer.STATIC_OBJECTS);
	}
}
