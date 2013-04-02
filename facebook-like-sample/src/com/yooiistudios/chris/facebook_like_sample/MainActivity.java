package com.yooiistudios.chris.facebook_like_sample;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.yooiistudios.chris.facebook_like_sample.Constants.ParseConstants;
import com.yooiistudios.chris.facebook_like_sample.facebook.BaseRequestListener;
import com.yooiistudios.chris.facebook_like_sample.facebook.FBLikeActivity;
import com.yooiistudios.chris.facebook_like_sample.facebook.FacebookAuthButton;
import com.yooiistudios.chris.facebook_like_sample.facebook.SessionEvents;
import com.yooiistudios.chris.facebook_like_sample.facebook.SessionStore;
import com.yooiistudios.chris.facebook_like_sample.facebook.Utility;

public class MainActivity extends Activity {
	/* Views */
	private TextView mUserName;
	private TextView mFollowToID;
	private TextView mLiked;
	private TextView mLikeList;
	private FacebookAuthButton mAuthBtn;
	private Button mLikeBtn;
	
	/* Objects */
	private Handler mHandler;
	private ArrayList<String> mLikeIds;
	private boolean mFacebookLiked = false;

	/* Primitives */
	/**
	 * true : if user clicked 'like' button previously, we don't check if the user still likes<br>
	 * false : if user 'unliked' FB_FOLLOW_ID after 'liking', we do check if the user still like FB_FOLLOW_ID.<br>
	 * 
	 * @see Constants.Common#FB_FOLLOW_ID
	 */
	private boolean mDontBother = false;

