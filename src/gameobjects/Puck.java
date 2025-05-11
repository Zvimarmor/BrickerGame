package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import main.Constants;

/**
 * A puck behaves like a ball but does not cost a life when falling.
 */
public class Puck extends GameObject {
	private final Sound collisionSound;
	private final GameObjectCollection gameObjects;

	/**
	 * Constructs a new puck instance.
	 *
	 * @param topLeftCorner   Position of the puck.
	 * @param dimensions      Size of the puck.
	 * @param renderable      Appearance.
	 * @param collisionSound  Sound to play on hit.
	 * @param gameObjects     Reference to game object collection.
	 */
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

		// Remove puck from game when it falls below window
		if (getCenter().y() > Constants.WINDOW_HEIGHT) {
			gameObjects.removeGameObject(this);
		}
	}
}
