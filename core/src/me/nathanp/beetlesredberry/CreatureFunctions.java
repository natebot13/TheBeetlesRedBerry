package me.nathanp.beetlesredberry;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.rooms.BerryRoom;

public abstract class CreatureFunctions {
	
	public abstract String getRoomName();
	
	public abstract void onCreate(GameRoom room, Creature creature);
	
	public abstract void activated(GameRoom room, Creature creature);
	
	public abstract void deactivated(GameRoom room, Creature creature);
	
	public abstract void headingToNode(GameRoom room, Creature creature, String node);
	
	public void arrivedAtNode(GameRoom room, Creature creature, String node) {
		if (!creature.name.equals("berry") && node.contains("entrance") && room.isCurrentCreature(creature)) {
			creature.gotoNextNode();
		}
	}
	
	public abstract void finishedPath(GameRoom room, Creature creature, String node);
	
	public void nextAnimation(GameRoom room, Creature creature, String prevAnim) {
		if (prevAnim.equals("break")) {
			room.gotoNextRoom(new BerryRoom(), "die", creature.animation);
		}
	}
	
	public void moving(GameRoom room, Creature creature, Point movement) {
		if (creature.name.equals("ant")) {
			creature.animation = "walkright";
			flipFromMovement(creature, true, movement);
		} else if (creature.name.equals("fly")) {
			creature.animation = "fly";
			flipFromMovement(creature, false, movement);
		} else if (creature.name.equals("partyfly")) {
			flipFromMovement(creature, false, movement);
		} else if (creature.name.equals("cookie") || creature.name.equals("notcookie")) {
			flipFromMovement(creature, true, movement);
		} else if (creature.name.equals("scissors")) {
			flipFromMovement(creature, true, movement);
		} else if (creature.name.equals("bumper")) {
			flipFromMovement(creature, false, movement);
		}
	}
	
	public void stopped(GameRoom room, Creature creature, String node) {
		if (creature.name.equals("fly")) {
			creature.angle = 0;
		} else if (creature.name.equals("ant")) {
			creature.animation = "idle";
		}
	}
	
	public abstract void clicked(GameRoom room, Creature creature);
	
	private void flipFromMovement(Creature creature, boolean angleWhileMoving, Point movement) {
		creature.moveAtAngle = angleWhileMoving;
		if (angleWhileMoving) {
			if (creature.flippedX() < 0) {
				creature.flipX();
			}
			if (movement.x < 0 && creature.flippedY() > 0) {
				creature.flipY();
			} else if (movement.x > 0 && creature.flippedY() < 0) {
				creature.flipY();
			}
		} else {
			if (creature.flippedY() < 0) {
				creature.flipY();
			}
			if (movement.x < 0 && creature.flippedX() > 0) {
				creature.flipX();
			} else if (movement.x > 0 && creature.flippedX() < 0) {
				creature.flipX();
			}
		}
	}
}
