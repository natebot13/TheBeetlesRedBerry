package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.GameRoom;
import me.nathanp.beetlesredberry.CreatureFunctions;

public class DebugRoom extends CreatureFunctions {
	
	String roomname;
	
	public DebugRoom(String room) {
		roomname = room;
	}
	
	@Override
	public void onCreate(GameRoom room, Creature craeture) {}

    @Override
    public void arrivedAtNode(GameRoom room, Creature creature, String node) {}
    
    @Override
    public void headingToNode(GameRoom room, Creature creature, String node) {}
    
	@Override
	public void finishedPath(GameRoom room, Creature creature, String node) {}

    @Override
    public void nextAnimation(GameRoom room, Creature creature, String prevAnim) {
    	creature.animation = "idle";
    }

	@Override
	public void moving(GameRoom room, Creature creature, Point movement) {}

	@Override
	public void stopped(GameRoom room, Creature creature, String node) {}

	@Override
	public void clicked(GameRoom room, Creature creature) {}

	@Override
	public String getRoomName() {
		return roomname;
	}

	@Override
	public void activated(GameRoom room, Creature creature) {}

	@Override
	public void deactivated(GameRoom room, Creature creature) {}
}
