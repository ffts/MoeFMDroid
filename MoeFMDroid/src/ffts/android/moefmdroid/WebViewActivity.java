package ffts.android.moefmdroid;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends Activity {

	public static String TAG = "MoeOAuthTest";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		WebView wv = (WebView) findViewById(R.id.wv);
		Intent it = this.getIntent();
		String url = it.getExtras().getString("url");
		//这行很重要一点要有，不然网页的认证按钮会无效                     
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);
         
        wv.loadUrl(url);
        
        wv.addJavascriptInterface(new JavaScriptInterface(), "Methods"); 
        WebViewClient wvc=new WebViewClient()
                {
                    public void onPageFinished(WebView view,String url)
                    {
                        view.loadUrl("javascript:window.Methods.getHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');"); 
                        super.onPageFinished(view, url);
                    }
                };
                wv.setWebViewClient(wvc);
	}

	class JavaScriptInterface  
    {  
        public void getHTML(String html)  
        {  
        	getVerifierCode(html);
        }  
    } 
	
	//从网页中提取verifier code
	public void getVerifierCode(String html){
		Document doc = Jsoup.parse(html);
		Element element = doc.getElementById("verifier");
		String code = "none";
		if(element==null){
			Log.d(TAG, "no element");
		}else{
			code = element.ownText();
			Log.d(TAG, code);
			returnResult(code);
		}
	}
	
	//将verifier code 返回给OAuthActivity
	public void returnResult(String code){
		Intent it = new Intent();
		it.putExtra("verfier", code);
		it.setClass(WebViewActivity.this, OAuthActivity.class);
		setResult(RESULT_FIRST_USER, it);
		WebViewActivity.this.finish();
	}
}
