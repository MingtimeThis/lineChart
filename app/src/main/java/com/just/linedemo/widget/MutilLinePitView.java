package com.just.linedemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.just.linedemo.DisplayUtils;
import com.just.linedemo.bean.DataLineSet;
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
public class MutilLinePitView extends View {
    private Paint mLinePaint;
    private Paint mAxisPaint;
    private Paint mAxisYPaint;
    private TextPaint mEntryPaint;


    private int startY;
    private int startX;

    private int width;
    private int height;


    private List<DataLineSet> entrySet = new LinkedList<>();
    private DataLineSet entryItem = new DataLineSet();

    private List<List<PointF>> allPoints = new ArrayList<>();

    private float proportionWidth;
    private float proportionHeight;

    private Paint mPoint;

    int labelCount = 6;
    float range;  //总的数标大小

    double interval;//  宽度间隙
    public float[] mEntries = new float[]{};

    float minValue;
    float maxValue;

    int yCount = 0;

//    private

    public MutilLinePitView(Context context) {
        this(context, null);
    }

    public MutilLinePitView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MutilLinePitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(DisplayUtils.dip2px(getContext(), 1));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.parseColor("#F81D22"));


        mEntryPaint = new TextPaint();
        mEntryPaint.setTextSize(DisplayUtils.sp2px(getContext(), 16));
        mEntryPaint.setAntiAlias(true);
        mEntryPaint.setColor(Color.parseColor("#F81D22"));


        mAxisPaint = new Paint();
        mAxisPaint.setStrokeWidth(1);
        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setColor(Color.parseColor("#666666"));


        mAxisYPaint = new Paint();
        mAxisYPaint.setTextSize(DisplayUtils.sp2px(getContext(), 12));
        mAxisYPaint.setAntiAlias(true);
        mAxisYPaint.setColor(Color.parseColor("#CCCCCC"));


        mPoint = new Paint();
        mPoint.setTextSize(DisplayUtils.sp2px(getContext(), 12));
        mPoint.setAntiAlias(true);
        mPoint.setStyle(Paint.Style.FILL);
        mPoint.setColor(Color.parseColor("#4AE122"));

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


//        if (entries == null || entries.size() == 0) {
//            return;
//        }

        if (entrySet == null || entrySet.size() == 0) {
//            canvas.drawLine(startX, startY, getWidth(), startY, mAxisPaint);
//            canvas.drawLine(startX, startY, startX, getHeight(), mAxisPaint);
            String entry = "暂无数据";
            Rect bounds = new Rect();
            mEntryPaint.getTextBounds(entry,
                    0, entry.length(), bounds);
            canvas.drawText(entry, (getWidth() - bounds.width()) / 2, getHeight() / 2, mEntryPaint);
            return;
        }
        initData();
        drawPoint(canvas);
        drawAxisY(canvas);
        drawAxisX(canvas);
    }

    private void drawPoint(Canvas canvas) {

        for (int i = 0; i < allPoints.size(); i++) {
            mLinePaint.setColor(entrySet.get(i).getColor());
            for (int j = 0; j < allPoints.get(i).size(); j++) {
                if (j == 0) {
                    continue;
                }

                PointF pointF = allPoints.get(i).get(j);
                if ((j + 1) != allPoints.get(i).size()) {
                    PointF pointF2 = allPoints.get(i).get(j + 1);
                    canvas.drawLine(startX + pointF.x, startY + pointF.y, startX + pointF2.x,
                            startY + pointF2.y, mLinePaint);
                }

//                mPoint.setColor(entrySet.get(i).getColor());
                canvas.drawCircle(startX + pointF.x, startY + pointF.y, DisplayUtils.dip2px(getContext(), 5), mPoint);
            }
        }
    }


    public void setMultiData(List<DataLineSet> entrySet) {
        this.entrySet = entrySet;
        invalidate();
    }

    private void initData() {
        allPoints.clear();
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        yCount = 0;
        for (int i = 0; i < entrySet.size(); i++) {
            if (yCount < entrySet.get(i).getEntryList().size()) {
                yCount = entrySet.get(i).getEntryList().size();
                entryItem = entrySet.get(i);
            }

            max = Math.max(max, getMaxXAxis(entrySet.get(i).getEntryList()));
            min = Math.min(min, getMinXAxis(entrySet.get(i).getEntryList()));
        }
        float ranges = Math.abs(max - min);

        minValue = min - (ranges / 10f);
        maxValue = max + (ranges / 10f);
        computeAxisValues(minValue, maxValue);

        proportionWidth = width / range;
        proportionHeight = height / (yCount);

        for (int i = 0; i < entrySet.size(); i++) {
            List<PointF> points = new ArrayList<>();
            points.add(new PointF(minValue, 0));
            for (int j = 0; j < entrySet.get(i).getEntryList().size(); j++) {
                Entry entry = entrySet.get(i).getEntryList().get(j);
                float currentPointX = (entry.getxValue() - minValue) * proportionWidth;
                float currentPointY = (j + 1) * proportionHeight;
                points.add(new PointF(currentPointX, currentPointY));
            }

            allPoints.add(points);
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


    private float getMaxXAxis(List<Entry> entries) {
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

    private float getMinXAxis(List<Entry> entries) {
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
        for (int i = 0; i < yCount; i++) {
            Rect bounds = new Rect();
            mAxisYPaint.getTextBounds(entryItem.getEntryList().get(i).getyValue(),
                    0, entryItem.getEntryList().get(i).getyValue().length(), bounds);
            canvas.drawLine(startX - DisplayUtils.dip2px(getContext(), 6), startY + ((i + 1) * proportionHeight), startX,
                    startY + ((i + 1) * proportionHeight), mAxisPaint);
            canvas.drawText(entryItem.getEntryList().get(i).getyValue(),
                    (startX - DisplayUtils.dip2px(getContext(), 8) - bounds.width()),
                    startY + ((i + 1) * proportionHeight) + bounds.height() / 2, mAxisYPaint
            );

        }
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
