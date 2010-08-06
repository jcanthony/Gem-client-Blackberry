package persistant.pkg;
import java.util.Vector;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.SeparatorField;




public class DataManager 
{
	
	private static DataManager instance = null;
	private static Vector data = new Vector();
	private static Points point ;
	Project current;
	static PersistentObject store;
	static PersistentObject serverhist;
	private static String server_url = "http:\\\\67.23.2.198 ";
	private static Vector server_hist = new Vector();
		
	//private Server server;
	
		// will need to include file reader and Much more
	protected DataManager()
	{
		//server = new Server();
	}
	
	public static DataManager getInstance()
	{
		if(instance == null){
			instance = new DataManager();
		}
		return instance;
			
	}
	
	public void addProject(Project p)
	{
		data.addElement(p);
		store.commit();
	}
	
	public void removeProject(int index)
	{
		data.removeElementAt(index);
		store.commit();
	}
	
	public void addURL(String url)
	{
		server_hist.addElement(url);
		serverhist.commit();
	}
	
	public void clearURLs()
	{
		server_hist.removeAllElements();
		serverhist.commit();
	}
	public Vector getURL_List()
	{
		return server_hist;
	}
	
	public Project getProject(int index){
		return (Project) data.elementAt(index);
	}
	
	public Vector getProjects(){
		return data;
	}
	
	public Points getPoint()
	{
		return point;
	}
	
	public  void setPoint(Points pt)
	{
	 point = pt;	
	}
	

	
	{
		store =
			PersistentStore.getPersistentObject(0x12345L);
		synchronized (store) {
			if (store.getContents() == null) {
				store.setContents(new Vector());
				store.commit();		
			}
		}
		data = (Vector) store.getContents();
	}
	
	{
		serverhist =
			PersistentStore.getPersistentObject(0x12346L);
		synchronized (serverhist) {
			if (serverhist.getContents() == null) {
				serverhist.setContents(new Vector());
				serverhist.commit();		
			}
		}
		server_hist = (Vector) serverhist.getContents();
	}
	
	 final class TestDialog extends Dialog{

	        private EditField userNameField;
	        private SeparatorField s,s1;
	        private Font ft;
	        
	      public TestDialog(String type)
	      {
	        super(Dialog.D_OK_CANCEL,"New " + type,1,Bitmap.getPredefinedBitmap(Bitmap.INFORMATION),Manager.FOCUSABLE);
	        s=new SeparatorField();
	        add(s);
	        if(type.equalsIgnoreCase("Server"))
	        {userNameField = new EditField("Enter " + type + " address: ", "", 50, EditField.EDITABLE|EditField.NO_NEWLINE);}
	        else
	        userNameField = new EditField("Enter " + type + " Name: ", "", 50, EditField.EDITABLE|EditField.NO_NEWLINE);
	      
	        ft=this.getFont().derive(FontFamily.SCALABLE_FONT, 18);
	        userNameField.setFont(ft);
	        add(userNameField);
	        s1=new SeparatorField();
	        add(s1);

	           
	     }            
	          public String getUsernameFromField(){
	        	 
	        		return userNameField.getText();
	        		
	        }

	    }
	 
	public String getURL()
	{
		return server_url;
	}
	
	public void setURL(String _url)
	{
	 server_url = _url;	
	}
	
	
}