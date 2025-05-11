package brick_strategies;

import danogl.GameObject;

/**
 * Interface for defining different collision strategies for bricks.
 */
public interface CollisionStrategy {
	void onCollision(GameObject object1, GameObject object2);
}
