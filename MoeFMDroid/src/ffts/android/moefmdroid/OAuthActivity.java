package ffts.android.moefmdroid;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OAuthActivity extends Activity {

	Button obt;
	MoeOAuth moeOAuth;
	SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oauth);
		
        obt = (Button) findViewById(R.id.btoa);
        obt.setOnClickListener(oListener);
        moeOAuth = new MoeOAuth();
        sp = getSharedPreferences("MoeFM", MODE_PRIVATE);
	}
	
	OnClickListener oListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
    		/*String authUrl = "none";
			try {
				authUrl = moeOAuth.getRequestToken();
			} catch (OAuthMessageSignerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (OAuthNotAuthorizedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (OAuthExpectationFailedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (OAuthCommunicationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.i("MOE", authUrl);
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));*/
			Thread th = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String authUrl = "none";
					try {
						authUrl = moeOAuth.getRequestToken();
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
//					Log.i("MOE", authUrl);
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
				}
			});
			th.start();
		}
	};
	
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i("MOE", "BACK");
    	Uri uri = intent.getData();
    	String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
    	moeOAuth.setVerifier(verifier);
    	Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					moeOAuth.getAccessToken();
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
				Editor ed = sp.edit();
//		    	User user = new User(moeOAuth.getAccess_Token(), moeOAuth.getAccess_Token_Secret());
		    	ed.putString("access_token", moeOAuth.getAccess_Token());
		    	ed.putString("access_token_secret", moeOAuth.getAccess_Token_Secret());
//		    	ed.putLong("uid", user.getID());
//		    	ed.putString("user_name", user.getName());
		    	ed.commit();
		    	Intent it = new Intent();
		    	it.setClass(OAuthActivity.this, MoePlayerActivity.class);
		    	startActivity(it);
		    	onDestroy();
			}
		});
    	th.start();
    	/*try {
			moeOAuth.getAccessToken();
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
    	
    	Editor ed = sp.edit();
//    	User user = new User(moeOAuth.getAccess_Token(), moeOAuth.getAccess_Token_Secret());
    	ed.putString("access_token", moeOAuth.getAccess_Token());
    	ed.putString("access_token_secret", moeOAuth.getAccess_Token_Secret());
//    	ed.putLong("uid", user.getID());
//    	ed.putString("user_name", user.getName());
    	ed.commit();
    	Intent it = new Intent();
    	it.setClass(OAuthActivity.this, MoePlayerActivity.class);
    	startActivity(it);
    	onDestroy();*/
	}
	
	public void onDestroy() { 
        try{ 
        	this.finish();
        } 
        catch(Exception e){} 
        super.onDestroy(); 
    }
}
