package org.nhnnext.android.selftest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class Proxy {

	public String getJson(){
		
		try{
			
			URL url = new URL("http://54.64.250.239:5009/loadData");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			
			conn.setConnectTimeout(10*1000);
			conn.setReadTimeout(10*1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Connection", "Kepp-Alive");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Accept", "application/json");
			
			conn.setDoInput(true);
			conn.connect();
			
			int status = conn.getResponseCode();
			Log.i("test", "ProxyResponseCode:"+ status);
			
			switch(status){
			case 200:
			case 201:
				BufferedReader br = new BufferedReader( new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while((line = br.readLine())!=null){
					sb.append(line + "\n");
				}
				br.close();
				return sb.toString();
		
			
			}
			
		} catch(Exception e){
			e.printStackTrace();
			Log.i("test", "NETWORK ERROR:" + e);
		}
		
		return null;
	}
}
