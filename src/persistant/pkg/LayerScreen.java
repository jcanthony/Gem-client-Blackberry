package persistant.pkg;

import java.util.Vector;

import net.rim.device.api.synchronization.UIDGenerator;
import persistant.pkg.DataManager.TestDialog;
import persistant.pkg.Managers.LayerScreenManager;


import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
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

public class LayerScreen extends MainScreen {
	// instantiate the managers and ID Gen
	DataManager dataMan = DataManager.getInstance();
	MediasManager medMan = MediasManager.getInstance();
	GPS_Time gps = new GPS_Time();

	
	int label_ID;
	// 0 for layer, 1 for points
	int manID = 0; 
	
	// vectors for bitmaps and labelfields
	Vector p_bitmap = new Vector();
	Vector l_bitmap = new Vector();
	Vector p_Name = new Vector();
	Vector l_Name = new Vector();
	
	// Loop counters and loop sizes
	int i;
	int j;
	int layer_size;
	int point_size;
	int UID;
	
	// imported information
	Project p;
	Layer l;
	
	// vectors to get layer data
	Vector v_Layer;
	Vector v_Point;
	Vector v_Route;
	
	// Managers to create screen
	Managers m = new Managers();
	VerticalFieldManager main_Man =new VerticalFieldManager(VerticalFieldManager.USE_ALL_HEIGHT | VerticalFieldManager.USE_ALL_WIDTH)
	{
		 public void paint(Graphics graphics)
         {
             graphics.setBackgroundColor(0x00BDC29A);// special green/grey
             graphics.clear();
             super.paint(graphics);
         }

	};
	LayerScreenManager L_Man = m.new LayerScreenManager();
//	{
//		  protected void onFocus(int direction)
//		   {
//			   manID = 1;
//		   }
//	};
	LayerScreenManager P_Man = m.new LayerScreenManager();
//	{
//		  protected void onFocus(int direction)
//		   {
//			   manID = 0;
//		   }
//	};
	
	
	// bitmaps for icons
	Bitmap bmp_Layer = Bitmap.getBitmapResource("folder_icon3.png");
	Bitmap bmp_Point =  Bitmap.getBitmapResource("blue_pushpin.png");
	
	
	
	
	
	
	
	
	public LayerScreen(Layer lay)
	{
		dataMan = DataManager.getInstance();
		medMan = MediasManager.getInstance();
		
		l = lay;
		v_Layer = lay.getLayers();
		v_Point = lay.getsPoints();
		
		layer_size = v_Layer.size();
		point_size = v_Point.size();
		
		
		
		setTitle("Layer : " + l.getName());
		
				
		layoutLayers();
		layoutPoints();
		
		main_Man.add(L_Man);
		main_Man.add(new SeparatorField(SeparatorField.LINE_HORIZONTAL));
		main_Man.add(P_Man);
		
		
		
		
		add(main_Man);
		

}
	
	 protected void makeMenu(Menu menu, int instance) {
			
			menu.add(new_L);
			menu.add(new_P);
			menu.add(MenuItem.separator(100));
			menu.add(_save);
			menu.add(del_L);
			menu.add(del_P);
			menu.add(MenuItem.separator(100));
			menu.add(_close);
							
			}
	  
	  private MenuItem _close = new MenuItem("Close", 110,
				10) 
	   {
		   public void run() {
			   System.exit(0);
		   }
	   
	   };
	   
	   private MenuItem _save = new MenuItem("Save", 110,
				10) 
	   {
		   public void run() {
			 getServer();
		   }
	   
	   };
	   
	   private MenuItem new_L = new MenuItem("New Layer", 110,
				10) 
	   {
		   public void run() {
			   TestDialog t = dataMan.new TestDialog("Layer");
			   int result = t.doModal();
			   
			   if( result == Dialog.OK)
			  {
			   Layer layr = new Layer(t.getUsernameFromField());
			   UID = UIDGenerator.getUID();
			   layr.setUID(UID);
			   v_Layer.addElement(layr);
			   l.setLayers(v_Layer);
			   
			   layoutLayers();
			     
			  }
		   	}
			   
	   };
	   
	   private MenuItem new_P = new MenuItem("New Point", 110,
				10) 
	   {
		   public void run() {
			   TestDialog t = dataMan.new TestDialog("Point");
			   int result = t.doModal();
			   
			   if( result == Dialog.OK)
			  {
				
			   Points pnt = new Points(t.getUsernameFromField());
			   setGPS(pnt);
			   v_Point.addElement(pnt);
			   l.setPoints(v_Point);
			   layoutPoints();
			   dataMan.setPoint(pnt);
			   UID = UIDGenerator.getUID();
			   pnt.setUID(UID);
			   UiApplication.getUiApplication().pushScreen(new PointScreen());
			   }
		   	}
			   
	   };
	   
	   private MenuItem del_L = new MenuItem("Delete Layer", 110,
				10) 
	   {
		   public void run() {
			  if(L_Man.getFieldCount() > 0)
			  {
			   
			   medMan.get_photos((Layer) v_Layer.elementAt(label_ID),true);
			   medMan.DeletePhotLayer();
			   l.removeLayer(label_ID);
			   layoutLayers();
			  }
			   
			   
		   }
	   
	   };
	   
