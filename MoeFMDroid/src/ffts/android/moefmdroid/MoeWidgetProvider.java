package ffts.android.moefmdroid;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class MoeWidgetProvider extends AppWidgetProvider {

	public static MoeWidgetProvider mInstance;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		Log.d("MOE", "widget update");
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.moe_widget_layout);
		setViewsOnClickListender(context, remoteViews);
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		askForUpdate(context);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	public static MoeWidgetProvider getInstance(){
		if(mInstance==null){
			mInstance =  new MoeWidgetProvider();
		}
		return mInstance;
	}

	//通知widget有内容需要更新
	public void notifyChange(MoePlayerService service, String action){
		if(MoePlayerService.UPDATE_SONG.equals(action)){
			RemoteViews views = new RemoteViews(service.getPackageName(), R.layout.moe_widget_layout);
			setTitleText(service, views, service.getCurSong().getTitle());
		}
		if(MoePlayerService.TOGGLE_PLAY.equals(action)){
			RemoteViews views = new RemoteViews(service.getPackageName(), R.layout.moe_widget_layout);
			setPlayButton(service, views);
		}
	}
	public void setViewsOnClickListender(Context context, RemoteViews views){
		
		//设置标题，点击打开播放界面
		Intent intent = new Intent(context,MoePlayerActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.widget_title, pendingIntent);
		
		//设置播放按钮
		intent= new Intent(MoePlayerService.TOGGLE_PLAY);
		pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.widget_play, pendingIntent);
		
		//设置下一首按钮
		intent = new Intent(MoePlayerService.NEXT);
		pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.widget_next, pendingIntent);
	}
	
	//设置标题文本
	public void setTitleText(Context context, RemoteViews views, String title){
		views.setTextViewText(R.id.widget_title, title);
		updateWidget(context, views, null);
	}
	
	//设置播放按钮状态
	public void setPlayButton(MoePlayerService service, RemoteViews views){
		if(service!=null){
			if(service.isPlaying){
				views.setImageViewResource(R.id.widget_play, R.drawable.btn_pause);
			}else{
				views.setImageViewResource(R.id.widget_play, R.drawable.btn_play);
			}
			updateWidget(service, views, null);
		}
	}
	
	//请求更新widget
	public void askForUpdate(Context context){
		Intent intent = new Intent(MoePlayerService.UPDATE_WIDGET);
		context.sendBroadcast(intent);
	}
	
	//更新widget，用来应用变更后的内容
	public void updateWidget(Context context, RemoteViews views, int[] appWidgetIds){
		AppWidgetManager appWidgetManager= AppWidgetManager.getInstance(context);
		if(appWidgetIds!=null){
			appWidgetManager.updateAppWidget(appWidgetIds, views);
		}else appWidgetManager.updateAppWidget(new ComponentName(context, this.getClass()), views);
	}
}
