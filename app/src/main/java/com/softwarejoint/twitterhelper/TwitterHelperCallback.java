package com.softwarejoint.twitterhelper;

import android.net.Uri;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by soni on 05-04-2016.
 */
public interface TwitterHelperCallback {

    void onGotRequestToken(RequestToken requestToken);
    void onGotAccessToken(AccessToken accessToken);

    void onLoginUriFailed(Uri uri);

    String getRequestToken();

    String getCallBackScheme();
    String getCallBackUrl();
    Twitter getTwitterInstance();

    void onError();
}
