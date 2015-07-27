package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class BranchRoom extends CreatureFunctions {

	@Override
	public String getRoomName() {
		return "branch";
	}

	@Override
	public void onCreate(GameRoom room, Creature craeture) {}

	@Override
	public void headingToNode(GameRoom room, Creature creature, String node) {}

	@Override
	public void arrivedAtNode(GameRoom room, Creature creature, String node) {
		if (node.equals("bottomentrance")) {
			creature.gotoNextNode();
		} else if (node.equals("bottomexit")) {
			room.gotoNextRoom(new FlyRoom(), "topentrance", creature.animation);
		}
	}

	@Override
	public void finishedPath(GameRoom room, Creature creature, String node) {}

	@Override
	public void nextAnimation(GameRoom room, Creature creature, String prevAnim) {}

	@Override
	public void moving(GameRoom room, Creature creature, Point movement) {}

	@Override
	public void stopped(GameRoom room, Creature creature, String node) {
		super.stopped(room, creature, node);
	}

	@Override
	public void clicked(GameRoom room, Creature creature) {}

	@Override
	public void activated(GameRoom room, Creature creature) {}

	@Override
	public void deactivated(GameRoom room, Creature creature) {}

}
