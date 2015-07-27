package me.nathanp.beetlesredberry;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brashmonkey.spriter.Point;

public class GameInput implements InputProcessor {
	GameRoom room;
	Viewport viewport;
	Vector3 input = new Vector3();
	
	public GameInput(GameRoom room, Viewport viewport) {
		this.room = room;
		this.viewport = viewport;
	}
	
	public void unprojectInputPos(int x, int y) {
		input.x = x;
		input.y = y;
		viewport.getCamera().unproject(input, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
	}
	
	@Override
	public boolean keyDown(int keycode) {
//		if (Gdx.input.isKeyPressed(Input.Keys.L) && Gdx.input.isKeyPressed(Input.Keys.V) &&
//			Gdx.input.isKeyPressed(Input.Keys.E) && Gdx.input.isKeyPressed(Input.Keys.D) &&
//			keycode == Input.Keys.ENTER) {
//			room.stopPlaytest();
//		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		unprojectInputPos(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		unprojectInputPos(screenX, screenY);
		String node = room.getNodes().getNearest(new Point(input.x, input.y));
		if (node != null) {
			room.moveToNode(node);
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		unprojectInputPos(screenX, screenY);
		room.touchdragged();
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		unprojectInputPos(screenX, screenY);
		room.mouseMoved();
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
