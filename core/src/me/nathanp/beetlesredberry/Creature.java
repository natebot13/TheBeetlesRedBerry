package me.nathanp.beetlesredberry;

import java.util.LinkedList;

import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.RoomNodes.Node;

import com.brashmonkey.spriter.Mainline.Key;

public class Creature {
	private transient GameRoom room;
	private transient Player creature;
	private transient CreatureFunctions callbacks;
	
	public String name;
	public int number;
    public Point pos;
    public float angle;
    public String animation;
    public int time;
    public int speed;
    public int moveSpeed;
    public String currentNode;
    public int zIndex = 0;
    public boolean moveAtAngle = true;
    
    private transient boolean movementPaused = false;
    private transient boolean arriveOnce = true;
    private transient LinkedList<String> currentPath = new LinkedList<String>();
    
    public Creature() {}
    
    public Creature(GameRoom room, CreatureFunctions callbacks, String name, int number, String startingNode, String animation) {
    	this.callbacks = callbacks;
    	this.room  = room;
    	this.name = name;
    	this.number = number;
    	init(room, callbacks);
    	currentNode = startingNode;
    	pos = new Point(room.getNodes().getPoint(currentNode));
    	angle = creature.getAngle();
    	this.animation = animation;
    	time = creature.getTime();
    	speed = creature.speed;
    	moveSpeed = room.getCreatureMoveSpeed();
    	setCallback();
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
    
    public void init(GameRoom room, CreatureFunctions callbacks) {
    	creature = new Player(room.getData().getEntity(name));
    	this.room = room;
    	this.callbacks = callbacks;
    	setCallback();
    }
    
    /**
     * Moves the player according to the path.
     * @param dt the delta time
     */
    private void move(float dt) {
        Point p2 = room.getNodes().getPoint(currentNode);
        double angle = RoomNodes.angle(pos, p2);
        double dist = RoomNodes.distance(pos, p2);
        if (dist > room.getNodes().getNodeRadius()) {
        	if (!arriveOnce) callbacks.headingToNode(room, this, currentNode);
            float dy = (float)Math.sin(angle) * moveSpeed * dt;
            float dx = (float)Math.cos(angle) * moveSpeed * dt;
            pos.translate(dx, dy);
            callbacks.moving(room, this, new Point(dx, dy));
            if (moveAtAngle) {
            	this.angle = (float) Math.toDegrees(angle);
            }
        } else if (arriveOnce) {
        	Node node = room.getNodes().getNode(currentNode);
        	zIndex = node.zIndex;
        	moveSpeed = node.moveSpeed;
        	speed = node.animationSpeed;
        	if (currentPath.size() == 0) {
        		callbacks.finishedPath(room, this, currentNode);
        	}
            callbacks.arrivedAtNode(room, this, currentNode);
            arriveOnce = false;
        }
        if (!movementPaused && !arriveOnce && currentPath.size() > 0) {
            currentNode = currentPath.remove();
            arriveOnce = true;
        } else if (!movementPaused && !arriveOnce && currentPath.size() == 0) {
        	callbacks.stopped(room, this, currentNode);
        }
    }
    
    public void update(float dt) {
    	move(dt);
    	creature.setPosition(pos);
    	creature.setAngle(angle);
    	creature.setAnimation(animation);
    	//creature.setTime(time);
    	creature.speed = speed;
    	creature.update();
    }
    
    public void draw(SpriterDrawer drawer) {
    	drawer.draw(creature);
    }
}
