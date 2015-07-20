package me.nathanp.beetlesredberry;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.SCMLReader;

import me.nathanp.beetlesredberry.rooms.MenuRoom;

public class BeetlesRedBerry implements ApplicationListener {
    private SpriterLoader loader;
    private SpriterDrawer drawer;
    private FileHandle scmlHandle;
    private SCMLReader reader;
    private Data data;
    
    public static OrthographicCamera camera;
    public static ShapeRenderer renderer;
    SpriteBatch batch;
    
    private static final String scmlFile = "thebeetlesredberry.scml";
    private static final int sizeX = 1600;
    private static final int sizeY = 1000;
    
    private GameRoom room;
    
    @Override
    public void create () {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(sizeX, sizeY);
        camera.setToOrtho(false);
        
        scmlHandle = Gdx.files.internal(scmlFile);
        reader = new SCMLReader(scmlHandle.read());
        data = reader.getData();
        loader = new SpriterLoader(data);
        loader.load(scmlHandle.file());
        renderer = new ShapeRenderer();
        drawer = new SpriterDrawer(loader, batch, renderer);
        
        boolean devMode = true;
        
        room = new GameRoom("Menu", new MenuRoom(), data, drawer, "spirit", "entrance", "idle", devMode);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        renderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        room.render(Gdx.graphics.getDeltaTime());
        batch.end();
    }
    
    @Override
    public void pause() {
        room.pause();
    }
    
    @Override
    public void resume() {
        room.resume();
    }
    
    @Override
    public void dispose() {
        room.dispose();
    }
    
    @Override
    public void resize(int width, int height) {
        
    }
}
