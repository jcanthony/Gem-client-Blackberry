package persistant.pkg;

import java.util.Vector;

import persistant.pkg.DataManager.TestDialog;
import persistant.pkg.Managers.MainScreenManager;




import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.PersistentObject;


import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;



public class FileIOPer extends UiApplication{
		
	 public static void main(String[] args)
	    {
	        FileIOPer theApp = new FileIOPer();       
	        theApp.enterEventDispatcher();
	    }
	   
	    public FileIOPer()
	    {        
	    	IOScreen pow = new IOScreen();
	    		pushScreen(pow);
	    }
	    
}

	class IOScreen extends MainScreen
	{
		// managers -----------------------
		Managers x = new Managers();
		MainScreenManager _mainManager = x.new MainScreenManager();
		DataManager datMan = new DataManager();
		GPS_Time gpsTm = new GPS_Time();
		
		
		// vectors -------------------------
		Vector _data = datMan.getProjects();
		Vector proj_name = new Vector(); 
		Vector bitmap = new Vector();
		
		// vars ----------------------------
		int label_ID;
		int ScreenID = 1;
		String newProjName;
		int data_Size = _data.size(); 
		int i;
		
		Font font;
		Bitmap _icon = Bitmap.getBitmapResource("icon2_onfocus.png");
		Bitmap _iconfocus = Bitmap.getBitmapResource("icon2.png");
		
	  	
			
	  	//default constructor for the opening screen
		public IOScreen()
 		{
			//getting singleton instances
			datMan = DataManager.getInstance();
			gpsTm = GPS_Time.getInstance();
	
			setTitle("NavaGIS Mobile Recon");
			setFont();
			// adds the main field manager to the screen
			add(_mainManager);
 			//lays out the icons on the screen
 			makeScreen();
 			
 		}
	
		
		// sets the font for the labels under the icon
		public void setFont()
		{
			 FontFamily fontFamily[] = FontFamily.getFontFamilies();
			 font = fontFamily[1].getFont(FontFamily.CBTF_FONT, 20);
		}
		
		
		// this section is for creating the screens menu-------
		 protected void makeMenu(Menu menu, int instance) 
		 	{
			 	menu.add(_new);
			 	menu.add(_del);
			 	menu.add(MenuItem.separator(100));
			 	menu.add(_server);
				menu.add(MenuItem.separator(100));
				menu.add(_close);
			}
		  
		  private MenuItem _close = new MenuItem("Exit", 110,
					10) 
		    {
			   public void run() {
				   System.exit(0);
			   }
		    };
		   
		   private MenuItem _server = new MenuItem("Server", 110,
					10) 
		    {
			   public void run()
			   {
				 getserver();
				 
			   }
		    };
		   
		   private MenuItem _new = new MenuItem("New Project", 110,
					10) 
		    {
			   public void run()
			   {
				   // test dialog found in dataManager
				   TestDialog t = datMan.new TestDialog("Project");
				   int result = t.doModal();
				   
				  // check result of dialog box
				  // If ok then add new project and call makescreen() function
				  if(result == Dialog.OK)
				  {
					  Project p = new Project(t.getUsernameFromField());
					  datMan.addProject(p);
					  _data = datMan.getProjects();
					  makeScreen();
				  }
				  else if(result == Dialog.CANCEL)
				  {
					  t.close();
				  }
				  
			   }
		    };
		   
		   private MenuItem _del = new MenuItem("Delete Project", 110,
					10) 
		    {
			   public void run() {
				    	   
				   datMan.removeProject(label_ID);
				   _data = datMan.getProjects();
				   makeScreen();
		   				   
			   }
		    }; 
	// end menu --------------------------------------------------------
	
	// getserver call from menu
	public void getserver()
	{
	  	UiApplication.getUiApplication().pushScreen(new Server());
	}	    
		    
	// custom bitmap field that has specific bitmap heights and widths, along with overloading
    // onfocus, and click event functions
	public class mybitmapField extends BitmapField 
   	  {
	   		// used in the Label ID system
			private int ID ;
	   		
	   		// pixels
	   		private int width_height = 57;
	   		
	   		
    	   public mybitmapField(){}
    	 
    	   protected void sublayout(int width, int height) {       
    	       setExtent(width,height);
    	   }
    	   
    	   public int getBitmapWidth() {
    	       return width_height;
    	   }
    	   
    	   public int getBitmapHeight() {     	      
    	       return width_height;
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
			   this.setBitmap(_iconfocus);
		   }
    	   
    	   protected void onUnfocus()
    	   {
    		   this.setBitmap(_icon);
    	   }
    	   
    	   
    	   public boolean trackwheelClick(int status, int time)
    	   {
    	   click();
    	   return true;
    	   }
    	};
    	
    	   public void click()
		   {
		    UiApplication.getUiApplication().pushScreen(new ProjectScreen((Project) _data.elementAt(label_ID)));
		   }
    	   
    	  
   //this displays the screen and handles new project and delete project
   // it erases the manager on Menu: new ,then deletes all to redraw/repopulate
   // the field manager this is done so that the label_Id system works for clicking
    	   
     public void makeScreen()
      {
   	   // recalculates the dynamic number for looping, getting the number from
       // the datamanagers [data] vector size
   	   data_Size = _data.size();
   	   
   	   // clear an unempty fieldmanager
   	   if(_mainManager.getFieldCount()!= 0)
   	   _mainManager.deleteAll();
   		   
   	   if(data_Size==0)
   	   {
   		   LabelField _none = new LabelField();
   		   _none.setText("(No Projects - Press Menu To Add New Project)");
   		   _mainManager.add(_none);
   	   }
   	   //repopulate the field manager with icons
   	   for(i=0;i<data_Size;i++)
   		{	
    		
    		
   			proj_name.addElement(new LabelField());    
   	    	((LabelField) proj_name.elementAt(i)).setFont(font);
   			Project p = (Project) _data.elementAt(i);
   			((LabelField) proj_name.elementAt(i)).setText(p.getName());
   			
    				
   			bitmap.addElement(new mybitmapField());  
    		 ((mybitmapField) bitmap.elementAt(i)).setBitmap(_icon);
        	((mybitmapField) bitmap.elementAt(i)).setID(i);
    		    	
        	_mainManager.add((mybitmapField) bitmap.elementAt(i));
        	_mainManager.add((LabelField) proj_name.elementAt(i));
    		   	 		    	 
    	}
    }
		   
}
	 
		   
	





 

