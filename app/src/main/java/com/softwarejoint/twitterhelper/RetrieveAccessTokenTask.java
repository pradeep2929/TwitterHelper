package com.softwarejoint.twitterhelper;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import twitter4j.auth.AccessToken;

class RetrieveAccessTokenTask extends AsyncTask<Void, Void, AccessToken> {

    private static final String TAG = "RetrieveAccessTokenTask";

    private Uri uri;
    private TwitterHelper twitterHelper;

    public RetrieveAccessTokenTask(Uri uri, TwitterHelper twitterHelper) {
        this.uri = uri;
        this.twitterHelper = twitterHelper;
    }

    @Override
    protected AccessToken doInBackground(Void... params) {

        String verifier = uri.getQueryParameter(Constants.URL_TWITTER_OAUTH_VERIFIER);

        AccessToken accessToken = null;

        try {
            accessToken = twitterHelper.getTwitterInstance()
                    .getOAuthAccessToken(twitterHelper.getRequestToken(), verifier);
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
