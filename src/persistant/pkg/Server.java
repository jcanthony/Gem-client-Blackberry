package persistant.pkg;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;

import persistant.pkg.DataManager.TestDialog;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class Server extends MainScreen {
	
	static DataManager datMan = new DataManager();
	static MediasManager medMan = new MediasManager();
	String URL;
	Vector url_list = new Vector();
	RadioButtonGroup _radioButtonGroup;
	JSON o_json = new JSON();
	String s_JSON;
	
	// setting the color for the background
	VerticalFieldManager _vfm = new VerticalFieldManager(VerticalFieldManager.USE_ALL_HEIGHT|VerticalFieldManager.USE_ALL_WIDTH)
	{
		public void paint(Graphics graphics)
	    {
	        graphics.setBackgroundColor(0x00BDC29A);// special grey
	        graphics.clear();
	        super.paint(graphics);
	    }
	};
	
	VerticalFieldManager _vfm2 = new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH);
	
	
	
	HorizontalFieldManager _hfm = new HorizontalFieldManager();
	
	ButtonField b_Upload = new ButtonField("Upload",100);
	ButtonField b_Edit = new ButtonField("Edit",100);
	ButtonField b_Clrhist = new ButtonField("Clear History",100);
	//overloading the inherited bg color 
	BasicEditField b_URL = new BasicEditField("", datMan.getURL())
	{
		public void paint(Graphics graphics)
	    {
	        graphics.setBackgroundColor(0x00FFFFFF);//  white
	        graphics.clear();
	        super.paint(graphics);
	    }
	};
	LabelField l_urllist = new LabelField("URL HISTORY :");
	LabelField _url = new LabelField("URL :");
	
	
	public Server()
	{
		
		datMan = DataManager.getInstance();
		medMan = MediasManager.getInstance();
		url_list = datMan.getURL_List();
		URL = datMan.getURL();
		setTitle("Server Page");
		createUI();
				
			
	}

	// Lays out the User Interface
	// 
	// 
	public void createUI()
	{
	_vfm.add(_url);
	_vfm.add(b_URL);
	_vfm.add(new LabelField());
	
	_hfm.add(b_Edit);
	_hfm.add(b_Upload);
	_hfm.add(b_Clrhist);
	
	_vfm.add(_hfm);
	_vfm.add(new SeparatorField(SeparatorField.LINE_HORIZONTAL));
	_vfm.add(l_urllist);
	createURLLIST();
	
	_vfm.add(_vfm2);
	add(_vfm);
	
	
	FieldChangeListener FL_edit= new FieldChangeListener() 
	{
		public void fieldChanged(Field field, int context) 
		{
			 TestDialog t = datMan.new TestDialog("Server");
			   int result = t.doModal();
			   
			  if(result == Dialog.OK)
			  {
				 datMan.setURL(t.getUsernameFromField());
				 b_URL.setText(t.getUsernameFromField());
				 datMan.addURL(t.getUsernameFromField());
				 createURLLIST();
			  }
			  else if(result == Dialog.CANCEL)
			  {
				  t.close();
			  }
		}
	};
	
	FieldChangeListener FL_upload= new FieldChangeListener() 
	{
		public void fieldChanged(Field field, int context) 
		{
			SEND();
		}
	};
	
	FieldChangeListener FL_clrhist= new FieldChangeListener() 
	{
		public void fieldChanged(Field field, int context) 
		{
			 int ret = Dialog.ask(Dialog.D_YES_NO,"Are You Sure",Dialog.YES);
			   
			  if(ret == Dialog.YES)
			  {
				 datMan.clearURLs();
				 url_list = datMan.getURL_List();
				 createURLLIST();
				 
			  }
			  else if(ret == Dialog.NO)
			  {
				 // not sure if i will need this
			  }
		}
	};
		
	b_Edit.setChangeListener(FL_edit);
	b_Clrhist.setChangeListener(FL_clrhist);
	b_Upload.setChangeListener(FL_upload);
	b_Edit.setFocus();
	}
	
	public void createURLLIST()
	{
		if(_vfm2.getFieldCount() != 0)
			_vfm2.deleteAll();
		
		  _radioButtonGroup = new RadioButtonGroup();
		  
		  if(url_list.size() == 0){
			  _vfm2.add(new LabelField("(no recent history)"));  
		  }
		  else{
			for(int i = 0; i < url_list.size(); i++)
	        {
	            RadioButtonField buttonField = new RadioButtonField((String) url_list.elementAt(i));
	            _radioButtonGroup.add(buttonField);
	            _vfm2.add(buttonField);
	        }
		  }  
	        
	        FieldChangeListener url_changer= new FieldChangeListener() 
	    	{
	    		public void fieldChanged(Field field, int context) 
	    		{
	    			 b_URL.setText((String) url_list.elementAt(_radioButtonGroup.getSelectedIndex()));
	    		}
	    	};
	    	
	    	    	
	    	 _radioButtonGroup.setChangeListener(url_changer);
	}
	
	
	public void SEND()
	{
//	String s_JSON = o_json.createJson();
//	MainScreen m = new MainScreen();
//	LabelField l = new LabelField(s_JSON);
//	m.add(l);
//	UiApplication.getUiApplication().pushModalScreen(m);
	
	Dialog.alert("Project Sending Initiated. \n Do not exit program!\n Please wait for a response. \n(May take a few minutes to complete)");
	new sendThread().start();	
	}
	
	
	class sendThread extends Thread{
		
		Vector photo_urls;
		Vector photo_names;
		Project proj = datMan.current;;
		String url = b_URL.getText();
		String s_JSON = o_json.createJson();
		StringBuffer b ;
	
		public void run(){
			HttpConnection c ;
			OutputStream os = null;
		
				
			//string vars 
			String boundary = "nv:)nvnvnvnvnvnvAnthologyVnvnvnvnvnvnv(:nv";
			String end = "\r\n";
			String twoHyphens = "--";
		
			//response var
			int response_code;
			int buffersize = 100000; 
		
			byte[] byteArray = new byte[0]; 
		
//			
//		
//		
		
			try{
				c = (HttpConnection)Connector.open(url + ";deviceside=true");
				c.setRequestMethod("POST");
				c.setRequestProperty("Connection", "Keep-Alive");
				c.setRequestProperty("Charset", "UTF-8");
				c.setRequestProperty("Content-Type", "multipart/form-data;boundary="+ boundary);
				//			c.setRequestProperty("Transfer-Encoding", "chunked");
				os = c.openOutputStream();
				
				os.write((twoHyphens + boundary + end).getBytes());
				os.write(("Content-Disposition: form-data;name=\"project\"" + end + end).getBytes());
//				os.write(("Content-Type: text/plain \n\n" + end).getBytes());
				os.write((proj.getName() + end).getBytes());
				os.write((twoHyphens + boundary + end).getBytes());
				os.write(("Content-Disposition: form-data;name=\"jsonFile\"; filename=\"json.json\"" + end).getBytes());
				os.write(("Content-Type: application/octet-stream"+end+end).getBytes());
				os.write((s_JSON + end).getBytes());
				os.write((twoHyphens + boundary + end).getBytes());
			
			
				// gets all photos for the project
				for(int i = 0;i<proj.getLayers().size();i++){
					Vector layers = proj.getLayers();
					medMan.get_photos((Layer) layers.elementAt(i),false);
				}
			
				photo_urls = medMan.getPhotourls();
				photo_names = medMan.getPhotonames();
			
			 
		    	for(int i = 0;i<photo_urls.size();i++){
		       		String name = (String) photo_names.elementAt(i);
		      		    
		       		//----pulls photo from file one at a time before send---	   
		       		FileConnection fconn = (FileConnection)Connector.open((String) photo_urls.elementAt(i), Connector.READ);
		       			if (fconn.exists()) 
		       			{	     	                    
		       				int fileSize = (int)fconn.fileSize();
		       				byteArray = new byte[fileSize];
		       				InputStream input = fconn.openInputStream();
		       				if (fileSize > 0) {
		       					input.read(byteArray);
		       					input.close();
		       				}
		       				fconn.close();
		       			}
		       		//---end file pull 
		    
		       		int full = byteArray.length;
		    
		       	   os.write(("Content-Disposition: form-data; name=\"" + name + "\";filename=\"" + name + ".jpg" + "\"" + end).getBytes());
		       	   os.write(("Content-Type: image/pjpeg"+end).getBytes());
		       	   os.write(("Content-Length:"+ full + end + end).getBytes());
		    	   os.write(byteArray);
		    	   
		    	   //if(i==(photo_urls.size()-1))
		    	   os.write((end + twoHyphens + boundary + end).getBytes());
		    	   //else
		    	   //
		    	   
				  // 
			

			
				byteArray = null;
						
		      }
		    //os.write((end + twoHyphens + boundary + twoHyphens).getBytes());
		    
		    os.flush();
		    os.close();
		    
			InputStream is = c.openDataInputStream();
			b = new StringBuffer();
			byte[] data = new byte[buffersize];
			int leng = -1;
			while((leng = is.read(data)) != -1) {
			   b.append(new String(data, 0, leng));
			}
			
			UiApplication.getUiApplication().invokeAndWait(new Runnable(){
				public void run(){
				Dialog.alert("PROJECT : " + b);}
				});
			}
		catch(Exception e){Dialog.alert("Err:" + e);}
		}
	}
}


	




