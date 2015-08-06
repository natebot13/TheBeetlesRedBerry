package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class CupsRoom extends CreatureFunctions {

	@Override
	public String getRoomName() {
		// TODO Auto-generated method stub
		return "cups";
	}

	@Override
	public void onCreate(GameRoom room, Creature craeture) {
		// TODO Auto-generated method stub

	}

	@Override
	public void activated(GameRoom room, Creature creature) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivated(GameRoom room, Creature creature) {
		// TODO Auto-generated method stub

	}

	@Override
	public void headingToNode(GameRoom room, Creature creature, String node) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void moving(GameRoom room, Creature creature, Point movement) {
		super.moving(room, creature, movement);
	}

	@Override
	public void arrivedAtNode(GameRoom room, Creature creature, String node) {
		super.arrivedAtNode(room, creature, node);
		if (node.equals("bottomexit") && room.isCurrentCreature(creature)) {
			room.gotoNextRoom(new BranchRoom(), "leftentrance", creature.animation);
		} else if (node.equals("topexit") && room.isCurrentCreature(creature)) {
			room.gotoNextRoom(new GolfRoom(), "middleentrance", creature.animation);
		} else if (node.equals("berryentrance1")) {
			creature.gotoNode("berryexit1");
		} else if (node.equals("berryentrance2")) {
			creature.gotoNode("berryexit2");
		} else if (node.equals("berryentrance3")) {
			creature.gotoNode("berryrightexit");
		} else if (node.equals("berryexit1") || node.equals("berryexit2")) {
			room.gotoNextRoom(new GrassRoom(), "berryentrance", creature.animation);
		} else if (node.equals("berryrightexit")) {
			room.gotoNextRoom(new MenuRoom(), "berryentrance", creature.animation);
		} else if (node.equals("cuptoss")) {
			room.getCreature("cup").animation = "toss";
		}
	}

	@Override
	public void finishedPath(GameRoom room, Creature creature, String node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nextAnimation(GameRoom room, Creature creature, String prevAnim) {
		if (creature.name.equals("cup")) {
			creature.animation = "idle";
		}
	}

	@Override
	public void stopped(GameRoom room, Creature creature, String node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clicked(GameRoom room, Creature creature) {
		// TODO Auto-generated method stub

	}

}
