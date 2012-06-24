package ffts.android.moefmdroid;

public class Song {

	private String mID;
	private String mTitle;
	private String mAblum;
	private String mArtist;
	private String mCover;
	private String mUrl;
	private int isLike = 0;
	
	public Song(){
		mID = "none";
		mTitle = "none";
		mAblum = "none";
		mArtist = "none";
		mCover = "none";
		mUrl = "none";
	}
	
	public void setID(String id ){
		this.mID = id;
	}
	
	public String getID(){
		return this.mID;
	}
	
	public void setTitle(String title){
		this.mTitle = title;
	}
	
	public String getTitle(){
		return this.mTitle;
	}
	
	public void setAblum(String ablum){
		this.mAblum = ablum;
	}
	
	public String getAblum(){
		return this.mAblum;
	}
	
	public void setArtist(String artist){
		this.mArtist = artist;
	}
	
	public String getArtist(){
		return this.mArtist;
	}
	
	public void setCover(String cover){
		this.mCover = cover;
	}
	
	public String getCover(){
		return this.mCover;
	}
	
	public void setUrl(String url){
		this.mUrl = url;
	}
	
	public String getUrl(){
		return this.mUrl;
	}
	
	public void isLike(int flag){
		this.isLike = flag;
	}
	
	public int isLike(){
		return this.isLike;
	}
}
