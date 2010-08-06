package persistant.pkg;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Vector;

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
	
	private Vector phot_urls = new Vector();
	private Vector phot_names = new Vector();
	
	private Vector vid_urls = new Vector();
	private Vector vid_names = new Vector();
	
	private String image_Url ;
	private String image_Path;
	private String vid_Url;
	private String vid_Name;
	
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
	
	
	// DeletePointPhot : obtains the photo, then the photo url from that photo object, 
	//  then passes it to the delete function to be completely delete it
	//
	// *Note that the photo object is not being deleted here. That is being handled by the 
	//  layer screen class
	
	void DeletePointPhot(Points pnt){
		Vector photos = pnt.getPhotos();
		for(int i = 0; i < photos.size();i++){
			Photos phot = (Photos) photos.elementAt(i);
			String phot_url = phot.getImgPath();
			DeleteImage(phot_url);
			
		}
		
	}
	
	// used to delete photos when deleting from a layer...used after get_photos() function below
	void DeletePhotLayer(){
		for(int i = 0; i<phot_urls.size();i++)
		{
		DeleteImage((String) phot_urls.elementAt(i));
		}
		phot_urls.removeAllElements();
		phot_names.removeAllElements();
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
	
	
	public Vector getPhotourls(){
		return phot_urls;
	}
	
	public Vector getPhotonames(){
		return phot_names;
	}
	
	public void get_photos(Layer l,boolean del)
	{
		Vector photos = new Vector();
		Vector points = new Vector();
		Vector layers = new Vector();
		Points p;
		Layer lay;
		Photos ph;
		
				
		// looks at the selected layers Points then gets all photos and Img urls associated
		if(l.getsPoints().size()!=0){
			points = l.getsPoints();
			for(int i = 0;i<l.getsPoints().size();i++){
				p = (Points) points.elementAt(i);
				photos = p.getPhotos();
				
				for(int j=0;j<photos.size();j++){
				 ph = (Photos) photos.elementAt(j);
				 
				 // adds the names and urls to their corresponding vectors
				 // the vectors are a property of the photo class
				 phot_urls.addElement(ph.getImgPath());
				 phot_names.addElement(ph.getName());
				 
				 if(del == true){
				 phot_urls.addElement(ph.getPreview());
				 }
				 
			
				}
			}
		}
		
		// Looks to see if there are other layers within the layer to recursively call the function until there 
		// are no more layers to recurse through.
		// l.getLayers.size() = size of the layer vector inside of the inputed Layer l
		// 
		if(l.getLayers().size() != 0){
			layers = l.getLayers();
			for(int i=0;i<layers.size();i++){
		    lay = (Layer) layers.elementAt(i);
		    // function recursion
		    // del uses the same property that was originally used
		    get_photos(lay,del);
			}
		}
		
	}
	
	
}
