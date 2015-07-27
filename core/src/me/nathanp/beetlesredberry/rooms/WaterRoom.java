package me.nathanp.beetlesredberry.rooms;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class WaterRoom extends CreatureFunctions {

	@Override
	public String getRoomName() {
		return "water";
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
			room.gotoNextRoom(new LilypadRoom(), "rightentrance", creature.animation);
		} else if (node.equals("downexit")) {
			room.gotoNextRoom(new UnderwaterRoom(), "topentrance", creature.animation);
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
