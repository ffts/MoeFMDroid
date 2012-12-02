package ffts.android.moefmdroid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Formatter;
import java.util.Locale;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import ffts.android.moefmdroid.MoePlayerService.MoeBinder;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MoePlayerActivity extends SherlockActivity implements OnNavigationListener,OnGestureListener {

//	private String[] mPlayModes;
	boolean isPlaying = true;
	Song curSong = new Song();
	MoePlayerService mService;
	MoeReceiver receiver;
	ImageButton btPlay;
	ImageButton btPlayNext;
	ImageButton btLike;
	ImageButton btUnLike;
	TextView tvTitle;
	TextView tvArtist;
	TextView tvAblum;
	ImageView ivCover;
	ImageView ivFavAblum;
	TextView tvCurTime;
	TextView tvTotalTime;
	ProgressBar pbProgress;
	Handler handler=new Handler();
	int isLike = 0;
	int isLikeAblum = 0;
	private GestureDetector gestureScanner;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		Utils.onActivityCreateSetSherlockTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moe_player);
		
		tvTitle = (TextView) findViewById(R.id.title);
		tvArtist = (TextView) findViewById(R.id.artist);
		tvAblum = (TextView) findViewById(R.id.ablum);
		ivCover = (ImageView) findViewById(R.id.cover);
		ivFavAblum = (ImageView) findViewById(R.id.favablum);
		tvCurTime = (TextView) findViewById(R.id.curtime);
		tvTotalTime = (TextView) findViewById(R.id.totaltime);
		pbProgress = (ProgressBar) findViewById(R.id.progress);
		
		gestureScanner = new GestureDetector(this);
		
		btPlay = (ImageButton) findViewById(R.id.play);
		btPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					if(mService==null) 
						Log.i("MOE", "no service");
					else{
						if(isPlaying){
							isPlaying = false;
							Drawable play = getResources().getDrawable(R.drawable.btn_play);
							btPlay.setImageDrawable(play);
							Log.i("MOE", "A paused");
						}else{
							isPlaying = true;
							Drawable pause = getResources().getDrawable(R.drawable.btn_pause);
							btPlay.setImageDrawable(pause);
							Log.i("MOE", "A played");
						}
						try {
							mService.togglePlay(isPlaying);
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
			}
			}
		});
		
		btPlayNext = (ImageButton) findViewById(R.id.next);
		btPlayNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					mService.playNext();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthMessageSignerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthExpectationFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthCommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!isPlaying){
					isPlaying = true;
					Drawable pause = getResources().getDrawable(R.drawable.btn_pause);
					btPlay.setImageDrawable(pause);
				}
			}
		});
		
		btLike = (ImageButton) findViewById(R.id.like);
		btLike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isLike==0){
					Drawable like = getResources().getDrawable(R.drawable.btn_liked);
					btLike.setImageDrawable(like);
					mService.addFav(1, User.TYPE_SONG);
					isLike = 1;
					Toast.makeText(getApplicationContext(), R.string.added, Toast.LENGTH_LONG).show();
				}else if(isLike==1){
					Drawable like = getResources().getDrawable(R.drawable.btn_like);
					btLike.setImageDrawable(like);
					mService.deleteFav(User.TYPE_SONG);
					isLike = 0;
					Toast.makeText(getApplicationContext(), R.string.deleted, Toast.LENGTH_LONG).show();
				}
			}
		});
		
		btUnLike = (ImageButton) findViewById(R.id.unlike);
		btUnLike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mService.addFav(2, User.TYPE_SONG);
				Toast.makeText(getApplicationContext(), R.string.unlike, Toast.LENGTH_LONG).show();
				try {
					mService.playNext();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthMessageSignerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthExpectationFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthCommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		tvAblum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isLikeAblum==0){
					Drawable likeAblum = getResources().getDrawable(R.drawable.btn_ablum_liked);
					ivFavAblum.setImageDrawable(likeAblum);
					mService.addFav(1, User.TYPE_MUSIC);
					isLikeAblum = 1;
					Toast.makeText(getApplicationContext(), R.string.added, Toast.LENGTH_LONG).show();
				}else if(isLikeAblum==1){
					Drawable likeAblum = getResources().getDrawable(R.drawable.btn_ablum_like);
					ivFavAblum.setImageDrawable(likeAblum);
					mService.deleteFav(User.TYPE_MUSIC);
					isLikeAblum = 0;
					Toast.makeText(getApplicationContext(), R.string.deleted, Toast.LENGTH_LONG).show();
				}
			}
		});
