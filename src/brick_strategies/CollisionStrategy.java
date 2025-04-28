package brick_strategies;

import danogl.GameObject;
import danogl.collisions.Collision;

public interface CollisionStrategy {
	void onCollision(GameObject object1, GameObject object2);
}
