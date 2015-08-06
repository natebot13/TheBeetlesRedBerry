package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class GrassRoom extends CreatureFunctions {

	@Override
	public String getRoomName() {
		return "grass";
	}

	@Override
	public void onCreate(GameRoom room, Creature craeture) {}

	@Override
	public void headingToNode(GameRoom room, Creature creature, String node) {}

	@Override
	public void arrivedAtNode(GameRoom room, Creature creature, String node) {
		super.arrivedAtNode(room, creature, node);
		if (node.equals("rightexit")) {
			room.gotoNextRoom(new FlyRoom(), "leftentrance", creature.animation);
		} else if (node.equals("berryentrance")) {
			creature.gotoNode("berrydie");
		} else if (node.equals("berrydie")) {
			creature.animation = "break";
		}
	}

	@Override
	public void finishedPath(GameRoom room, Creature creature, String node) {}

	@Override
	public void nextAnimation(GameRoom room, Creature creature, String prevAnim) {
		super.nextAnimation(room, creature, prevAnim);
	}

	@Override
	public void moving(GameRoom room, Creature creature, Point movement) {
		super.moving(room, creature, movement);
	}

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
