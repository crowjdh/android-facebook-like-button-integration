/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yooiistudios.chris.facebook_like_sample.facebook;

import java.util.Hashtable;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.yooiistudios.chris.facebook_like_sample.Constants;
import com.yooiistudios.chris.facebook_like_sample.Constants.SharedPreference;
import com.yooiistudios.chris.facebook_like_sample.Constants.SharedPreference.NAME;

public class Utility {

    public static Facebook mFacebook;
    @SuppressWarnings("deprecation")
    public static AsyncFacebookRunner mAsyncRunner;
    public static String userUID = null;
    public static String userName = null;
    public static String objectID = null;
    public static AndroidHttpClient httpclient = null;
    public static Hashtable<String, String> currentPermissions = new Hashtable<String, String>();
    
    public static void storeFacebookLiked(Context con, boolean liked){
    	SharedPreferences sharedPref = con.getSharedPreferences(Constants.SharedPreference.NAME_COMMON, Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sharedPref.edit();
		edit.putBoolean(Constants.SharedPreference.NAME.FB_LIKED, liked);
		edit.commit();
    }
}
