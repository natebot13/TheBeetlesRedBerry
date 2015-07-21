package me.nathanp.beetlesredberry;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.brashmonkey.spriter.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class GameRoom extends ApplicationAdapter{
    private transient final int creatureMoveSpeed = 300;
    private transient final static String savePath = "redberry/sav/rooms/";
    private transient final static String roomsPath = "rooms/";
    private transient final static String backgroundName = "background.jpg";
    private transient final static String defaultName = "default";
    private transient boolean paused = false;
    
    private transient String roomName;
    private transient CreatureFunctions callbacks;
    private transient final Data data;
    private transient final SpriterDrawer drawer;
    private transient Texture background;
    private transient ArrayList<Creature> creatureList;
    
    private HashMap<String, Integer> creatureNumbers = new HashMap<String, Integer>();
    public RoomNodes nodes;
    public HashMap<String, Creature> creatures;
    private String currentCreature;
    
    private FileHandle saveTo;
    private float backgroundX;
    private float backgroundY;
    
    private boolean devMode;
    
    public GameRoom(String roomName, CreatureFunctions callbacks, Data data, SpriterDrawer drawer, String initialCreature, String enterFrom, String enterAnimation) {
    	this(roomName, callbacks, data, drawer, initialCreature, enterFrom, enterAnimation, false);
    }
    
    public GameRoom(String roomName, CreatureFunctions callbacks, Data data, SpriterDrawer drawer, String initialCreature, String enterFrom, String enterAnimation, boolean devMode) {
    	this.data = data;
        this.drawer = drawer;
        this.devMode = devMode;
        currentCreature = initialCreature;
        nextRoom(roomName, callbacks, enterFrom, enterAnimation, false);
    }
    
    public void nextRoom(String name, CreatureFunctions callbacks, String enterFrom, String enterAnimation) {
    	nextRoom(name, callbacks, enterFrom, enterAnimation, true);
    }
    
    private void nextRoom(String name, CreatureFunctions callbacks, String enterFrom, String enterAnimation, boolean update) {
    	if (update) {
    		saveRoom();
    		dispose();
    	}
    	
    	this.callbacks = callbacks;
        roomName = name;
        
        saveTo = Gdx.files.local(savePath + roomName);
        
    	loadRoom(roomName);
    	
    	background = new Texture(roomsPath + roomName + "/" + backgroundName);
        creatureList = new ArrayList<Creature>(creatures.values());
    	if (!devMode) { 
	    	newCreature(currentCreature, enterFrom, enterAnimation);
    	}
        for (Entry<String, Creature> c : creatures.entrySet()) {
        	c.getValue().init(this, callbacks);
        }
    }
    
    /**
     * Updates the whole room. Doesn't update if paused.
     * @param dt the delta time
     */
    private void update(float dt) {
        if (!paused) {
        	sortByZ();
            for (Creature creature : creatureList) {
                creature.update(dt);
            }
        }
    }
    
    /**
     * Draws the room onto the screen.
     * @param drawer the drawer
     */
    private void draw() {
    	drawer.draw(background, backgroundY, backgroundX);
        if (devMode) {
        	nodes.draw(drawer);
        }
        for (Creature creature : creatureList) {
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
    
    public String newCreature(String name, String startingNode, String startingAnimation) {
    	int num;
    	if (creatureNumbers.containsKey(name)) num = creatureNumbers.get(name) + 1;
    	else num = 0;
    	creatureNumbers.put(name, num);
    	Creature c = new Creature(this, callbacks, name, num, startingNode, startingAnimation);
    	creatures.put(name + "_" + num, c);
    	creatureList.add(c);
    	return name + "_" + num;
    }
    
    public void removeCreature(String name) {
    	if (creatures.containsKey(name)) {
    		if (creatureNumbers.get(name) == 0) {
    			creatureNumbers.remove(name);
    		} else {
    			creatureNumbers.put(name, creatureNumbers.get(name) - 1);
    		}
	    	creatureList.remove(creatures.remove(name));
    	}
    }
    
    public void sortByZ() {
    	for (int i = 1; i < creatureList.size(); i += 1) {
    		Creature temp = creatureList.get(i);
    		for (int j = i - 1; j >= 0 && temp.zIndex < creatureList.get(j).zIndex; ) {
    			creatureList.set(j + 1, creatureList.get(j));
    			creatureList.set(j, temp);
    		}
    	}
    }
    
    @Override
    public void resize(int width, int height) {}
    
    @Override
    public void pause() {
        paused = true;
    }
    
    public boolean isPaused() {
    	return paused;
    }
    
    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void create() {}

    public void render(float dt) {
        this.update(dt);
        this.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
    
    private static class RoomState {
        RoomNodes nodes;
        HashMap<String, Creature> creatures;
        HashMap<String, Integer> creatureNumbers;
        
        float backgroundX;
        float backgroundY;
        
        public static void loadFromState(GameRoom room, RoomState state) {
        	if (state == null) {
        		System.out.println("Warning! There was no state for this room found. Initializing empty variables.");
        		room.nodes = new RoomNodes();
        		room.creatures = new HashMap<String, Creature>();
        		room.creatureNumbers = new HashMap<String, Integer>();
        		room.backgroundX = 0;
        		room.backgroundY = 0;
        	} else {
	        	room.nodes = state.nodes;
	        	room.creatures = state.creatures;
	        	room.creatureNumbers = state.creatureNumbers;
	        	room.backgroundX = state.backgroundX;
	        	room.backgroundY = state.backgroundY;
        	}
        	room.nodes.init();
        }
        
        public static void saveToState(GameRoom room, RoomState state) {
        	state.nodes = room.nodes;
        	state.creatures = room.creatures;
        	state.creatureNumbers = room.creatureNumbers;
        	state.backgroundX = room.backgroundX;
        	state.backgroundY = room.backgroundY;
        }
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
        	if (devMode) {
        		System.out.println("Using existing room save");
        	}
        	state = json.fromJson(RoomState.class, readFrom);
        } else {
        	if (devMode) {
        		System.out.println("Using default room loader");
        	}
        	readFrom = Gdx.files.internal(roomsPath + name + "/" + defaultName);
        	if (readFrom.exists()) {
        		state = json.fromJson(RoomState.class, readFrom);
        	} else {
        		if (devMode) {
        			System.out.println("Warning! Couldn't find default loader. You need to create this room!");
        		}
        		state = null;
        	}
        }
        RoomState.loadFromState(this, state);
    }
}