	/* Constants */
    final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
    final static int LIKE_ACTIVITY_RESULT_CODE = 1;

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        /*
         * if this is the activity result from authorization flow, do a call
         * back to authorizeCallback Source Tag: login_tag
         */
            case AUTHORIZE_ACTIVITY_RESULT_CODE: {
                Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
                break;
            }
            case LIKE_ACTIVITY_RESULT_CODE:{
            	if (resultCode == RESULT_OK){
            		boolean liked = data.getExtras().getBoolean(Constants.Common.INTENT_FB_LIKE);
            		if (mFacebookLiked == liked)
            			break;
            		mFacebookLiked = liked;
            		if (liked)
            			mLikeIds.add(Constants.Common.FB_FOLLOW_ID);
            		else{
	            		for (String id : mLikeIds){
	            			if (id.equals(Constants.Common.FB_FOLLOW_ID)){
	                    		mLikeIds.remove(id);
	                    		break;
	            			}
	            		}
            		}
            		showFacebookStatus();
            	}
            	break;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mUserName = (TextView)findViewById(R.id.user_name);
        mFollowToID = (TextView)findViewById(R.id.follow_to_id);
        mLiked = (TextView)findViewById(R.id.like);
        mLikeList = (TextView)findViewById(R.id.likes_list);
        mAuthBtn = (FacebookAuthButton)findViewById(R.id.btn_auth);
        mLikeBtn = (Button)findViewById(R.id.btn_like);
        
        mHandler = new Handler();
        mLikeIds = new ArrayList<String>();
        
        mFollowToID.setText(getString(R.string.display_follow_id_prefix) + Constants.Common.FB_FOLLOW_ID);
        mAuthBtn.setBackgroundColor(Color.BLUE);
        mLikeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
        		/* Opens activity with webview to connect to the facebook page to follow. */
        		Intent intent = new Intent(MainActivity.this, FBLikeActivity.class);
            	intent.putExtra(Constants.Common.INTENT_FB_LIKE, mFacebookLiked);
        		startActivityForResult(intent, LIKE_ACTIVITY_RESULT_CODE);
			}
		});
        
        
        if (mDontBother){
	        SharedPreferences sharedPref = getSharedPreferences(Constants.SharedPreference.NAME_COMMON, MODE_PRIVATE);
	        mFacebookLiked = sharedPref.getBoolean(Constants.SharedPreference.NAME.FB_LIKED, false);
        }
        
    	showFacebookStatus();
        
        initFacebook();
    }
    
    private void initFacebook(){
    	// Create the Facebook Object using the app id.
        Utility.mFacebook = new Facebook(Constants.Common.FB_APP_ID);
        // Instantiate the asynrunner object for asynchronous api calls.
        Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

        // restore session if one exists
        SessionStore.restore(Utility.mFacebook, this);
        
        /* This listener will be called when succeed to log in. */
        SessionEvents.addAuthListener(new SessionEvents.AuthListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onAuthSucceed() {
				// TODO Auto-generated method stub

				/* Log in succeed. specify parameters you need to get and request to async task */
				Bundle params = new Bundle();
		        params.putString("fields", "name, picture, likes");
		        
		        Utility.mAsyncRunner.request("me", params, new BaseRequestListener() {
					
					@Override
					public void onComplete(String response, Object state) {
						// TODO Auto-generated method stub

			            JSONObject jsonObject;
			            try {
			                jsonObject = new JSONObject(response);

			                /* Parse data you've requested. */
//			                final String picURL = jsonObject.getJSONObject("picture")
//			                        .getJSONObject("data").getString("url");
			                Utility.userName = jsonObject.getString(ParseConstants.Facebook.User.NAME);
			                Utility.userUID = jsonObject.getString(ParseConstants.Facebook.User.ID);
			                Log.i("fb session", Utility.userName);

							if (mFacebookLiked && mDontBother)
								return;
			                
			                /* check if user previously liked the YOOII STUDIOS */
			                JSONArray likesList = jsonObject.getJSONObject(ParseConstants.Facebook.Likes.LIKES).getJSONArray(ParseConstants.Facebook.Likes.DATA);
			                mLikeIds.clear();
			                for (int i = 0; i < likesList.length(); i++){
			                	JSONObject obj = likesList.getJSONObject(i);
			                	String likesId = obj.getString(ParseConstants.Facebook.Likes.ID);
		                		mLikeIds.add(likesId);
			                	
			                	if (likesId.equals(Constants.Common.FB_FOLLOW_ID)){
//			                		liked = true;
			                		mFacebookLiked = true;
//			                		break;
			                	}
			                }
			                mHandler.post(new Runnable() {
								
								@Override
								public void run() {
							    	//여기에다가 좋아요 했으면 버튼 못누르게 설정.
			                		Utility.storeFacebookLiked(MainActivity.this, mFacebookLiked);
									
						        	showFacebookStatus();
								}
							});
			            } catch (JSONException e) {
			                e.printStackTrace();
			            }
					}
				});
			}
			
			@Override
			public void onAuthFail(String error) {
				// TODO Auto-generated method stub
                Log.i("fb session", error);
			}
		});
        SessionEvents.addLogoutListener(new SessionEvents.LogoutListener() {
			
			@Override
			public void onLogoutFinish() {
				// TODO Auto-generated method stub
				showFacebookStatus();
			}
			
			@Override
			public void onLogoutBegin() {
				// TODO Auto-generated method stub
				
			}
		});

    	mAuthBtn.init(this, AUTHORIZE_ACTIVITY_RESULT_CODE, Utility.mFacebook, Constants.Common.permissions);
    }
    private void showFacebookStatus(){
    	final boolean loggedIn = (Utility.mFacebook != null) ? Utility.mFacebook.isSessionValid() : false;
    	final boolean liked = mFacebookLiked;

    	mAuthBtn.setBackgroundColor(loggedIn ? Color.RED : Color.BLUE);
		mUserName.setText(loggedIn ? getString(R.string.display_username_prefix) + Utility.userName : "");
		mLikeBtn.setVisibility(loggedIn ? View.VISIBLE : View.INVISIBLE);
		
    	mLiked.setText(loggedIn ? getString(R.string.display_like_prefix) +
    			(liked ? getString(R.string.display_following) : getString(R.string.display_unfollowing)): "");
    	mLikeBtn.setText(loggedIn ? (liked ? getString(R.string.unliked) : getString(R.string.liked)) : "");
    	
    	String likeList = "";
		StringBuilder builder = new StringBuilder();
		builder.append(getString(R.string.display_followlist));
    	if (loggedIn){
			for (String id : mLikeIds)
				builder.append(id).append("\n");
    	}
		likeList = builder.toString();
    	mLikeList.setText(likeList);
    }
    @Override
    public void onResume(){
    	super.onResume();
    	showFacebookStatus();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
