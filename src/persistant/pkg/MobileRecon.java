/* Main
 *   @author : Navagis (John Anthony)
 *   @version: 0.1.0
 *   @since  : 0.0.1
 *   
 *   This is the main class and the beggining of the program, and this 
 *   is the first screen class.
 * 
 *   The application is put into the the event thread. Then the application
 *   references the global class(@see : (class) DataManager) to get data. The data
 *   is then run through a UI creation method that builds the Main Screen (@see : net.rim.device.api.ui.container.MainScreen)
 *   using a manager method from another global class(@see :(class) Managers). 
 *   
 */

package persistant.pkg;

import java.util.Vector;

import persistant.pkg.DataManager.TestDialog;
import persistant.pkg.Managers.MainScreenManager;




import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;


/*
 * @param :   main(String[]arg) : used to create the main class of java program
 *                                and creates app for app thread.
 *                                
 *            FileIOPer()       : creates a Screen object that will contain all
 *                                of the program UI
 */
public class MobileRecon extends UiApplication{
		
	 public static void main(String[] args)
	    {
	        MobileRecon theApp = new MobileRecon();       
	        theApp.enterEventDispatcher();
	    }
	   
	    public MobileRecon()
	    {        
	    	IOScreen pow = new IOScreen();
	    		pushScreen(pow);
	    }
	    
}

