package com.softwarejoint.twitterhelper;

/**
 * Created by soni on 05-04-2016.
 */
public interface TwitterLoginCallback {
    void onLoginSuccess();
    void onLoginFailed(Exception e);
}
