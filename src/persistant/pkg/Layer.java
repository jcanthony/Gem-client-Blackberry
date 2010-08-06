package persistant.pkg;
import java.util.Vector;

import net.rim.device.api.util.Persistable;


public class Layer implements Persistable {
	private String name;
	private Vector points;
	private Vector layers;
	private Vector routes;
	private int UID;
	
	
	public Layer(String name){
		super();
		this.name = name;
		this.points = new Vector();
		this.layers = new Vector();
		this.routes = new Vector();
		this.UID = 0;
	}
	
	public String getName() {
		return name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public void addLayer(Layer l){
		layers.addElement(l);
	}
	
	
	public void removeLayer(int index){
		layers.removeElementAt(index);
	}
	
	
	public Vector getLayers() {
		return layers;
	}
	
	
	public void setLayers(Vector layers) {
		this.layers = layers;
	}
	
	
	public void addPoint(Points pnt){
		points.addElement(pnt);
	}
	
	
	public void removesPoint(int index){
		points.removeElementAt(index);
	}
	
	
	public Vector getsPoints(){
		return points;
	}
	
	
	public void setPoints(Vector points){
		this.points = points;
	}
	
	
	public void addRoute(Route r){
		routes.addElement(r);
	}
	
	
	public void removeRoute(int ind){
		routes.removeElementAt(ind);
	}
	
	
	public Vector getRoutes(){
		return this.routes;
	}
	
	
	public void setRoutes(Vector rtes){
		this.routes = rtes;
	}
	
	public void setUID(int u){
		this.UID = u;
	}
	
	public int getUID(){
		return this.UID;
	}

	
	
}
