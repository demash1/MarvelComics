package com.otonomis.marvelcomics;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.otonomis.marvelcomics.adapter.ComicsAdapter;
import com.otonomis.marvelcomics.helper.Constant;
import com.otonomis.marvelcomics.helper.GridSpacingItemDecoration;
import com.otonomis.marvelcomics.pojo.ComicsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ComicsModel comicsModel;
    List<ComicsModel> comicsModelList = new ArrayList<>();
    ComicsAdapter comicsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        comicsParse();
        comicsAdapter = new ComicsAdapter(comicsModelList, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(comicsAdapter);


    }

    public void comicsParse() {
        final String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = Constant.BASE_URL + Constant.COMICS + "ts=" + timeStamp + "&apikey=" + Constant.PUBLIC_KEY + "&hash=" + md5(timeStamp + Constant.PRIVATE_KEY + Constant.PUBLIC_KEY);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jObject = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jObject.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String title = object.getString("title");
                                String description = object.getString("description");
                                JSONObject object1 = object.getJSONObject("thumbnail");
                                String image = object1.getString("path");
                                String extension = object1.getString("extension");
                                comicsModel = new ComicsModel(id, title, description, image + extension);
                                comicsModelList.add(comicsModel);
                                comicsAdapter.notifyDataSetChanged();


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ts", timeStamp);
                params.put("hash", md5(timeStamp + Constant.PRIVATE_KEY + Constant.PUBLIC_KEY));


                return params;
            }
        };
        queue.add(postRequest);

    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
