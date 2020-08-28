package com.just.linedemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.just.linedemo.DisplayUtils;
import com.just.linedemo.bean.Entry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <pre>
 * 作者 : hyf
 * 公司: 广州佳时达软件股份有限公司
 * 时间 : 2020-08-21
 * </pre>
 */
public class LinePitView extends View {
    private Paint mLinePaint;
    private Paint mAxisPaint;
    private Paint mAxisYPaint;


    private int startY;
    private int startX;

    private int width;
    private int height;

    private List<Entry> entries = new LinkedList<>();

    private List<PointF> points = new ArrayList<>();

    private float proportionWidth;
    private float proportionHeight;

    private Paint mPoint;

    int labelCount = 6;
    float range;  //总的数标大小

    double interval;//  宽度间隙
    public float[] mEntries = new float[]{};

    float minValue;
    float maxValue;

//    private

    public LinePitView(Context context) {
        this(context, null);
    }

    public LinePitView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LinePitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        entries.add(new Entry(-3, "08:00"));
        entries.add(new Entry(-18, "08:30"));
        entries.add(new Entry(0, "09:00"));
        entries.add(new Entry(10, "09:30"));
        entries.add(new Entry(20, "09:30"));
        entries.add(new Entry(30, "09:30"));
        entries.add(new Entry(40, "09:30"));
        entries.add(new Entry(150, "09:30"));


