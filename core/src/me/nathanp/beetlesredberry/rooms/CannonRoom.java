package me.nathanp.beetlesredberry.rooms;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class CannonRoom extends CreatureFunctions {

	@Override
	public String getRoomName() {
		// TODO Auto-generated method stub
		return "cannon";
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
		if (node.equals("rightexit")) {
			room.gotoNextRoom(new OrchidRoom(), "leftentrance", creature.animation);
		} else if (node.equals("cut") && creature.animation.equals("fly") && !room.isCreatureAtNode("cannonhole", "hole")) {
			creature.gotoNode("cutdone");
		} else if (node.equals("cutdone")) {
			room.newCreature("cannonhole", "hole", "idle");
			creature.gotoNextNode();
		} else if (node.equals("berryentrance")) {
			creature.gotoNode("check");
		} else if (node.equals("check") && creature.name.equals("berry")) {
			if (room.isCreatureAtNode("cannonhole", "hole")) {
				creature.gotoNode("berryexit");
			} else {
				creature.gotoNode("berrydie");
			}
		} else if (node.equals("berryexit")) {
			room.gotoNextRoom(new OrchidRoom(), "berryentrance", creature.animation);
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
	public void stopped(GameRoom room, Creature creature, String node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clicked(GameRoom room, Creature creature) {
		// TODO Auto-generated method stub

	}

}
