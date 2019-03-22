package com.example.circleprogressviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

public class CircleProgressView extends ProgressBar {
    private  int CircleProgressRadius = 50;//圆的半径
    private  int CircleProgressBackStroken = 5;//背景宽度
    private  int CircleProgressForeStroken = 5;//前景宽度
    private  int CircleProgressBackColor = Color.GRAY;//背景颜色
    private  int CircleProgressForeColor = Color.RED;//前景颜色
    private  int CricleProgressStart = -90;//起点位置
    private  int CricleProgressEnd= 270;//终点位置
    private  boolean CricleProgressText1Show = false;//百分比是否显示
    private  boolean CricleProgressText2Show = false;//文字说明是否显示
    private  int CircleProgresText1Size = 18;//百分比大小
    private  int CircleProgresText2Size = 16;//文字说明大小
    private  int CircleProgresText1Color = Color.RED;//百分比颜色
    private  int CircleProgresText2Color = Color.GRAY;//文字说明颜色
    private String CricleProgressContent = "";//文字说明



    //默认值
    private static final int DefaultCircleProgressRadius = 50;//圆的半径
    private static final int DefaultCircleProgressBackStroken = 5;//背景宽度
    private static final int DefaultCircleProgressForeStroken = 5;//前景宽度
    private static final int DefaultCircleProgressBackColor = Color.GRAY;//背景颜色
    private static final int DefaultCircleProgressForeColor = Color.RED;//前景颜色
    private static final int DefaultCricleProgressStart = 0;
    private static final int DefaultCricleProgressEnd= 360;
    private static final boolean DefaultCricleProgressText1Show = false;
    private static final boolean DefaultCricleProgressText2Show = false;
    private static final int DefaultCircleProgresText1Size = 18;
    private static final int DefaultCircleProgresText2Size = 16;
    private static final int DefaultCircleProgresText1Color = Color.RED;
    private static final int DefaultCircleProgresText2Color = Color.GRAY;


    protected Paint mPaint = new Paint();
    private RectF mRectFOval;

    public CircleProgressView(Context context) {
        super(context);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getStyleabletAttr(attrs);

    }



