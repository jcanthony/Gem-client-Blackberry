package persistant.pkg;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.util.Persistable;

public class Managers implements Persistable {

	
	// used on the very first Screen to manage the icons and labels ---------
	//-----------------------------------------------------------------------
	 class MainScreenManager extends Manager 
		{
	   		public MainScreenManager()
	   		{
	   			super(Manager.VERTICAL_SCROLL| Manager.VERTICAL_SCROLLBAR);	
	   		}
	   
	   		protected void sublayout(int width,int height)
	   		{
	   			Field field;
	   			int numberOfFields = getFieldCount();
	   			int x = 30;
	   			int y = 10;
	   			int y1 = 10;
	   			int y2 = y1+60;
	   			int z = 1;
	   		
	   			for (int i =0; i < numberOfFields; i++)
	   			{
	   				field = getField(i);
	   				
	   				layoutChild(field, width, height);
	   				
	   				if (z==1)
	   				{   y = y1;
	   				  	z++;
	   				  	setPositionChild(field,x,y); 
	   				}
	   				else if(z==2)
	   				{	
	   					y = y2;
	   					z = 1;
	   					setPositionChild(field,x,y); 
	   					x += 100;
	   					
	   				}	
	   				
	   				if (x>400)
					{
					 y1+=field.getHeight()+90;
					 y2= y1+60;
					 x = 30;
					 y = y1;
					}
	   				
	   				
	   				
	   			}
	   			setExtent(width, height);
	   		}
	   		
	   	 public void paint(Graphics graphics)
         {
             graphics.setBackgroundColor(0x00BDC29A);// special green/grey
             graphics.clear();
             super.paint(graphics);
         }

	   	 
		}
	 
	 
	 // used on the layer screen----------------------------------------------------
	 //-----------------------------------------------------------------------------
	 public class LayerScreenManager extends Manager
	 {
		 LayerScreenManager()
		 {
			 super(Manager.VERTICAL_SCROLL);
		 }
		protected void sublayout(int width,int height)
		{
			
			Field field;
			int x = 1;
			int x1 = 1;
			int x2 = x1 + 60;
			int y = 1;
			int numberOfFields = getFieldCount();
			int z = 1;
			
			for (int i =0; i < numberOfFields; i++)
   			{
				field = getField(i);
				layoutChild(field, width, height);
				
				if (z==1)
   				{   x = x1;
   				  	z++;
   				  	setPositionChild(field,x,y); 
   				}
   				else if(z==2)
   				{	
   					x = x2;
   					z = 1;
   					setPositionChild(field,x,y); 
   					y += field.getHeight() + 20;
   					
   				}
			}
			setExtent(width,getPreferredHeight());
			
			
		}
		
		public void paint(Graphics graphics)
        {
            graphics.setBackgroundColor(0x00BDC29A);// special blue
            graphics.clear();
            super.paint(graphics);
        }
		
		public int getPreferredHeight() 
	 	{
	       int height= 0;
	       int numberOfFields= getFieldCount();
	       for (int i= 0; i < numberOfFields; i++) {
	           height+=27;   
	       	}
	       return height;
	 	}

		

	 }
	 
	 // used on the pointscreen--------------------------------
	 //--------------------------------------------------------
	 class PhotoFieldManager extends Manager
	 {
		 PhotoFieldManager()
		 {
			 super(Manager.VERTICAL_SCROLL);
		 } 
		 
		 protected void sublayout(int width,int height)
			{
				setExtent(Display.getWidth(),height);
				
				Field field;
				int x =20; 
				int y = 10;
				int numberOfFields = getFieldCount();
				
				for (int i =0; i < numberOfFields; i++)
	   			{
	   				field = getField(i);
	 
	   				layoutChild(field, 204 , 178);
	   						
	   				if(x > 400)
	   				{
	   				x=20;
	   				y+= field.getHeight() + 20;
	   				setPositionChild(field,x,y);
	   				}
	   				else
	   				{
	   				setPositionChild(field,x,y);
	   				x += 200;	
	   				}
	   				
	   			}
					
			}
		 
		 public void paint(Graphics graphics)
	        {
	            graphics.setBackgroundColor(0x00BDC29A);// special blue
	            graphics.clear();
	            super.paint(graphics);
	        }
		 
		 public int getPreferredHeight() 
		 	{
		       int height= 0;
		       int numberOfFields= getFieldCount();
		       for (int i= 0; i < numberOfFields; i++) {
		           height+=200;   
		       	}
		       return height;
		 	}
		 
		 
		 
	}
	 
	 
	 
}
