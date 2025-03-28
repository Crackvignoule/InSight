package com.killianpavy.insight.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.killianpavy.insight.R;
import com.killianpavy.insight.model.Sol;
import com.killianpavy.insight.service.Callback;
import com.killianpavy.insight.service.NasaApiService;
import com.killianpavy.insight.view.SolView;

public class DetailActivity extends BaseActivity {

    private NasaApiService nasaApiService;

    private TextView tvSolName;
    private TextView tvSolTemperature;
    private TextView tvSolPressure;
    private TextView tvSolWindSpeed;
    private TextView tvSolSeason;
    private SolView solView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nasaApiService = NasaApiService.getInstance();

        tvSolName = findViewById(R.id.tvSolName);
        tvSolTemperature = findViewById(R.id.tvSolTemperature);
        tvSolPressure = findViewById(R.id.tvSolPressure);
        tvSolWindSpeed = findViewById(R.id.tvSolWindSpeed);
        tvSolSeason = findViewById(R.id.tvSolSeason);
        solView = findViewById(R.id.solView);

        Sol sol = (Sol) getIntent().getSerializableExtra("sol");
        if (sol != null) {
            showSol(sol);
        }
    }

    private void showSol(Sol sol) {
        tvSolName.setText(sol.getName());
        
        String temperatureText = getString(R.string.temperature) + ": " +
                "avg: " + formatValue(sol.getTemperature().doubleValue()) + "°C, " +
                "min: " + formatValue(sol.getTemperatureMin().doubleValue()) + "°C, " +
                "max: " + formatValue(sol.getTemperatureMax().doubleValue()) + "°C";
        tvSolTemperature.setText(temperatureText);
        
        String pressureText = getString(R.string.pressure) + ": " +
                "avg: " + formatValue(sol.getPressure().doubleValue()) + " Pa, " +
                "min: " + formatValue(sol.getPressureMin().doubleValue()) + " Pa, " +
                "max: " + formatValue(sol.getPressureMax().doubleValue()) + " Pa";
        tvSolPressure.setText(pressureText);
        
        String windSpeedText = "Wind Speed: " + formatValue(sol.getWindSpeed().doubleValue()) + " m/s";
        tvSolWindSpeed.setText(windSpeedText);
        
        String seasonText = "Season: " + sol.getSeason();
        tvSolSeason.setText(seasonText);

        solView.setSol(sol);
    }
    
    private String formatValue(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.toString();
    }
}