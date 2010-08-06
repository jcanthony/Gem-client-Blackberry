package persistant.pkg;

import java.util.Date;
import java.util.Vector;
import net.rim.device.api.util.Persistable;

public class Points implements Persistable {
	private String name;
	private String notes = null;
	private int UID;
	private double lat;
	private double lon;
	private String timestamp;
	private String s_timestamp;
	private Vector photos = null;	
	private Vector videos = null;
	private byte[] thumb = null;
	
		
	
	public Points(String name) {
		super();
		this.name = name;
		photos = new Vector();
		videos = new Vector();
		thumb = null;
		UID = 0;
		
	}		

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getTimestamp() {
		return timestamp;
	
	}
	public void setTimestamp(String timestamp) {
		this.timestamp =  timestamp;
	}
	
	public void sets_Timestamp(String st){
		this.s_timestamp = st;
	}
	
	public String gets_Timestamp(){
		return this.s_timestamp;
	}
	
	public Vector getPhotos(){
		return photos;
	}	
	
	public void removePhoto(int index){
		photos.removeElementAt(index);
	}
//	public Photo getPhoto(int index){
//		return photos.elementAt(index);
//	}	
	public void addPhoto(Photos img){
		photos.addElement(img);
	}	
//	public void removePhoto(int index){
//		photos.removeElementAt(index);
//	}
	
	public Vector getVideos(){
		return videos;
	}
	

	public void addVideo(Video video) {
		videos.addElement(video);
	}	
	public byte[] getThumb() {
		return thumb;
	}
	public void setThumb(byte[] thumb) {
		this.thumb = thumb;
	}
	
	public void setUID(int u){
		this.UID = u;
	}
	
	public int getUID(){
		return this.UID;
	}

	
}