//		mPlayModes = getResources().getStringArray(R.array.playmode);
/*		Log.i("MOE", mPlayModes[0]);
		Log.i("MOE", mPlayModes[1]);
		Log.i("MOE", mPlayModes[2]);
		Log.i("MOE", mPlayModes[3]);*/

        Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.playmode, R.layout.sherlock_spinner_item);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        
        
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this);
//        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MoePlayerActivity.this, MoePlayerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
      //注册BroadcastReceiver
        receiver = new MoeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ffts.android.moefmdroid.updatesong");
        filter.addAction("ffts.android.moefmdroid.updatetime");
        this.registerReceiver(receiver, filter);
        
        if(mService!=null){
        	isPlaying = mService.isPlaying;
        	Song curSong = mService.getCurSong();
        	if(curSong!=null){
    			updatePlaying(isPlaying);
    			String[] info = {curSong.getTitle(),curSong.getArtist(),curSong.getAblum(),curSong.getCover()};
    			updatePlayer(info, isLike, isLikeAblum);
    		}
        }
		super.onStart();
//        Log.i("MOE", "onStart bind");
	}
	

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.unregisterReceiver(receiver);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		if(mService!=null){
			mService.changeMode(itemPosition);
		}
		Log.i("MOE", Integer.toString(itemPosition));
		Log.i("MOE", "mService:"+mService);
//		Toast.makeText(getApplicationContext(), Integer.toString(itemPosition), Toast.LENGTH_LONG).show();
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater mInflater = getSupportMenuInflater();
		mInflater.inflate(R.menu.player_action, menu);
    	return true;
	}
	
	@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
//			Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_LONG).show();
			if(item.getTitle().equals("设置")){
//				goSettings();
				Toast.makeText(getApplicationContext(), "还没有什么可以设置的...", Toast.LENGTH_LONG).show();
			}else if(item.getTitle().equals("关闭")){
				unbindService(mConnection);
				finish();
			}
			return true;
		}
	
	 private ServiceConnection mConnection = new ServiceConnection() {

	        @Override
	        public void onServiceConnected(ComponentName className,
	                IBinder service) {
	            // We've bound to LocalService, cast the IBinder and get LocalService instance
	        	MoeBinder binder = (MoeBinder) service;
	            mService = binder.getPlayerService();
	            isPlaying = mService.isPlaying;
	            if(!isPlaying){
	            	Drawable play = getResources().getDrawable(R.drawable.btn_play);
					btPlay.setImageDrawable(play);
	            }
	            if(mService.mListCur!=null){
	            	 mService.sendSongInfo();
	            }
//	            Log.i("MOE", "bind");
	        }

	        @Override
	        public void onServiceDisconnected(ComponentName arg0) {
	        	
	        }
	    };
	    
	    class MoeReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getAction();
