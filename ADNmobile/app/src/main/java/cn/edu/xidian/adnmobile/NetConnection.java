package cn.edu.xidian.adnmobile;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetConnection {
	public NetConnection(final String url,final HttpMethod method,final SuccessCallback successCallback,final FailCallback failCallback,final String ... kvs){
		
		
		
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				
				StringBuffer paramStr = new StringBuffer();
				for(int i = 0;i < kvs.length;i+=2){
					paramStr.append(kvs[i]).append("=").append(kvs[i+1]).append("&");
				}
				
				try {
					URLConnection uc;
					switch (method) {
					case POST:
						uc = new URL(url).openConnection();
						uc.setDoOutput(true);
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(),"utf-8"));
						bw.write(paramStr.toString());
						bw.flush();
						break;
					default:
						uc = new URL(url+"?"+paramStr.toString()).openConnection();
						break;
					}
					
					System.out.println("Request url:"+uc.getURL());
					System.out.println("Request data:" + paramStr);
					
					BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(),"utf-8"));
					String line = null;
					StringBuffer result = new StringBuffer();

					while((line = br.readLine()) != null){
						result.append(line);
					}

					System.out.println("Result:"+result);
					return result.toString();

					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(String result) {
				if(result != null){
					if(successCallback!=null){
						System.out.println(">>>>>>>>>here is NetConnection->onPostExecute->successCallback.onSuccess>>>>>>>>>>>>");
						successCallback.onSuccess(result);
					}
				}else{
					if (failCallback!=null) {
						failCallback.onFail();
					}
				}
				super.onPostExecute(result);
			}
		}.execute();
		
	}
	
	public static interface SuccessCallback{
		void onSuccess(String result);
	}
	
	public static interface FailCallback{
		void onFail();
	}
}
