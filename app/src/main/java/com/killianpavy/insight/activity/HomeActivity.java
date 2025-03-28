package com.killianpavy.insight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.killianpavy.insight.R;
import com.killianpavy.insight.model.Sol;
import com.killianpavy.insight.service.Callback;
import com.killianpavy.insight.service.NasaApiService;
import com.killianpavy.insight.view.adapter.SolAdapter;

public class HomeActivity extends BaseActivity {

    private NasaApiService nasaApiService;
    private ListView lvSols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.lvSols = findViewById(R.id.lvHomeAst);

        this.nasaApiService = NasaApiService.getInstance(getApplicationContext());
        this.nasaApiService.getSols(
                new Callback<JSONObject>() {
                    @Override
                    public void onMessage(JSONObject jsonResponse) {
                        try {
                            List<Sol> sols = parseSolsFromJson(jsonResponse);

                            if (sols != null && !sols.isEmpty()) {
                                SolAdapter adapter = new SolAdapter(HomeActivity.this, sols);
                                lvSols.setAdapter(adapter);

                                Toast.makeText(HomeActivity.this, 
                                    getString(R.string.mars_sols_count, sols.size()), 
                                    Toast.LENGTH_SHORT).show();

                                lvSols.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Sol selectedSol = sols.get(position);
                                        Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                                        intent.putExtra("sol", selectedSol);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                Toast.makeText(HomeActivity.this, R.string.no_sols_data, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this, "JSON Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },

                new Callback<Exception>() {
                    @Override
                    public void onMessage(Exception e) {
                        Toast.makeText(HomeActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
        );
    }

    private List<Sol> parseSolsFromJson(JSONObject jsonResponse) throws JSONException {
        List<Sol> sols = new ArrayList<>();

        if (jsonResponse.has("sol_keys")) {
            JSONArray solKeysArray = jsonResponse.getJSONArray("sol_keys");

            for (int i = 0; i < solKeysArray.length(); i++) {
                String solKey = solKeysArray.getString(i);

                if (jsonResponse.has(solKey)) {
                    JSONObject solData = jsonResponse.getJSONObject(solKey);

                    Sol sol = new Sol();
                    sol.setName("Sol n°" + solKey);

                    if (solData.has("AT")) {
                        JSONObject atData = solData.getJSONObject("AT");
                        sol.setTemperature(atData.optDouble("av", 0));
                        sol.setTemperatureMin(atData.optDouble("mn", 0));
                        sol.setTemperatureMax(atData.optDouble("mx", 0));
                    }

                    if (solData.has("PRE")) {
                        JSONObject preData = solData.getJSONObject("PRE");
                        sol.setPressure(preData.optDouble("av", 0));
                        sol.setPressureMin(preData.optDouble("mn", 0));
                        sol.setPressureMax(preData.optDouble("mx", 0));
                    }

                    if (solData.has("HWS")) {
                        JSONObject hwsData = solData.getJSONObject("HWS");
                        sol.setWindSpeed(hwsData.optDouble("av", 0));
                    }

                    if (solData.has("WD")) {
                        JSONObject wdData = solData.getJSONObject("WD");
                        Iterator<String> keys = wdData.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            try {
                                int direction = Integer.parseInt(key);
                                if (direction >= 0 && direction < 16) {
                                    JSONObject directionData = wdData.optJSONObject(key);
                                    if (directionData != null && directionData.has("ct")) {
                                        int count = directionData.optInt("ct", 0);
                                        sol.setWindDirection(direction, count);
                                    }
                                }
                            } catch (NumberFormatException e) {
                            }
                        }
                    }

                    sol.setSeason(solData.optString("Season", "Unknown"));

                    sols.add(sol);
                }
            }
        }

        return sols;
    }
}
