package ffts.android.moefmdroid;

import org.apache.http.HttpStatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class MoeFMDroidActivity extends SherlockActivity {
	Button btl;
	Button bto;
	TextView token,token_secret;
	SharedPreferences sp;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        
        Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sp = getSharedPreferences("MoeFM", MODE_PRIVATE);
		        String token = sp.getString("access_token", "");
		        String secret = sp.getString("access_token_secret", "");
		        if((token!="")&&(secret!="")){
		        	User user = new User(token, secret);
		        	Intent it = new Intent();
		        	if(user.checkStatus()==HttpStatus.SC_UNAUTHORIZED){
		        		it.setClass(MoeFMDroidActivity.this, OAuthActivity.class);
						startActivity(it);
						onDestroy();
		        	}else if(user.checkStatus()==HttpStatus.SC_OK){
		        		it.putExtra("token", token);
			        	it.putExtra("secret", secret);
			        	it.setClass(MoeFMDroidActivity.this, MoePlayerActivity.class);
			        	startActivity(it);
			        	onDestroy();
		        	}else if(user.checkStatus()==-1){//链接失败
//		        		Toast.makeText(getApplicationContext(), R.string.network_error	, Toast.LENGTH_LONG).show();
		        	}
		        }else{
		        	Intent it = new Intent();
		        	it.setClass(MoeFMDroidActivity.this, OAuthActivity.class);
					startActivity(it);
					onDestroy();
		        }
			}
		});
        th.start();
        /*btl = (Button) findViewById(R.id.bt);
        btl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(MoeFMDroidActivity.this, MoePlayerActivity.class);
				startActivity(it);
			}
		});
        
        bto = (Button) findViewById(R.id.bto);
        bto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(MoeFMDroidActivity.this, OAuthActivity.class);
				startActivity(it);
			}
		});
        
        token = (TextView) findViewById(R.id.s_token);
        token_secret = (TextView) findViewById(R.id.s_token_secret);*/
        
    }
    public void onDestroy() { 
        try{ 
        	this.finish();
        } 
        catch(Exception e){} 
        super.onDestroy(); 
    }
}