package persistant.pkg;

import java.util.Vector;

import net.rim.device.api.system.DeviceInfo;

public class JSON {
	DataManager datMan = new DataManager();
	private Vector path;
	StringBuffer json = new StringBuffer();
	Object o;
	Project p;
		
	JSON()
	{
	p = datMan.getProject(0);
	String JSON = funct1(p);
	
	
	}
	
	public String createJson()
	{
	 String JSON = funct1(p);
	 return JSON;
	}
	
	public String funct1(Project p)
	{
		String temp = "";
		temp += "{"; 
		temp += "\"project\":";
		temp += "{";
		temp += "\"deviceName\": \"BlackBerry\", ";
		temp += "\"deviceID\":" + "\"" + DeviceInfo.getDeviceId() + "\"," ;
		temp += " \"name\":" + "\"" +p.getName() + "\",";
		temp += "\"layer\":"+ func2(p);
		temp += "}";
		temp += "}";
	
		return temp;
	}
	
	public String func2(Project p)
	{
		String temp="";
		temp += "[";
		Vector layers;
		
		if(p.getLayers()!=null)
		layers = p.getLayers();
		else
		layers = new Vector();
		
		int i;
		int _loop = layers.size();
		
		for (i = 0; i<layers.size() ; i++){
		Layer l = (Layer) layers.elementAt(i);
		 
		temp+= "{";
		temp+= "\"name\":" + "\"" + l.getName()+ "\",";
		temp+= "\"layer\":" + func3(l);
		temp+= "\"point\":" + func4(l);
		
		if (i<layers.size() && (i!=_loop-1))
		temp+= "},";
		else
		temp+= "}";
		}
		
		temp+= "]";
		
		return temp;
			
	}
	
	public String func3(Layer l)
	{
		String temp = "";
		Vector layers;
		
		// checks to see if layers exist
		if(l.getLayers()!= null)
		 layers = l.getLayers();
		else
		 layers = new Vector();
		
		int i;
		int _loop = layers.size();
		
		temp+="[";
		
		for( i = 0;i<layers.size();i++)
		{
			Layer lay = (Layer) layers.elementAt(i);
			temp+="{";
			temp+="\"name\":" + "\"" +lay.getName() + "\"," ;
						
			temp+="\"layer\":"+ func3(lay);
			temp+="\"point\":"+ func4(lay);
			
			// checks to see if it is the last object or not
			if (i<layers.size()&& (i!=_loop-1))
			temp+= "},";
			else
			temp+= "}";
		}
		
		//checks to see if it is the last layer
		temp+= "],";
		
		
		return temp;
	}
	
	public String func4(Layer l)
	{
		String temp = "";
		Vector points;
		
		if(l.getsPoints()!=null){
		points = l.getsPoints();
		}
		else
		{
		points = new Vector();
		}
		
		
		int _loop = points.size();
		int i;
		
		temp +="[";
		
		for(i = 0; i<points.size();i++)
		{
			Points p = (Points) points.elementAt(i);
			temp+="{";
			temp+="\"name\":" + "\"" + p.getName()+ "\"," ; 
			temp+="\"notes\":" + "\"" + p.getNotes()+ "\"," ;
			temp+="\"timestamp\":"+ "\"" +p.gets_Timestamp()+ "\"," ;
			temp+="\"lon\":" + "\"" + p.getLon()+ "\"," ;
			temp+="\"lat\":"+ "\"" +p.getLat()+ "\",";
			//temp+="\"id\":"+ "\"" + p.getUID()+ "\",";
			temp+="\"photo\":" + func5(p);
			
			if (i<points.size()&& (i!=_loop-1))
			temp+= "},";
			else
			temp+= "}";
		}
		
		temp+= "]";
		
		return temp;
	}
		
	public String func5(Points p)
	{
		String temp= "";
		temp+="[";
		Vector phot = p.getPhotos();
		//checks to see if there are photos
		if(p.getPhotos()!=null)
		phot = p.getPhotos();
		else
		phot = new Vector();
		
		int _loop = phot.size();
		
		for(int i = 0;i<phot.size();i++)
		{
			Photos ph = (Photos)phot.elementAt(i);
			temp+="{";
			temp+="\"img\":"+ "\"" + ph.getName()+ "\"";
			
			if(i<phot.size() && (i!=_loop-1))
			temp+="},";
			else
			temp+="}";
			
		}
		temp+="]";
		return temp;
	}
		
}


