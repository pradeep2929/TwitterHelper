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


    private TwitterHelper twitterHelper;

    public TwitterLoginDialog(Context context, String url, TwitterHelper twitterHelper) {
        super(context, style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);

        this.twitterHelper = twitterHelper;

        setContentView(getWebView(context, url));
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setOnCancelListener(this);
	}

    @Override
    public void onCancel(DialogInterface dialog) {
        twitterHelper.onError();
    }

    private WebView getWebView(Context mContext, String url) {
		WebView mWebView = new WebView(mContext);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(url);
		return mWebView;
	}

    class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (uri.getScheme().equals(twitterHelper.getCallBackScheme())){
                String verifier = uri.getQueryParameter(Constants.OAUTH_VERIFIER);
                twitterHelper.onGotOAuthVerifier(verifier);
                dismiss();
                return true;
            }
            return false;
        }
    }
}
