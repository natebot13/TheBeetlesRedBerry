package me.nathanp.beetlesredberry.rooms;

import me.nathanp.beetlesredberry.GameRoom;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;

public class FlipFlopRoom extends CreatureFunctions {
	
	@Override
	public void onCreate(GameRoom room, Creature craeture) {
	}
	
	@Override
	public void arrivedAtNode(GameRoom room, Creature creature, String node) {
		
	}

	@Override
	public void nextAnimation(GameRoom room, Creature creature, String prevAnim) {}

	@Override
	public void headingToNode(GameRoom room, Creature creature, String node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moving(GameRoom room, Creature creature, Point movement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishedPath(GameRoom room, Creature creature, String node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopped(GameRoom room, Creature creature, String node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clicked(GameRoom room, Creature creature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getRoomName() {
		return "flipflop";
	}

	@Override
	public void activated(GameRoom room, Creature creature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivated(GameRoom room, Creature creature) {
		// TODO Auto-generated method stub
		
	}
}
