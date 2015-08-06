package me.nathanp.beetlesredberry;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brashmonkey.spriter.Point;

import me.nathanp.beetlesredberry.RoomNodes.Node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.TextInputListener;

public class LevelEditorInput implements InputProcessor {
	GameRoom room;
	Viewport viewport;
	Vector3 input = new Vector3();
	boolean creatureMode = false;
	String selectedNode;
	String selectedCreature;
	boolean nodeMode = true;
	String mouseDownNode = null;
	String mouseUpNode = null;
	boolean clickable = true;
	boolean disabled = false;
	int zIndex = 0;
	int moveSpeed = 300;
	int animationSpeed = 15;
	String defaultCreature = "spirit";
	String defaultEntrance = "entrance";
	String defaultAnimation = "idle";
	
	public LevelEditorInput(GameRoom room, Viewport viewport) {
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
		if (keycode == Input.Keys.ENTER) {
//			if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
//				Gdx.input.getTextInput(new PlaytestRoomInputListener(), "Are you sure you want to playtest? To return to editting, press: L+V+E+D+Enter", "", "yes/no");
//				System.out.println("Changing to playtest mode...");
//				return true;
//			} else {
				Gdx.input.getTextInput(new ReloadRoomInputListener(), "Are you sure you want to reload the room?", "", "yes/no");
				return true;
//			}
		}
		if (selectedNode != null) {
			if (keycode == Input.Keys.DOWN) {
				room.getNodes().getNode(selectedNode).pos.y -= 10;
				return true;
			} else if (keycode == Input.Keys.UP) {
				room.getNodes().getNode(selectedNode).pos.y += 10;
				return true;
			} else if (keycode == Input.Keys.LEFT) {
				room.getNodes().getNode(selectedNode).pos.x -= 10;
				return true;
			} else if (keycode == Input.Keys.RIGHT) {
				room.getNodes().getNode(selectedNode).pos.x += 10;
				return true;
			} else if (keycode == Input.Keys.PAGE_UP) {
				room.getNodes().getNode(selectedNode).clickableRadius += 5;
				return true;
			} else if (keycode == Input.Keys.PAGE_DOWN) {
				room.getNodes().getNode(selectedNode).clickableRadius -= 5;
				return true;
			} 
		}
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
			if (selectedNode != null) {
				Node n = room.getNodes().getNode(selectedNode);
				n.zIndex -= 1;
				System.out.println(n.name + " zIndex: " + n.zIndex);
			} else {
				zIndex -= 1;
				System.out.println("zIndex: " + zIndex);
			}
			return true;
		} else if (character == '.') {
			if (selectedNode != null) {
				Node n = room.getNodes().getNode(selectedNode);
				n.zIndex += 1;
				System.out.println(n.name + " zIndex: " + n.zIndex);
			} else {
				zIndex += 1;
				System.out.println("zIndex: " + zIndex);
			}
			return true;
		} else if (character == 'z') {
			if (selectedNode != null) {
				Node n = room.getNodes().getNode(selectedNode);
				n.scale -= 0.05f;
				System.out.println(n.name + " scale: " + n.scale);
			}
			return true;
		} else if (character == 'x') {
			if (selectedNode != null) {
				Node n = room.getNodes().getNode(selectedNode);
				n.scale += 0.05f;
				System.out.println(n.name + " scale: " + n.scale);
			}
			return true;
		} else if (character == 'p') {
			if (room.isPaused()) {
				room.pause();
			} else {
				room.resume();
			}
			return true;
		} else if (character == 'u') {
			selectedNode = null;
			selectedCreature = null;
			System.out.println("Unselected all");
			return true;
		} else if (character == 'r') {
			if (selectedNode != null) {
				Gdx.input.getTextInput(new NodeRenameInputListener(), "Rename Node", "", selectedNode);
				return true;
			} else if (selectedCreature != null) {
				Gdx.input.getTextInput(new CreatureRenameInputListener(), "Rename Creature", "", selectedCreature);
				return true;
			}
		} else if (character == 'c') {
			Gdx.input.getTextInput(new CreatureCreatorInputListener(), "New Creature", "spirit:idle", "");
			return true;
		} else if (character == 'd') {
			if (selectedCreature != null) {
				room.removeCreature(selectedCreature);
				return true;
			} else if (selectedNode != null) {
				room.getNodes().disableNode(selectedNode);
				return true;
			}
		} else if (character == 'e') {
			if (selectedNode != null) {
				room.getNodes().enableNode(selectedNode);
				return true;
			}
		} else if (character == 'b') {
			if (selectedNode != null) {
				room.getNodes().makeClickable(selectedNode);
				return true;
			}
		} else if (character == 'n') {
			if (selectedNode != null) {
				room.getNodes().removeClickable(selectedNode);
				return true;
			}
		} else if (character == 's') {
			room.saveRoom("editor/");
			System.out.println("Room Saved!");
			return true;
		} else if (character == '1') {
			for (Node n : room.getNodes().nodes.values()) {
				System.out.println("Scaling node: " + n.name);
				n.scale = 1f;
				System.out.println("New scale: " + n.scale);
			}
		} else if (character == '=') {
			for (Node n : room.getNodes().nodes.values()) {
				if (n.clickableRadius < 2) {
					n.clickableRadius = 5;
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		unprojectInputPos(screenX, screenY);
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
		unprojectInputPos(screenX, screenY);
		if (button == Input.Buttons.LEFT) {
			mouseUpNode = room.nodes.getNearest(new Point(input.x, input.y), true, false, false);
			if (mouseDownNode != null) {
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
				selectedCreature = room.nearestCreature(input.x, input.y);
				if (selectedCreature != null) {
					System.out.println("Selected creature: " + selectedCreature);
				} else {
					String newnode = room.nodes.newNode(clickable, disabled, "newnode", (int) input.x, (int) input.y, zIndex, moveSpeed, animationSpeed);
					System.out.println("Created node: " + newnode);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		unprojectInputPos(screenX, screenY);
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		unprojectInputPos(screenX, screenY);
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if (selectedNode != null) {
			Node n = room.getNodes().getNode(selectedNode);
			n.moveSpeed += amount * 10;
			System.out.println(n.name + " move speed: " + n.moveSpeed);
			return true;
		} else {
			moveSpeed += amount * 10;
			System.out.println("Move speed: " + moveSpeed);
			return true;
		}
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
	
	public class CreatureRenameInputListener implements TextInputListener {

		@Override
		public void input(String text) {
			room.renameCreature(selectedCreature, text);
			System.out.println("Creature renamed to: " + text);
		}

		@Override
		public void canceled() {}
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
	
	public class ReloadRoomInputListener implements TextInputListener {

		@Override
		public void input(String text) {
			if (text.equals("yes")) {
				room.reload(null, null);
			}
		}

		@Override
		public void canceled() {}
	}
}
