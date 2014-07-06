package com.matrix.asynchttplibrary;

import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.asynchttplibrary.logger.ALogger;
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.request.AsyncRequestHeader;
import com.matrix.asynchttplibrary.request.AsyncRequestParam;
import com.matrix.asynchttplibrary.security.CustomSSLSocketFactory;
import com.matrix.asynchttplibrary.util.AsyncUtil;

/**
 * 
 * @author Yajnesh T
 * 
 * @version v1.0
 * 
 * @description
 * 
 *              AsyncH is a simple HTTP/S library based on
 *              android-async-http-1.4.4.jar <br>
 *              In addition to the features provided by android-async-http,
 *              "AsyncH" keeps your code clean and organized with the following
 *              features <br>
 * <br>
 *              Features:<br>
 * 
 *              1) Generic binding of Request/Response/Header Models <br>
 *              2) Annotation support to modify parameters (prefix, postfix and
 *              override) <br>
 *              3) Automatically pick up protocol/method and baseurl from
 *              property file. The method "communicate" automatically picks us
 *              suitable protocol and method <br>
 *              4) String/JSON/JSONArray Parsers to handle the response <br>
 * 
 */
public class AsyncH extends AsyncHttpClient {

	private Context context = null;

	public AsyncH() {
		super();
	}

	public AsyncH(boolean fixNoHttpResponseException, int httpPort,
			int httpsPort) {
		super(fixNoHttpResponseException, httpPort, httpsPort);
	}

	public AsyncH(int httpPort, int httpsPort) {
		super(httpPort, httpsPort);
	}

	public AsyncH(int httpPort) {
		super(httpPort);
	}

	public AsyncH(SchemeRegistry schemeRegistry) {
		super(schemeRegistry);
	}

	/**
	 * Generic method to make a HTTP/S request
	 * 
	 * @param callProperties
	 *            : consists protocol(Currently only REST), method (Currently
	 *            only GET and POST) and baseURL
	 * @param header
	 *            : header values to be bound
	 * @param param
	 *            : parameters to be bound
	 * @param handler
	 *            : On success/failure callback
	 */
	public void communicate(CallProperties callProperties,
			AsyncRequestHeader header, AsyncRequestParam param,
			AsyncHttpResponseHandler handler) {
		if (callProperties == null) {
			ALogger.e("callProperties is null");
			return;
		}

		if (callProperties.protocol.equalsIgnoreCase("REST")) {
			generateRESTRequest(callProperties, header, param, handler);
		} else {
			ALogger.e("Not implemented " + callProperties.protocol);
		}

	}

	private void generateRESTRequest(CallProperties callProperties,
			AsyncRequestHeader header, AsyncRequestParam param,
			AsyncHttpResponseHandler handler) {

		if (callProperties.method.equalsIgnoreCase("GET")) {
			generateGetRequest(callProperties.baseURL, header, param, handler);
		} else if (callProperties.method.equalsIgnoreCase("POST")) {
			generatePostRequest(callProperties.baseURL, header, param, handler);
		} else if (callProperties.method.equalsIgnoreCase("DELETE")) {
			generateDeleteRequest(callProperties.baseURL, header, param,
					handler);
		} else {
			ALogger.e("Not implemented " + callProperties.method);
		}
	}

	private void generateDeleteRequest(String baseURL,
			AsyncRequestHeader header, AsyncRequestParam param,
			AsyncHttpResponseHandler handler) {

		if (header != null) {
			bindHeaders(header);
		}

		if (baseURL == null) {
			ALogger.e("baseUrl is null");
			handler.onFailure(0, null, "baseUrl is null".getBytes(), null);
			return;
		}
		if (handler == null) {
			ALogger.e("handler is null");
			return;
		}
		delete(baseURL, handler);

	}

	private void generatePostRequest(String baseURL, AsyncRequestHeader header,
			AsyncRequestParam param, AsyncHttpResponseHandler handler) {
		if (param.getRequestType() == AsyncRequestParam.Type.DEFAULT) {
			generatePostRequestDefault(baseURL, header, param, handler);
		} else if (param.getRequestType() == AsyncRequestParam.Type.JSON) {
			generatePostRequestJSON(baseURL, header, param, handler);

		}
	}

