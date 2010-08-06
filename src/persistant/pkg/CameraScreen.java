package persistant.pkg;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.KeyListener;
import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;


public class CameraScreen extends MainScreen{
	
	private MediasManager medMan = new MediasManager();
	
	private VideoControl _vidcon;
	Player player ;
	private Field _videofield;
	
	private String image_Type = "encoding=jpeg&width=1024&height=768&quality=fine";
	private byte[] image_Array;

	Photos phto;
 
	CameraScreen _camScreen;
	
	VerticalFieldManager _vfm = new VerticalFieldManager(VerticalFieldManager.USE_ALL_HEIGHT|VerticalFieldManager.USE_ALL_WIDTH)
	{public void paint(Graphics graphics)
    {
        graphics.setBackgroundColor(0x00BDC29A);// special grey
        graphics.clear();
        super.paint(graphics);
    }};
	
	
    private UiApplication UiApp;
    
	CameraScreen(Photos pht){
		// sets address to singleton
		medMan = MediasManager.getInstance();
		UiApp = UiApplication.getUiApplication();
		// gets photo sets it locally and sets it to the photomanager
		this.phto = pht;
		medMan.setPhoto(phto);
		// make screen for easy popscreen
		_camScreen = this;
		
		
		
	
		
		
		// adds the player to the screen to be used
		//_vfm.add(_videofield);
		
	
		// this starts the camara media feature
		
		
		CreateCamera();
		try {
			player.start();
		} catch (MediaException e) {
			Dialog.alert("Err:" + e);
		}
		
		
		Dialog.alert("Press Enter To Take Picture");
}

	public boolean keyChar( char key, int status, int time )
    {
        boolean retval = false;
        if (key == Characters.ENTER){
        	takePicture();
        	retval = true;
          
        }
        else {
            //other key was pessed
            return super.keyChar(key, status, time);
        }
        return retval;
    }
			 

	 // this takes the picture and displays a preview(preview not implemented yet
	 
	 public void takePicture() 
	 {
		
			try{ 
			medMan.setImageData(_vidcon.getSnapshot("encoding=jpeg&width=1024&height=768&quality=fine"));
				}catch(MediaException e){};
			//sets the image to the photoManager
				
					
			UiApp.pushScreen(new CamPreviewScreen(_camScreen));
		
	
	}
	 
	public void CreateCamera()
	{
		try{
			player = Manager.createPlayer("capture://video");
			player.realize();
			
			_vidcon = (VideoControl)player.getControl( "VideoControl" );
			
			if (_vidcon !=null)
			{
				  _videofield = (Field) _vidcon.initDisplayMode (VideoControl.USE_GUI_PRIMITIVE, "net.rim.device.api.ui.Field");
				  _vidcon.setDisplayFullScreen(true);
	              _vidcon.setVisible(true);
	              
			}
			add(_videofield);
			
			
			
		
			}catch(Exception e){
				
				Dialog.alert("E:"+e);
			}
	}

	
	
	 protected void makeMenu(Menu menu, int instance) {
			
			
			menu.add(Done);
			menu.add(MenuItem.separator(100));
			menu.add(TakeAnother);
							
			}
	 
	 private MenuItem Done = new MenuItem("Take Picture", 110,
				10) 
	   {
		   public void run() {
			   takePicture();
		   }
	   
	   };
	   
	   private MenuItem TakeAnother = new MenuItem("Back", 110,
				10) 
	   {
		   public void run() {
			   
		   }
	   
	   };
	  
	
	
	 
}
