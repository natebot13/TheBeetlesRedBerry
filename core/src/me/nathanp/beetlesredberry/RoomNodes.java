package me.nathanp.beetlesredberry;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.brashmonkey.spriter.Point;

public class RoomNodes {
	private transient final int nodeRadius = 5;
	private Point originalSize;
	public int clickableRadius;
    public HashMap<String, Node> nodes;
    public HashSet<String> clickableNodes;
    private final HashSet<String> disabledNodes = new HashSet<String>();
    
    private transient final HashMap<String, Integer> computed;
    private transient int setnum = 0;
    private transient HashSet<String> marked;
    private transient LinkedList<String> queue;
    private transient HashMap<String, String> edgeto;
    
    public RoomNodes() {
        this(10);
    }
    
    public RoomNodes(int clickableRadius) {
    	originalSize = new Point();
    	this.clickableRadius = clickableRadius;
        nodes = new HashMap<String, Node>();
        clickableNodes = new HashSet<String>(nodes.keySet());
        
        computed = new HashMap<String, Integer>();
    }
    
    public void init() {
    	for (Node node : nodes.values()) {
    		node.init(this);
    	}
    }
    
    public void rescale(int x, int y) {
    	for (Entry<String, Node> n : nodes.entrySet()) {
    		n.getValue().pos.scale(x / originalSize.x, y / originalSize.y);
    	}
    }
    
    public boolean isConnected(String name1, String name2) {
        return isConnected(name1, name2, "");
    }
    
    private boolean isConnected(String from, String to, String cameFrom) {
        if (from.equals(to)) return true;
        if (computed.containsKey(from)) {
            if (computed.containsKey(to)) {
                return computed.get(from) == computed.get(to);
            }
        } else {
            computed.put(from, setnum);
        }
        for (String name : nodes.get(from).children) {
            computed.put(name, setnum);
            if (to.equals(name)) return true;
            if (!cameFrom.equals(name)) {
                return isConnected(name, to, from);
            }
        }
        setnum += 1;
        return false;
    }
    
    public List<String> getPath(String from, String to){
        LinkedList<String> ret = new LinkedList<String>();
        if (!disabledNodes.contains(to) && isConnected(from ,to)) {
            marked = new HashSet<String>();
            edgeto = new HashMap<String, String>();
            queue = new LinkedList<String>();
            computePath(from, to);
            String curr = to;
            while (!curr.equals(from)) {
                ret.addFirst(curr);
                curr = edgeto.get(curr);
            }
        }
        return ret;
    }
    
    public String getNextNode(String from) {
    	if (nodes.containsKey(from)) {
    		for (String node : nodes.get(from).children) {
    			return node;
    		}
    	}
    	return null;
    }
    
    public String getRandomChild(String from) {
    	int r = (int) (Math.random() * nodes.get(from).children.size());
    	int i = 0;
		for (String child : nodes.get(from).children) {
			if (i == r) {
				return child;
			} i += 1;
		}
		return null;
    }
    
    private void computePath(String from, String to) {
        marked.add(from);
        
        if (from.equals(to)) {
            return;
        }
        
        for (String child : nodes.get(from).children) {
            if (!marked.contains(child) && !disabledNodes.contains(child)) {
                edgeto.put(child, from);
                queue.add(child);
            }
        }
        computePath(queue.remove(), to);
    }
    
    public Node getNode(String name) {
    	return nodes.get(name);
    }

    public Point getPoint(String name) {
        return new Point(nodes.get(name).pos);
    }
    
    public int getNodeRadius() {
    	return nodeRadius;
    }
    
    public String getNearest(Point pos) {
        return getNearest(pos, true, true, true);
    }
    
    public String getNearest(Point pos, boolean useRadius) {
        return getNearest(pos, useRadius, true, true);
    }
    
    public String getNearest(Point pos, boolean useRadius, boolean onlyClickables) {
        return getNearest(pos, useRadius, onlyClickables, true);
    }
    
    public String getNearest(Point pos, boolean useRadius, boolean onlyClickableNodes, boolean ignoreDisabledNodes) {
        int min;
        if (useRadius) min = clickableRadius;
        else min = Integer.MAX_VALUE;
        String nearest = null;
        for (Entry<String, Node> entry : nodes.entrySet()) {
        	if (ignoreDisabledNodes && disabledNodes.contains(entry.getKey())) continue;
            if (onlyClickableNodes && !clickableNodes.contains(entry.getKey())) continue;
            int dist = (int)distance(pos, entry.getValue().pos);
            if (dist < min) {
                min = dist;
                nearest  = entry.getKey();
            }
        }
        return nearest;
    }
    
    static double distance(Point p1, Point p2) {
        double xDist = p2.x - p1.x;
        double yDist = p2.y - p1.y;
        return Math.sqrt((xDist * xDist) + (yDist * yDist));
    }
    
    static double angle(Point p1, Point p2) {
        return Math.atan2(p2.y - p1.y, p2.x - p1.x);
    }
    
    public String newNode(boolean clickable, boolean disabled, String name, int x, int y, int z, int moveSpeed, int animationSpeed) {
    	if (nodes.containsKey(name)) {
	    	int id = 1;
	    	while (nodes.containsKey(name + "_" + id)) {
	    		id += 1;
	    	}
	    	name += "_" + id;
    	}
    	this.nodes.put(name, new Node(this, name, x, y, z, moveSpeed, animationSpeed));
    	if (clickable) {
    		makeClickable(name);
    	}
    	if (disabled) {
    		disableNode(name);
    	}
    	return name;
    }
    
