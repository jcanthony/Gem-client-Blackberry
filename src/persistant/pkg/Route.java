package persistant.pkg;

import java.util.Vector;

import net.rim.device.api.util.Persistable;

public class Route implements Persistable {
	Vector Points;
	String name;
	
	Route(){
		Points = new Vector();
		name = new String();
	}
	
	public void setName(String n){
		name = n;
	}
	
	public String getName(){
		return name;
	}
	
	public void addPoint(Points p){
		Points.addElement(p);
	}
	
	public void clearRoute(){
		Points.removeAllElements();
	}
	
	
}
