package me.nathanp.beetlesredberry.rooms;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class BerryRoom extends CreatureFunctions {

	@Override
	public String getRoomName() {
		// TODO Auto-generated method stub
		return "berry";
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
		if (node.equals("topexit")) {
			room.gotoNextRoom(new BushRoom(), "topentrance", creature.animation);
		} else if (node.equals("berrydrop") && room.isCurrentCreature(creature)) {
			room.changeCurrentCreature("berry");
		} else if (node.equals("berryexit")) {
			room.gotoNextRoom(new CannonRoom(), "berryentrance", creature.animation);
		}
	}

	@Override
	public void finishedPath(GameRoom room, Creature creature, String node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nextAnimation(GameRoom room, Creature creature, String prevAnim) {
		if (prevAnim.equals("grow")) {
			creature.animation = "idle";
		} else if (prevAnim.equals("break")) {
			creature.teleToNode("berry");
			creature.animation = "grow";
			if (room.isCreatureAtNode("cookie", "berrydrop")) {
				room.changeCurrentCreature("cookie");
			} else if (room.isCreatureAtNode("scissors", "berrydrop")) {
				room.changeCurrentCreature("scissors");
			} else if (room.isCreatureAtNode("muscle", "berrydrop")) {
				room.changeCurrentCreature("muscle");
			}
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
