package ffts.android.moefmdroid;

import android.os.AsyncTask;
import android.util.Log;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class MoeOAuth {

	private CommonsHttpOAuthConsumer consumer;
	private CommonsHttpOAuthProvider provider;
	private String access_token,access_token_secret,verifier;
	private String oauth_url = "none";
	public static String CONSUMERKEY = "3e28e27549737c96f4fae920b1a3191d05003bb10";
	public static String CONSUMERSECRET = "417dda4cadc840ae283d52b01507f3a6";
	
	MoeOAuth(	){
		consumer = new CommonsHttpOAuthConsumer(CONSUMERKEY, CONSUMERSECRET);
		provider = new CommonsHttpOAuthProvider("http://api.moefou.org/oauth/request_token", 
																			"http://api.moefou.org/oauth/access_token", 
																			"http://api.moefou.org/oauth/authorize");
	}
	
	public String getAccess_Token(){
		return this.access_token;
	}
	
	public String getAccess_Token_Secret(){
		return this.access_token_secret;
	}
	
	public String getVerifier(){
		return this.verifier;
	}
	
	public void setVerifier(String verifier){
		this.verifier = verifier;
	}
	
	public String getOAuthUrl(){
		return this.oauth_url;
	}
	
	public String getRequestToken() throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException{
		
		String callBackUrl="mfmd://OAuthActivity";
		String oauthURL = provider.retrieveRequestToken(consumer, callBackUrl);
		return oauthURL;
//		getRequestTokenTask task = new getRequestTokenTask();
//		task.execute("start");
//		Log.d("MOE", oauth_url);
		/*Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String callBackUrl="mfmd://OAuthActivity";
				String oauthURL = null;
				try {
					oauthURL = provider.retrieveRequestToken(consumer, callBackUrl);
				} catch (OAuthMessageSignerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthNotAuthorizedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthExpectationFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthCommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(oauthURL!=null){
					oauth_url = oauthURL;
					Log.i("MOE", oauthURL);
				}else{
					Log.i("MOE", "oauth fail");
				}
			}
		});
		th.start();*/
//		return oauth_url;
	}
	
	/*class getRequestTokenTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String callBackUrl="mfmd://OAuthActivity";
			String oauthURL = null;
			try {
				oauthURL = provider.retrieveRequestToken(consumer, callBackUrl);
			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("MOe", oauthURL);
			return oauthURL;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null){
				oauth_url = result;
			}else{
				Log.i("MOE", "oauth fail");
			}
		}
		
		
		
	}*/
	
	public void getAccessToken() throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException{
		
		provider.setOAuth10a(true);
		provider.retrieveAccessToken(consumer, verifier);
		this.access_token = consumer.getToken();
		this.access_token_secret = consumer.getTokenSecret();
//		new getAccessToken().execute("");
	}
	
	/*class getAccessToken extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			provider.setOAuth10a(true);
			try {
				provider.retrieveAccessToken(consumer, verifier);
			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			access_token = consumer.getToken();
			access_token_secret = consumer.getTokenSecret();
			return null;
		}
		
	}*/
	
}
