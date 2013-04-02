package com.yooiistudios.chris.facebook_like_sample;

public class Constants {

	public static final class Common{
		/* id of the user to follow. same as 'like' button*/
		/**
		 * Facebook id to follow. Should be an object id(String object in numeric form).<br> 
		 */
		public static final String FB_FOLLOW_ID = "168356489891715";
		public static final String FB_APP_ID = "429606567129217";
		public static final String FB_PAGE_PREFIX = "http://m.facebook.com/";
		public static final String INTENT_FB_LIKE = "like";

	    public static final String[] permissions = {"publish_stream"};
	}
	public static final class SharedPreference{
		/* shared preference constants */
		public static final String NAME_COMMON = "preference";
	    public static final String NAME_FB_SESSION_PREF = "facebook-session";
	    
		public static final class NAME{
		    public static final String FB_SESSION_TOKEN = "access_token";
		    public static final String FB_SESSION_EXPIRES = "expires_in";
		    public static final String FB_SESSION_LAST_UPDATE = "last_update";
		    
		    public static final String FB_LIKED = "facebook-liked";
		}
		public static final class VALUE{
			public static final String FB_SESSION_TOKEN = null;
		    public static final long FB_SESSION_EXPIRES = 0;
		    public static final long FB_SESSION_LAST_UPDATE = 0;
		    
			public static final boolean FB_LIKED = false;
		}
	}
//	public static final class Facebook{
//		public static final class REQUEST{
//			public static final String FIELDS = "fields";
//		}
//	}
	public static final class ParseConstants{
		public static final class Facebook{
			public static final class Likes{
				public static final String LIKES = "likes";
				public static final String ID = "id";
				public static final String DATA = "data";
			}
			public static final class User{
				public static final String NAME = "name";
				public static final String ID = "id";
			}
		}
	}
}