//				Log.i("MOE", "action:"+action);
				if(action.equals("ffts.android.moefmdroid.updatesong")){
//					Log.i("MOE", "update UI");
					updatePlayer(intent.getStringArrayExtra("info"),intent.getIntExtra("fav", 0),intent.getIntExtra("fav_ablum",0));
				}else if(action.equals("ffts.android.moefmdroid.updatetime")){
					int[] time = intent.getIntArrayExtra("time");
					updateTime(time[0], time[1]);
//					Log.i("MOE", "update time");
				}
			}
	    	
	    }
	    //0:title;1:artist;2:ablum;3:cover
	    public void updatePlayer(String[] info ,int flagFav, int flagFavAblum){
	    	Log.i("MOE", "update UI start>>>>>>>>");
	    	tvTitle.setText(info[0]);
	    	tvTitle.requestFocus();
//	    	Log.i("MOE", info[0]);
	    	tvArtist.setText(info[1]);
//	    	Log.i("MOE", info[1]);
	    	tvAblum.setText(info[2]);
//	    	Log.i("MOE", info[2]);
//	    	updateCover(info[3]);
	    	new CoverUpdater().execute(info[3]);
	    	updateLike(flagFav);
	    	updateLikeAblum(flagFavAblum);
	    	/*if(flag==0){
	    		Drawable pause = getResources().getDrawable(R.drawable.btn_pause);
				btPlay.setImageDrawable(pause);
	    	}else{
	    		Drawable like = getResources().getDrawable(R.drawable.btn_like);
				btLike.setImageDrawable(like);
	    	}*/
//	    	Log.i("MOE", "update UI end>>>>>>>>>");
	    	
	    	
	    }
	    
	    
	    class CoverUpdater extends AsyncTask<String, String, Drawable>{

			@Override
			protected Drawable doInBackground(String... params) {
				// TODO Auto-generated method stub
				Drawable drawable = null;
				try {
					drawable = Drawable.createFromStream( new URL(params[0]).openStream(), "image.jpg");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return drawable;
			}

			@Override
			protected void onPostExecute(final Drawable result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				ivCover.post(new Runnable(){  
	    	        @Override  
	    	        public void run() {  
	    	            // TODO Auto-generated method stub  
	    	        	ivCover.setImageDrawable(result) ;  
	    	        }}) ; 
			}
			
	    	
	    }
	    
	    public void updatePlaying(boolean isPlaying){
	    	final boolean play = isPlaying;
	    	btPlay.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Drawable playing;
					if(play){
						playing = getResources().getDrawable(R.drawable.btn_pause);
					}else{
						playing = getResources().getDrawable(R.drawable.btn_play);
					}
					btPlay.setImageDrawable(playing);
				}
			});
	    }
	    public void updateLike(int flag){
	    	
	    	final int likeFlag = flag;
	    	isLike = flag;
	    	btLike.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Drawable like;
			    	if(likeFlag==0){
			    		like = getResources().getDrawable(R.drawable.btn_like);
			    	}else{
			    		like = getResources().getDrawable(R.drawable.btn_liked);
			    	}
					btLike.setImageDrawable(like);
				}
			});
	    }
	    
	    public void updateLikeAblum(int flag){
	    	
	    	final int likeAblumFlag = flag;
	    	isLikeAblum = flag;
	    	ivFavAblum.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Drawable like;
			    	if(likeAblumFlag==0){
			    		like = getResources().getDrawable(R.drawable.btn_ablum_like);
			    	}else{
			    		like = getResources().getDrawable(R.drawable.btn_ablum_liked);
			    	}
					ivFavAblum.setImageDrawable(like);
				}
			});
	    }
	    
	    public void updateTime(int cur, int total){
	    	if(total!=0){
	    		tvCurTime.setText(formateTime(cur));
		    	tvTotalTime.setText(formateTime(total));
	    		final int progress = cur*100/total;
//	    		Log.i("MOE", Float.toString(progress));
//	    		pbProgress.setProgress(progress);
//	    		pbProgress.postInvalidate();
	    		/*handle.post(new Runnable(){  
		    	    @Override  
		    	    public void run() {  
		    	          
		    	        // post() 特别关键，就是到UI主线程去更新
		    	        pbProgress.post(new Runnable(){  
		    	        @Override  
		    	        public void run() {  
		    	            // TODO Auto-generated method stub  
		    	        	pbProgress.setProgress(progress);
		    	        }}) ;  
		    	        }  
		    	          
		    	}) ; */ 
	    		new Thread(new Runnable() {
	                public void run() {
	                        // Update the progress bar
	                        handler.post(new Runnable() {
	                            public void run() {
	                                pbProgress.setProgress(progress);
	                            }
	                        });
	                    }
	            }).start();
	    	}
	    }
	    
	    public String formateTime(int milliseconds){
	    	StringBuilder sFormatBuilder = new StringBuilder();  
	        Formatter sFormatter = new Formatter(sFormatBuilder, Locale.getDefault()); 
	        int seconds = milliseconds/1000;
	        int minutes = seconds/60;
	        seconds = seconds%60;
	        return sFormatter.format("%02d:%02d", minutes, seconds).toString();
	        
	    }

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			if(e1.getX()>e2.getX()){
				try {
					mService.playNext();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthMessageSignerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthExpectationFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthCommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!isPlaying){
					isPlaying = true;
					Drawable pause = getResources().getDrawable(R.drawable.btn_pause);
					btPlay.setImageDrawable(pause);
				}
				return true;
			}else return false;
			
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			super.onTouchEvent(event);
			return gestureScanner.onTouchEvent(event);
		}

		/*@Override
			protected void onNewIntent(Intent intent) {
				// TODO Auto-generated method stub
				super.onNewIntent(intent);
				if(intent.getIntExtra("from", 0)==1){
					mService.sendSongInfo();
				}
			}*/
	    
	   /* public class AngelBeats implements ActionMode.Callback{

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				menu.add(R.string.settings)
						.setIcon(R.drawable.ico_settings)
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				menu.add(R.string.close)
						.setIcon(R.drawable.ico_settings)
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				return true;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), Integer.toString(item.getOrder()), Toast.LENGTH_LONG).show();
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub
				
			}
	    	
	    }*/
	    
	    public void goSettings(){
	    	Intent it = new Intent();
			it.setClass(MoePlayerActivity.this, MoeSettingsActivity.class);
			startActivity(it);
	    }

}
