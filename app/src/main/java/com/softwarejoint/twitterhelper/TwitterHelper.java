package com.softwarejoint.twitterhelper;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

public class TwitterHelper {

	/**
	 * Register your here app https://dev.twitter.com/apps/new and get your
	 * consumer key and secret
	 * */

    private static final String TAG = "TwitterHelper";

    private static final String CALLBACK_SCHEME = "x-oauthflow-twitter";
    private static final String CALLBACK_URL = CALLBACK_SCHEME + "://callback";

    private String CONSUMER_KEY = "";
    private String CONSUMER_SECRET = "";

	// Shared Preferences
    private SharedPreferences mSharedPreferences;
    private Context mContext;

	// Twitter
    private Twitter twitter;
    private RequestToken requestToken;
    private TwitterLoginDialog twitterLoginDialog;
    private TwitterLoginCallback twitterLoginCallback;

	public TwitterHelper(Context context, String TWITTER_CONSUMER_KEY, String TWITTER_CONSUMER_SECRET){
		mContext = context;
		CONSUMER_KEY = TWITTER_CONSUMER_KEY;
		CONSUMER_SECRET = TWITTER_CONSUMER_SECRET;
        mSharedPreferences = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

	public void logIn(Context context, TwitterLoginCallback callback) {
		mContext = context;
        twitterLoginCallback = callback;

		if (isTwitterLoggedIn()) twitterLoginCallback.onLoginSuccess();
        else new RequestTokenAsyncTask(this).execute();
	}

	public void postStatus(final String tweetMessage, final Bitmap tweetBitmapImage,
                                       final TwitterStatusCallback callback) {

        final PostTwitterStatusTask pst =
                new PostTwitterStatusTask(tweetMessage, tweetBitmapImage, callback, this);

		if (isTwitterLoggedIn()) {
            pst.execute();
		} else
            logIn(mContext, new TwitterLoginCallback() {
                @Override
                public void onLoginSuccess() {
                    pst.execute();
                }

                @Override
                public void onLoginFailed(Exception e) {
                    Log.e(TAG, "Twitter post failed because user not authenticated with reason", e);
                }
            });
	}


	/**
	 * Logout from Twitter
	 */
	public void logoutFromTwitter() {
		mSharedPreferences.edit().clear().apply();
	}

	/**
	 * Check user already logged in your application using twitter Login flag is
	 * fetched from Shared Preferences
	 * */
	public boolean isTwitterLoggedIn() {
		return mSharedPreferences.getBoolean(Constants.PREF_KEY_TWITTER_LOGIN, false);
	}

    public String getCallBackScheme() {
        return CALLBACK_SCHEME;
    }

    public String getCallBackUrl() {
        return CALLBACK_URL;
    }

    private TwitterFactory getTwitterFactory(){
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(CONSUMER_KEY);
        builder.setOAuthConsumerSecret(CONSUMER_SECRET);
        builder.setDebugEnabled(true);
        Configuration configuration = builder.build();
        return new TwitterFactory(configuration);
    }

    public Twitter getTwitterAuthenticatedInstance() {
        TwitterFactory factory = getTwitterFactory();
        String access_token = mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_TOKEN, "");
        String access_token_secret = mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_SECRET, "");
        AccessToken accessToken = new AccessToken(access_token, access_token_secret);
        return factory.getInstance(accessToken);
    }

    public Twitter getTwitterInstance() {
        if(twitter == null){
            TwitterFactory factory = getTwitterFactory();
            twitter = factory.getInstance();
        }

        return twitter;
    }

    public RequestToken getRequestToken() {
        return requestToken;
    }

    public void onGotRequestToken(RequestToken requestToken) {
        this.requestToken = requestToken;
        if(twitterLoginDialog == null) {
            String authenticationURL = requestToken.getAuthenticationURL();
            twitterLoginDialog = new TwitterLoginDialog(mContext, authenticationURL, this);
        }
        twitterLoginDialog.show();
    }

    public void onGotAccessToken(AccessToken accessToken) {

        Log.i("Twitter OAuth Token", "> " + accessToken.getToken());

        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putString(Constants.PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
        e.putString(Constants.PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
        e.putBoolean(Constants.PREF_KEY_TWITTER_LOGIN, true);
        e.commit();

        dismissLoginDialog();

        if (twitterLoginCallback != null) twitterLoginCallback.onLoginSuccess();
    }

    public void onGotOAuthVerifier(String oAuthVerifier) {
        Log.i("retrieveAndSaveOauth", "oAuthVerifier received : " + oAuthVerifier);
        dismissLoginDialog();
        new RetrieveAccessTokenTask(oAuthVerifier, this).execute();
    }

    public void onError() {
        dismissLoginDialog();
        if(twitterLoginCallback != null){
            twitterLoginCallback.onLoginFailed(new Exception("Error getting request token"));
        }
    }

    private void dismissLoginDialog(){
        if(twitterLoginDialog != null && twitterLoginDialog.isShowing()){
            twitterLoginDialog.dismiss();
        }
    }
}
