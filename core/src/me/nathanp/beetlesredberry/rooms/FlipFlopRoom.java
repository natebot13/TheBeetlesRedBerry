package me.nathanp.beetlesredberry.rooms;

import me.nathanp.beetlesredberry.GameRoom;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.CreatureFunctions;

public class FlipFlopRoom extends CreatureFunctions {
	
	@Override
	public void onCreate(GameRoom room, Creature craeture) {
	}
	
	@Override
	public void arrivedAtNode(GameRoom room, Creature creature, String node) {
		super.arrivedAtNode(room, creature, node);
		if (node.equals("bottomexit")) {
			room.gotoNextRoom(new StalkRoom(), "topentrance", creature.animation);
		} else if (node.equals("berryentrance1")) {
			creature.gotoNode("berryleftexit");
		} else if (node.equals("berryentrance2")) {
			creature.gotoNode("berrydie2");
		} else if (node.equals("berryentrance3")) {
			creature.gotoNode("berryrightexit");
		} else if (node.equals("berryleft")) {
			Creature flower = room.getCreature("flower");
			if (flower.animation.equals("idleclosed")) {
				flower.animation = "leftcatch";
			}
		} else if (node.equals("berryright")) {
			Creature flower = room.getCreature("flower");
			if (flower.animation.equals("idleclosed")) {
				flower.animation = "rightcatch";
			}
		} else if (node.equals("berrydie1")) {
			Creature flower = room.getCreature("flower");
			if (flower.animation.equals("rightcatch")) {
				creature.animation = "break";
			}
		} else if (node.equals("berrydie2")) {
			Creature flower = room.getCreature("flower");
			if (flower.animation.equals("idleclosed")) {
				flower.animation = "pinchclosed";
			} else if (flower.animation.equals("idleopen")) {
				flower.animation = "pinchopen";
			}
			creature.animation = "break";
		} else if (node.equals("berrydie3")) {
			Creature flower = room.getCreature("flower");
			if (flower.animation.equals("leftcatch")) {
				creature.animation = "break";
			}
		} else if (node.equals("leftpetal")) {
			room.getCreature("flower").animation = "left_bounce";
		} else if (node.equals("rightpetal")) {
			room.getCreature("flower").animation = "right_bounce";
		} else if (node.equals("berryleftexit")) {
			room.gotoNextRoom(new LeftcupRoom(), "berryentrance", creature.animation);
		} else if (node.equals("berryrightexit")) {
			room.gotoNextRoom(new RightcupRoom(), "berryentrance", creature.animation);
		} else if (node.equals("pryopen") && creature.name.equals("muscle") && room.getCreature("flower").animation.equals("idleclosed")) {
			room.getCreature("flower").animation = "open";
		}
	}

	@Override
	public void nextAnimation(GameRoom room, Creature creature, String prevAnim) {
		super.nextAnimation(room, creature, prevAnim);
		if (prevAnim.equals("left_bounce") || prevAnim.equals("right_bounce")) {
			creature.animation = "idleopen";
		} else if (prevAnim.equals("pinchclosed") || prevAnim.equals("leftcatch") || prevAnim.equals("rightcatch")) {
			creature.animation = "idleclosed";
		} else if (prevAnim.equals("pinchopen") || prevAnim.equals("left_bounce") || prevAnim.equals("right_bounce")) {
			creature.animation = "idleopen";
		} else if (prevAnim.equals("open")) {
			creature.animation = "idleopen";
		}
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
	public void finishedPath(GameRoom room, Creature creature, String node) {
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

	@Override
	public String getRoomName() {
		return "flipflop";
	}

	@Override
	public void activated(GameRoom room, Creature creature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivated(GameRoom room, Creature creature) {
		// TODO Auto-generated method stub
		
	}
}
