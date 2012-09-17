package ffts.android.moefmdroid;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class OAuthActivity extends Activity {

	Button obt;
	MoeOAuth moeOAuth;
	SharedPreferences sp;
	public static String TAG = "MOED";
	
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
			
			//使用webview进行授权，使用asynctask
			new getOAuthUrl().execute(null,null);
			
    		//旧的调用系统浏览器的授权方法
//			Thread th = new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					String authUrl = "none";
//					try {
//						authUrl = moeOAuth.getRequestToken();
//					} catch (OAuthMessageSignerException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (OAuthNotAuthorizedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (OAuthExpectationFailedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (OAuthCommunicationException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
////					Log.i("MOE", authUrl);
//					Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
////					 it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
//					startActivity(it);
//				}
//			});
//			th.start();
		}
	};
	
	//旧的授权方法，从系统浏览器返回的intent中获取verifier code
	/*@Override
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
	}*/
	
	//发送comsumer key和comsumer secret，得到获取verifier code的url
	class getOAuthUrl extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String url = "none";
			try {
				url = moeOAuth.getRequestToken();
				Log.d(TAG, url);
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
			return url;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
//			super.onPostExecute(result);
			Intent it = new Intent();
			it.putExtra("url", result);
			it.setClass(OAuthActivity.this, WebViewActivity.class);
//			startActivity(it);
			startActivityForResult(it, RESULT_FIRST_USER);
		}
    	
    }
    
	//获取access token和access token secret
    class getAccessToken extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
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
			Log.d(TAG, moeOAuth.getAccess_Token());
			Log.d(TAG, moeOAuth.getAccess_Token_Secret());
			User user = new User(moeOAuth.getAccess_Token(), moeOAuth.getAccess_Token_Secret());
			if(user.checkStatus()==HttpStatus.SC_OK){
				return true;
			}else return false;
		}

		//获取到access存储，接着跳转到播放界面
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
//			super.onPostExecute(result);
			if(result){
				Log.d(TAG, "OAuth OK");
				Editor ed = sp.edit();
		    	ed.putString("access_token", moeOAuth.getAccess_Token());
		    	ed.putString("access_token_secret", moeOAuth.getAccess_Token_Secret());
		    	ed.commit();
		    	Intent it = new Intent();
		    	it.setClass(OAuthActivity.this, MoePlayerActivity.class);
		    	startActivity(it);
		    	onDestroy();
			}
			else{
				Log.d(TAG, "OAuth faild");
				Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_LONG).show();
			}
				
		}
    	
    }
    
    //当从webview返回结果时，进行处理
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "on result");
		Log.d(TAG, "result code:"+resultCode);
		if(RESULT_FIRST_USER==resultCode){
			Log.d(TAG, "get verifier:"+data.getStringExtra("verfier"));
			moeOAuth.setVerifier(data.getStringExtra("verfier"));
			new getAccessToken().execute(null,null);
		}
	}
	
	public void onDestroy() { 
        try{ 
        	this.finish();
        } 
        catch(Exception e){} 
        super.onDestroy(); 
    }
}
