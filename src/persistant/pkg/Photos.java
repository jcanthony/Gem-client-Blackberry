package persistant.pkg;

import net.rim.device.api.util.Persistable;


public class Photos implements Persistable{

	
	private String imgPath;
	private String previewPath;
	private String name;
	
	public Photos(){
		
	}	
		
	public String getImgPath() {
		return imgPath;
	}	
	
	public void setImgPath(String path)
	{
		this.imgPath = path; 
	}
	
		
	public String getPreview() {
		return previewPath;
	}
	public void setPreview(String prevPath) {
		this.previewPath = prevPath;
	}
	
	public void setName(String n)
	{
		name = n;
	}
	
	public String getName()
	{
		return name;
	}
}
