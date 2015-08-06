package me.nathanp.beetlesredberry.rooms;

import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.Creature;
import me.nathanp.beetlesredberry.GameRoom;
import me.nathanp.beetlesredberry.CreatureFunctions;

public class MenuRoom extends CreatureFunctions {
	
	@Override
	public void onCreate(GameRoom room, Creature creature) {
		if (creature.name.equals("talkbubble")) {
			creature.animation = "turnon";
		} else if (creature.name.equals("beetle")) {
			creature.animation = "idle";
		}
	}
	
    @Override
    public void arrivedAtNode(GameRoom room, Creature creature, String node) {
    	if (creature.name.equals("talkbubble")) {
    		creature.speed = 0;
    	}
        if (node.equals("rightentrance")) {
            creature.gotoNextNode();
        } else if (node.equals("rightexit")) {
        	room.gotoNextRoom(new LilypadRoom(), "leftentrance", creature.animation);
        } else if (node.equals("leftexit") && room.getCurrentCreature().equals(creature.name)) {
        	room.gotoNextRoom(new FlyRoom(), "rightentrance", creature.animation);
        } else if (node.equals("leftentrance")) {
        	creature.gotoNextNode();
        } else if (node.equals("chat")) {
        	room.getCreature("beetle").animation = "talk";
        	room.getCreature("talkbubble").speed = 15;
        } else if (node.equals("berryentrance")) {
        	creature.gotoNode("berryland");
        } else if (node.equals("berryland")) {
        	room.newCreature("thankyou", "thankyou", "idle");
    	} else if (node.equals("thankyou")) {
        	creature.gotoNextNode();
        }
    }
    
    @Override
    public void headingToNode(GameRoom room, Creature creature, String node) {}

    @Override
    public void nextAnimation(GameRoom room, Creature creature, String prevAnim) {
    	if (creature.name.equals("talkbubble")) {
    		if (prevAnim.equals("turnon")) {
    			creature.animation = "idle";
    		} else if (prevAnim.equals("turnoff")) {
    			creature.speed = 0;
    			creature.animation = "turnon";
    		}
    	} else if (creature.name.equals("beetle") && prevAnim.equals("talk")) {
    		room.getCreature("talkbubble").animation = "turnoff";
    		creature.animation = "idle";
    	}
    }

	@Override
	public void moving(GameRoom room, Creature creature, Point movement) {
		super.moving(room, creature, movement);
	}

	@Override
	public void finishedPath(GameRoom room, Creature creature, String node) {}

	@Override
	public void stopped(GameRoom room, Creature creature, String node) {
		if (creature.name.equals("ant")) {
			creature.animation = "idle";
		}
	}

	@Override
	public void clicked(GameRoom room, Creature creature) {}

	@Override
	public String getRoomName() {
		return "menu";
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
