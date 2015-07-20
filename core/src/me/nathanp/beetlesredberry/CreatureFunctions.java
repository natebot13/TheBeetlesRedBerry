package me.nathanp.beetlesredberry;

import com.brashmonkey.spriter.Point;

public abstract class CreatureFunctions {
	
	public abstract void headingToNode(GameRoom room, Creature creature, String node);
	
	public abstract void arrivedAtNode(GameRoom room, Creature creature, String node);
	
	public abstract String nextAnimation(GameRoom room, Creature creature, String prevAnim);
	
	public abstract void moving(GameRoom room, Creature creature, Point movement);
}
