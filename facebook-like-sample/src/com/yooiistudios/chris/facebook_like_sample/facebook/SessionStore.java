/**
 * Copyright 2010-present Facebook
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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.facebook.android.Facebook;
import com.yooiistudios.chris.facebook_like_sample.Constants.SharedPreference;
import com.yooiistudios.chris.facebook_like_sample.Constants.SharedPreference.NAME;

@SuppressWarnings("deprecation")
public class SessionStore {
    /*
     * Save the access token and expiry date so you don't have to fetch it each
     * time
     */
    public static boolean save(Facebook session, Context context) {
        Editor editor = context.getSharedPreferences(SharedPreference.NAME_FB_SESSION_PREF, Context.MODE_PRIVATE).edit();
        editor.putString(SharedPreference.NAME.FB_SESSION_TOKEN, session.getAccessToken());
        editor.putLong(SharedPreference.NAME.FB_SESSION_EXPIRES, session.getAccessExpires());
        editor.putLong(SharedPreference.NAME.FB_SESSION_LAST_UPDATE, session.getLastAccessUpdate());
        return editor.commit();
    }

    /*
     * Restore the access token and the expiry date from the shared preferences.
     */
    public static boolean restore(Facebook session, Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(SharedPreference.NAME_FB_SESSION_PREF, Context.MODE_PRIVATE);
        session.setTokenFromCache(
                savedSession.getString(SharedPreference.NAME.FB_SESSION_TOKEN, null),
                savedSession.getLong(SharedPreference.NAME.FB_SESSION_EXPIRES, 0),
                savedSession.getLong(SharedPreference.NAME.FB_SESSION_LAST_UPDATE, 0));
        return session.isSessionValid();
    }

    public static void clear(Context context) {
        Editor editor = context.getSharedPreferences(SharedPreference.NAME_FB_SESSION_PREF, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

}
