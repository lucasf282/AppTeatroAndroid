package br.com.appteatro.appteatro.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import br.com.appteatro.appteatro.R;

/**
 * TODO: document your custom view class.
 */
public class TheaterLoadView extends View {

    private int fillColor = Color.GRAY;
    private Paint mPaint;
    private boolean isIncreasing = true;
    private int increasing = 2;
    private int minRadius = 100;
    private int radius = minRadius;
    private int fps = 60;
    private Context context;


    public TheaterLoadView(Context context) {
        super(context);
        this.context = context;
        init(null, 0);
    }

    public TheaterLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public TheaterLoadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = this.context.getTheme().obtainStyledAttributes(attrs, R.styleable.TheaterLoadView, 0, 0);

        try {
            fillColor = a.getColor(R.styleable.TheaterLoadView_fillColor, fillColor);
        }finally {
            a.recycle();
        }

        mPaint = new Paint();
        mPaint.setColor(fillColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        increasing = Math.round(increasing * getResources().getDisplayMetrics().density);

        startAnimation();
    }

    android.os.Handler mHandler = new android.os.Handler();
    Runnable mTick = new Runnable() {
        @Override
        public void run() {
            invalidate();
            mHandler.postDelayed(this, 1000 / fps);
        }
    };


    public void startAnimation() {
        mHandler.removeCallbacks(mTick);
        mHandler.post(mTick);
    }

    public void stopAnimation() {
        mHandler.removeCallbacks(mTick);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final Canvas myCanvas = canvas;
        final int x = getWidth()/2;
        final int y = getHeight()/2;

        int maxRadius = Math.min(x, y);

        canvas.drawCircle(x, y, radius, mPaint);

        if (isIncreasing){
            if (radius >= maxRadius){
                isIncreasing = false;
            }else {
                radius += increasing;
                canvas.drawCircle(x, y, radius, mPaint);
            }
        }else{
            if (radius <= minRadius){
                isIncreasing = true;
            }else {
                radius -= increasing;
                canvas.drawCircle(x, y, radius, mPaint);
            }
        }

    }
}