        mLinePaint = new Paint();
        mLinePaint.setTextSize(DisplayUtils.sp2px(getContext(), 16));
        mLinePaint.setStrokeWidth(DisplayUtils.dip2px(getContext(), 1));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.parseColor("#F81D22"));


        mAxisPaint = new Paint();
        mAxisPaint.setStrokeWidth(DisplayUtils.dip2px(getContext(), 1));
        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setColor(Color.parseColor("#2c2c2c"));


        mAxisYPaint = new Paint();
        mAxisYPaint.setTextSize(DisplayUtils.sp2px(getContext(), 12));
        mAxisYPaint.setAntiAlias(true);
        mAxisYPaint.setColor(Color.parseColor("#F81D22"));


        mPoint = new Paint();
        mPoint.setTextSize(DisplayUtils.sp2px(getContext(), 12));
        mPoint.setAntiAlias(true);
        mPoint.setStyle(Paint.Style.FILL);
        mPoint.setColor(Color.parseColor("#F81D22"));

        startY = DisplayUtils.dip2px(getContext(), 20);
        startX = DisplayUtils.dip2px(getContext(), 40);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth() - getPaddingRight() - getPaddingLeft();
        //x轴的可用宽度
        width = width - startX - DisplayUtils.dip2px(getContext(), 10);
        height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        //Y轴的可用高度
        height = height - startY - DisplayUtils.dip2px(getContext(), 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (entries == null || entries.size() == 0) {
            return;
        }
        initData();
        drawPoint(canvas);
        drawAxisY(canvas);
        drawAxisX(canvas);

    }

    private void drawPoint(Canvas canvas) {
        for (int i = 0; i < points.size(); i++) {
            if (i == 0) {
                continue;
            }
            canvas.drawCircle(startX + points.get(i).x, startY + points.get(i).y, 5, mPoint);
        }


    }

    private void initData() {
        points.clear();
        float max = getMaxXAxis();
        float min = getMinXAxis();

        float ranges = Math.abs(max - min);

        minValue = min - (ranges / 10f);
        maxValue = max + (ranges / 10f);
        computeAxisValues(minValue, maxValue);

        proportionWidth = width / range;
        proportionHeight = height / (entries.size());

        //第一个位置不要
        points.add(new PointF(minValue, 0));
        for (int i = 0; i < entries.size(); i++) {
            float currentPointX = (entries.get(i).getxValue() - minValue) * proportionWidth;
            float currentPointY = (i + 1) * proportionHeight;
            points.add(new PointF(currentPointX, currentPointY));
        }
    }

    private void computeAxisValues(float minXAxis, float maxXAxis) {
        range = Math.abs(maxXAxis - minXAxis);
        double rawInterval = range / labelCount;
        interval = roundToNextSignificant(rawInterval);

        double intervalMagnitude = roundToNextSignificant(Math.pow(10, (int) Math.log10(interval)));
        int intervalSigDigit = (int) (interval / intervalMagnitude);
        if (intervalSigDigit > 5) {
            interval = Math.floor(10 * intervalMagnitude);
        }

        double first = interval == 0.0 ? 0.0 : Math.ceil(minXAxis / interval) * interval;
        double last = interval == 0.0 ? 0.0 : nextUp(Math.floor(maxXAxis / interval) * interval);
        double f;
        int n = 0;

        if (interval != 0.0) {
            for (f = first; f <= last; f += interval) {
                ++n;
            }
        }
        mEntries = new float[n];
        int i;
        for (f = first, i = 0; i < n; f += interval, ++i) {

            if (f == 0.0) // Fix for negative zero case (Where value == -0.0, and 0.0 == -0.0)
                f = 0.0;

            mEntries[i] = (float) f;
        }

    }


    private float getMaxXAxis() {
        float max = 0;
        for (int i = 0; i < entries.size(); i++) {
            if (i == 0) {
                max = entries.get(i).getxValue();
            } else {
                max = Math.max(max, entries.get(i).getxValue());
            }
        }
        return max;


    }

    public static double nextUp(double d) {
        if (d == Double.POSITIVE_INFINITY)
            return d;
        else {
            d += 0.0d;
            return Double.longBitsToDouble(Double.doubleToRawLongBits(d) +
                    ((d >= 0.0d) ? +1L : -1L));
        }
    }

    private float getMinXAxis() {
        float min = 0;
        for (int i = 0; i < entries.size(); i++) {
            if (i == 0) {
                min = entries.get(i).getxValue();
            } else {
                min = Math.min(min, entries.get(i).getxValue());
            }
        }
        return min;


    }


    private void drawAxisY(Canvas canvas) {
        for (int i = 0; i < entries.size(); i++) {
            Rect bounds = new Rect();
            mAxisYPaint.getTextBounds(entries.get(i).getyValue(), 0, entries.get(i).getyValue().length(), bounds);
            canvas.drawLine(startX, startY + ((i + 1) * proportionHeight), startX + 10,
                    startY + ((i + 1) * proportionHeight), mAxisPaint);
            canvas.drawText(entries.get(i).getyValue(), (startX - DisplayUtils.dip2px(getContext(), 5) - bounds.width()),
                    startY + ((i + 1) * proportionHeight) + bounds.height() / 2, mAxisYPaint
            );

        }
//        int yheight = height / ySpace;
//
//        for (int i = 1; i <= entries.size(); i++) {
//            canvas.drawLine(startX, startY + (i * yheight), startX + 10,
//                    startY + (i * yheight), mAxisPaint);
//            canvas.drawText(entries.get(i - 1).getyValue(), (startX - DisplayUtils.dip2px(getContext(), 5) - bounds.width()),
//                    startY + (i * yheight) + bounds.height() / 2, mAxisYPaint
//            );
//        }
        canvas.drawLine(startX, startY, startX, getHeight(), mAxisPaint);
    }

    private void drawAxisX(Canvas canvas) {
        canvas.drawLine(startX, startY, getWidth(), startY, mAxisPaint);

        for (int i = 0; i < mEntries.length; i++) {
            float x = startX + (mEntries[i] - minValue) * proportionWidth;
//            Log.e("输出x轴位置信息", "drawAxisX: ", );
            String xValue = String.valueOf(mEntries[i]);
            Rect bounds = new Rect();
            mAxisYPaint.getTextBounds(xValue, 0, xValue.length(), bounds);
            canvas.drawText(xValue, (x - bounds.width() / 2),
                    startY - DisplayUtils.dip2px(getContext(), 5), mAxisYPaint);
            canvas.drawLine(x, startY, x, getHeight(), mAxisPaint);
        }


    }


    public void setData(List<Entry> entries) {
        this.entries = entries;


        invalidate();
    }


    public static float roundToNextSignificant(double number) {
        if (Double.isInfinite(number) ||
                Double.isNaN(number) ||
                number == 0.0)
            return 0;

        final float d = (float) Math.ceil((float) Math.log10(number < 0 ? -number : number));
        final int pw = 1 - (int) d;
        final float magnitude = (float) Math.pow(10, pw);
        final long shifted = Math.round(number * magnitude);
        return shifted / magnitude;
    }

}
