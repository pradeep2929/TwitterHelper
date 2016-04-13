package com.softwarejoint.twitterhelper;

import android.R.style;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class TwitterLoginDialog extends Dialog implements OnCancelListener {


    private TwitterHelperCallback twitterHelperCallback;

    public TwitterLoginDialog(Context context, Uri uri, TwitterHelperCallback callback) {
        super(context, style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);

        this.twitterHelperCallback = callback;

        setContentView(getWebView(context, uri.toString()));
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setOnCancelListener(this);
	}

    @Override
    public void onCancel(DialogInterface dialog) {
        twitterHelperCallback.onError();
    }

    private WebView getWebView(Context mContext, String url) {
		WebView mWebView = new WebView(mContext);
		// Let's display the progress in the activity title bar, like the
		// browser app does.
		// mActivity.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		mWebView.getSettings().setJavaScriptEnabled(true);

		mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Uri uri = Uri.parse(failingUrl);
                String scheme = twitterHelperCallback.getCallBackScheme();
                if (scheme.equals(uri.getScheme())) twitterHelperCallback.onLoginUriFailed(uri);
                else twitterHelperCallback.onError();
            }
        });

		mWebView.loadUrl(url);
		return mWebView;

	}
}
