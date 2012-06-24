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
	private static String mConsumerKey = "2bbeff3c3349208e0e4f8bad2b1b2f4a04fd6f3fd";
	private static String mConsumerSecret = "79ec971c5dd50e848cd58b5c7399c6f0";
	private CommonsHttpOAuthConsumer mConsumer;
	public static String uListened = "http://moe.fm/ajax/log?log_obj_type=sub&log_type=listen&obj_type=song&api=json&obj_id=";
	public static String uLike = "http://api.moefou.org/fav/add.json?fav_obj_type=song&fav_type=1&save_status=1&fav_obj_id=";
	public static String uUnLike = "http://api.moefou.org/fav/add.json?fav_obj_type=song&fav_type=2&save_status=1&fav_obj_id=";
	public static String uDeleteFav = "http://api.moefou.org/fav/delete.json?fav_obj_type=song&fav_obj_id=";
	public static String uUser = "http://api.moefou.org/user/detail.json";
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
			throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, 
			ParseException, IOException, JSONException
	{
		
		HttpPost httpPost = new HttpPost();
		HttpClient client = new DefaultHttpClient();
		org.apache.http.HttpResponse response = null;
		URI uri = new URI(url);
		httpPost.setURI(uri);
		mConsumer.sign(httpPost);
		Log.i("MOE", httpPost.getURI().toURL().toString());
		response = client.execute(httpPost);
		Log.i("MOE", response.getEntity().toString());
		Log.i("MOE", "HttpStatus:"+Integer.toString(response.getStatusLine().getStatusCode()));
		if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {

			mStatus = response.getStatusLine().getStatusCode();
			String result = EntityUtils.toString(response.getEntity());
//			Log.i("MOE", result);
			JSONObject jso = new JSONObject(result);
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
	
	public void addFav(String id, int flag){
		String url = "";
		if(flag == 1){
			url = uLike;
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
	
	public void deleteFav(String id){
		HttpPost httpPost = new HttpPost();
		HttpClient client = new DefaultHttpClient();
		URI uri = null;
		try {
			uri = new URI(uDeleteFav+id);
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
