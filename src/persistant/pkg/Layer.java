package persistant.pkg;
import java.util.Vector;

import net.rim.device.api.util.Persistable;


public class Layer implements Persistable {
	private String name;
	private Vector points;
	private Vector layers;
	private long PLayer = -1;
	private long PProj = -1;
	private int V_Loc = -1;
	
	
	
	
	
	public Layer(String name){
		this.name = name;
		this.points = new Vector();
		this.layers = new Vector();
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
	
	public void removesPoint(int index)
	{
		points.removeElementAt(index);
	}
	
	public Vector getsPoints()
	{
		return points;
	}
	
	public void setPoints(Vector points){
		this.points = points;
	}
	
	
}
