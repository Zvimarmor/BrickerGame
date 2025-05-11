/**
 * Interface for defining various collision behaviors for bricks upon collision events.
 * Implementations decide how to handle the collision interaction between two GameObjects.
 */
package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Defines a contract for implementing different brick collision behaviors.
 * Classes that implement this interface define what happens when a brick
 * collides with another game object (ball).
 * Used in the Bricker game to allow dynamic assignment of effects to bricks,
 * such as spawning new objects, changing paddle behavior, or altering ball state.
 */
public interface CollisionStrategy {
	/**
	 * Called when two game objects collide.
	 *
	 * @param object1 The primary object involved in the collision (e.g., the brick).
	 * @param object2 The other object involved in the collision (e.g., the ball or paddle).
	 */
	void onCollision(GameObject object1, GameObject object2);
}