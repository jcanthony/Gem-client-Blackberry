package persistant.pkg;


import java.io.InputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import persistant.pkg.Managers.PhotoFieldManager;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;


public class PointScreen extends MainScreen {
	DataManager datMan = new DataManager();
	MediasManager medMan = new MediasManager();
	
	
	// FieldManagers to control the screen------------------------------------------------------------------
	
	VerticalFieldManager MainMan = new VerticalFieldManager()
	{	};
	
	HorizontalFieldManager Desc = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
	HorizontalFieldManager LatLong = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER){public void paint(Graphics graphics)
    {
        graphics.setBackgroundColor(0x00BDC29A);// special grey
        graphics.clear();
        super.paint(graphics);
    }};

	HorizontalFieldManager TimeStamp = new HorizontalFieldManager(HorizontalFieldManager.USE_ALL_WIDTH){public void paint(Graphics graphics)
    {
        graphics.setBackgroundColor(0x00BDC29A);// special grey
        graphics.clear();
        super.paint(graphics);
    }};
    
    Managers m = new Managers();
    PhotoFieldManager photoPrev = m.new PhotoFieldManager();
    //end field managers -------------------------------------------------------------------------------------------
	
    
    // Fields to be used in Screen
    //----------------------------------------
	LabelField Lat_Long = new LabelField("",LabelField.USE_ALL_WIDTH|LabelField.USE_ALL_HEIGHT);
	LabelField Time = new LabelField("" ,LabelField.FIELD_HCENTER|LabelField.USE_ALL_HEIGHT);
	BasicEditField Description = new BasicEditField(BasicEditField.FIELD_HCENTER);
	
	// Vars
	int label_ID;
	String time;
	String _latLong = new String();
	Points point;
	GPS_Time gps = new GPS_Time();
	Vector V_photos = new Vector();
	Vector V_bitmaps = new Vector();
	Bitmap _pic;
	Font font;
	Photos photo = null;
	
	// counters
	int i = 0;
	
	
	//The screen code
	//
	//
	
	PointScreen()
	{
		// instantiates singleton managers
		getInstances();		
		medMan.pntScr = this;
		this.point = (Points) datMan.getPoint();
		
		setTitle("Point: " + point.getName());
	
	
	
		//sets the font for the gps and timestamp
		setFont();
		Lat_Long.setFont(font);
		Time.setFont(font);
	
	
	
	
		// gets photos, checks for photos,then displays if there are any
		V_photos = point.getPhotos();
		
		
	
		
	// sees if the description edit box already has text or not
	if(point.getNotes()== null)
	{
		Description.setText("Enter A Desription...");
	}
	else
	{
		Description.setText(point.getNotes());
	}
	
	// add description box to the local manager then to the global screen manager
	XYEdges padding =  new XYEdges(10, 30, 10, 30);
	Border border = BorderFactory.createBevelBorder(padding);
	Description.setBorder(border);
	Desc.add(Description);
	MainMan.add(Desc);
		
	// Call the time function to set time in CDT
	 Time.setText("Time :" + point.getTimestamp());	
	 TimeStamp.add(Time);
	 MainMan.add(TimeStamp);
		 
	
	// get,set, display lat long
	
	_latLong = "		Latitude : " + point.getLat() + " | Longitude : " + point.getLon();
	Lat_Long.setText(_latLong);
	
		
	 LatLong.add(Lat_Long);
	 MainMan.add(LatLong);
 
	 //photos
	 MainMan.add(photoPrev);
	 
	
	 layoutPics();
		 
	 add(MainMan);
	 
	 
	
	 // this makes it so that as the user types the Description is saved
	 FieldChangeListener fcl = new FieldChangeListener()
	 {
	   public void fieldChanged(Field field, int context)
	   {
	     point.setNotes(Description.getText());
	   }
	 };
	 
	 Description.setChangeListener(fcl);
	}
	
	
	//Custom BitmapField
	//--------------------------------------------------
	//--------------------------------------------------
	public class mybitmapField extends BitmapField 
 	  {
	   		private int ID ;
	   		
  	   public mybitmapField(){}
  	 
  	   protected void sublayout(int width, int height) {	       
  	       setExtent(width, height);
  	   }
  	   
  	   public int getBitmapWidth() {
  	       return 175;
  	   }
  	   
  	   public int getBitmapHeight() {     	      
  	       return 130;
  	       }
  	   
  	   public boolean isSelectable()
  	   {
  		   return true;
  	   }
  	   
  	   public boolean isFocusable()
  	   {
  		   return true;
  	   }
  	   
  	   public void setID(int id)
  	   {
  		   ID = id;
  	   }
  	
  	   
  	   protected void onFocus(int direction)
  	   {
			   label_ID = ID;
  	   }
  	   
  	   
  	   
//  	   public boolean trackwheelClick(int status, int time)
//  	   {
//  	   click();
//  	   return true;
//  	   }
  	};
  	
  	  	
	 
	 protected void makeMenu(Menu menu, int instance) 
	 	{
		 	menu.add(_new);
		 	menu.add(_newv);
		 	menu.add(_newa);
		 	menu.add(MenuItem.separator(100));
		 	menu.add(_server);
		 	menu.add(_delp);
		 	//menu.add(MenuItem.separator(50));
			menu.add(_close);
		}
	  
	 private MenuItem _server = new MenuItem("Server", 110,
				10) 
	    {
		   public void run() {
			  getServer(); 
		   }
	    };
	 
	private MenuItem _delp = new MenuItem("Delete Picture", 110,
				10) 
	    {
		   public void run() {
			 Photos delp = (Photos) V_photos.elementAt(label_ID);
			 medMan.DeleteImage(delp.getImgPath());
			 medMan.DeleteImage(delp.getPreview());
			 point.removePhoto(label_ID);
			 V_photos = point.getPhotos();
			 // clears the thumbnail byte array
			 point.setThumb(new byte[0]);
			 layoutPics();			 
		   }
		   
	    };
	
	
	private MenuItem _close = new MenuItem("Exit", 110,
				10) 
	    {
		   public void run() {
			   System.exit(0);
		   }
	    };
	
	 private MenuItem _new = new MenuItem("Add Photo", 110,
				10) 
	    {
		   public void run() {
			  Photos pht = new Photos();
			  UiApplication.getUiApplication().pushScreen(new CameraScreen(pht));	  				  
		   }
	    };
	    
	private MenuItem _newv = new MenuItem("Add Video", 110,
				10) 
	    {
		   public void run() {
			//  UiApplication.getUiApplication().pushScreen(new VidCameraScreen());	  				  
		   }
	    };
	
	private MenuItem _newa = new MenuItem("Add Audio",110,10)
	{
		public void run(){
		  // this is where the audio page will go	
		}
	};
	
	   
	    
	  
