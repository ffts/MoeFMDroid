package ffts.android.moefmdroid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class User {

//	private String mAccessToken;
//	private String mAccessTokenSecret;;
	private static String mConsumerKey = "3e28e27549737c96f4fae920b1a3191d05003bb10";
	private static String mConsumerSecret = "417dda4cadc840ae283d52b01507f3a6";
	private CommonsHttpOAuthConsumer mConsumer;
	public static String uListened = "http://moe.fm/ajax/log?log_obj_type=sub&log_type=listen&obj_type=song&api=json&obj_id=";
	public static String uLike = "http://api.moefou.org/fav/add.json?fav_obj_type=song&fav_type=1&save_status=1&fav_obj_id=";
	public static String uUnLike = "http://api.moefou.org/fav/add.json?fav_obj_type=song&fav_type=2&save_status=1&fav_obj_id=";
	public static String uDeleteFav = "http://api.moefou.org/fav/delete.json?fav_obj_type=song&fav_obj_id=";
	public static String uUser = "http://api.moefou.org/user/detail.json";
	public static String uLikeAblum = "http://api.moefou.org/fav/add.json?fav_obj_type=music&fav_type=1&save_status=1&fav_obj_id=";
	public static String uDeleteFavAblum = "http://api.moefou.org/fav/delete.json?fav_obj_type=music&fav_obj_id=";
	public static int TYPE_SONG = 0;
	public static int TYPE_MUSIC = 1;
	public static int TYPE_RADIO = 2;
//	private CommonsHttpOAuthProvider mProvider;
	public int mStatus;
	
	private long mID;
	private String mName;
	private String mNickName;
	
	public User(String token, String secret){
//		this.mAccessToken = token;
//		this.mAccessTokenSecret = secret;
		mConsumer = new CommonsHttpOAuthConsumer(mConsumerKey, mConsumerSecret);
		mConsumer.setTokenWithSecret(token, secret);
//		getUserInfo();
//		Log.i("MOE", "token:"+token);
//		Log.i("MOE", "token_s:"+secret);
	}

	public JSONObject requestPlayList(String url) 
			/*throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, 
			ParseException, IOException, JSONException*/
	{
		
		HttpPost httpPost = new HttpPost();
		HttpClient client = new DefaultHttpClient();
		org.apache.http.HttpResponse response = null;
		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpPost.setURI(uri);
		try {
			mConsumer.sign(httpPost);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Log.i("MOE", httpPost.getURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			response = client.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("MOE", response.getEntity().toString());
		Log.i("MOE", "HttpStatus:"+Integer.toString(response.getStatusLine().getStatusCode()));
		if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {

			mStatus = response.getStatusLine().getStatusCode();
			String result = null;
			try {
				result = EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			Log.i("MOE", result);
			JSONObject jso = null;
			try {
				jso = new JSONObject(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jso;
			
		}else if(HttpStatus.SC_UNAUTHORIZED == response.getStatusLine().getStatusCode()){
			mStatus = response.getStatusLine().getStatusCode();
			return null;
		}else return null;
		
	}
	
	public void setListened(String id){
		HttpPost httpPost = new HttpPost();
		HttpClient client = new DefaultHttpClient();
		URI uri = null;
		try {
			uri = new URI(uListened+id);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpPost.setURI(uri);
		try {
			mConsumer.sign(httpPost);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Log.i("MOE", httpPost.getURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			client.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*public void setLike(String id){
		HttpPost httpPost = new HttpPost();
		HttpClient client = new DefaultHttpClient();
		URI uri = null;
		try {
			uri = new URI(uLike+id);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpPost.setURI(uri);
		try {
			mConsumer.sign(httpPost);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Log.i("MOE", httpPost.getURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			client.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setUnLike(String id){
		HttpPost httpPost = new HttpPost();
		HttpClient client = new DefaultHttpClient();
		URI uri = null;
		try {
			uri = new URI(uUnLike+id);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpPost.setURI(uri);
		try {
			mConsumer.sign(httpPost);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Log.i("MOE", httpPost.getURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			client.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public void addFav(String id, int flag, int type){
		String url = "";
		if(flag == 1){
			if(type==TYPE_SONG){
				url = uLike;
			}else if(type==TYPE_MUSIC){
				url = uLikeAblum;
			}
			
		}else if(flag == 2){
			url = uUnLike;
		}
		HttpPost httpPost = new HttpPost();
		HttpClient client = new DefaultHttpClient();
		URI uri = null;
		try {
			uri = new URI(url+id);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpPost.setURI(uri);
		try {
			mConsumer.sign(httpPost);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Log.i("MOE", httpPost.getURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			client.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteFav(String id, int type){
		HttpPost httpPost = new HttpPost();
		HttpClient client = new DefaultHttpClient();
		URI uri = null;
		String url = "";
		if(type==TYPE_SONG){
			url = uDeleteFav;
		}else if(type==TYPE_MUSIC){
			url = uDeleteFavAblum;
		}
		try {
			uri = new URI(url+id);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpPost.setURI(uri);
		try {
			mConsumer.sign(httpPost);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Log.i("MOE", httpPost.getURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			client.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getUserInfo(){
		HttpPost httpPost = new HttpPost();
		HttpClient client = new DefaultHttpClient();
		URI uri = null;
		HttpResponse response = null;
		try {
			uri = new URI(uUser);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpPost.setURI(uri);
		try {
			mConsumer.sign(httpPost);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			response = client.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {

//			mStatus = response.getStatusLine().getStatusCode();
			Log.i("MOE", "User:"+Integer.toString(response.getStatusLine().getStatusCode()));
			String result = "";
			try {
				result = EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("MOE", result);
			try {
				JSONObject jso = new JSONObject(result);
//				this.setID(jso.getLong("uid"));
				mID = jso.getLong("uid");
//				this.setName(jso.getString("user_name"));
				mName = jso.getString("user_name");
//				this.setNickName(jso.getString("user_nickname"));
				mNickName = jso.getString("user_nickname");
//				Log.i("MOE", "id:"+mID);
//				Log.i("MOE", "name:"+mName);
//				Log.i("MOE", "nickname:"+mNickName);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int checkStatus(){
		HttpPost httpPost = new HttpPost();
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		URI uri = null;
		try {
			uri = new URI(uUser);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpPost.setURI(uri);
		try {
			mConsumer.sign(httpPost);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Log.i("MOE", httpPost.getURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			response = client.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(response==null){
			return -1;
		}else{
			return response.getStatusLine().getStatusCode();
		}
	}
	
	public long getID(){
		return this.mID;
	}
	
	public void setID(long id){
		this.mID = id;
	}
	
	public String getName(){
		return this.mName;
	}
	
	public void setName(String name){
		this.mName = name;
	}
	
	public String getNickName(){
		return this.mNickName;
	}
	
	public void setNickName(String nickname){
		this.mNickName = nickname;
	}
}