	   private MenuItem del_P = new MenuItem("Delete Point", 110,
				10) 
	   {
		   public void run() {
			   if(P_Man.getFieldCount() > 0)
			   {
				   Points del =  (Points) v_Point.elementAt(label_ID);
				   medMan.DeletePointPhot(del);
				   l.removesPoint(label_ID);
				   layoutPoints();
			   }
		   }
	   
	   };
	
	   
	   //  custom overloaded bitmap made to be clickable
	   // 
	   public class mybitmapField extends BitmapField 
	   	  {
		   		// used in the Label ID system
				private int ID ;
				private int mID;
		   		
		   		// pixels
		   		private int width_height = 48;
		   		
		   		
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
	    	   
	    	   // used to differentiate between points and layers
	    	   public void setmID(int mid)
	    	   {
	    		   mID = mid;
	    	   }
	    	   
	    	   
	    	   //changes ID to pics id when Highlighted
	    	   protected void onFocus(int direction)
			   {
				   label_ID = ID;
				   manID = mID;
				   
			   }
	    	   protected void onUnfocus()
	    	   {
	    		   if(mID == 0)
	    		   this.setBitmap(bmp_Layer);
	    	   }
	    	   
	    	   
	    	   public boolean trackwheelClick(int status, int time)
	    	   {
	    	   click();
	    	   return true;
	    	   }
	    	};
	   
	 
	 public void click()
	   {
		 if (manID == 0){
		   UiApplication.getUiApplication().pushScreen(new LayerScreen((Layer) v_Layer.elementAt(label_ID)));
		   }
		 else
		   {
		   dataMan.setPoint((Points) v_Point.elementAt(label_ID));
		   UiApplication.getUiApplication().pushScreen(new PointScreen());
		   }
	   }
	 
	 // this accesses the gps manager to get the current location informaiton 
	 public void setGPS(Points P) {
		 
		
		 gps = GPS_Time.getInstance();
		
		 
		 P.setLat(gps.getLat());
		 P.setLon(gps.getLong());
		 P.setTimestamp(gps.getTime());
		 P.sets_Timestamp(gps.getSTime());
		 
	 }
	 
	 // creates first half of UI by laying out the layers sublayers
	 public void layoutLayers()
	 {
		 v_Layer = l.getLayers();
		 layer_size = v_Layer.size();
		 
		 if(L_Man.getFieldCount() != 0)
			 L_Man.deleteAll();
		 
		 
		 for(i=0;i<layer_size;i++)
			{	
				
				l_Name.addElement(new LabelField());    
		    	Layer l = (Layer) v_Layer.elementAt(i);
				((LabelField) l_Name.elementAt(i)).setText(l.getName());
				
				
				l_bitmap.addElement(new mybitmapField());  
				 ((mybitmapField) l_bitmap.elementAt(i)).setBitmap(bmp_Layer);
		    	((mybitmapField) l_bitmap.elementAt(i)).setID(i);
		    	((mybitmapField) l_bitmap.elementAt(i)).setmID(0);
		    	
		    	
		    	L_Man.add((mybitmapField) l_bitmap.elementAt(i));
		    	L_Man.add((LabelField) l_Name.elementAt(i));
		    	 		    	 
			}
		 
	}
	 
	// creates the second half of ui by laying out layers points (thumbnail if a pic has been taken, point icon if not)
	 public void layoutPoints() 
	 {
		 v_Point = l.getsPoints();
		 point_size = v_Point.size();
		 
		 if(P_Man.getFieldCount() != 0)
			 P_Man.deleteAll();
		 
		 for(j=0;j<point_size;j++)
			{	
				
				p_Name.addElement(new LabelField());    
		    	Points p = (Points) v_Point.elementAt(j);
				((LabelField) p_Name.elementAt(j)).setText(p.getName());
				
				
				p_bitmap.addElement(new mybitmapField());  
				// checks for thumb in point
				setBitmaps(p);
		    	((mybitmapField) p_bitmap.elementAt(j)).setID(j);
		    	((mybitmapField) p_bitmap.elementAt(j)).setmID(1);
		    	P_Man.add((mybitmapField) p_bitmap.elementAt(j));
		    	P_Man.add((LabelField) p_Name.elementAt(j));
		    	 		    	 
			}
	
	 }
	 
	 
	// sets the bitmap for the thumbnails
	// p : point that is copy created from the object stored in the layer's point vector
	//
	// mybitmapField : custom bitmapField created that allows for selection and custom sizing
	public void setBitmaps(Points p)
	{
		if(p.getThumb() == null)
		{
		((mybitmapField) p_bitmap.elementAt(j)).setBitmap(bmp_Point);	
		}
		else
		{
		Bitmap thumbnail =  Bitmap.createBitmapFromBytes(p.getThumb(), 0, p.getThumb().length, 1);
		
		((mybitmapField) p_bitmap.elementAt(j)).setBitmap(thumbnail);	
		}
	}
	
	 // called from the server menu item to launch a new server screen
	   public void getServer()
		 {
			  	UiApplication.getUiApplication().pushScreen(new Server());
		 }	
	   
	  }
