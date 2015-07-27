package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;
import me.nathanp.beetlesredberry.GameRoom;

public class FlyRoom extends CreatureFunctions {
	
	@Override
	public void onCreate(GameRoom room, Creature craeture) {
	}

	@Override
	public void headingToNode(GameRoom room, Creature creature, String node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void arrivedAtNode(GameRoom room, Creature creature, String node) {
		if (node.equals("leftentrance")) {
			creature.gotoNextNode();
		} else if (node.equals("rightentrance")) {
			creature.gotoNextNode();
		} else if (node.equals("topentrance")) {
			creature.gotoNextNode();
		} else if (node.equals("rightexit") && room.getCurrentCreature().equals(creature.name)) {
			room.gotoNextRoom(new MenuRoom(), "leftentrance", creature.animation);
		} else if (node.equals("leftexit") && room.getCurrentCreature().equals(creature.name)) {
			room.gotoNextRoom(new GrassRoom(), "rightentrance", creature.animation);
		} else if (node.equals("topexit") && room.getCurrentCreature().equals(creature.name)) {
			room.gotoNextRoom(new BranchRoom(), "bottomentrance", creature.animation);
		} else if (node.equals("chat")) {
			room.changeCurrentCreature("fly");
		} else if (node.equals("land")) {
			room.changeCurrentCreature("ant");
			creature.animation = "idle";
			if (creature.flippedY() > 0) {
				creature.flipY();
			}
			room.moveToNode("previous");
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

	@Override
	public String getRoomName() {
		return "flyup";
	}

	@Override
	public void activated(GameRoom room, Creature creature) {
		if (creature.name.equals("fly")) {
			room.moveToNode("hover");
		}
	}

	@Override
	public void deactivated(GameRoom room, Creature creature) {}

}
