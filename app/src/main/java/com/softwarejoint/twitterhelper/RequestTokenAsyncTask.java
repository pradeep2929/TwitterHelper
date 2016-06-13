package com.softwarejoint.twitterhelper;

import twitter4j.Twitter;
import twitter4j.auth.RequestToken;

import android.os.AsyncTask;
import android.util.Log;

class RequestTokenAsyncTask extends AsyncTask<Void, Void, RequestToken> {

	private static final String TAG = "TwitterRequestToken";

	private TwitterHelper twitterHelper;

    public RequestTokenAsyncTask(TwitterHelper twitterHelper) {
		this.twitterHelper = twitterHelper;
    }

	@Override
	protected RequestToken doInBackground(Void... params) {
		Twitter twitter = twitterHelper.getTwitterInstance();
		try {
			return twitter.getOAuthRequestToken(twitterHelper.getCallBackUrl());
		} catch (Exception e) {
			Log.e(TAG, "RequestTokenAsyncTask", e);
		}

		return null;
	}

	@Override
	protected void onPostExecute(RequestToken requestToken) {
		if (requestToken != null) twitterHelper.onGotRequestToken(requestToken);
        else twitterHelper.onError();
	}
}
