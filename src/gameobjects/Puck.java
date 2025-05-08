package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a "Puck" – an additional ball that does not reduce lives if it falls.
 */
public class Puck extends GameObject {
	private final Sound collisionSound;
	private final GameObjectCollection gameObjects;

	public Puck(Vector2 topLeftCorner, Vector2 dimensions,
				Renderable renderable, Sound collisionSound,
				GameObjectCollection gameObjects) {
		super(topLeftCorner, dimensions, renderable);
		this.collisionSound = collisionSound;
		this.gameObjects = gameObjects;
	}

	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		setVelocity(getVelocity().flipped(collision.getNormal()));
		collisionSound.play();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		// Remove puck from the game if it falls below the window
		if (getCenter().y() > 700) { // Use actual window height if accessible
			gameObjects.removeGameObject(this);
		}
	}
}