    public void removeNode(String name) {
    	nodes.get(name).removeSelf();
    	disabledNodes.remove(name);
		clickableNodes.remove(name);
    }
    
    public void renameNode(String orig, String newname) {
    	nodes.get(orig).renameSelf(newname);
    	if (clickableNodes.contains(orig)) {
    		removeClickable(orig);
    		makeClickable(newname);
    	}
    	if (disabledNodes.contains(orig)) {
    		enableNode(orig);
    		disableNode(newname);
    	}
    }
    
    public void setChild(String parent, String child) {
    	nodes.get(parent).addChild(child);
    }
    
    public void makeClickable(String name) {
    	clickableNodes.add(name);
    }
    
    public void removeClickable(String name) {
    	clickableNodes.remove(name);
    }
    
    public void disableNodes(Collection<String> disNodes) {
        for (String node : disNodes) {
            disabledNodes.add(node);
        }
    }
    
    public void enableNodes(Collection<String> enNodes) {
        for (String node : enNodes) {
            disabledNodes.remove(node);
        }
    }
    
    public void disableNode(String node) {
        disabledNodes.add(node);
    }
    
    public void enableNode(String node) {
        disabledNodes.remove(node);
    }
    
    public Collection<String> getDisabledNodes() {
        return disabledNodes;
    }
    
    public int size() {
    	return nodes.size();
    }
    
    public void draw(SpriterDrawer drawer) {
    	for (Node node : nodes.values()) {
    		node.draw(drawer);
    	}
    }
    
    @Override
    public String toString() {
        String ret = "{";
        for (String n : nodes.keySet()) {
            ret += n + ": " + nodes.get(n) + ", ";
        }
        ret += "}\nDisabledNodes: {";
        for (String d : disabledNodes) {
            ret += d + ", ";
        }
        ret += "}\n";
        return ret;
    }
    
    public static class Node {
    	private transient RoomNodes roomnodes;
    	public String name;
    	public Point pos;
    	public int zIndex;
    	public int moveSpeed;
    	public int animationSpeed = 15;
    	public HashSet<String> children;
    	
    	public Node() {}
    	
    	public Node(RoomNodes roomnodes, String name, int x, int y, int moveSpeed, int animationSpeed) {
    		this(roomnodes, name, new Point(x, y), moveSpeed, animationSpeed);
    	}
    	
    	public Node(RoomNodes roomnodes, String name, int x, int y, int z, int moveSpeed, int animationSpeed) {
    		this(roomnodes, name, new Point(x, y), z, moveSpeed, animationSpeed);
    	}
    	
    	public Node(RoomNodes roomnodes, String name, Point pos, int speed, int animationSpeed) {
    		this(roomnodes, name, pos, 0, speed, animationSpeed, new HashSet<String>());
    	}
    	
    	public Node(RoomNodes roomnodes, String name, Point pos, int z, int speed, int animationSpeed) {
    		this(roomnodes, name, pos, z, speed, animationSpeed, new HashSet<String>());
    	}
    	
    	public Node(RoomNodes roomnodes, String name, Point pos, int z, int moveSpeed, int animationSpeed, HashSet<String> children) {
    		this.roomnodes = roomnodes;
    		this.name = name;
    		this.pos = pos;
    		this.zIndex = z;
    		this.moveSpeed = moveSpeed;
    		this.animationSpeed = animationSpeed;
    		this.children = children;
    	}
    	
    	public void init(RoomNodes roomnodes) {
    		this.roomnodes = roomnodes;
    	}
    	
    	public void addChild(String child) {
    		this.children.add(child);
    	}
    	
    	public void addChildren(Collection<String> children) {
    		for (String child : children) {
    			addChild(child);
    		}
    	}
    	
    	public void removeChild(String child) {
    		this.children.remove(child);
    	}
    	
    	public void renameSelf(String newname) {
    		for (Node node : roomnodes.nodes.values()) {
    			node.renameChild(name, newname);
    		}
    		roomnodes.nodes.put(newname, roomnodes.nodes.remove(name));
    		name = newname;
    	}
    	
    	public void renameChild(String from, String to) {
    		if (children.remove(from)) {
    			children.add(to);
    		}
    	}
    	
    	public void removeSelf() {
    		for (Node node : roomnodes.nodes.values()) {
    			node.removeChild(name);
    		}
    		roomnodes.nodes.remove(name);
    	}
    	
    	public void draw(SpriterDrawer drawer) {
    		drawer.renderer.setColor(1, 0, 0, 1);
    		drawer.renderer.set(ShapeType.Filled);
    		drawer.circle(pos.x, pos.y, roomnodes.clickableRadius);
    		for (String child : children) {
    			Node childNode = roomnodes.nodes.get(child);
    			drawer.line(pos.x, pos.y, childNode.pos.x, childNode.pos.y);
    		}
    	}
    	
    	@Override
    	public String toString() {
    		return "{" + name + ":" + pos + ",z:" + zIndex + ",moveSpeed:" + moveSpeed + ",animationSpeed:" + animationSpeed + ",children:" + children + "}";
    	}
    }
}
