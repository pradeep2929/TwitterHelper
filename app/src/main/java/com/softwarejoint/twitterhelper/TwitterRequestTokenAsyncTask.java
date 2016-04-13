package com.softwarejoint.twitterhelper;

import twitter4j.auth.RequestToken;

import android.os.AsyncTask;
import android.util.Log;

class TwitterRequestTokenAsyncTask extends AsyncTask<Void, Void, RequestToken> {

	private static final String TAG = "TwitterRequestToken";

	private TwitterHelperCallback listener;

    public TwitterRequestTokenAsyncTask(TwitterHelperCallback listener) {
		this.listener = listener;
    }

	@Override
	protected RequestToken doInBackground(Void... params) {
		try {
            return listener.getTwitterInstance().getOAuthRequestToken(listener.getCallBackUrl());
		} catch (Exception e) {
			Log.e(TAG, "TwitterRequestTokenAsyncTask", e);
		}

		return null;
	}

	@Override
	protected void onPostExecute(RequestToken requestToken) {
		if (requestToken != null) listener.onGotRequestToken(requestToken);
        else listener.onError();
	}
}
