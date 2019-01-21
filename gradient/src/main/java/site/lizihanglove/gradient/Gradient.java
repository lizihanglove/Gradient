package site.lizihanglove.gradient;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.midi.MidiDevice;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.math.BigInteger;


public class Gradient extends View {

    private static final String TAG = Gradient.class.getSimpleName();

    private float mRingWidth = 20;
    private int mInnerColor = 0x4bc2c5;
    private int mOuterColor = 0xFFFFFF;
    private int mTextColor = 0x2470a0;
    private String mText = "";

    private Paint mRingPaint;
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private float mTextSize = 20;


    public Gradient(Context context) {
        this(context, null);
    }

    public Gradient(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Gradient(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Gradient);
        mRingWidth = array.getDimension(R.styleable.Gradient_ringWidth, mRingWidth);
        mTextSize = array.getDimension(R.styleable.Gradient_textSize, mTextSize);
        mInnerColor = array.getColor(R.styleable.Gradient_innerColor, mInnerColor);
        mOuterColor = array.getColor(R.styleable.Gradient_outerColor, mOuterColor);
        mTextColor = array.getColor(R.styleable.Gradient_textColor, mTextColor);
        mText = array.getString(R.styleable.Gradient_text);
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setStrokeWidth(mRingWidth);
        mRingPaint.setStyle(Paint.Style.STROKE);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mInnerColor);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int finalSize = Math.min(width, height);
        setMeasuredDimension(finalSize, finalSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();

        //画圆环
        RectF oval = new RectF(mRingWidth / 2, mRingWidth / 2, width - mRingWidth / 2, height - mRingWidth / 2);
        String outer = toHexEncoding(mOuterColor);
        String inner = toHexEncoding(mInnerColor);
        String middle = getMiddleColor(inner, outer);
        int middleColor = Color.parseColor(middle);
        mRingPaint.setColor(middleColor);
        canvas.drawArc(oval, 0, 360, false, mRingPaint);
        oval.set(mRingWidth / 2 * 3, mRingWidth / 2 * 3, width - mRingWidth / 2 * 3, height - mRingWidth / 2 * 3);
        middle = getMiddleColor(inner, middle);
        middleColor = Color.parseColor(middle);
        mRingPaint.setColor(middleColor);
        canvas.drawArc(oval, 0, 360, false, mRingPaint);

        //画圆
        canvas.drawCircle(width / 2, height / 2, (width - mRingWidth * 4) / 2, mCirclePaint);

        //画文字
        canvas.drawText(mText, width / 2, height / 2, mTextPaint);
    }

    /**
     * 将颜色值转成十六进制编码
     *
     * @param color
     * @return
     */
    public static String toHexEncoding(int color) {
        String R, G, B;
        StringBuffer sb = new StringBuffer();
        int red = Color.red(color);
        R = Integer.toHexString(red);
        int green = Color.green(color);
        G = Integer.toHexString(green);
        int blue = Color.blue(color);
        B = Integer.toHexString(blue);
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;
        sb.append("#");
        sb.append(R);
        sb.append(G);
        sb.append(B);
        return sb.toString();
    }

    public static String getMiddleColor(String color1, String color2) {
        if (color1.contains("#") && color2.contains("#") && color1.length() == color2.length() && color2.length() == 7) {
            String tempColor1 = color1.replace("#", "");
            String tempColor2 = color2.replace("#", "");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("#");
            for (int i = 0; i < tempColor1.length(); i++) {
                String tempResult = (changeHex2Int(String.valueOf(tempColor1.charAt(i)))
                        + changeHex2Int(String.valueOf(tempColor2.charAt(i)))) / 2 + "";
                stringBuffer.append(changeInt2Hex(tempResult));
            }
            return stringBuffer.toString();
        }
        return "";
    }

    /**
     * 十六进制转十进制
     *
     * @param temp
     * @return
     */
    private static int changeHex2Int(String temp) {
        BigInteger srch = new BigInteger(temp, 16);
        return Integer.valueOf(srch.toString());
    }

    /**
     * 十进制转十六进制
     *
     * @param temp
     * @return
     */
    private static String changeInt2Hex(String temp) {
        BigInteger srch = new BigInteger(temp, 10);
        return Integer.toHexString(Integer.parseInt(srch.toString()));
    }

}
