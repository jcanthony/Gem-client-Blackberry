package persistant.pkg;



import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class CamPreviewScreen extends MainScreen {
	//sets up the managers to hold the fields for a better UI look
	VerticalFieldManager _vfm = new VerticalFieldManager(VerticalFieldManager.USE_ALL_HEIGHT)
	{public void paint(Graphics graphics)
    {
        graphics.setBackgroundColor(0x00BDC29A);// special grey
        graphics.clear();
        super.paint(graphics);
    }};
	HorizontalFieldManager _hfm = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER);
	
	LabelField _saving = new LabelField("Saving Picture Please Wait For Confirmation....",LabelField.HCENTER);
	
	private MediasManager photMan = new MediasManager();
	private DataManager datMan = new DataManager();
	private UiApplication UiApp;
	private CamPreviewScreen _camPrevScreen;
	private CameraScreen camScr;
	
	public CamPreviewScreen(CameraScreen cs)
	{
		camScr = cs;
		
		_camPrevScreen = this;
		
		photMan = MediasManager.getInstance();
		datMan = DataManager.getInstance();

		UiApp = (UiApplication) UiApplication.getApplication();
		
		
		
		// makes the preview out of the Byte array passed to the Photomanager
		//photMan.makeImg();
		photMan.makePrev();
		
		
		
		setTitle("						  Photo Preview");
		// this is done to keep the display stack form becoming cluttered
		// from the back and forth between camera and preview
				
		Bitmap B_preview = new Bitmap(0, 0);
		BitmapField _prev = new BitmapField(null, BitmapField.HCENTER);
		ButtonField Bt_save = new ButtonField("Save",100);
		ButtonField Bt_retake = new ButtonField("Retake",100);
		 
	
		B_preview = photMan.getPreview(); 
		_prev.setBitmap(B_preview);
		 
		add(_vfm);
		 
		 _vfm.add(_prev);
		
		
		
		
		 FieldChangeListener listenSave = new FieldChangeListener() 
			{
				public void fieldChanged(Field field, int context) 
				{
					 wait_save();
					 photMan.makeImg();
					 photMan.makeThumb();
					 
					 Points p = datMan.getPoint();
					 p.setThumb(photMan.getThumb());
					 p = null;
					 photMan.saveImg();
					 photMan.savePrev();
					 //call to the point screen on stack to do functions before we return to it
					 photMan.pntScr.addPhoto();
					 photMan.pntScr.layoutPics();
					 
					 System.gc();
					 
					 camScr.close();
					 _camPrevScreen.close();
					 
					 
				}
			};
			
			
		 
		FieldChangeListener listenRetake = new FieldChangeListener() 
			{
				public void fieldChanged(Field field, int context) 
				{
					camScr.close();
					UiApp.pushScreen(new CameraScreen(photMan.getPhoto()));
					_camPrevScreen.close();
					
				}
			};
			
		Bt_save.setChangeListener(listenSave);
		Bt_retake.setChangeListener(listenRetake);
		
		 _vfm.add(_hfm);
		 _hfm.add(Bt_save);		 
		 _hfm.add(Bt_retake);
	}
	
	public void wait_save()
	{
		_vfm.add(_saving);
	}
}


