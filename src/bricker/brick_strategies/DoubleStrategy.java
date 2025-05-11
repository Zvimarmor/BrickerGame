// === DoubleStrategy.java ===
package bricker.brick_strategies;

import bricker.main.Constants;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import bricker.gameobjects.HeartsPanel;

import java.util.Random;

/**
 * Composes multiple collision strategies for compound effects.
 * Randomly selects two or three sub-strategies at construction,
 * allowing nested behaviors like extra balls, paddle duplication,
 * life restoration, and turbo mode.
 */
public class DoubleStrategy implements CollisionStrategy {
	private final Random rand;
	private final GameObjectCollection gameObjects;
	private final Counter bricksNum;
	private final ImageReader imageReader;
	private final SoundReader soundReader;
	private final HeartsPanel heartsPanel;

	private CollisionStrategy strategy1;
	private CollisionStrategy strategy2;
	private CollisionStrategy strategy3 = null;

	/**
	 * User input listener used by strategies that require paddle control input,
	 * such as PaddleDuplicatorStrategy.
	 */
	public final UserInputListener userInputListener;

	/**
	 * Constructs the double strategy with random sub-strategies.
	 *
	 * @param gameObjects        Collection of game objects.
	 * @param bricksNum          Brick counter.
	 * @param imageReader        Image loader.
	 * @param soundReader        Sound loader.
	 * @param userInputListener  Keyboard listener.
	 * @param heartsPanel        Lives display panel.
	 */
	public DoubleStrategy(GameObjectCollection gameObjects,
						  Counter bricksNum,
						  ImageReader imageReader,
						  SoundReader soundReader,
						  UserInputListener userInputListener,
						  HeartsPanel heartsPanel) {
		this.rand = new Random();
		this.gameObjects = gameObjects;
		this.bricksNum = bricksNum;
		this.imageReader = imageReader;
		this.soundReader = soundReader;
		this.userInputListener = userInputListener;
		this.heartsPanel = heartsPanel;

		// Choose first strategy
		strategy1 = getStrategy(rand.nextInt(Constants.DOUBLE_STRATEGY_INDEX_BOUND));

		// Choose second or nested strategies
		int second = rand.nextInt(Constants.DOUBLE_STRATEGY_INDEX_BOUND_WITH);
		if (second == Constants.DOUBLE_STRATEGY_INDEX_BOUND) {
			strategy2 = getStrategy(rand.nextInt(Constants.DOUBLE_STRATEGY_INDEX_BOUND));
			strategy3 = getStrategy(rand.nextInt(Constants.DOUBLE_STRATEGY_INDEX_BOUND));
		} else {
			strategy2 = getStrategy(second);
		}
	}

	/**
	 * Returns a specific strategy instance based on index.
	 *
	 * @param num Strategy index
	 * @return CollisionStrategy.
	 */
	private CollisionStrategy getStrategy(int num) {
		switch (num) {
			case 0: return new ExtraBallsStrategy(gameObjects, bricksNum, imageReader, soundReader);
			case 1: return new PaddleDuplicatorStrategy(gameObjects, bricksNum,
					imageReader, userInputListener);
			case 2: return new TurboModeStrategy(gameObjects, bricksNum, imageReader);
			case 3: return new LifeRestorationStrategy(gameObjects, bricksNum, imageReader, heartsPanel);
			default:
				// Should not occur; fallback
				return new BasicCollisionStrategy(gameObjects, bricksNum);
		}
	}

	/**
	 * Executes all composed strategies upon collision.
	 * If a third strategy was selected (nested case), it will also be executed.
	 *
	 * @param brick The brick object being collided with.
	 * @param collider The object that collided with the brick.
	 */
	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		strategy1.onCollision(brick, collider);
		strategy2.onCollision(brick, collider);
		if (strategy3 != null) {
			strategy3.onCollision(brick, collider);
		}
	}
}
