package persistant.pkg;
import java.util.Date;
import java.util.Vector;

import net.rim.device.api.util.Persistable;


public class Project implements Persistable{
	private String name;
	long pPID; 
	Vector layers;
	
	public Project(String name)
	{
     this.name = name;
     this.layers = new Vector();
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name){
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

		
}