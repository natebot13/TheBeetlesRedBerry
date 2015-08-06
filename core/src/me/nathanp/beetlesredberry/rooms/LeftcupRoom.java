package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class LeftcupRoom extends CreatureFunctions {

	@Override
	public String getRoomName() {
		return "leftcup";
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
	public void arrivedAtNode(GameRoom room, Creature creature, String node) {
		super.arrivedAtNode(room, creature, node);
		if (node.equals("bottomexit") && room.isCurrentCreature(creature)) {
			room.gotoNextRoom(new GolfRoom(), "bottomentrance", creature.animation);
		} else if (node.equals("cut") && creature.name.equals("scissors") && !room.isCreatureAtNode("hole", "holespawn")) {
			creature.gotoNode("cutdone");
		} else if (node.equals("cutdone") && creature.name.equals("scissors")) {
			room.getNodes().disableNode("berrydie");
			room.getNodes().enableNode("hole");
			creature.gotoNextNode();
			room.newCreature("hole", "holespawn", "idle");
		} else if (node.equals("berryentrance")) {
			creature.gotoNode("berryexit");
		} else if (node.equals("berrydie")) {
			creature.animation = "break";
		}
	}

	@Override
	public void finishedPath(GameRoom room, Creature creature, String node) {
		// TODO Auto-generated method stub

	}

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
	public void clicked(GameRoom room, Creature creature) {
		// TODO Auto-generated method stub

	}

}
