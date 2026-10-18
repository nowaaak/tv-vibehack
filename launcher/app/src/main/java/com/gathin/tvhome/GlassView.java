package com.gathin.tvhome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * A translucent glass slab: soft sheen fill, bright top specular, crisp rim and a faint outer glow.
 * One instance floats over the grid and is moved with spring animations between tiles.
 */
public class GlassView extends View {

    static final float INSET_DP = 12f;
    private static final float RING_GAP_DP = 3f;

    private final Paint gapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint rim = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();
    private final float density;
    private float radius;

    public GlassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = getResources().getDisplayMetrics().density;
        radius = 20f * density;
        rim.setStyle(Paint.Style.STROKE);
        rim.setStrokeWidth(2f * density);
        rim.setColor(0xF2FFFFFF);
        gapPaint.setStyle(Paint.Style.STROKE);
        gapPaint.setStrokeWidth(RING_GAP_DP * density);
        gapPaint.setColor(0xB3000000);
    }

    void setRadius(float px) {
        radius = px;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float inset = INSET_DP * density;   // room for the glow
        rect.set(inset, inset, getWidth() - inset, getHeight() - inset);

        float g = gapPaint.getStrokeWidth() / 2f;
        canvas.drawRoundRect(rect.left - g, rect.top - g, rect.right + g, rect.bottom + g, radius + g, radius + g, gapPaint);

        float ring = gapPaint.getStrokeWidth() + rim.getStrokeWidth() / 2f;
        float r = radius + ring;
        canvas.drawRoundRect(rect.left - ring, rect.top - ring, rect.right + ring, rect.bottom + ring, r, r, rim);
    }
}
