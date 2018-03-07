package ai;

import ScotlandYardFrame.GameMaster;

public interface AI {

	void update(GameMaster g);
	
	void decide() throws Exception;
	
}