// functions to be used in the program---------------------------
//-------------------------------------------------------------------
	 //accessed from camerapreviewscreen, this makes it so that we only add the photo after it has been saved
	 public void addPhoto()
	 {
		 point.addPhoto(medMan.getPhoto());
	 }
	 
//	 public void addVideo()
//	 {
//		 Video v = medMan.getVideo();
//		 point.addVideo(v);
//	 }
	 
	 public void getServer()
	 {
		  	UiApplication.getUiApplication().pushScreen(new Server());
	 }	   
	 
	 
// function for calling the images from file system...converting to byte array, then setting in Bitmap
	 private BitmapField getImage(String url)
	 {
		
	     String filePath = url;
	     mybitmapField mbf =  new mybitmapField();

	    try
	    {
	        FileConnection fconn = (FileConnection)Connector.open(filePath, Connector.READ);
	        if (fconn.exists()) 
	        {        
	           	                    
	            int fileSize = (int)fconn.fileSize();
	            byte[] byteArray = new byte[fileSize];
	        	InputStream input = fconn.openInputStream();
	            if (fileSize > 0) {
	             input.read(byteArray);
	            }    
	            // sets the array into the bitmap
	            Bitmap bitmapImage = Bitmap.createBitmapFromBytes(byteArray, 0, byteArray.length, 1);
	             
	           
	            mbf.setBitmap(bitmapImage);
	            
	            input.close();
	         
	        }
	        else
	        {
	           Dialog.alert("No Image Found");
	        }
	      
	        //close connection
	        fconn.close();
	      
	    }
	    catch(Exception e)
	    {
	        Dialog.alert(" Err:"+ e);
	    }
	    
	   
	    
	    
		return mbf;

		 
	 }
	 
	 
	 // this is a test to see if I can call this function when I need to
	 public void layoutPics()
	 {
		 mybitmapField temp;
		 
		 if(photoPrev.getFieldCount() != 0)
		 {
		  photoPrev.deleteAll();
		 }
		 
		 V_photos = point.getPhotos();
		 
	
		 for(i = 0; i<V_photos.size() ; i++)
			{
			 	photo = (Photos) V_photos.elementAt(i);
				String photo_url = photo.getPreview();
				if(photo_url == null)
				{continue;}
				else{
				temp = (mybitmapField) getImage(photo_url);
				temp.setID(i);
				V_bitmaps.addElement(temp);
				photoPrev.add((mybitmapField) V_bitmaps.elementAt(i));
				}
			}
	 }
	 
	// sets font
    public void setFont()
	 {
		 FontFamily fontFamily[] = FontFamily.getFontFamilies();
		 font = fontFamily[1].getFont(FontFamily.CBTF_FONT, 15);
	 }
    
    // gets instances of managers
    public void getInstances()
    {
    	datMan = DataManager.getInstance();
		gps = GPS_Time.getInstance();
		medMan = MediasManager.getInstance();
    }
    
    protected void onUndisplay()
    {  	
    	    	
    	//memory deallocation
    	for(int i = 0;i<V_bitmaps.size(); i++)
    	{ mybitmapField _del;
    	  _del = (mybitmapField) V_bitmaps.elementAt(i);
    	  _del = null;
    	}
    	
    	
    	V_photos = null;
        V_bitmaps = null;
        datMan = null;
        gps = null;
        medMan = null;
    	System.gc();
    }
}
