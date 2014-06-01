package com.matrix.visitingcard;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

import com.matrix.visitingcard.template.Template;
import com.matrix.visitingcard.util.ZipUtil;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		File sdCard = Environment.getExternalStorageDirectory();
		String path = sdCard.getAbsolutePath() + "/visiting";

	//	Log.e("tag", "hello" + ZipUtil.unpackZip(path, "test.zip"));
		WebView wv = (WebView) findViewById(R.id.wv);
		wv.loadUrl(Template.getTemplatePath(0));

	}
}
