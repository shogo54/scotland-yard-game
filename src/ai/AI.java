package ai;

import game.GameMaster;

/**
 * Interface that will be implemented by CleverAI and RandomAI
 * 
 * @author Shogo Akiyama
 *
 */
public interface AI {

	void update(GameMaster g);
	
	void decide() throws Exception;
	
}
