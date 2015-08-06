package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class PartyRoom extends CreatureFunctions {

	@Override
	public String getRoomName() {
		return "party";
	}

	@Override
	public void onCreate(GameRoom room, Creature creature) {
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
		if (node.equals("middleexit")) {
			room.gotoNextRoom(new RightcupRoom(), "leftentrance", creature.animation);
		} else if (node.equals("leftexit")) {
			room.gotoNextRoom(new OrchidRoom(), "rightentrance", creature.animation);
		} else if (node.equals("chat") && room.isCurrentCreature(creature)) {
			creature.gotoNode("mount");
		} else if (node.equals("mount")) {
			if (room.isCurrentCreature(creature)) {
				if (creature.name.equals("scissors")) {
					room.getCreature("partyfly").animation = "fly";
					room.changeCurrentCreature("partyfly");
					creature.teleToNode("offscreen");
				} else if (creature.name.equals("muscle")) {
					room.getCreature("partyfly").animation = "musclefly";
					room.changeCurrentCreature("partyfly");
					creature.teleToNode("offscreen");
				} else {
					creature.gotoNextNode();
				}
			} else {
				if (creature.name.equals("scissors")) {
					room.changeCurrentCreature("scissors");
				} else if (creature.name.equals("muscle")) {
					room.changeCurrentCreature("muscle");
				}
			}
		} else if (node.equals("land") && room.isCurrentCreature(creature)) {
			if (creature.animation.equals("fly")) {
				room.getCreature("scissors").teleToNode("mount");
			} else if (creature.animation.equals("musclefly")) {
				room.getCreature("muscle").teleToNode("mount");
			}
			creature.animation = "idle";
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
		if (creature.name.equals("spirit")) {
			creature.animation = "move";
		}
	}
	
	@Override
	public void stopped(GameRoom room, Creature creature, String node) {
		super.stopped(room, creature, node);
		if (creature.name.equals("spirit")) {
			if (Math.random() > 0.99) {
				creature.gotoRandomNextNode();
			}
		}
	}

	@Override
	public void clicked(GameRoom room, Creature creature) {
		// TODO Auto-generated method stub

	}

}
