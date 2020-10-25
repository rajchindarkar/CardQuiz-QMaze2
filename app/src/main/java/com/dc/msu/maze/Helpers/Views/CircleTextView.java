package com.dc.msu.maze.Helpers.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.dc.msu.maze.R;

public class CircleTextView extends androidx.appcompat.widget.AppCompatTextView {

    public CircleTextView(@NonNull Context context) {
        super(context);
    }

    public CircleTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        final int diameter, radius, h, w;

        Paint circlePaint = new Paint();
        circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        //get Height and Width
        h = this.getHeight();
        w = this.getWidth();

        diameter = (Math.max(h, w));
        radius = diameter / 2;

        //setting Height and width
        this.setHeight(diameter);
        this.setWidth(diameter);

        canvas.drawCircle(diameter / 2, diameter / 2, radius, circlePaint);
        super.draw(canvas);
    }
}
