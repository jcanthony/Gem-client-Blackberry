package persistant.pkg;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.PNGEncodedImage;
import net.rim.device.api.ui.component.Dialog;

public class MediasManager {
	private static MediasManager instance = null;
	Photos _photo;
	Video _vid;
	private byte[] image_Raw;
	private byte[] thumb_Raw;
	
	private String image_Url ;
	private String image_Path;
	
	Date timestamp = new Date();
	
	Bitmap B_prev;
	Bitmap B_img;
	Bitmap B_thumb;
	
	int prev_id = 5;
	int img_id = 7;
	int counter = 10000;
	int i = 1;
	
	public PointScreen pntScr;
	             
		
	// makes the PhotoManager a singleton
	public static MediasManager getInstance()
	{
		if(instance == null){
			instance = new MediasManager();
		}
		return instance;
	}

	
	// setters & getters
	public void setImageData(byte[] img_Raw)
	{
		this.image_Raw = img_Raw;
	}
	
	public Bitmap getPreview()
	{
		return B_prev;
	}
	
	public void setPhoto(Photos pht)
	{
		this._photo = pht;
	}
	
	public Photos getPhoto()
	{
		return _photo;
	}
	
	public void setVideo(Video v){
		this._vid = v;
	}
	
	public Video getVideo(){
		return _vid;
	}
	//functions
	
	public void makePrev()
	{
		B_prev = Bitmap.createBitmapFromBytes(image_Raw,0,image_Raw.length, 6);	
	}
	
	public void makeImg()
	{		
		B_img = Bitmap.createBitmapFromBytes(image_Raw,0,image_Raw.length, 1);
	}
	
	public void makeThumb()
	{
		B_thumb =  Bitmap.createBitmapFromBytes(image_Raw,0,image_Raw.length,20);
		PNGEncodedImage enImg = PNGEncodedImage.encode(B_thumb);
		thumb_Raw = enImg.getData();
		
		// deallocate memory
		enImg = null;
		B_thumb = null;
	}
	
	public byte[] getThumb()
	{
	  return thumb_Raw;	
	}

	// saves the prev. The only difference between this and the image is the 
	// way that it sets the photos url
	// if you can work around this then there could be a singular save function
	
	public void savePrev()
	{
		String fName = setUrl(prev_id) ;
		Bitmap local;
	
		 // sets up outputstreem and fileconnect to be used
		 OutputStream out = null;
	     FileConnection filecon = null;
	     
	     try{
	     filecon = (FileConnection) Connector.open(fName,Connector.READ_WRITE);	
	     filecon.create();
	     
	     //sets image url to the photo object
	     _photo.setPreview(fName);
	     out = filecon.openOutputStream();
	     local = B_prev;
		   
		 PNGEncodedImage encodedImage = PNGEncodedImage.encode(local);
	     
	     out.write(encodedImage.getData());
	     out.flush();
	     out.close();
	     
	     //deallocate memory
	     local = null;
	     encodedImage = null;
	     B_prev = null;
	     
	     filecon.close();
	     
	     }catch(IOException e)
	     {
	    	Dialog.alert("Err: " + e ); 
	     }
	     
	     _photo.setPreview(fName);
	     //DeleteImage(image_Path);
	     
	}
	
	
	// saves the image. The only difference between this and the prev is the 
	// way that it sets the photos url
	// if you can work around this then there could be a singular save function
	public void saveImg()
	{
		String fName = setUrl(img_id);
		Bitmap local;
		
		OutputStream out = null;
	    FileConnection filecon = null;
	    
	    try{
	    filecon = (FileConnection) Connector.open(fName,Connector.READ_WRITE);
	    if(!filecon.exists())
	    {	
	    filecon.create();
	    _photo.setImgPath(fName);
	    out = filecon.openOutputStream();
	    local = B_img;
	   
	    PNGEncodedImage encodedImage = PNGEncodedImage.encode(local);
	   
	    out.write(encodedImage.getData());
	    out.flush();
	    out.close();
	    
	    // deallocate memory
	    local = null;
	    image_Raw = null;
	    encodedImage = null;
	    B_img = null;
	    
	    filecon.close();
	    
	    }
	    }catch(IOException e)
	     {
	    	   Dialog.alert("Err : " + e);
	     }	
	    
	    _photo.setImgPath(fName);
	}
	
	void DeleteImage(String url)
	{
		 FileConnection filecon = null;
		   try{
			   filecon = (FileConnection) Connector.open(url,Connector.READ_WRITE);
			   filecon.delete();
		   }catch(Exception e){}
	}
	
	
	// this sets the directory file path and name for the files
	// the id is to help when problems would arise from using the time stamp
	// 7 represents an image, and 5 represents a preview
	// These numbers have no significant meaning...if you were wondering
	public String setUrl(int id)
	{
		 image_Path = System.getProperty("fileconn.dir.memorycard.photos");
		 _photo.setName("" + counter + timestamp.getTime()+"-"+ id );
		 image_Url = image_Path + counter + timestamp.getTime() +"-"+id + ".jpg";
		 counter++;
		 
		 
		 
		if (i == 1)
		{
			i ++;
		}
		else
		{
			Dialog.alert("Images Saved!");
			i = 1;
		}
		
			
			 
		 return image_Url;
	}
	
}
