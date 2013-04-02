package com.yooiistudios.chris.facebook_like_sample.facebook;

import com.yooiistudios.chris.facebook_like_sample.Constants;
import com.yooiistudios.chris.facebook_like_sample.R;
import com.yooiistudios.chris.facebook_like_sample.Constants.Common;
import com.yooiistudios.chris.facebook_like_sample.R.id;
import com.yooiistudios.chris.facebook_like_sample.R.layout;
import com.yooiistudios.chris.facebook_like_sample.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class FBLikeActivity extends Activity {
//	String url = "http://m.facebook.com/intelkorea";
	//168356489891715
	
	private WebView mWebView;
	
	private boolean mLiked = false;

	/* Constants */
	private final String likeURL = "?fan&";
	private final String unlikeURL = "?unfan&";
	private final String url = Constants.Common.FB_PAGE_PREFIX + Constants.Common.FB_FOLLOW_ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fblike);
        
        mLiked = getIntent().getExtras().getBoolean(Constants.Common.INTENT_FB_LIKE);
        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new FacebookWebViewClient());
        mWebView.loadUrl(url);
    }

	class FacebookWebViewClient extends WebViewClient {

		@Override
		public void onFormResubmission(WebView view, Message dontResend,
				Message resend) {
			super.onFormResubmission(view, dontResend, resend);
			Log.e("FacebookWebViewClient", "FacebookWebViewClient");
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			super.onLoadResource(view, url);
			Log.e("onLoadResource", url);
			/* Check url to check if user clicked 'like' or 'unlike' button.*/
			boolean following = url.indexOf(likeURL) > -1;
			boolean unFollowing = url.indexOf(unlikeURL) > -1;
			/* More, check if user clicked 'like' button with our facebook page.
			 * This prevents user from clicking other facebook page, not our page.*/
			boolean isOurPage = url.indexOf(Constants.Common.FB_FOLLOW_ID) > -1;
			
			if (!isOurPage)
				return;
			if (following) {
				Log.i("like", "liked");
				mLiked = true;
				Toast.makeText(getApplicationContext(),
						"You have just selected 'like' ",
						Toast.LENGTH_LONG).show();
				//you liked
			} else if (unFollowing) {
				Log.i("like", "unliked");
				mLiked = false;
				Toast.makeText(getApplicationContext(),
						"You have just selected 'unlike' ",
						Toast.LENGTH_LONG).show();

			} else {
				// other operations
			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.e("onPageFinished", "onPageFinished");
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			Log.e("onPageStarted", "onPageStarted");
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			Log.e("onReceivedError", "onReceivedError");
		}

		@Override
		public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
			super.onUnhandledKeyEvent(view, event);
			Log.e("onUnhandledKeyEvent", "onUnhandledKeyEvent");
		}

		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			Log.e("shouldOverrideKeyEvent", "shouldOverrideKeyEvent");
			return super.shouldOverrideKeyEvent(view, event);

		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.e("shouldOverrideUrlLoading", url);
//			return super.shouldOverrideUrlLoading(view, url);
			return false;

		}

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_fblike, menu);
        return true;
    }

	/** Allow user to navigate our facebook page.
	 * If you don't override, android's back key will just lead user to go back to the main activity. */
    @Override
    public void onBackPressed(){
    	if (!mWebView.canGoBack()){
        	Intent intent = getIntent();
        	intent.putExtra(Constants.Common.INTENT_FB_LIKE, mLiked);

    		setResult(RESULT_OK, intent);
    		finish();
    	}
    	else{
    		mWebView.goBack();
    	}
    }
}
