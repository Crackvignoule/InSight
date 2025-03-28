package com.killianpavy.insight.service;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
import static com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class NasaApiService {

    private static final String NASA_API_BASE_URL = "https://api.nasa.gov/neo/rest/v1";
    private static final String NASA_API_TOKEN = "j6fy9skXuyk1siXdFMy4djS5GKQjla99ye0BdFWq";

    private static NasaApiService INSTANCE;

    private RequestQueue requestQueue;
    private Context context;

    private NasaApiService(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public synchronized static NasaApiService getInstance() {
        if (INSTANCE == null) throw new RuntimeException("NasApiService not initialized. Call getInstance(Context context) first.");
        return INSTANCE;
    }

    public synchronized static NasaApiService getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NasaApiService(context);
        }
        return INSTANCE;
    }

    public void getSols(Callback<JSONObject> onSuccess, Callback<Exception> onError) {
        String url = String.format("https://api.nasa.gov/insight_weather/?api_key=%s&feedtype=json&ver=1.0",
                NASA_API_TOKEN);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
                        onSuccess.onMessage(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "KO", Toast.LENGTH_SHORT).show();
                        onError.onMessage(error);
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        30 * 1000,
                        DEFAULT_MAX_RETRIES,
                        DEFAULT_BACKOFF_MULT));

        this.requestQueue.add(jsonObjectRequest);
    }
}
