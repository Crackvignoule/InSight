package com.killianpavy.insight.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.killianpavy.insight.model.Sol;

import java.util.Map;

public class SolView extends View {

    private static final int EARTH_ORBIT = 149597870;
    private static final int NUM_DIRECTIONS = 16;
    private static final float TRIANGLE_WIDTH_ANGLE = 20f;
    
    private Sol sol;

    private Paint circlePaint;
    private Paint trianglePaint;
    private Paint textPaint;
    private Paint gridPaint;
    
    public SolView(Context context) {
        super(context);
        init();
    }

    public SolView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SolView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setSol(Sol sol) {
        this.sol = sol;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        int width = getWidth();
        int height = getHeight();
        
        if (width == 0 || height == 0) return;
        
        int centerX = width / 2;
        int centerY = height / 2;
        float radius = Math.min(centerX, centerY) * 0.8f;
        
        canvas.drawColor(Color.BLACK);
        
        for (int i = 1; i <= 4; i++) {
            float circleRadius = radius * i / 4;
            canvas.drawCircle(centerX, centerY, circleRadius, gridPaint);
        }
        
        String[] directions = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", 
                               "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
        
        for (int i = 0; i < NUM_DIRECTIONS; i++) {
            float angle = (float) (i * 360 / NUM_DIRECTIONS - 90); // -90 to start from North
            float angleRad = (float) Math.toRadians(angle);
            
            float labelDistance = radius * 1.1f;
            float labelX = centerX + (float)(labelDistance * Math.cos(angleRad));
            float labelY = centerY + (float)(labelDistance * Math.sin(angleRad));
            
            canvas.drawText(directions[i], labelX, labelY, textPaint);
        }
        
        if (sol != null && sol.getMaxWindDirectionCount() > 0) {
            for (int direction = 0; direction < NUM_DIRECTIONS; direction++) {
                int count = sol.getWindDirectionCount(direction);
                if (count > 0) {
                    float normalizedValue = (float) count / sol.getMaxWindDirectionCount();
                    float triangleLength = radius * normalizedValue;
                    
                    float angle = (float) (direction * 360 / NUM_DIRECTIONS - 90); // -90 to start from North
                    float halfWidth = (float) Math.toRadians(TRIANGLE_WIDTH_ANGLE / 2);
                    
                    float angleRad = (float) Math.toRadians(angle);
                    float x1 = centerX;
                    float y1 = centerY;
                    
                    float x2 = centerX + (float)(triangleLength * Math.cos(angleRad - halfWidth));
                    float y2 = centerY + (float)(triangleLength * Math.sin(angleRad - halfWidth));
                    
                    float x3 = centerX + (float)(triangleLength * Math.cos(angleRad + halfWidth));
                    float y3 = centerY + (float)(triangleLength * Math.sin(angleRad + halfWidth));
                    
                    Path trianglePath = new Path();
                    trianglePath.moveTo(x1, y1);
                    trianglePath.lineTo(x2, y2);
                    trianglePath.lineTo(x3, y3);
                    trianglePath.close();
                    
                    canvas.drawPath(trianglePath, trianglePaint);
                }
            }
        }
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(2);
        circlePaint.setAntiAlias(true);
        
        trianglePaint = new Paint();
        trianglePaint.setColor(Color.RED);
        trianglePaint.setStyle(Paint.Style.FILL);
        trianglePaint.setAntiAlias(true);
        
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(24);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        
        gridPaint = new Paint();
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1);
        gridPaint.setAntiAlias(true);
    }
}
