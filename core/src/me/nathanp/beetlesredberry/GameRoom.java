package me.nathanp.beetlesredberry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GameRoom {
	
    private transient final int creatureMoveSpeed = 300;
    private transient final int clickableCreatureRadius = 20;
    private transient final static String savePath = "redberry/sav/rooms/";
    private transient final static String roomsPath = "rooms/";
    private transient final static String backgroundName = "background.jpg";
//    private transient final static String defaultName = "default";
    private transient boolean paused = false;
    
    public transient String roomName;
    public transient final float roomHeight, roomWidth;
    private transient CreatureFunctions callbacks;
    private transient final Data data;
    private transient final SpriterDrawer drawer;
    private transient Sprite background;
    private transient LinkedList<String> loadQueue = new LinkedList<String>();
    private transient LinkedList<Event> eventQueue = new LinkedList<Event>();
    public transient String currentEntrance, currentAnimation;
    //private transient ArrayList<Creature> creatureList;
    
    private HashMap<String, Integer> creatureNumbers = new HashMap<String, Integer>();
    public RoomNodes nodes;
    public HashMap<String, Creature> creatures;
    private transient String currentCreature, currentCreatureType;
    private transient int currentCreatureNumber;
    
    private transient FileHandle saveTo;
    
    private transient boolean devMode;
    
    public GameRoom(int roomHeight, int roomWidth, CreatureFunctions callbacks, Viewport viewport, Data data, SpriterDrawer drawer, String initialCreature, String enterFrom, String enterAnimation) {
    	this(roomHeight, roomWidth, callbacks, viewport, data, drawer, initialCreature, enterFrom, enterAnimation, false);
    }
    
    public GameRoom(float roomWidth, float roomHeight, CreatureFunctions callbacks, Viewport viewport, Data data, SpriterDrawer drawer, String initialCreature, String enterFrom, String enterAnimation, boolean devMode) {
    	this.roomWidth = roomWidth;
    	this.roomHeight = roomHeight;
    	this.data = data;
        this.drawer = drawer;
        this.devMode = devMode;
        setCurrentCreature(initialCreature, initialCreature);
        if (devMode) {
        	Gdx.input.setInputProcessor(new LevelEditorInput(this, viewport));
        	System.out.println("Now Level Editting...");
        } else {
        	Gdx.input.setInputProcessor(new GameInput(this, viewport));
        }
        nextRoom(callbacks, enterFrom, enterAnimation, false);
    }
    
    public void nextRoom(CreatureFunctions callbacks, String enterFrom, String enterAnimation) {
    	nextRoom(callbacks, enterFrom, enterAnimation, true);
    }
    
    private void nextRoom(CreatureFunctions callbacks, String enterFrom, String enterAnimation, boolean update) {
    	if (update) {
    		dispose();
    	}
    	//setSave(update);
    	this.callbacks = callbacks;
        roomName = callbacks.getRoomName();
        
        saveTo = Gdx.files.local(savePath + roomName);
    	
    	background = new Sprite(new Texture(roomsPath + roomName + "/" + backgroundName));
    	background.setPosition(0, 0);
    	background.setSize(roomWidth, roomHeight);
    	
    	currentEntrance = enterFrom;
    	currentAnimation = enterAnimation;
    	loadQueue.add(roomName);
    }
    
    public void gotoNextRoom(final CreatureFunctions callbacks, final String enterFrom, final String enterAnimation) {
    	eventQueue.add(new Event() {
			@Override
			public void run() {
				nextRoom(callbacks, enterFrom, enterAnimation, true);
			}
    	});
    }
    
    public void reload(String enterFrom, String enterAnimation) {
    	nextRoom(callbacks, enterFrom, enterAnimation, false);
    }
    
    /**
     * Updates the whole room. Doesn't update if paused.
     * @param dt the delta time
     */
    private void update(float dt) {
    	while(!loadQueue.isEmpty()) {
    		loadRoom(loadQueue.remove());
    	}
        if (!paused) {
            for (String creature : creatures.keySet()) {
                creatures.get(creature).update(dt);
            }
        }
        while (!eventQueue.isEmpty()) {
        	eventQueue.remove().run();
        }
    }
    
    /**
     * Draws the room onto the screen.
     * @param drawer the drawer
     */
    private void draw() {
    	background.draw(drawer.batch);
        if (devMode) {
        	nodes.draw(drawer);
        }
        List<Creature> temp = new ArrayList<Creature>(creatures.values());
        Collections.sort(temp, new Creature.CreatureComparator());
        for (Creature creature : temp) {
            creature.draw(drawer);
        }
    }
    
    /**
     * Returns the data object.
     * @return the data object
     */
    public Data getData() {
        return data;
    }
    
    /**
     * Returns the drawer.
     * @return the drawer
     */
    public SpriterDrawer getDrawer() {
        return drawer;
    }
    
    public RoomNodes getNodes() {
    	return nodes;
    }
    
    public int getCreatureMoveSpeed() {
    	return creatureMoveSpeed;
    }
    
    public Creature getCreature(String name) {
    	return creatures.get(name);
    }
    
    public String getCurrentCreature() {
		return currentCreature;
	}

	public void setCurrentCreature(String name, String type) {
		currentCreature = name;
		currentCreatureType = type;
	}
	
	public boolean isCurrentCreature(Creature creature) {
		System.out.println(creature);
		return creature.name.equals(currentCreature);
	}
	
	public void changeCurrentCreature(String currentCreature) {
		if (creatures.containsKey(currentCreature)) {
			creatures.get(getCurrentCreature()).deactivated();
			currentCreatureType = creatures.get(currentCreature).name;
			currentCreatureNumber = creatures.get(currentCreature).number;
			this.currentCreature = currentCreature;
			creatures.get(getCurrentCreature()).activated();
			moveToNextNode();
		} else {
			this.currentCreature = currentCreature;
		}
	}
	
	public void moveToNode(String node) {
		currentEntrance = node;
		creatures.get(currentCreature).gotoNode(node);
	}
	
	public void moveToNextNode() {
		creatures.get(currentCreature).gotoNextNode();
		currentEntrance = creatures.get(currentCreature).currentNode;
	}
	
	public String newCreature(String type, String startingNode, String startingAnimation) {
		int num = 0;
		if (creatureNumbers.containsKey(type)) num = creatureNumbers.get(type) + 1;
    	creatureNumbers.put(type, num);
    	String name = type;
    	if (num > 0) name = type + "_" + num;
    	return newCreature(name, type, startingNode, startingAnimation, num);
	}

	public String newCreature(String name, String type, String startingNode, String startingAnimation, int num) {
    	Creature c = new Creature(this, callbacks, type, num, startingNode, startingAnimation);
    	creatures.put(name, c);
    	return name;
    }
    
    public String nearestCreature(float x, float y) {
    	return nearestCreature(x, y, true);
    }
    
    public String nearestCreature(float x, float y, boolean useRadius) {
    	String creature = null;
    	int min = Integer.MAX_VALUE;
    	if (useRadius) min = clickableCreatureRadius;
    	for (String name : creatures.keySet()) {
    		double dist = Util.distance(creatures.get(name).pos, new Point(x, y));
    		if (dist < min) {
    			min = (int) dist;
    			creature = name;
    		}
    	}
		return creature;
	}
    
    public void renameCreature(String creature, String newname) {
    	Creature c = creatures.remove(creature);
    	creatures.put(newname, c);
    }
    
    public void removeCreature(String name) {
    	if (creatures.get(name) != null) {
    		//String creature = creatures.get(name).name;
        	if (creatures.remove(name) != null) {
//        		if (creatureNumbers.get(creature) == 0) {
//        			creatureNumbers.remove(creature);
//        		} else {
//        			creatureNumbers.put(creature, creatureNumbers.get(creature) - 1);
//        		}
        	}
    	}
    }
    
    public boolean isCreatureAtNode(String creature, String node) {
    	if (!creatures.containsKey(creature)) {
    		return false;
    	}
    	return creatures.get(creature).currentNode.equals(node);
    }
    
    public void pause() {
        paused = true;
    }
    
    public boolean isPaused() {
    	return paused;
    }
    
    public void resume() {
        paused = false;
    }

    public void create() {}

    public void render(float dt) {
        this.update(dt);
        this.draw();
    }

    public void dispose() {
    	removeCreature(getCurrentCreature());
    	saveRoom();
        background.getTexture().dispose();
    }
    
    private static class RoomState {
        RoomNodes nodes;
        HashMap<String, Creature> creatures;
        HashMap<String, Integer> creatureNumbers;
        
        public static void loadFromState(GameRoom room, RoomState state) {
        	if (state == null) {
        		System.out.println("Warning! There was no state for this room found. Initializing empty variables.");
        		room.nodes = new RoomNodes();
        		room.creatures = new HashMap<String, Creature>();
        		room.creatureNumbers = new HashMap<String, Integer>();
        	} else {
	        	room.nodes = state.nodes;
	        	room.creatures = state.creatures;
	        	room.creatureNumbers = state.creatureNumbers;
        	}
        	room.nodes.init();
        }
        
        public static void saveToState(GameRoom room, RoomState state) {
        	state.nodes = room.nodes;
        	state.creatures = room.creatures;
        	state.creatureNumbers = room.creatureNumbers;
        }
    }
    
    public void saveRoom(String path) {
    	RoomState state = new RoomState();
    	RoomState.saveToState(this, state);
        Json json = new Json(JsonWriter.OutputType.json);
        FileHandle file = Gdx.files.local(path + roomName);
        file.writeString(json.toJson(state), false);
    }
    
    /**
     * Saves the state of the room into a local save file in JSON.
     */
    public void saveRoom() {
    	RoomState state = new RoomState();
    	RoomState.saveToState(this, state);
        Json json = new Json(JsonWriter.OutputType.json);
        saveTo.writeString(json.toJson(state), false);
    }
    
    /**
     * Loads a saved game state, or if one doesn't exist, reads a default file.
     */
    private void loadRoom(String name) {
        Json json = new Json();
        FileHandle readFrom = Gdx.files.local(savePath + name);
        RoomState state;
        if (readFrom.exists()) {
        	System.out.println("Using existing room save");
        	state = json.fromJson(RoomState.class, readFrom);
        } else {
        	System.out.println("Using default room loader");
        	readFrom = Gdx.files.internal(roomsPath + name + "/" + name);
        	if (readFrom.exists()) {
        		
        		state = json.fromJson(RoomState.class, readFrom);
        	} else {
        		System.out.println("Warning! Couldn't find default loader. You need to create this room!");
        		state = null;
        	}
        }
        RoomState.loadFromState(this, state);
        nodes.devMode = devMode;
    	if (!devMode) {
    		System.out.println(currentCreature);
    		System.out.println(currentCreatureType);
    		newCreature(currentCreature, currentCreatureType, currentEntrance, currentAnimation, currentCreatureNumber);
    	}
        for (Creature c : creatures.values()) {
        	c.init(this, callbacks);
        }
    }

	public void touchdragged() {
		// STUB Not implementing. Would call creature events.
	}

	public void mouseMoved() {
		// STUB Not implementing. Would call creature events.
		
	}

	public String getCurrentAnimation() {
		return creatures.get(getCurrentCreature()).animation;
	}
}
