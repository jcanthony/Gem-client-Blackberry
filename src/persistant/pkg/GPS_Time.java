package persistant.pkg;


import java.util.Date;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import net.rim.device.api.ui.component.Dialog;

public class GPS_Time {
	static GPS_Time instance = null;
	private double Longitude = 0.0;
	private double Latitude = 0.0;
	private String TimeStamp = "00000000";
	Location local;
	
	public static GPS_Time getInstance()
	{
		if(instance == null){
			instance = new GPS_Time();
		}
		return instance;
	}
	
	public GPS_Time() 
	{
		LocationProvider provider ;
		
		
				
		Criteria c = new Criteria();
			c.setHorizontalAccuracy(Criteria.NO_REQUIREMENT);
			c.setVerticalAccuracy(Criteria.NO_REQUIREMENT);
			c.setCostAllowed(true);
			c.setPreferredPowerConsumption(Criteria.POWER_USAGE_HIGH);
		
		try {
			provider = LocationProvider.getInstance(c);
			provider.setLocationListener(new LocationListenerImpl(), -1, 1, 1);
			}catch(LocationException e){
				Dialog.alert("Error : " + e);
			}
	
	}
	
			private class LocationListenerImpl implements LocationListener
		    {
			
				private LocationListenerImpl()
				{
					
				}
				
			  public void locationUpdated(LocationProvider provider, Location location)
		        {
				  if(location.isValid())
				  {
				  	Longitude = location.getQualifiedCoordinates().getLongitude();
				  	Latitude = location.getQualifiedCoordinates().getLatitude();
				  	Date times = new Date(location.getTimestamp());
				  	TimeStamp = "		Time : " + times.toString(); 
				  }
		        }
			  
			  public void providerStateChanged(LocationProvider provider, int newState)
		        {
		            // Not implemented.
		        }      
		    }
	
		public double getLat()
		{
			return Latitude;
		}
		
		public double getLong()
		{
			return Longitude;
		}
		
		public String getTime()
		{
			return TimeStamp;
		}
			 
}
	
	


 


