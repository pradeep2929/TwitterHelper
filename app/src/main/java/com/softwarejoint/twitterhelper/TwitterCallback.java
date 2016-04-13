package com.softwarejoint.twitterhelper;

public interface TwitterCallback extends TwitterHelperCallback {

	void onSuccess();
	void onFailed(Exception e);

}
