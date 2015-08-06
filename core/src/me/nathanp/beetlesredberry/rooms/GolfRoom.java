package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class GolfRoom extends CreatureFunctions {

	@Override
	public String getRoomName() {
		// TODO Auto-generated method stub
		return "golf";
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
		if (node.equals("middleexit") && room.isCurrentCreature(creature)) {
			room.gotoNextRoom(new CupsRoom(), "topentrance", creature.animation);
		} else if (node.equals("bottomexit") && room.isCurrentCreature(creature)) {
			room.gotoNextRoom(new LeftcupRoom(), "bottomentrance", creature.animation);
		} else if (node.equals("chat") && room.isCurrentCreature(creature)) {
			room.moveToNextNode();
			if (creature.name.equals("cookie")) {
				room.changeCurrentCreature("scissors");
			} else if (creature.name.equals("scissors")) {
				room.changeCurrentCreature("cookie");
			}
		} else if (node.equals("movebumper")) {
			room.getCreature("bumper").gotoNextNode();
		} else if (node.equals("berryentrance")) {
			creature.gotoNode("berryexit");
		} else if (node.equals("rightenable")) {
			room.getNodes().disableNode("lefthole");
			room.getNodes().disableNode("middlehole");
			room.getNodes().enableNode("righthole");
		} else if (node.equals("middleenable")) {
			room.getNodes().disableNode("lefthole");
			room.getNodes().disableNode("righthole");
			room.getNodes().enableNode("middlehole");
		} else if (node.equals("leftenable")) {
			room.getNodes().disableNode("righthole");
			room.getNodes().disableNode("middlehole");
			room.getNodes().enableNode("lefthole");
		} else if (node.equals("lefthole")) {
			room.gotoNextRoom(new CupsRoom(), "berryentrance1", creature.animation);
		} else if (node.equals("middlehole")) {
			room.gotoNextRoom(new CupsRoom(), "berryentrance2", creature.animation);
		} else if (node.equals("righthole")) {
			room.gotoNextRoom(new CupsRoom(), "berryentrance3", creature.animation);
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
		super.stopped(room, creature, node);
	}

	@Override
	public void clicked(GameRoom room, Creature creature) {
		// TODO Auto-generated method stub

	}

}