/*
 *    This is the screen object. It holds all of the UI for the Main/Home Screen
 *    
 *    @see : net.rim.device.api.ui.container.MainScreen
 *            This is extended for this class
 *    
 *    @param : IOScreen() - Default Constructor that starts GPS manager and creates DataManager, checks device permissions
 *                           and  calls to create UI
 *                         
 *             setFont() -  sets the font for the labels under the icon
 *             
 *             makeMenu() - creates the menu for the screen (@see : see method section below)
 *             
 *             getserver() - call from menu, that creates a ServerScreen (@see : (class) Server)
 *             
 *             mybitmapField() - **see the method below
 *             
 *             makeScreen() -  displays the screen(ui) and handles new project and delete project.
 *  						   It erases the field manager(_mainManager) on user interaction with Menu: (new or delete) 
 * 							   to redraw/repopulate the field manager (_mainManager) 
 *  						   This is done so that the label_Id system works for clicking
 *  	   
 *  						   Side note : the manager class/type that _mainManager is an instantiation of "MainScreenManager"
 *  						   Found in "Managers" class 
 *             
 *             checkPermission()- used to check the permissions on the phone for the first time
 *            
 *             
 *                       
 *             
 */

	class IOScreen extends MainScreen
	{
		// @vars : managers -----------------------
		/*
		 * x: instantiation of the Managers class. Used to call ui Screen Managers
		 */
		Managers x = new Managers();
		
		
		/*
		 *  _mainManager : Screen Manager found in Managers class used to layout fields in the screen  
		 */
		MainScreenManager _mainManager = x.new MainScreenManager();
		
		
		/*
		 * datMan: singleton global variable container that holds globally shared data (@see : (class) DataManager)
		 */
		DataManager datMan = new DataManager();
		
		
		/*
		 * gpsTm  - singleton global variable container that holds globally shared GPS information 
 *                                      (@see : (class)GPS_Time)
		 */
		GPS_Time gpsTm = new GPS_Time();
		
		
		// vectors -------------------------
		
		/*
		 * holds all the project objects found in the persitant storage
		 */
		Vector _data = datMan.getProjects();
		
		/*
		 * holds all of the project names (strings) to be used in creating UI labels   
		 */
		Vector proj_name = new Vector();
		
		/*
		 * holds all the bitmaps for projects UI 
		 */
		Vector bitmap = new Vector();
		
		
		// vars -----------------------------------------------------------------
		
		/*
		 * used to identify which project is highlighted in the UI
		 */
		int label_ID;
	
		
		/*
		 * Loop size based on the size of the Project vector
		 */
		int data_Size = _data.size(); 
		
		
		/*
		 * loop counter 
		 */
		int i;
		
		
		/*
		 * font to later be used for the labels
		 */
		Font font;
		
		
		/*
		 * bitmaps for icon focused and unfocused : used in UI
		 */
		Bitmap _icon = Bitmap.getBitmapResource("icon2_onfocus.png");
		Bitmap _iconfocus = Bitmap.getBitmapResource("icon2.png");
		
	  	
		
		
		/*
		 * IOScreen()	
		 */
	  	//Default constructor for the main/opening screen
		public IOScreen()
 		{
			//getting singleton instances
			datMan = DataManager.getInstance();
			gpsTm = GPS_Time.getInstance();
	
			
			checkPermission();
			
			// sets the screens title
			setTitle("NavaGIS Mobile Recon");
			
			// calls setFont
			setFont();
			
			// adds the main field manager to the screen
			add(_mainManager);
 			
			//lays out the icons on the screen
 			makeScreen();
 			
 		}
		
	
		
		/*
		 * setFont()
		 */
		public void setFont()
		{
			 FontFamily fontFamily[] = FontFamily.getFontFamilies();
			 font = fontFamily[1].getFont(FontFamily.CBTF_FONT, 20);
		}
		
		
		// this section is for creating this screen's menu-----------------------------
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
				   
				  /*
				   *  Check result of dialog box
				   *  if ok, then add new project and call makescreen() function
				   *  
				   *  @see : DataManager - .getProjects()
				   *                       .addProject(Project p)
				   */
				  
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
				   //directly deletes the project from the Main persistable object in Data Manager
				   datMan.removeProject(label_ID);
				   makeScreen();
			   }
		    }; 
	     // end menu --------------------------------------------------------
	
		    
		    
	/*
	 * getserver()
	 */
	public void getserver()
	{
	  	UiApplication.getUiApplication().pushScreen(new Server());
	}	    
		    
	
	/*
	 * mybitmapField()
	 *   custom bitmap field that has specific bitmap heights and widths, along with overloading
	 *   onfocus, and click event functions
	 */
	public class mybitmapField extends BitmapField 
   	  {
	   		// used in the Label ID system
			private int ID ;
	   		
	   		// pixels
	   		private int width_height = 52;
	   		
	   		
	   	/*
	   	 * Default Constructor
	   	 */
    	   public mybitmapField(){}
    	 
    	   protected void sublayout(int width, int height) {       
    	       setExtent(width,height);
    	   }
    	   
           //@see : net.rim.device.api.ui.component.BitmapField
    	   public int getBitmapWidth() {
    	       return width_height;
    	   }
    	   
    	   //@see : net.rim.device.api.ui.component.BitmapField
    	   public int getBitmapHeight() {     	      
    	       return width_height;
    	       }
    	   
    	   //@see : net.rim.device.api.ui.component.BitmapField
    	   public boolean isSelectable()
    	   {
    		   return true;
    	   }
    	   
    	   //@see : net.rim.device.api.ui.component.BitmapField
    	   public boolean isFocusable()
    	   {
    		   return true;
    	   }
    	   
    	   //@see : net.rim.device.api.ui.component.BitmapField
    	   public void setID(int id)
    	   {
    		   ID = id;
    	   }
    	   
    	   //@see : net.rim.device.api.ui.component.BitmapField
    	   protected void onFocus(int direction)
		   {
    		   datMan.current = (Project) _data.elementAt(label_ID);
    		   label_ID = ID;
			   this.setBitmap(_iconfocus);
		   }
    	   
    	   //@see : net.rim.device.api.ui.component.BitmapField
    	   protected void onUnfocus()
    	   {
    		   this.setBitmap(_icon);
    	   }
    	   
    	   //@see : net.rim.device.api.ui.component.BitmapField
    	   public boolean trackwheelClick(int status, int time)
    	   {
    	   click();
    	   return true;
    	   }
    	};
    	
    	// accepts click then opens a project screen passing through the project clicked using label_ID
    	   public void click()
		   {
    		UiApplication.getUiApplication().pushScreen(new ProjectScreen((Project) _data.elementAt(label_ID)));
		   }
    // end ------------------------------------------------
    	   
    	   
    	  
     
     /*
      * makeScreen()    	   
      */	   
     public void makeScreen()
      {
   	   // recalculates the dynamic number for looping, getting the number from
       // the datamanagers [data] vector size
    	 
   	   _data = datMan.getProjects(); 
       data_Size = _data.size();
   	   
   	   // clear an unempty fieldmanager
   	   if(_mainManager.getFieldCount()!= 0)
   	   _mainManager.deleteAll();
   		   
   	   if(data_Size==0)
   	   {
   		   LabelField _none = new LabelField();
   		   _none.setText("(No Projects - Press Menu To Add New)");
   		   _mainManager.add(_none);
   	   }
   	   
   	   //repopulate the field manager with icons
   	   for(i=0;i<data_Size;i++)
   		{	
    		
    		// array that holds proj names is adding a new Labelfield
   			proj_name.addElement(new LabelField());    
   	    	((LabelField) proj_name.elementAt(i)).setFont(font);
   	    	
   	    	// makes a project then sets it to the project in the data vector
   			Project p = (Project) _data.elementAt(i);
   			
   			//sets the text in the Labelfield to the name of the project object
   			((LabelField) proj_name.elementAt(i)).setText(p.getName());
   			
    				
   			bitmap.addElement(new mybitmapField());  
    		 ((mybitmapField) bitmap.elementAt(i)).setBitmap(_icon);
        	((mybitmapField) bitmap.elementAt(i)).setID(i);
    		    	
        	_mainManager.add((mybitmapField) bitmap.elementAt(i));
        	_mainManager.add((LabelField) proj_name.elementAt(i));
    		   	 		    	 
    	}
    }
	
     
     
    /*
     * checkPermission() 
     */
    public void checkPermission(){
    	 
    //	Dialog.alert("Read Me: Checking your phone permissions to ensure proper functionality. Just press save on all requests.");
    	
    	 
    	 ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
    	 ApplicationPermissions original = apm.getApplicationPermissions();
    	 
    	 if(original.getPermission( ApplicationPermissions.PERMISSION_FILE_API ) != ApplicationPermissions.VALUE_ALLOW)
    	 {
    		 original.addPermission(original.PERMISSION_FILE_API);
    		 ApplicationPermissionsManager.getInstance().invokePermissionsRequest(original);
    	 }
    	 
    	 if(original.getPermission( ApplicationPermissions.PERMISSION_INTERNET ) != ApplicationPermissions.VALUE_ALLOW)
    	 {
    		 original.addPermission(original.PERMISSION_INTERNET);
    		 ApplicationPermissionsManager.getInstance().invokePermissionsRequest(original);
    	 }
    	 
    	 if(original.getPermission( ApplicationPermissions.PERMISSION_MEDIA ) != ApplicationPermissions.VALUE_ALLOW)
    	 {
    		 original.addPermission(original.PERMISSION_MEDIA);
    		 ApplicationPermissionsManager.getInstance().invokePermissionsRequest(original);
         }
    	 
    	 if(original.getPermission( ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION ) != ApplicationPermissions.VALUE_ALLOW)
    	 {
    		 original.addPermission(original.PERMISSION_CROSS_APPLICATION_COMMUNICATION);
    		 ApplicationPermissionsManager.getInstance().invokePermissionsRequest(original);
    	 }
    	 
    	 if(original.getPermission( ApplicationPermissions.PERMISSION_EVENT_INJECTOR) != ApplicationPermissions.VALUE_ALLOW)
    	 {
    		 original.addPermission(original.PERMISSION_EVENT_INJECTOR);
    		 ApplicationPermissionsManager.getInstance().invokePermissionsRequest(original);
    	 }
    	 
    	 if(original.getPermission( ApplicationPermissions.PERMISSION_RECORDING) != ApplicationPermissions.VALUE_ALLOW)
    	 {
    		 original.addPermission(original.PERMISSION_RECORDING);
    		 ApplicationPermissionsManager.getInstance().invokePermissionsRequest(original);
    	 }
    }
}
	 
		   
	





 

