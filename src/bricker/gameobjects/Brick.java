package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A brick object that executes a collision strategy upon impact.
 */
public class Brick extends GameObject {
	private CollisionStrategy collisionStrategy;

	/**
	 * Construct a new Brick instance.
	 *
	 * @param topLeftCorner      Top-left position of the brick.
	 * @param dimensions         Size of the brick.
	 * @param renderable         Visual representation.
	 * @param collisionStrategy  Strategy to execute on collision.
	 */
	public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
				 CollisionStrategy collisionStrategy) {
		super(topLeftCorner, dimensions, renderable);
		this.collisionStrategy = collisionStrategy;
	}

	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		collisionStrategy.onCollision(this, other);
	}
}