    /**
     * 获取自定义属性
     */
    protected void getStyleabletAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgressStyle);
        CircleProgressRadius = (int) typedArray.getDimension(R.styleable.CircleProgressStyle_CircleProgressRadius, dp2px(getContext(), DefaultCircleProgressRadius));
        CircleProgressBackStroken = (int) typedArray.getDimension(R.styleable.CircleProgressStyle_CircleProgressBackStroken, dp2px(getContext(), DefaultCircleProgressBackStroken));
        CircleProgressForeStroken = (int) typedArray.getDimension(R.styleable.CircleProgressStyle_CircleProgressForeStroken, dp2px(getContext(), DefaultCircleProgressForeStroken));
        CircleProgressBackColor = typedArray.getColor(R.styleable.CircleProgressStyle_CircleProgressBackColor, DefaultCircleProgressBackColor);
        CircleProgressForeColor = typedArray.getColor(R.styleable.CircleProgressStyle_CircleProgressForeColor, DefaultCircleProgressForeColor);
        CricleProgressStart = typedArray.getInteger(R.styleable.CircleProgressStyle_CricleProgressStart, DefaultCricleProgressStart);
        CricleProgressEnd = typedArray.getInteger(R.styleable.CircleProgressStyle_CricleProgressEnd, DefaultCricleProgressEnd);
        CricleProgressText1Show = typedArray.getBoolean(R.styleable.CircleProgressStyle_CricleProgressText1Show, DefaultCricleProgressText1Show);
        CricleProgressText2Show = typedArray.getBoolean(R.styleable.CircleProgressStyle_CricleProgressText2Show, DefaultCricleProgressText2Show);
        CircleProgresText1Size = (int) typedArray.getDimension(R.styleable.CircleProgressStyle_CircleProgressText1Size, dp2px(getContext(), DefaultCircleProgresText1Size));
        CircleProgresText2Size = (int) typedArray.getDimension(R.styleable.CircleProgressStyle_CircleProgressText2Size, dp2px(getContext(), DefaultCircleProgresText2Size));
        CircleProgresText1Color = typedArray.getColor(R.styleable.CircleProgressStyle_CircleProgressText1Color, DefaultCircleProgresText1Color);
        CircleProgresText2Color = typedArray.getColor(R.styleable.CircleProgressStyle_CircleProgressText2Color, DefaultCircleProgresText2Color);
        CricleProgressContent= typedArray.getString(R.styleable.CircleProgressStyle_CricleProgressContent);
        typedArray.recycle();//记得加这句
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);//计算宽高
        int height = measureHeight(heightMeasureSpec);
        width = height = Math.min(width,height);//取宽高的最小值
        if (CircleProgressRadius>width/2){
            CircleProgressRadius = width/2;
        }
        CircleProgressRadius = CircleProgressRadius - CircleProgressForeStroken/2;//还要减去画笔的宽度
        setMeasuredDimension(width,height);//设置宽高
    }
    protected int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = Math.min(specSize,CircleProgressRadius*2);
        } else {
            result = dp2px(getContext(),2*CircleProgressRadius);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    protected int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            //此处高度为走完的进度高度和未走完的机大以及文字的高度的最大值
            result = 2*CircleProgressRadius + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //super.onDraw(canvas);//不需要父类里面的代码


        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        //移动画布,减少后续画图换算
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;
        canvas.save();//save、restore 图层的保存和回滚相关的方法 详见 http://blog.csdn.net/tianjian4592/article/details/45234419
        canvas.translate(centerX,centerY);//移动图层到垂直居中位置
        //draw unreach circle
        mPaint.setColor(CircleProgressBackColor);
        mPaint.setStrokeWidth(CircleProgressBackStroken);
        canvas.drawCircle(0,0,CircleProgressRadius,mPaint);
        //draw reach circle
        mPaint.setColor(CircleProgressForeColor);
        mPaint.setStrokeWidth(CircleProgressForeStroken);
        int progress = (int) ((getProgress()*1.0f/getMax())*360);//圆弧度数
        int mTotalProgress = Math.abs(CricleProgressStart) + Math.abs(CricleProgressEnd);
        if (progress<=mTotalProgress){
            if (mRectFOval==null){//只创建一次,减少内存的创建与回收
                mRectFOval = new RectF(-CircleProgressRadius,-CircleProgressRadius,CircleProgressRadius,CircleProgressRadius);//float left, float top, float right, float bottom
            }
            canvas.drawArc(mRectFOval,CricleProgressStart,progress,false,mPaint);////false绘制圆弧，不含圆心   @NonNull RectF oval, float startAngle, float sweepAngle, boolean useCenter, @NonNull Paint paint
        } else{
            if (mRectFOval==null){//只创建一次,减少内存的创建与回收
                mRectFOval = new RectF(-CircleProgressRadius,-CircleProgressRadius,CircleProgressRadius,CircleProgressRadius);//float left, float top, float right, float bottom
            }
            canvas.drawArc(mRectFOval,CricleProgressStart,mTotalProgress,false,mPaint);////false绘制圆弧，不含圆心   @NonNull RectF oval, float startAngle, float sweepAngle, boolean useCenter, @NonNull Paint paint
        }
        //draw Text
        if (CricleProgressText1Show){
            mPaint.setColor(CircleProgresText1Color);
            mPaint.setTextSize(CircleProgresText1Size);
            mPaint.setStyle(Paint.Style.FILL);
            String currentProgress = getProgress()+"%";
            Rect mRectF1 = new Rect();
            mPaint.getTextBounds(currentProgress,0,currentProgress.length(),mRectF1);
            canvas.drawText(currentProgress,0 - mRectF1.width()/2,0+mRectF1.height()/2-20,mPaint);
        }
        if (CricleProgressText2Show){
            mPaint.setColor(CircleProgresText2Color);
            mPaint.setTextSize(CircleProgresText2Size);
            mPaint.setStyle(Paint.Style.FILL);
            String currentProgress = CricleProgressContent;
            Rect mRectF = new Rect();
            mPaint.getTextBounds(currentProgress,0,currentProgress.length(),mRectF);
            canvas.drawText(currentProgress,0 - mRectF.width()/2,0+mRectF.height()/2+20,mPaint);
        }
        canvas.restore();
    }

}
