package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.GameRoom;
import me.nathanp.beetlesredberry.CreatureFunctions;

public class DebugRoom extends CreatureFunctions {

    @Override
    public void arrivedAtNode(GameRoom room, Creature creature, String node) {}
    
    @Override
    public void headingToNode(GameRoom room, Creature creature, String node) {
    }
    
	@Override
	public void finishedPath(GameRoom room, Creature creature, String node) {
		creature.gotoRandomNextNode();
	}

    @Override
    public String nextAnimation(GameRoom room, Creature creature, String prevAnim) {
    	return "idle";
    }

	@Override
	public void moving(GameRoom room, Creature creature, Point movement) {
		creature.animation = "move";
	}

	@Override
	public void stopped(GameRoom room, Creature creature, String node) {
		creature.gotoRandomNextNode();
	}
}
