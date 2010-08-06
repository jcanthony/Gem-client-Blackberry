package persistant.pkg;

import net.rim.blackberry.api.invoke.CameraArguments;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.device.api.io.file.FileSystemJournal;
import net.rim.device.api.io.file.FileSystemJournalEntry;
import net.rim.device.api.io.file.FileSystemJournalListener;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.EventInjector;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;

class VidCameraScreen extends MainScreen implements FileSystemJournalListener
	{
	// NOT REALLY USEFULL FOR ANYTHING BUT FOR COLOR CONTINUITY
	HorizontalFieldManager _hfm = new HorizontalFieldManager(HorizontalFieldManager.USE_ALL_HEIGHT|HorizontalFieldManager.USE_ALL_WIDTH)
	{
		protected void paint(Graphics g)
		{
			g.setBackgroundColor(0x00BDC29A);// special grey
	        g.clear();
			super.paint(g);
		}
	};
	
	UiApplication UiApp = UiApplication.getUiApplication();
	VidCameraScreen popMe = this;
	
	private MediasManager medMan = null;
	Video r_Vid;
	
	long _lastUSN;
	String capturedImgPath;
	String full_path;
	
	VidCameraScreen(Video v)
	{
		medMan = MediasManager.getInstance();
		r_Vid= v;
		
		// change to set video
		//medMan.setPhoto(r_Vid);
		
		doTakeVid();	
		
		
	}
	
		
	public void doTakeVid(){
		UiApplication.getUiApplication().addFileSystemJournalListener(this);
		
	    _lastUSN = FileSystemJournal.getNextUSN();
		Invoke.invokeApplication(Invoke.APP_TYPE_CAMERA, new CameraArguments());
	}

	public void fileJournalChanged() {
		  long nextUSN = FileSystemJournal.getNextUSN();
	        String msg = null;
	        String path = null;
	       
	        for (long lookUSN = nextUSN - 1; lookUSN >= _lastUSN && msg == null; --lookUSN) 
	        {
	            FileSystemJournalEntry entry = FileSystemJournal.getEntry(lookUSN);
	            
	            if (entry == null) 
	            { 
	                break;
	            }

	             path = entry.getPath();
	             full_path = "file://"+path;
	            
	            if (entry.getEvent() == FileSystemJournalEntry.FILE_ADDED)
	            {
	                switch (entry.getEvent()) 
	                {
	                    case FileSystemJournalEntry.FILE_ADDED:
	                    	setImage();
	                    	closeCamera();
	                        break;
	                }
	            }
	        }
	        _lastUSN = nextUSN;

}
	         void closeCamera()
	        {
	            EventInjector.KeyEvent inject = new EventInjector.KeyEvent(EventInjector.KeyEvent.KEY_DOWN, Characters.ESCAPE,50);
	            inject.post();
	            inject.post();
	            // change the below into mediasmanager  addvideo
//	            photMan.setImgPath(full_path);
	            //UiApp.pushScreen(new CamPreviewScreen(popMe));
	        
	       }

	         
	        void setImage()
	       {
	           
	       }
	       
	        

	        	 
}

	