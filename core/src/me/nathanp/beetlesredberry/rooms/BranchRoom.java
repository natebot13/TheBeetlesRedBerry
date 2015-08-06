package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class BranchRoom extends CreatureFunctions {

	@Override
	public String getRoomName() {
		return "branch";
	}

	@Override
	public void onCreate(GameRoom room, Creature craeture) {}

	@Override
	public void headingToNode(GameRoom room, Creature creature, String node) {}

	@Override
	public void arrivedAtNode(GameRoom room, Creature creature, String node) {
		super.arrivedAtNode(room, creature, node);
		if (node.equals("bottomexit")) {
			room.gotoNextRoom(new FlyRoom(), "topentrance", creature.animation);
		} else if (node.equals("chat") && room.isCurrentCreature(creature)) {
			if (room.isCreatureAtNode("cookie", "messenger")) {
				room.changeCurrentCreature("cookie");
			} else if (room.isCreatureAtNode("scissors", "messenger")) {
				room.changeCurrentCreature("scissors");
			}
		} else if (node.equals("messenger") && room.isCurrentCreature(creature)) {
			room.changeCurrentCreature("fly");
		} else if (node.equals("leftexit") && room.isCurrentCreature(creature)) {
			room.gotoNextRoom(new CupsRoom(), "bottomentrance", creature.animation);
		} else if (node.equals("topexit") && room.isCurrentCreature(creature)) {
			room.gotoNextRoom(new StalkRoom(), "bottomentrance", creature.animation);
		}
	}

	@Override
	public void finishedPath(GameRoom room, Creature creature, String node) {}

	@Override
	public void nextAnimation(GameRoom room, Creature creature, String prevAnim) {}

	@Override
	public void moving(GameRoom room, Creature creature, Point movement) {
		super.moving(room, creature, movement);
	}

	@Override
	public void stopped(GameRoom room, Creature creature, String node) {
		super.stopped(room, creature, node);
	}

	@Override
	public void clicked(GameRoom room, Creature creature) {}

	@Override
	public void activated(GameRoom room, Creature creature) {}

	@Override
	public void deactivated(GameRoom room, Creature creature) {}

}
