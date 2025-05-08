package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class PaddleDuplicatorStrategy implements CollisionStrategy {
	public PaddleDuplicatorStrategy(GameObjectCollection gameObjects, Counter bricksNum) {
	}

	@Override
	public void onCollision(GameObject object1, GameObject object2) {
		System.out.println("PaddleDuplicatorStrategy");
	}
}
