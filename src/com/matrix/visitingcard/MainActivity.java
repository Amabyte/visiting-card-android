package com.matrix.visitingcard;

import com.matrix.visitingcard.template.Template;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		WebView wv = (WebView) findViewById(R.id.wv);
		wv.loadUrl(Template.getTemplatePath(0));
	}
}
