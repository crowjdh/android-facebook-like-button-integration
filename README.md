android-facebook-like-integration
=================================

This is a simple example for facebook "like(following specific user or corporation on facebook)" feature integration on Android.<br>
<br>
Features<br>
  1. Log-in and log-out functionality.<br>
  2. Navigate to the facebook page you set directly in your app.(doesn't connect to android web browser.)<br>
  3. Detect whether the user clicked "like" button to the page you set.<br>
<br>

Preparing(on Eclipse)
  1. You must download Facebook SDK from "https://developers.facebook.com/android/" and import "FacebookSDK".<br>
    It is provided as a project.(Don't copy into workspace)<br>
    You don't have to import other sample projects that Facebook SDK provides.<br>
  2. After that, right click the sample project and click 'android' tab and add library.<br>
    It's not provided as a jar file, so you have to add "FacebookSDK project" as a library.<br>
    For detail instruction, see below.<br>
    http://developer.android.com/tools/projects/projects-eclipse.html#ReferencingLibraryProject<br>
  3. You have to add a permission "user_like" to fetch user's like list.<br>
    For detail information, see  below.<br>
    https://developers.facebook.com/docs/guides/appcenter/#authorization.<br>


If you have a question, please <a href='crowjdh@gmail.com'>mail me</a>.
