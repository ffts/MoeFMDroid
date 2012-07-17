package ffts.android.moefmdroid;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Vector;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MoePlayerService extends Service {

	public static int DEFAULT = 0;
	public static int MUSIC = 1;
	public static int SONG = 2;
	public static int RADIO = 3;
	
	public static String uPlayListMode_0 = "http://moe.fm/listen/playlist?api=json";
	public static String uPlayListMode_1 = "http://moe.fm/listen/playlist?api=json&fav=music";
	public static String uPlayListMode_2 = "http://moe.fm/listen/playlist?api=json&fav=song";
	public static String uPlayListMode_3 = "http://moe.fm/listen/playlist?api=json&fav=radio";
	
	MoeBinder mBinder = new MoeBinder();
	Vector<Song> mListCur;//当前使用列表
	Vector<Song> mListA = new Vector<Song>();//列表A
	Vector<Song> mListB = new Vector<Song>();//列表B
	User mUser;
	Player mPlayer;
	String uNextPage = "none";
	int mCount = 0;
	boolean isPlaying = true;
	boolean isDiscA = true;
	int mPlayMode;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SharedPreferences sp = getSharedPreferences("MoeFM", MODE_PRIVATE);
		mUser = new User(sp.getString("access_token", ""), sp.getString("access_token_secret", ""));
		mPlayer = new  Player();
//		initPlayerService();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("MOE", "in service onBind");
			startPlay();
		
		return mBinder;
	}


	public class MoeBinder extends Binder {
		public MoePlayerService getPlayerService(){
			return MoePlayerService.this;
		}
	}
	
	//初始化播放服务，主要是获取播放列表
	public void initPlayerService(){
		
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mListA = updatePlayList();
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mListCur = mListA;
			}
		});
		th.start();
		/*try {
//			updatePlayList(mListA)
			mListA = updatePlayList();
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mListCur = mListA;*/
	}
	
	//开始播放
	public void startPlay() {
		/*if(mUser.mStatus==HttpStatus.SC_UNAUTHORIZED){
			goOauth();
		}else{
			
		}*/
		/*updateTask task = new updateTask();
		task.execute("start");*/
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mListA = updatePlayList();
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mListCur = mListA;
				try {
					mPlayer.initPlayer();
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
				}
				Thread timeUpdater = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						sendTime();
					}
				});
				timeUpdater.start();
				sendSongInfo(mListCur.get(0));
				MoePlayerService.this.notify(mListCur.get(0));
				try {
					mListB = updatePlayList();
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		th.start();
		/*try {
			mPlayer.initPlayer();
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
		}*/
		/*Thread timeUpdater = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sendTime();
			}
		});
		timeUpdater.start();
		sendSongInfo(mListCur.get(0));
		notify(mListCur.get(0));*/
//		Log.i("MOE", "Player init");
//		updatePlayList(mListB);
		/*Thread th2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mListB = updatePlayList();
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		th2.start();*/
		/*try {
			mListB = updatePlayList();
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
//		Log.i("MOE", "listB update");
	}
	
	public Vector<Song > updatePlayList() throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ParseException, URISyntaxException, IOException, JSONException{
		
		Vector<Song> list = new Vector<Song>();
		if(uNextPage.equals("none")){
			switch(mPlayMode){
			case 0:	uNextPage = uPlayListMode_0;
						break;
			case 1:	uNextPage = uPlayListMode_1;
						break;
			case 2:	uNextPage = uPlayListMode_2;
						break;
			case 3:	uNextPage = uPlayListMode_3;
						break;
			default:	return null;
		}
		}
		Log.i("MOE", "play mode:"+Integer.toString(mPlayMode));
				JSONObject jso = mUser.requestPlayList(uNextPage);
				if(jso==null){
//					Log.i("MOE", "jso null");
//					goOauth();
					return null;
				}else{
					uNextPage = jso.getJSONObject("response").getJSONObject("information").getString("next_url");
					for(int i=0;i<9;i++){
						Song song = new Song();
						JSONObject moeSong = jso.getJSONObject("response").getJSONArray("playlist").getJSONObject(i);
						song.setTitle(moeSong.getString("title"));
						Log.i("MOE", song.getTitle());
						song.setUrl(moeSong.getString("url"));
//						Log.i("MOE", song.getUrl());
						song.setID(moeSong.getString("sub_id"));
						song.setAblum(moeSong.getString("wiki_title"));
						song.setAblumID(moeSong.getString("wiki_id"));
						song.setArtist(moeSong.getString("artist"));
						song.setCover(moeSong.getJSONObject("cover").getString("square"));
//						Log.i("MOE", "cover:"+song.getCover());
//						JSONObject fav = moeSong.getJSONObject("fav_sub");
						if(!moeSong.isNull("fav_sub")){
							song.isLike(moeSong.getJSONObject("fav_sub").getInt("fav_type"));
						}else{
							song.isLike(0);
						}
						if(!moeSong.isNull("fav_wiki")){
							song.isLikeAblum(moeSong.getJSONObject("fav_wiki").getInt("fav_type"));
						}else{
							song.isLikeAblum(0);
						}
						list.add(song);
					}
				}
		return list;
	}
	
	public void changePlayList() throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ParseException, URISyntaxException, IOException, JSONException{
		if(isDiscA){
			mListCur = mListB;
			isDiscA = false;
			preparePlayList();
		}else{
			mListCur = mListA;
			isDiscA = true;
			preparePlayList();
		}
	}
	
	public void preparePlayList() throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ParseException, URISyntaxException, IOException, JSONException{
		if(isDiscA){
//			updatePlayList(mListB);
//			mListB = updatePlayList();
			Thread th = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						mListB = updatePlayList();
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
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			th.start();
		}else{
//			updatePlayList(mListA);
//			mListA = updatePlayList();
			Thread th = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						mListA = updatePlayList();
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
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			th.start();
		}
	}
	
	public void togglePlay(boolean playing) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		Log.i("MOE", Boolean.toString(playing));
		if(isPlaying){
			mPlayer.pause();
		}else{
			mPlayer.resum();
		}
//		Log.i("MOE", "now playing:"+mListCur.get(mCount).getTitle());
		isPlaying = playing;
	}
	
	public void playNext() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ParseException, URISyntaxException, JSONException{
		isPlaying = false;
		mCount++;
		if(mCount>8) {
			changePlayList();
			mCount = 0;
		}
		mPlayer.next(mListCur.get(mCount).getUrl());
		isPlaying = true;
//		Log.i("MOE", "now playing:"+mListCur.get(mCount).getTitle());
		sendSongInfo(mListCur.get(mCount));
		notify(mListCur.get(mCount));
		isPlaying = true;
	}
	
	public void changeMode(int mode){
		if(mPlayMode!=mode){
			mPlayMode = mode;
			uNextPage = "none";
			mPlayer.reset();
//			initPlayerService();
			startPlay();
	
		}
	}
	public void addFav(final int flag, final int type){
//		mUser.addFav(mListCur.get(mCount).getID(), flag);
		String tID = "";
		if(type==User.TYPE_SONG){
			tID = mListCur.get(mCount).getID();
		}else if(type==User.TYPE_MUSIC){
			tID = mListCur.get(mCount).getAblumID();
		}
		final String id = tID;
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mUser.addFav(id, flag, type);
			}
		});
		th.start();
	}
	
	public void deleteFav(final int type){
//		mUser.deleteFav(mListCur.get(mCount).getID());
		String tID = "";
		if(type==User.TYPE_SONG){
			tID = mListCur.get(mCount).getID();
		}else if(type==User.TYPE_MUSIC){
			tID = mListCur.get(mCount).getAblumID();
		}
		final String id = tID;
//		final String id = mListCur.get(mCount).getID();
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mUser.deleteFav(id, type);
			}
		});
		th.start();
	}
	
	public void notify(Song song){
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = song.getTitle();
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.flags |= Notification.FLAG_ONGOING_EVENT;    
        notification.flags |= Notification.FLAG_NO_CLEAR;
		Context context = getApplicationContext();
		CharSequence contentTitle = song.getTitle();
		CharSequence contentText = song.getArtist()+"<"+song.getAblum()+">";
		Intent notificationIntent = new Intent(this, MoePlayerActivity.class);
		notificationIntent.putExtra("from", 1);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		mNotificationManager.notify(1, notification);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		mNotificationManager.cancelAll();
		super.onDestroy();
		
	}


	class Player {
		MediaPlayer mp;
		Player(){
			mp = new MediaPlayer();
			mp.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					final String id = mListCur.get(mCount).getID();
					
					try {
						playNext();
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
					Thread th = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mUser.setListened(id);
						}
					});
					th.start();
				}
			});
		}
		public void initPlayer() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
			mp.setDataSource(mListCur.get(0).getUrl());
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.prepare();
			mp.start();
			isPlaying = true;
		}
		
		public void play(String url) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
			mp.setDataSource(url);
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.prepare();
			mp.start();
		}
		
		public void next(String url) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
			mp.reset();
			mp.setDataSource(url);
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.prepare();
			mp.start();
		}
		
		public void pause(){
			mp.pause();
		}
		
		public void resum(){
			mp.start();
		}
		
		public int getTimeTotal(){
			if(mp.isPlaying()){
				return mp.getDuration();
			}else return 0;
			
		}
		
		public int getTimeCur(){
			if(mp.isPlaying()){
				return mp.getCurrentPosition();
			}else return 0;
		}
		
		public void stop(){
//			timeUpdater.stop();
			mp.stop();
//			mp.release();
		}
		
		public void reset(){
			mp.reset();
		}
		
		
	}
	
	class updateThread implements Runnable{

		Vector<Song> list;
		Thread th = new Thread(this);
		public Vector<Song> update(){
			th.start();
			return list;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(uNextPage.equals("none")){
				switch(mPlayMode){
				case 0:	uNextPage = uPlayListMode_0;
							break;
				case 1:	uNextPage = uPlayListMode_1;
							break;
				case 2:	uNextPage = uPlayListMode_2;
							break;
				case 3:	uNextPage = uPlayListMode_3;
							break;
				default:	list =  null;
			}
			}
			Log.i("MOE", "play mode:"+Integer.toString(mPlayMode));
					JSONObject jso = mUser.requestPlayList(uNextPage);
					if(jso==null){
//						Log.i("MOE", "jso null");
//						goOauth();
						list =  null;
					}else{
						try {
							uNextPage = jso.getJSONObject("response").getJSONObject("information").getString("next_url");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for(int i=0;i<9;i++){
							Song song = new Song();
							JSONObject moeSong = null;
							try {
								moeSong = jso.getJSONObject("response").getJSONArray("playlist").getJSONObject(i);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								song.setTitle(moeSong.getString("title"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//							Log.i("MOE", song.getTitle());
							try {
								song.setUrl(moeSong.getString("url"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//							Log.i("MOE", song.getUrl());
							try {
								song.setID(moeSong.getString("sub_id"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								song.setAblum(moeSong.getString("wiki_title"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								song.setArtist(moeSong.getString("artist"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								song.setCover(moeSong.getJSONObject("cover").getString("square"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//							Log.i("MOE", "cover:"+song.getCover());
//							JSONObject fav = moeSong.getJSONObject("fav_sub");
							if(!moeSong.isNull("fav_sub")){
								try {
									song.isLike(moeSong.getJSONObject("fav_sub").getInt("fav_type"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else{
								song.isLike(0);
							}
							list.add(song);
						}
					}
			/*JSONObject jso = null;
			try {
				jso = mUser.requestPlayList(uNextPage);
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(jso==null) Log.i("MOE", "jso null");
			for(int i=0;i<9;i++){
				Song song = new Song();
				try {
					song.setTitle(jso.getJSONObject("response").getJSONArray("playlist").getJSONObject(i).getString("title"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				Log.i("MOE", song.getTitle());
				try {
					song.setUrl(jso.getJSONObject("response").getJSONArray("playlist").getJSONObject(i).getString("url"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				Log.i("MOE", song.getUrl());
				list.add(song);
		}
		*/
	}
	}
	
	public void sendSongInfo(Song song){
		Intent it = new Intent("ffts.android.moefmdroid.updatesong");
		String[] info = {"none","none","none","none"};
		info[0] = song.getTitle();
		info[1] = song.getArtist();
		info[2] = song.getAblum();
		info[3] = song.getCover();
		int fav = song.isLike();
		int favAblum = song.isLikeAblum();
		it.putExtra("info", info);
		it.putExtra("fav", fav);
		it.putExtra("fav_ablum", favAblum);
		this.sendBroadcast(it);
	}
	
	public void sendSongInfo(){
		sendSongInfo(mListCur.get(mCount));
	}
	public void sendTime(){
		Intent it = new Intent("ffts.android.moefmdroid.updatetime");
		int[] time = {0,0};
		while(true){
			if(isPlaying){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				time[0] = mPlayer.getTimeCur();
				time[1] = mPlayer.getTimeTotal();
				it.putExtra("time", time);
				this.sendBroadcast(it);
			}
		}
	}
	
	class updater implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		mPlayer.stop();
		return super.onUnbind(intent);
	}
	
	/*public void goOauth(){
		Intent it = new Intent();
		it.setClass(MoePlayerService.this, OAuthActivity.class);
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		startActivity(it);
	}*/
	
	class updateTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	class initPlayerService extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				mListA = updatePlayList();
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mListCur = mListA;
			Log.d("MOE", "do background");
			return "ok";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d("MOE", "post execute");
			try {
				mPlayer.initPlayer();
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
			}
			Thread timeUpdater = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					sendTime();
				}
			});
			timeUpdater.start();
			sendSongInfo(mListCur.get(0));
			MoePlayerService.this.notify(mListCur.get(0));
			try {
				mListB = updatePlayList();
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
