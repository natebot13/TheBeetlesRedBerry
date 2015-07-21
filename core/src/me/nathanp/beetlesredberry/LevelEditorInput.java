package me.nathanp.beetlesredberry;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.brashmonkey.spriter.Point;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.TextInputListener;

public class LevelEditorInput implements InputProcessor {
	GameRoom room;
	Camera cam;
	Vector3 input = new Vector3();
	boolean creatureMode = false;
	String selectedNode;
	boolean nodeMode = true;
	String mouseDownNode = null;
	String mouseUpNode = null;
	boolean clickable = true;
	boolean disabled = false;
	int zIndex = 0;
	int moveSpeed = 300;
	int animationSpeed = 15;
	
	public LevelEditorInput(GameRoom room, Camera cam) {
		this.cam = cam;
		this.room = room;
	}
	
	public void updateInputPos(int x, int y) {
		input.x = x;
		input.y = y;
		cam.unproject(input);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (character == ',') {
			zIndex -= 1;
			System.out.println("zIndex: " + zIndex);
			return true;
		} else if (character == '.') {
			zIndex += 1;
			System.out.println("zIndex: " + zIndex);
			return true;
		} else if (character == 'p') {
			if (room.isPaused()) {
				room.resume();
			} else {
				room.pause();
			}
			return true;
		} else if (character == 'r') { 
			Gdx.input.getTextInput(new NodeRenameInputListener(), "Rename Node", selectedNode, selectedNode);
			return true;
		} else if (character == 'c') {
			Gdx.input.getTextInput(new CreatureCreatorInputListener(), "New Creature", "spirit:idle", "name:animation");
			return true;
		} else if (character == 's') {
			room.saveRoom();
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		updateInputPos(screenX, screenY);
		if (button == Input.Buttons.LEFT) {
			mouseDownNode = room.nodes.getNearest(new Point(input.x, input.y), true, false, false);
			return true;
		}
		if (button == Input.Buttons.RIGHT) {
			String node = room.nodes.getNearest(new Point(input.x, input.y), true, false, false);
			if (node != null) {
				room.nodes.removeNode(node);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		updateInputPos(screenX, screenY);
		if (button == Input.Buttons.LEFT) {
			if (mouseDownNode != null) {
				mouseUpNode = room.nodes.getNearest(new Point(input.x, input.y), true, false, false);
				if (mouseUpNode != null) {
					if (mouseDownNode.equals(mouseUpNode)) {
						selectedNode = mouseDownNode;
						System.out.println("Selected node: " + selectedNode);
						return true;
					} else {
						room.nodes.setChild(mouseDownNode, mouseUpNode);
						System.out.println("Linked " + mouseDownNode + " to " + mouseUpNode);
						return true;
					}
				} else {
					String newnode = room.nodes.newNode(clickable, disabled, "newnode", (int) input.x, (int) input.y, zIndex, moveSpeed, animationSpeed);
					System.out.println("Created node: " + newnode);
					room.nodes.setChild(mouseDownNode, newnode);
					System.out.println(mouseDownNode + "--->" + newnode);
					return true;
				}
			} else {
				String newnode = room.nodes.newNode(clickable, disabled, "newnode", (int) input.x, (int) input.y, zIndex, moveSpeed, animationSpeed);
				System.out.println("Created node: " + newnode);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		updateInputPos(screenX, screenY);
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		updateInputPos(screenX, screenY);
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if (nodeMode) {
			moveSpeed += amount * 10;
			System.out.println("Move speed: " + moveSpeed);
			return true;
		}
		return false;
	}
	
	public class NodeRenameInputListener implements TextInputListener {
		
		@Override
		public void input (String text) {
			room.nodes.renameNode(selectedNode, text);
			System.out.println("Node " + selectedNode + " renamed to " + text);
			selectedNode = text;
		}
		
		@Override
		public void canceled () {}
	}
	
	public class CreatureCreatorInputListener implements TextInputListener {
		
		@Override
		public void input (String text) {
			String[] creature = text.split(":");
			System.out.println("New creature created on " + selectedNode + " named " + creature[0] + " and animationg with " + creature[1]);
			String created = room.newCreature(creature[0], selectedNode, creature[1]);
			System.out.println("New creature created on " + selectedNode + " named " + created + " and animationg with " + creature[1]);
		}
		
		@Override
		public void canceled () {}
	}
}
