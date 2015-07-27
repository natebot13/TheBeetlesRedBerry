package me.nathanp.beetlesredberry;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.SCMLReader;

import me.nathanp.beetlesredberry.rooms.*;

public class BeetlesRedBerry implements ApplicationListener {
    private SpriterLoader loader;
    private SpriterDrawer drawer;
    private FileHandle scmlHandle;
    private SCMLReader reader;
    private Data data;
    
    private static OrthographicCamera camera;
    private static Viewport viewport;
    public static ShapeRenderer renderer;
    SpriteBatch batch;
    
    private static final String scmlFile = "thebeetlesredberry.scml";
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;
    private static final Vector2 VIEWPORT = new Vector2(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    
    private GameRoom room;
    
    boolean debug;
    
    public BeetlesRedBerry() {
    	this(false);
    }
    
    public BeetlesRedBerry(boolean debug) {
    	this.debug = debug;
	}
    
    @Override
    public void create () {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        viewport = new ScalingViewport(Scaling.fillX, WORLD_WIDTH, WORLD_HEIGHT * (w / h), camera);
        viewport.apply();
        
        batch = new SpriteBatch();
        
        scmlHandle = Gdx.files.internal(scmlFile);
        reader = new SCMLReader(scmlHandle.read());
        data = reader.getData();
        loader = new SpriterLoader(data);
        loader.load(scmlHandle.file());
        renderer = new ShapeRenderer();
        drawer = new SpriterDrawer(loader, batch, renderer);
        
        if (debug) {
        	room = new GameRoom(WORLD_WIDTH, WORLD_HEIGHT, new DebugRoom("lilypad"), viewport, data, drawer, "ant", "rightentrance", "idle", debug);
        } else {
        	CurrentRoomData roomdata = loadRoomData();
        	room = new GameRoom(WORLD_WIDTH, WORLD_HEIGHT, getCreatureFunction(roomdata.roomname), viewport, data, drawer, roomdata.creature, roomdata.entrance, roomdata.animation, debug);
        }
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.set(VIEWPORT.x, VIEWPORT.y, 0);
        camera.update();

        renderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        
        if (room != null) {
	        renderer.begin(ShapeType.Filled);
	        batch.begin();
	        room.render(Gdx.graphics.getDeltaTime());
	        batch.end();
	        renderer.end();
        }
    }
    
    @Override
    public void pause() {
    	if (room != null) {
    		room.pause();
    	}
    }
    
    @Override
    public void resume() {
    	if (room != null) {
    		room.resume();
    	}
    }
    
    @Override
    public void dispose() {
    	CurrentRoomData roomdata = new CurrentRoomData(room.roomName, room.currentEntrance, room.getCurrentCreature(), room.currentAnimation);
    	roomdata.save();
        room.dispose();
        batch.dispose();
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
    }
    
    private CreatureFunctions getCreatureFunction(String name) {
    	if (name.equals("menu")) {
    		return new MenuRoom();
    	} else if (name.equals("flyup")) {
    		return new FlyRoom();
    	} else if (name.equals("branch")) {
    		return new BranchRoom();
    	} else if (name.equals("grass")) {
    		return new GrassRoom();
    	} else if (name.equals("flipflop")) {
    		return new FlipFlopRoom();
    	}
    	return new MenuRoom();
    }
    
    public CurrentRoomData loadRoomData() {
		FileHandle readfile = Gdx.files.local("redberry/sav/currentroom");
		if (readfile.exists()) {
			Json json = new Json();
			return json.fromJson(CurrentRoomData.class, readfile);
		} else {
			return new CurrentRoomData();
		}
	}
    
    private static class CurrentRoomData {
    	String roomname = "menu";
    	String entrance = "rightentrance";
    	String creature = "ant";
    	String animation = "idle";
    	
    	public CurrentRoomData() {}
    	
    	public CurrentRoomData(String name, String entrance, String creature, String anim) {
    		roomname = name;
    		this.entrance = entrance;
    		this.creature = creature;
    		animation = anim;
    	}
    	
    	public void save() {
    		Json json = new Json(JsonWriter.OutputType.json);
    		FileHandle file = Gdx.files.local("redberry/sav/currentroom");
    		file.writeString(json.toJson(this), false);
    	}
    }
}
