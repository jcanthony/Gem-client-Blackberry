/*
 * @since 0.0.1
 * 
 * Project : object that contains : name (String)
 *                                  Layers (Vector of objects)
 * 
 *     The implementation of Persistable makes it so that the object is "Serializable"
 *        Persistable is the RIM equivalent of Serialization
 *          It uses an address in memory similar to Serialization and the Persistance is 
 *          held even when a hard restart (battery pull) is used.
 *   
 */

package persistant.pkg;
import java.util.Date;
import java.util.Vector;

import net.rim.device.api.util.Persistable;


/*
 *  Main Class For Project
 *  
 *  @param : Constructor 
 *           getName()
 *           setName(String)
 *           addLayer(Layer)
 *           removeLayer(int) - the int is the index number in the vector
 */
public class Project implements Persistable{
	private String name;
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