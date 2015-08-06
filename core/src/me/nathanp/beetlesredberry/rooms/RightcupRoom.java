package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class RightcupRoom extends CreatureFunctions {

	@Override
	public String getRoomName() {
		// TODO Auto-generated method stub
		return "rightcup";
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
		if (node.equals("bottomexit")) {
			room.gotoNextRoom(new StalkRoom(), "rightentrance", creature.animation);
		} else if (node.equals("leftexit")) {
			room.gotoNextRoom(new PartyRoom(), "middleentrance", creature.animation);
		} else if (node.equals("topexit")) {
			room.gotoNextRoom(new BushRoom(), "bottomentrance", creature.animation);
		} else if (node.equals("chat") && room.isCurrentCreature(creature)) {
			creature.gotoNode("wait");
			room.changeCurrentCreature("muscle");
		} else if (node.equals("berryentrance")) {
			creature.gotoNode("berrydie");
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
