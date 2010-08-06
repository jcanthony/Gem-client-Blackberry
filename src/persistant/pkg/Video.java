package persistant.pkg;

import net.rim.device.api.util.Persistable;

public class Video implements Persistable {
	
	private String vidPath;
	private String name;
	
	Video(){
		
	}
	
	public String getVid_Path()
	{
		return vidPath;
	}
	
	public String getName(){
		return name;
	}
	
	public void setVid_Path(String url){
		vidPath = url;
	}
	
	public void setName(String n){
		name = n;
	}
	
	

}
