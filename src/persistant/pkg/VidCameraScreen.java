package persistant.pkg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.RecordControl;
import javax.microedition.media.control.VideoControl;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

public class VidCameraScreen extends MainScreen {
	 private Player _player;
	 private VideoControl _videoControl;
	 private RecordControl _recordControl;
	 private String _videoFile;
	 long count = 000000;
	 Date timestamp;
	 
	 ByteArrayOutputStream _outStream;
	
	
	VidCameraScreen()
	{
		 try{
		 
			try {
				_player = javax.microedition.media.Manager.createPlayer("capture://video?" + "encoding=video/3gpp&width=480&height=352&video_codec=MPEG-4&audio_codec=AMR");
			    _player.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
         
          _videoControl = (VideoControl) _player.getControl("VideoControl");
          _recordControl = (RecordControl) _player.getControl("RecordControl");

         // Initialize the video display
          Field videoField = (Field) _videoControl.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, "net.rim.device.api.ui.Field");            
         
          try
          {
             _videoControl.setDisplaySize( Display.getWidth(), Display.getHeight() );
          }
          catch( MediaException me )
          {
             // setDisplaySize is not supported
          }
          
          add(videoField);

          int choice = Dialog.ask(Dialog.D_YES_NO, "Record Video?", 1);
          if(choice == Dialog.YES)
          {
        	  Dialog.alert("Press Enter To Stop Recording...");
              _outStream = new ByteArrayOutputStream();
              _videoFile = System.getProperty("fileconn.dir.memorycard.photos") + "vid"+ count +"-" + timestamp.getTime()+".mpg";
              count ++;
          }
          else 
          {                 
        	 _videoControl = null;
        	 _recordControl = null;
        	 _player.stop();
        	 _player.close();
             UiApplication.getUiApplication().popScreen(this);
          }            
          
          startRecord();
 		
          
		 }catch(MediaException me){}
		 
		
	}
	
	 private void startRecord()
	    {
	        try
	        {
	            // If the recording is not pending a commit, then we need to set
	            // the location to commit to      
	           
	            _recordControl.setRecordStream(_outStream);
	            _recordControl.startRecord();
	                           
	        }
	        catch( Exception e )
	        {
	           
	        }
	    }
	 
	 private void stopRecord()
	    {
	        try
	        {
	            _recordControl.stopRecord();            
	            UiApplication.getUiApplication().popScreen(this);	            
	        }
	        catch( Exception e )
	        {
	            //test
	        }
	    }
	
		public boolean keyChar( char key, int status, int time )
	    {
	        boolean retval = false;
	        if (key == Characters.SPACE){
	        	
	        	stopRecord();
	        	retval = true;
	          
	        }
	        else {
	            //other key was pessed
	            return super.keyChar(key, status, time);
	        }
	        return retval;
	    }
				 

}
