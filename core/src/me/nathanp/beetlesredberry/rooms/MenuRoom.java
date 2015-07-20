package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.GameRoom;
import me.nathanp.beetlesredberry.CreatureFunctions;

public class MenuRoom extends CreatureFunctions {

    @Override
    public void arrivedAtNode(GameRoom room, Creature creature, String node) {
        if (node.equals("entrance")) {
            creature.gotoNode("next");
            creature.pauseMovement();
            creature.changePlayerAnimation("shiver");
        } else if (node.equals("next")) {
            creature.gotoNode("entrance");
            creature.pauseMovement();
            creature.changePlayerAnimation("shiver");
        } else if (node.equals("exit")) {
        	room.nextRoom("Menu", new MenuRoom(), "entrance", "idle");
        }
    }
    
    @Override
    public void headingToNode(GameRoom room, Creature creature, String node) {
    	
    }

    @Override
    public String nextAnimation(GameRoom room, Creature creature, String prevAnim) {
        if (creature.name.equals("spirit")) {
            if (prevAnim.equals("shiver")) {
                creature.resumeMovement();
                return "idle";
            } else if (prevAnim.equals("move")) {
                return "move";
            }
        } else if (creature.name.equals("beetle")) {
            return "scurry,idle,roll".split(",")[(int)Math.random() * 3];
        }
        return "idle";
    }

	@Override
	public void moving(GameRoom room, Creature creature, Point movement) {
		creature.animation = "move";
	}
}
