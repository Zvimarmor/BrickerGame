package brick_strategies;

import danogl.GameObject;

public class DoubleStrategy implements CollisionStrategy {
	public DoubleStrategy(BasicCollisionStrategy basicCollisionStrategy) {
	}

	@Override
	public void onCollision(GameObject object1, GameObject object2) {
		System.out.println("DoubleStrategy");
	}
}
