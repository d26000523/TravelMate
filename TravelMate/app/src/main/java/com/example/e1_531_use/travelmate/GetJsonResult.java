package com.example.e1_531_use.travelmate;

import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by E1-531-USE on 2016/5/23.
 */
public class GetJsonResult {
    private String targetUrl;
    private String jsonResult=null;
    private ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    public GetJsonResult(String url, ArrayList<NameValuePair> n)
    {
        targetUrl = url;
        nameValuePairs = n;

    }
    public GetJsonResult(String url)
    {
        targetUrl = url;


    }
    public String GetResultQuery()
    {
        return executeQuery(jsonResult);

    }
    private String executeQuery(String query)
    {
        String result = "";
        HttpClient httpClient = new DefaultHttpClient();
        try
        {
            httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(targetUrl);
            //ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            //nameValuePairs.add(new BasicNameValuePair("page", "12"));
            System.out.println("nameValuePairs:"+nameValuePairs.size());

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));

            HttpResponse httpResponse = httpClient.execute(post);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null)
            {
                builder.append(line + "\n");
            }
            inputStream.close();
            result = builder.toString();
        }
        catch (Exception e)
        {
            Log.e("log_tag", e.toString());
        }
        System.out.println(result);
        return result;
    }
}
