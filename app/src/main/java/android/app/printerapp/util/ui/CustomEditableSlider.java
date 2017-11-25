package android.app.printerapp.util.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.R;
import com.gc.materialdesign.utils.Utils;
import com.gc.materialdesign.views.CustomView;
import com.nineoldandroids.view.ViewHelper;


/**
 * Copy of the Slider class by MaterialDesign
 */

public class CustomEditableSlider extends CustomView {

    // Event when slider change value
    public interface OnValueChangedListener {
        public void onValueChanged(int value);
    }

    int backgroundColor = Color.parseColor("#4CAF50");

    Ball ball;
    NumberIndicator numberIndicator;

    boolean showNumberIndicator = false;
    boolean press = false;

    int value = 0;
    public int shownValue = 0;
    int max = 100;
    int min = 0;

    public static final int DEFAULT_SLIDER_VALUE = 12;

    OnValueChangedListener onValueChangedListener;

    public CustomEditableSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
    }

    // Set atributtes of XML to View
    protected void setAttributes(AttributeSet attrs) {

        setBackgroundResource(R.drawable.background_transparent);

        // Set size of view
        setMinimumHeight(Utils.dpToPx(48, getResources()));
        setMinimumWidth(Utils.dpToPx(80, getResources()));
        ball = new Ball(getContext());
        RelativeLayout.LayoutParams params = new LayoutParams(Utils.dpToPx(20,
                getResources()), Utils.dpToPx(20, getResources()));
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        ball.setLayoutParams(params);
        addView(ball);

        // Set if slider content number indicator
        if (showNumberIndicator) {
            numberIndicator = new NumberIndicator(getContext());
        }

    }

    @Override
    public void invalidate() {
        ball.invalidate();
        super.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!placedBall)
            placeBall();

        if (value == DEFAULT_SLIDER_VALUE) {
            // Crop line to transparent effect
            Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(),
                    canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas temp = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#B0B0B0"));
            paint.setStrokeWidth(Utils.dpToPx(2, getResources()));
            temp.drawLine(getHeight() / 2, getHeight() / 2, getWidth()
                    - getHeight() / 2, getHeight() / 2, paint);
            Paint transparentPaint = new Paint();
            transparentPaint.setColor(getResources().getColor(
                    android.R.color.transparent));
            transparentPaint.setXfermode(new PorterDuffXfermode(
                    PorterDuff.Mode.CLEAR));
            temp.drawCircle(ViewHelper.getX(ball) + ball.getWidth() / 2,
                    ViewHelper.getY(ball) + ball.getHeight() / 2,
                    ball.getWidth() / 2, transparentPaint);

            canvas.drawBitmap(bitmap, 0, 0, new Paint());
        } else {
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#B0B0B0"));
            paint.setStrokeWidth(Utils.dpToPx(2, getResources()));
            canvas.drawLine(getHeight() / 2, getHeight() / 2, getWidth()
                    - getHeight() / 2, getHeight() / 2, paint);
            paint.setColor(backgroundColor);
            float division = (ball.xFin - ball.xIni) / (max - min);
            int value = this.value - min;
            canvas.drawLine(getHeight() / 2, getHeight() / 2, value * division
                    + getHeight() / 2, getHeight() / 2, paint);

        }

        if (press && !showNumberIndicator) {
            Paint paint = new Paint();
            paint.setColor(backgroundColor);
            paint.setAntiAlias(true);
            canvas.drawCircle(ViewHelper.getX(ball) + ball.getWidth() / 2,
                    getHeight() / 2, getHeight() / 3, paint);
        }
        invalidate();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isLastTouch = true;
        if (isEnabled()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN
                    || event.getAction() == MotionEvent.ACTION_MOVE) {
                if (numberIndicator != null
                        && numberIndicator.isShowing() == false)
                    numberIndicator.show();
                if ((event.getX() <= getWidth() && event.getX() >= 0)) {
                    press = true;
                    // calculate value
                    int newValue = 0;
                    float division = (ball.xFin - ball.xIni) / (max - min);
                    if (event.getX() > ball.xFin) {
                        newValue = max;
                    } else if (event.getX() < ball.xIni) {
                        newValue = min;
                    } else {
                        newValue = min + (int) ((event.getX() - ball.xIni) / division);
                    }
                    if (value != newValue) {
                        value = newValue;
                        if (onValueChangedListener != null)
                            onValueChangedListener.onValueChanged(newValue);
                    }
                    // move ball indicator
                    float x = event.getX();
                    x = (x < ball.xIni) ? ball.xIni : x;
                    x = (x > ball.xFin) ? ball.xFin : x;
                    ViewHelper.setX(ball, x);
                    ball.changeBackground();

                    // If slider has number indicator
                    if (numberIndicator != null) {
                        // move number indicator
                        numberIndicator.indicator.x = x;
                        numberIndicator.indicator.finalY = Utils
                                .getRelativeTop(this) - getHeight() / 2;
                        numberIndicator.indicator.finalSize = getHeight() / 2;
                        numberIndicator.numberIndicator.setText("");
                    }

                } else {
                    press = false;
                    isLastTouch = false;
                    if (numberIndicator != null)
                        numberIndicator.dismiss();

                }

            } else if (event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (numberIndicator != null)
                    numberIndicator.dismiss();
                isLastTouch = false;
                press = false;
            }
        }
        return true;
    }

    /**
     * Make a dark color to press effect
     *
     * @return
     */
    protected int makePressColor() {
        int r = (this.backgroundColor >> 16) & 0xFF;
        int g = (this.backgroundColor >> 8) & 0xFF;
        int b = (this.backgroundColor >> 0) & 0xFF;
        r = (r - 30 < 0) ? 0 : r - 30;
        g = (g - 30 < 0) ? 0 : g - 30;
        b = (b - 30 < 0) ? 0 : b - 30;
        return Color.argb(70, r, g, b);
    }

    private void placeBall() {
        ViewHelper.setX(ball, getHeight() / 2 - ball.getWidth() / 2);
        ball.xIni = ViewHelper.getX(ball);
        ball.xFin = getWidth() - getHeight() / 2 - ball.getWidth() / 2;
        ball.xCen = getWidth() / 2 - ball.getWidth() / 2;
        placedBall = true;
    }

    // GETERS & SETTERS

    public OnValueChangedListener getOnValueChangedListener() {
        return onValueChangedListener;
    }

    public void setOnValueChangedListener(
            OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        if (placedBall == false)
            post(new Runnable() {

                @Override
                public void run() {
                    setValue(value);
                }
            });
        else {
            this.value = value;
            float division = (ball.xFin - ball.xIni) / max;
            ViewHelper.setX(ball,
                    value * division + getHeight() / 2 - ball.getWidth() / 2);
            ball.changeBackground();
        }


    }

    public void setShownValue(int value){
        this.shownValue = value;
        //Log.i("Slider", "new value " + shownValue);

    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public boolean isShowNumberIndicator() {
        return showNumberIndicator;
    }

    public void setShowNumberIndicator(boolean showNumberIndicator) {
        this.showNumberIndicator = showNumberIndicator;
        numberIndicator = (showNumberIndicator) ? new NumberIndicator(
                getContext()) : null;
    }

    @Override
    public void setBackgroundColor(int color) {
        backgroundColor = color;
    }

    boolean placedBall = false;

    class Ball extends View {

        float xIni, xFin, xCen;

        public Ball(Context context) {
            super(context);
            setBackgroundResource(R.drawable.background_switch_ball_uncheck);
        }

        public void changeBackground() {
            if (value != DEFAULT_SLIDER_VALUE) { //Modified min value for empty ball
                setBackgroundResource(R.drawable.background_checkbox);
                LayerDrawable layer = (LayerDrawable) getBackground();
                GradientDrawable shape = (GradientDrawable) layer
                        .findDrawableByLayerId(R.id.shape_bacground);
                shape.setColor(backgroundColor);
            } else {
                setBackgroundResource(R.drawable.background_switch_ball_uncheck);
            }
        }

    }

    // Slider Number Indicator

    class NumberIndicator extends Dialog {

        Indicator indicator;
        TextView numberIndicator;

        public NumberIndicator(Context context) {
            super(context, android.R.style.Theme_Translucent);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.number_indicator_spinner);
            setCanceledOnTouchOutside(false);

            RelativeLayout content = (RelativeLayout) this
                    .findViewById(R.id.number_indicator_spinner_content);
            indicator = new Indicator(this.getContext());
            content.addView(indicator);

            numberIndicator = new TextView(getContext());
            numberIndicator.setTextColor(Color.WHITE);
            numberIndicator.setGravity(Gravity.CENTER);
            content.addView(numberIndicator);

            indicator.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT,
                    RelativeLayout.LayoutParams.FILL_PARENT));
        }

        @Override
        public void dismiss() {
            super.dismiss();
            indicator.y = 0;
            indicator.size = 0;
            indicator.animate = true;
        }

        @Override
        public void onBackPressed() {
        }

    }

    class Indicator extends RelativeLayout {

        // Position of number indicator
        float x = 0;
        float y = 0;
        // Size of number indicator
        float size = 0;

        // Final y position after animation
        float finalY = 0;
        // Final size after animation
        float finalSize = 0;

        boolean animate = true;

        boolean numberIndicatorResize = false;

        public Indicator(Context context) {
            super(context);
            setBackgroundColor(getResources().getColor(
                    android.R.color.transparent));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (numberIndicatorResize == false) {
                RelativeLayout.LayoutParams params = (LayoutParams) numberIndicator.numberIndicator
                        .getLayoutParams();
                params.height = (int) finalSize * 2;
                params.width = (int) finalSize * 2;
                numberIndicator.numberIndicator.setLayoutParams(params);
            }

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(backgroundColor);
            if (animate) {
                if (y == 0)
                    y = finalY + finalSize * 2;
                y -= Utils.dpToPx(6, getResources());
                size += Utils.dpToPx(2, getResources());
            }
            canvas.drawCircle(
                    ViewHelper.getX(ball)
                            + Utils.getRelativeLeft((View) ball.getParent())
                            + ball.getWidth() / 2, y, size, paint);
            if (animate && size >= finalSize)
                animate = false;
            if (animate == false) {
                ViewHelper
                        .setX(numberIndicator.numberIndicator,
                                (ViewHelper.getX(ball)
                                        + Utils.getRelativeLeft((View) ball
                                        .getParent()) + ball.getWidth() / 2)
                                        - size);
                ViewHelper.setY(numberIndicator.numberIndicator, y - size);
                numberIndicator.numberIndicator.setText(shownValue + "º"); //Modified shown value
            }

            invalidate();
        }

    }
}