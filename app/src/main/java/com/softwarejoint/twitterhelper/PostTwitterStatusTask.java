package com.softwarejoint.twitterhelper;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;

class PostTwitterStatusTask extends AsyncTask<Void, Void, twitter4j.Status> {

    private static final String TAG = "PostTwitterStatusTask";

    private TwitterStatusCallback mCallback;
    private TwitterHelper twitterHelper;
    private String tweetMessage;
    private Bitmap bitmapImage;

    public PostTwitterStatusTask(String tweet, Bitmap bitmap, TwitterStatusCallback callback,
                                 TwitterHelper twitterHelper) {
        tweetMessage = tweet;
        bitmapImage = bitmap;
        this.mCallback = callback;
        this.twitterHelper = twitterHelper;
    }

    @Override
    protected twitter4j.Status doInBackground(Void... params) {

        twitter4j.Status result = null;

        Twitter twitter = twitterHelper.getTwitterAuthenticatedInstance();

        StatusUpdate mStatusUpdate = new StatusUpdate(tweetMessage);

        try {

            if (bitmapImage != null) {
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                if (bitmapImage.compress(Bitmap.CompressFormat.PNG, 0, bao)) {
                    byte[] bitmapData = bao.toByteArray();
                    ByteArrayInputStream bs = new ByteArrayInputStream(bitmapData);
                    mStatusUpdate.setMedia("Photo", bs);
                }
            }

            result = twitter.updateStatus(mStatusUpdate);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground", e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(twitter4j.Status status) {
        if (status != null) mCallback.onTweetSuccess(status);
        else mCallback.onTweetFailed();
    }
}