	private void generatePostRequestJSON(String baseURL,
			AsyncRequestHeader header, AsyncRequestParam param,
			AsyncHttpResponseHandler handler) {

		if (context == null) {
			ALogger.e("Context is null, POST request with JSON params require context, AsyncH asyncHRequest = new AsyncH(); asyncHRequest.setContext(getApplicationContext());");
			return;
		}

		if (header != null) {
			bindHeaders(header);
		}

		JSONObject jsonParams = null;
		if (param != null) {
			jsonParams = (JSONObject) AsyncUtil.processParams(param, false);

		}

		if (baseURL == null) {
			ALogger.e("baseUrl is null");
			handler.onFailure(0, null, "baseUrl is null".getBytes(), null);
			return;
		}
		if (handler == null) {
			ALogger.e("handler is null");
			return;
		}

		StringEntity entity = null;
		try {
			entity = new StringEntity(jsonParams.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if (entity == null) {
			ALogger.e("Unable to create entity from : " + jsonParams.toString());
			return;
		}

		post(context, baseURL, entity, "application/json", handler);

	}

	private void generatePostRequestDefault(String baseUrl,
			AsyncRequestHeader header, AsyncRequestParam param,
			AsyncHttpResponseHandler handler) {

		RequestParams params = null;

		if (header != null) {
			bindHeaders(header);
		}
		if (param != null) {
			params = (RequestParams) AsyncUtil.processParams(param, false);

		}

		if (baseUrl == null) {
			ALogger.e("baseUrl is null");
			handler.onFailure(0, null, "baseUrl is null".getBytes(), null);
			return;
		}
		if (handler == null) {
			ALogger.e("handler is null");
			return;
		}

		post(baseUrl, params, handler);

	}

	public void generatePostRequestTemperoryMethod(String baseUrl,
			AsyncRequestHeader header, RequestParams param,
			AsyncHttpResponseHandler handler) {

		if (header != null) {
			bindHeaders(header);
		}

		if (baseUrl == null) {
			ALogger.e("baseUrl is null");
			handler.onFailure(0, null, "baseUrl is null".getBytes(), null);
			return;
		}
		if (handler == null) {
			ALogger.e("handler is null");
			return;
		}

		post(baseUrl, param, handler);

	}

	private void generateGetRequest(String baseUrl, AsyncRequestHeader header,
			AsyncRequestParam param, AsyncHttpResponseHandler handler) {
		RequestParams params = null;

		if (header != null) {
			bindHeaders(header);
		}
		if (param != null) {
			params = (RequestParams) AsyncUtil.processParams(param, true);

		}

		if (baseUrl == null) {
			ALogger.e("baseUrl is null");
			handler.onFailure(0, null, "baseUrl is null".getBytes(), null);
			return;
		}
		if (handler == null) {
			ALogger.e("handler is null");
			return;
		}

		get(baseUrl, params, handler);

	}

	private void bindHeaders(AsyncRequestHeader header) {

		HashMap<String, String> modelMap = AsyncUtil.createHashMapFromModel(
				header, false);

		Set<Entry<String, String>> set = modelMap.entrySet();

		Iterator<Entry<String, String>> iterator = set.iterator();

		while ((iterator.hasNext())) {
			Entry<String, String> data = iterator.next();
			ALogger.v(data.getKey() + ":" + data.getValue());
			addHeader(data.getKey(), data.getValue());
		}
	}

	/**
	 * Use with caution, Accepts all secure certificates.
	 */
	public void enableHTTPSCertificates() {

		try {

			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			CustomSSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
			sf.setHostnameVerifier(CustomSSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			setSSLSocketFactory(sf);
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(),
					"Unable to set HTTPS certificate  \n" + e.getMessage());
		}
	}

	public void setContext(Context context) {
		this.context = context;
	}

}