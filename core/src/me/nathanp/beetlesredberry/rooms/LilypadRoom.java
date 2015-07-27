package me.nathanp.beetlesredberry.rooms;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class LilypadRoom extends CreatureFunctions {

	@Override
	public String getRoomName() {
		return "lilypad";
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
		if (node.equals("leftexit")) {
			room.gotoNextRoom(new MenuRoom(), "rightentrance", creature.animation);
		} else if (node.equals("rightexit")) {
			room.gotoNextRoom(new WaterRoom(), "leftentrance", creature.animation);
		} else if (node.equals("leftentrance")) {
			creature.gotoNextNode();
		} else if (node.equals("rightentrance")) {
			creature.gotoNextNode();
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
	public void clicked(GameRoom room, Creature creature) {
		// TODO Auto-generated method stub

	}

}
