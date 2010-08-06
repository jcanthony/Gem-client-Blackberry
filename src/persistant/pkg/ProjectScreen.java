package persistant.pkg;
import java.util.Date;
import java.util.Vector;

import persistant.pkg.DataManager.TestDialog;
import persistant.pkg.Managers.LayerScreenManager;

import net.rim.device.api.synchronization.UIDGenerator;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;


public class ProjectScreen extends MainScreen {
	
	DataManager datMan = DataManager.getInstance();
	MediasManager medMan = MediasManager.getInstance();
	int i = 0;
	int label_ID ;
	int ProjIndex;
	int data_Size;
	Project project ;
	
	Vector V_layer;
	
	Vector layer_Name = new Vector(); 
	Vector bitmap = new Vector();
	
	
	Date timestamp;
	long tpID;
	long tlID;
	int UID;
	Bitmap _folder = Bitmap.getBitmapResource("folder_icon3.png");
	Bitmap _folderFocus = Bitmap.getBitmapResource("folder_icon4.png");
	
	
	
	
	VerticalFieldManager vfm = new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH|VerticalFieldManager.USE_ALL_HEIGHT){public void paint(Graphics graphics)
    {
        graphics.setBackgroundColor(0x00BDC29A);// special grey
        graphics.clear();
        super.paint(graphics);
    }};
    
    Managers m = new Managers();
    LayerScreenManager _LsMan = m.new LayerScreenManager();
	
	
	
	
	// Default Constructor
	public ProjectScreen(Project pro)
	{
		project = pro;
		V_layer = project.getLayers();
		data_Size = V_layer.size();
		
				
		setTitle("Project: " + project.getName());
		
		makeScreen();
		
		
		vfm.add(_LsMan);
		add(vfm);	
	}
	
	
	// Building the Menu---------------------------------------------------------------------
	// Menu Items:
	//    1) New Layer
	//    2) Server
	//	  3) Delete Layer
	//	  4) Close Program
	//    5/6) BBerry defaults - not programatically seen here, but seen on screen
	
	 protected void makeMenu(Menu menu, int instance) {
		
			menu.add(_new);
			menu.add(MenuItem.separator(100));
			menu.add(_server);
			menu.add(_del);
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
		   public void run() {
			   UiApplication.getUiApplication().pushScreen(new Server());
		   }
	   
	   };
	   
	   private MenuItem _new = new MenuItem("New Layer", 110,
				10) 
	   {
		   public void run() {
			   TestDialog t = datMan.new TestDialog("Layer");
			   int result = t.doModal();
			   
			   if(result == Dialog.OK)
			   {
				   Layer l = new Layer(t.getUsernameFromField());
				   UID = UIDGenerator.getUID();
				   l.setUID(UID);
				   V_layer.addElement(l);
				   V_layer = project.getLayers();
				   makeScreen();
			   }
			   else{t.close();}
			   
		   	}
			   
	   };
	   
	   private MenuItem _del = new MenuItem("Delete Layer", 110,
				10) 
	   {
		   public void run() {
			   medMan.get_photos((Layer) V_layer.elementAt(label_ID), true);
			   medMan.DeletePhotLayer();
			   project.removeLayer(label_ID);
			   makeScreen();
		   }
	   
	   };
	   // end of menu -------------------------------------------------------------------------
	 
	   
	// Custom Clickable Bitmapfield ----------------------------------------------------------- 
	//   Overloaded BitmapField That Allows for clicking
	// 	 used onscreen as the Folder icon
	//   ex)   Folder Icon (mybitmapField) | Layer Name
	   
	   public class mybitmapField extends BitmapField 
	   	  {
		   		// used in the Label ID system
				private int ID ;
		   		
		   		// pixels
		   		private int width_height = 48;
		   		
		   		
		   	// custom bitmap : [overloads] focusable, selectable(click), onFocus, onUnFocus
		    //                             		         
		    //                 [custom] setId (sets id for clicking)
		    //
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
	    	   
	    	   
	    	   //changes ID to pics id when Highlighted
	    	   protected void onFocus(int direction)
			   {
				   label_ID = ID;
				   this.setBitmap(_folderFocus);
			   }
	    	   
	    	   protected void onUnfocus()
	    	   {
	    		   this.setBitmap(_folder);
	    	   }
	    	   
	    	   
	    	   public boolean trackwheelClick(int status, int time)
	    	   {
	    	   click();
	    	   return true;
	    	   }
	    	};
	   
	  
	   
	   public void click()
	   {
		  UiApplication.getUiApplication().pushScreen(new LayerScreen((Layer) V_layer.elementAt(label_ID)));
	   }
	   // end of custom Bitmapfield -----------------------------------------------------
	   
	   
	   // 	Builds The UI----------------------------------------------------------------
	   //   	Used in conjunction with Managers class (LayerScreenManager - _LsMan)
	   //		_LsMan declared above
	   public void makeScreen()
	   {
		   	V_layer = project.getLayers();
		   	data_Size = V_layer.size();
		
		   	if(_LsMan.getFieldCount() != 0)
		   		_LsMan.deleteAll();
		   	
		    for(i=0;i<data_Size;i++)
			{	
				
				layer_Name.addElement(new LabelField());    
		    	Layer l = (Layer) V_layer.elementAt(i);
				((LabelField) layer_Name.elementAt(i)).setText(l.getName());
				
				
				bitmap.addElement(new mybitmapField());  
				 ((mybitmapField) bitmap.elementAt(i)).setBitmap(_folder);
		    	((mybitmapField) bitmap.elementAt(i)).setID(i);
		    	
		    	_LsMan.add((mybitmapField) bitmap.elementAt(i));
		    	_LsMan.add((LabelField) layer_Name.elementAt(i));
		    	_LsMan.add(new SeparatorField(SeparatorField.LINE_HORIZONTAL));
		    	_LsMan.add(new  SeparatorField(SeparatorField.LINE_HORIZONTAL));
		    
		    	 		    	 
			}			
	   }
	 // end build ui----------------------------------------------------------------
	   
	   
	    
}
