package me.nathanp.beetlesredberry;

import java.util.Comparator;
import java.util.LinkedList;

import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Point;
import com.brashmonkey.spriter.SpriterException;

import me.nathanp.beetlesredberry.RoomNodes.Node;
import me.nathanp.beetlesredberry.util.Util;

import com.brashmonkey.spriter.Mainline.Key;

public class Creature {
	private transient GameRoom room;
	private transient Player creature;
	private transient CreatureFunctions callbacks;
	
	public String name;
	public int number;
    public Point pos;
    public float angle = 0;
    public String animation;
    public int time;
    public int speed = 15;
    public int moveSpeed;
    public String currentNode;
    public int zIndex = 0;
    public float scale = 1f;
    public boolean moveAtAngle = true;
    private LinkedList<String> currentPath = new LinkedList<String>();
    
    private transient boolean movementPaused = false;
    private transient boolean arriveOnce = true;
    
    public Creature() {}
    
    public Creature(GameRoom room, CreatureFunctions callbacks, String name, int number, String startingNode, String animation) {
    	this.callbacks = callbacks;
    	this.room  = room;
    	this.name = name;
    	this.number = number;
    	currentNode = startingNode;
    	Point p = room.getNodes().getPoint(currentNode);
    	if (p != null) pos = new Point(p);
    	else pos = new Point();
    	this.animation = animation;
    	this.moveSpeed = room.getCreatureMoveSpeed();
    	init(room, callbacks);
    	this.time = creature.getTime();
    }
    
    public void setCallback() {
    	final Creature self = this;
    	creature.addListener(new Player.PlayerListener() {
			
			@Override
			public void preProcess(Player player) {}
			
			@Override
			public void postProcess(Player player) {}
			
			@Override
			public void mainlineKeyChanged(Key prevKey, Key newKey) {}
			
			@Override
			public void animationFinished(Animation animation) {
				callbacks.nextAnimation(room, self, animation.name);
			}
			
			@Override
			public void animationChanged(Animation oldAnim, Animation newAnim) {}
    	});
    }
    
    /**
     * Tells the creature to attempt to navigate to the node from the current node.
     * Appends the computed path to the previous path so the player will pass through all points.
     * @param name the name of the node
     */
    public void gotoNode(String to) {
        gotoNode(to, currentNode);
    }
    
    /**
     * Calculates and appends the path between 'from' and 'to', to the creatures route queue.
     * Note that if the creature is not already at the 'from' node, it will beeline to it,
     * and then follow the calulated path. 
     * @param to the destination node
     * @param from the starting node
     */
    public void gotoNode(String to, String from) {
    	for (String n : room.getNodes().getPath(from, to)) {
        	if (!currentPath.contains(n)) {
        		currentPath.add(n);
        	}
        }
    }
    
    /**
     * Tells the creature to go to the child of the current node.
     * If the current node has more than one child, there is no guarantee
     * which child will be returned.
     */
    public void gotoNextNode() {
    	addToPath(room.getNodes().getNextNode(currentNode));
    }
    
    public void gotoRandomNextNode() {
    	addToPath(room.getNodes().getRandomChild(currentNode));
    }
    
    public void cutToNode(String node) {
    	addToPath(node);
    }
    
    public void teleToNode(String node) {
    	currentNode = node;
    	currentPath.clear();
    	pos.set(room.getNodes().getPoint(node));
    }
    
    private boolean addToPath(String node) {
    	if (node != null && !currentPath.contains(node)) {
    		currentPath.add(node);
    		return true;
    	}
    	return false;
    }
    /**
     * Changes the player animation to name
     * @param name the name of the animation
     */
    public void changePlayerAnimation(String name) {
        creature.setAnimation(name);
    }
    
    /**
     * Pauses movement.
     */
    public void pauseMovement() {
        movementPaused = true;
    }
    
    /**
     * Resumes movement.
     */
    public void resumeMovement() {
        movementPaused = false;
    }
    
    public void clicked() {
    	callbacks.clicked(room, this);
    }
    
    public void activated() {
    	callbacks.activated(room, this);
    }
    
    public void deactivated() {
    	callbacks.deactivated(room, this);
    }
    
    public void init(GameRoom room, CreatureFunctions callbacks) {
    	this.creature = new Player(room.getData().getEntity(name));
    	this.room = room;
    	this.callbacks = callbacks;
    	setCallback();
    	callbacks.onCreate(room, this);
    }
    
    /**
     * Moves the player according to the path.
     * @param dt the delta time
     */
    private void move(float dt) {
    	if (currentNode != null) {
	        Point p2 = room.getNodes().getPoint(currentNode);
	        if (p2 == null) {
	        	p2 = pos;
	        	currentNode = null;
	        }
	        double angle = Util.angle(pos, p2);
	        double dist = Util.distance(pos, p2);
	        if (dist > room.getNodes().getNodeRadius()) {
	            float dy = (float)Math.sin(angle) * moveSpeed * dt;
	            float dx = (float)Math.cos(angle) * moveSpeed * dt;
	            pos.translate(dx, dy);
	            if (currentNode != null) {
	            	if (!arriveOnce) callbacks.headingToNode(room, this, currentNode);
	            	callbacks.moving(room, this, new Point(dx, dy));
	            }
	            if (moveAtAngle) {
	            	this.angle = (float) Math.toDegrees(angle);
	            }
	        } else if (arriveOnce) {
	        	if (currentNode != null) {
		        	Node node = room.getNodes().getNode(currentNode);
		        	zIndex = node.zIndex;
		        	moveSpeed = node.moveSpeed;
		        	speed = node.animationSpeed;
		        	scale = node.scale;
		        	if (currentPath.size() == 0) {
		        		callbacks.finishedPath(room, this, currentNode);
		        	}
		        	callbacks.arrivedAtNode(room, this, currentNode);
	        	}
	        	arriveOnce = false;
	        }
	        if (!movementPaused && !arriveOnce && currentPath.size() > 0) {
	            currentNode = currentPath.remove();
	            arriveOnce = true;
	        } else if (!movementPaused && !arriveOnce && currentPath.size() == 0) {
	        	callbacks.stopped(room, this, currentNode);
	        }
    	}
    }
    
    public void rename(String newname) {
    	name = newname;
    }
    
    public String getName() {
    	return name;
    }
    
    public void flipY() {
    	this.creature.flipY();
    }
    
    public void flipX() {
    	this.creature.flipX();
    }
    
    public int flippedY() {
    	return this.creature.flippedY();
    }
    
    public int flippedX() {
    	return this.creature.flippedX();
    }
    
    public void update(float dt) {
    	move(dt);
    	creature.setPosition(this.pos);
    	creature.setAngle(this.angle);
    	try {
    		creature.setAnimation(animation);
    	} catch (SpriterException se) {}
    	//creature.setTime(time);
    	creature.speed = this.speed;
    	creature.setScale(scale);
    	creature.update();
    }
    
    public void draw(SpriterDrawer drawer) {
    	drawer.draw(creature);
    }

	static class CreatureComparator implements Comparator<Creature> {

		@Override
		public int compare(Creature creature1, Creature creature2) {
			return creature1.zIndex - creature2.zIndex;
		}
	}
}
