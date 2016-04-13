package com.softwarejoint.twitterhelper;

import twitter4j.Status;

public interface TwitterStatusCallback {
	void onTweetSuccess(Status status);
	void onTweetFailed();
}
