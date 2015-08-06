package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class OrchidRoom extends CreatureFunctions {

	@Override
	public String getRoomName() {
		// TODO Auto-generated method stub
		return "orchid";
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
		if (node.equals("rightexit")) {
			room.gotoNextRoom(new PartyRoom(), "leftentrance", creature.animation);
		} else if (node.equals("leftexit")) {
			room.gotoNextRoom(new CannonRoom(), "rightentrance", creature.animation);
		}
		if (node.equals("berryentrance") && room.isCurrentCreature(creature)) {
			room.moveToNode("land");
		} else if (node.equals("land") && room.isCurrentCreature(creature) && creature.name.equals("berry") && room.isCreatureAtNode("rolly", "land")) {
			room.moveToNode("berryexit2");
		} else if (room.isCurrentCreature(creature) && creature.animation.equals("musclefly") && room.isCreatureAtNode("rolly", "land")) {
			if (node.equals("rightpush")) {
				room.getCreature("rolly").animation = "leftroll";
				room.getCreature("rolly").gotoNode("berrydie1");
				room.moveToNode("land");
			} else if (node.equals("leftpush")) {
				room.getCreature("rolly").animation = "rightroll";
				room.getCreature("rolly").gotoNode("berrydie3");
				room.moveToNode("land");
			}
		} else if (creature.name.equals("berry") && room.isCurrentCreature(creature)) {
			if (node.equals("leftpush")) {
				creature.clearPath();
				room.moveToNode("berryexit1");
			} else if (node.equals("rightpush")) {
				creature.clearPath();
				room.moveToNode("berryexit3");
			}
		} else if (node.equals("berryexit1") && creature.name.equals("berry")) {
			room.gotoNextRoom(new FlipFlopRoom(), "berryentrance1", creature.animation);
		} else if (node.equals("berryexit2") && creature.name.equals("berry")) {
			room.gotoNextRoom(new FlipFlopRoom(), "berryentrance2", creature.animation);
		} else if (node.equals("berryexit3") && creature.name.equals("berry")) {
			room.gotoNextRoom(new FlipFlopRoom(), "berryentrance3", creature.animation);
		}
	}

	@Override
	public void finishedPath(GameRoom room, Creature creature, String node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nextAnimation(GameRoom room, Creature creature, String prevAnim) {
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

}
