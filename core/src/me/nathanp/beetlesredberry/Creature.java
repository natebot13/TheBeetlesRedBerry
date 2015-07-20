package me.nathanp.beetlesredberry;

import java.util.LinkedList;

import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Point;
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
    
    public Creature(GameRoom room, CreatureFunctions callbacks, String name, int number, String startingNode, String animation) {
    	this.callbacks = callbacks;
    	this.room  = room;
    	this.name = name;
    	this.number = number;
    	init(room, callbacks);
    	pos = room.getNodes().getPoint(currentNode);
    	angle = creature.getAngle();
    	this.animation = animation;
    	time = creature.getTime();
    	speed = creature.speed;
    	moveSpeed = room.getCreatureMoveSpeed();
    	currentNode = startingNode;
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
     * Tells the player to attempt to navigate to the node.
     * Appends the computed path to the previous path so the player will pass through all points.
     * @param name the name of the node
     */
    public void gotoNode(String name) {
        for (String n : room.getNodes().getPath(currentNode, name)) {
            currentPath.add(n);
        }
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
            	angle = (float) Math.toDegrees(angle);
            }
        } else if (arriveOnce) {
        	zIndex = room.getNodes().getNode(currentNode).zIndex;
            callbacks.arrivedAtNode(room, this, currentNode);
            arriveOnce = false;
        }
        if (!movementPaused && !arriveOnce && currentPath.size() > 0) {
            currentNode = currentPath.remove();
            arriveOnce = true;
        }
    }
    
    public void update(float dt) {
    	move(dt);
    	creature.setPosition(pos);
    	creature.setAngle(angle);
    	creature.setAnimation(animation);
    	creature.setTime(time);
    	creature.speed = speed;
    	creature.update();
    }
    
    public void draw(SpriterDrawer drawer) {
    	drawer.draw(creature);
    }
}
