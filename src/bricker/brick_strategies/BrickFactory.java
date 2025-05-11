package bricker.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import bricker.gameobjects.HeartsPanel;
import bricker.main.Constants;

import java.util.Random;

/**
 * Factory class responsible for generating collision strategies for bricks.
 * Chooses a random strategy based on a bounded roll, enabling varied brick behaviors.
 * <p>Supported strategies include ExtraBalls, PaddleDuplicator, TurboMode,
 * LifeRestoration, DoubleHits, and Basic.</p>
 *
 * @see CollisionStrategy
 */
public class BrickFactory {
	private final Random rand;
	private final GameObjectCollection gameObjects;
	private final Counter bricksNum;
	private final ImageReader imageReader;
	private final SoundReader soundReader;
	private final HeartsPanel heartsPanel;
	/**
	 * User input listener used by strategies that require paddle control input,
	 * such as PaddleDuplicatorStrategy.
	 */
	public final UserInputListener userInputListener;

	/**
	 * Constructs a new BrickFactory with required game dependencies.
	 *
	 * @param gameObjects        Collection managing game objects.
	 * @param bricksNum          Counter tracking remaining bricks.
	 * @param imageReader        Loader for image assets.
	 * @param soundReader        Loader for sound assets.
	 * @param userInputListener  Listener for player input.
	 * @param heartsPanel        Panel displaying player lives.
	 */
	public BrickFactory(GameObjectCollection gameObjects,
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
	}

	/**
	 * Returns a randomly selected CollisionStrategy based on a roll within
	 * [0, STRATEGY_ROLL_BOUND).
	 * Ensures uniform distribution across available strategies.
	 *
	 * @return A CollisionStrategy instance.
	 */
	public CollisionStrategy getStrategy() {
		int roll = rand.nextInt(Constants.STRATEGY_ROLL_BOUND);

		switch (roll) {
			case 0:
				return new ExtraBallsStrategy(gameObjects, bricksNum, imageReader, soundReader);
			case 1:
				return new PaddleDuplicatorStrategy(
						gameObjects, bricksNum, imageReader, userInputListener);
			case 2:
				return new TurboModeStrategy(gameObjects, bricksNum, imageReader);
			case 3:
				return new LifeRestorationStrategy(gameObjects, bricksNum, imageReader, heartsPanel);
			case 4:
				return new DoubleStrategy(
						gameObjects, bricksNum, imageReader, soundReader,
						userInputListener, heartsPanel);
			default:
				// Fallback to basic removal behavior (from 5 to STRATEGY_ROLL_BOUND -1)
				return new BasicCollisionStrategy(gameObjects, bricksNum);
		}
	}
}