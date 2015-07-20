package me.nathanp.beetlesredberry;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

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
        this(50);
    }
    
    public RoomNodes(int clickableRadius) {
    	originalSize = new Point();
    	this.clickableRadius = clickableRadius;
        nodes = new HashMap<String, Node>();
        clickableNodes = new HashSet<String>(nodes.keySet());
        
        computed = new HashMap<String, Integer>();
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
        return nodes.get(name).pos;
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
    
    public String getNearest(Point pos, boolean useRadius, boolean onlyClickables, boolean ignoreDisabled) {
        int min;
        if (useRadius) min = clickableRadius;
        else min = Integer.MAX_VALUE;
        String nearest = null;
        for (Entry<String, Node> entry : nodes.entrySet()) {
            if (onlyClickables && !clickableNodes.contains(entry.getKey())) continue;
            if (ignoreDisabled && disabledNodes.contains(entry.getKey())) continue;
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
    
    public class Node {
    	public String name;
    	public Point pos;
    	public int zIndex;
    	public int speed;
    	public HashSet<String> children;
    	
    	public Node(String name, int x, int y, int speed) {
    		this(name, new Point(x, y), speed);
    	}
    	
    	public Node(String name, Point pos, int speed) {
    		this(name, pos, 0, speed, new HashSet<String>());
    	}
    	
    	public Node(String name, Point pos, int z, int speed, HashSet<String> children) {
    		this.name = name;
    		this.pos = pos;
    		this.zIndex = z;
    		this.speed = speed;
    		this.children = children;
    	}
    	
    	public void addChild(String child) {
    		this.children.add(child);
    	}
    	
    	public void addChildren(Collection<String> children) {
    		for (String child : children) {
    			addChild(child);
    		}
    	}
    	
    	@Override
    	public String toString() {
    		return "{" + name + ":" + pos + ",z:" + zIndex + ",speed:" + speed + "," + children + "}";
    	}
    }
}
