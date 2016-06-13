package com.softwarejoint.twitterhelper;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;

class RetrieveAccessTokenTask extends AsyncTask<Void, Void, AccessToken> {

    private static final String TAG = "RetrieveAccessTokenTask";

    private String oAuthVerifier;
    private TwitterHelper twitterHelper;

    public RetrieveAccessTokenTask(String oAuthVerifier, TwitterHelper twitterHelper) {
        this.oAuthVerifier = oAuthVerifier;
        this.twitterHelper = twitterHelper;
    }

    @Override
    protected AccessToken doInBackground(Void... params) {

        AccessToken accessToken = null;

        try {
            Twitter twitter = twitterHelper.getTwitterInstance();
            accessToken = twitter.getOAuthAccessToken(twitterHelper.getRequestToken(), oAuthVerifier);
        } catch (Exception e) {
            Log.e(TAG, "RetrieveAccessTokenTask", e);
        }

        return accessToken;
    }

    @Override
    protected void onPostExecute(AccessToken accessToken) {
        if (accessToken == null) twitterHelper.onError();
        else twitterHelper.onGotAccessToken(accessToken);
    }
}
