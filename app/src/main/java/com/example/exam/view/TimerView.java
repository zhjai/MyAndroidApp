package com.example.exam.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.exam.R;

public class TimerView extends View {
    private Paint incompletePaint;
    private Paint completePaint;
    private float progress;
    private int totalSegments = 120; // 总的线段数
    private float segmentDegrees; // 每个线段所占的角度
    private int segmentLength = 50; // 线段长度

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        incompletePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        incompletePaint.setStrokeWidth(8f);
        incompletePaint.setColor(ContextCompat.getColor(getContext(), R.color.deep_green));
        incompletePaint.setStyle(Paint.Style.STROKE);

        completePaint = new Paint(incompletePaint);
        completePaint.setColor(ContextCompat.getColor(getContext(), R.color.gray_white));

        segmentDegrees = 360f / totalSegments;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;
        int cx = width / 2;
        int cy = height / 2;

        int completedSegments = (int) (progress * totalSegments);

        for (int i = 0; i < totalSegments; i++) {
            float startAngle = (totalSegments - i - 1) * segmentDegrees - 90;
            float endAngle = startAngle + segmentDegrees;
            drawSegment(canvas, cx, cy, radius, startAngle, endAngle, incompletePaint);
        }

        for (int i = 0; i < completedSegments; i++) {
            float startAngle = (totalSegments - i - 1) * segmentDegrees - 90;
            float endAngle = startAngle + segmentDegrees;
            drawSegment(canvas, cx, cy, radius, startAngle, endAngle, completePaint);
        }
    }

    private void drawSegment(Canvas canvas, int cx, int cy, int radius, float startAngle, float endAngle, Paint paint) {
        radius -= paint.getStrokeWidth() / 2 + segmentLength;

        float startX = cx + (float) (radius * Math.cos(Math.toRadians(startAngle)));
        float startY = cy + (float) (radius * Math.sin(Math.toRadians(startAngle)));
        float endX = cx + (float) ((radius + segmentLength) * Math.cos(Math.toRadians(startAngle)));
        float endY = cy + (float) ((radius + segmentLength) * Math.sin(Math.toRadians(startAngle)));

        canvas.drawLine(startX, startY, endX, endY, paint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate(); // 重新绘制
    }

}