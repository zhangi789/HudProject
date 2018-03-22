package com.shdnxc.cn.activity.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import com.shdnxc.cn.activity.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;


import static com.shdnxc.cn.activity.view.BubbleSeekBar.TextPosition.BELOW_SECTION_MARK;
import static com.shdnxc.cn.activity.view.BubbleSeekBar.TextPosition.BOTTOM_SIDES;
import static com.shdnxc.cn.activity.view.BubbleSeekBar.TextPosition.SIDES;

/**
 * 气泡形式可视化的自定义SeekBar
 * <p>
 * Created by woxingxiao on 2016-10-27.
 */
public class BubbleSeekBar extends View {

    @IntDef({SIDES, BOTTOM_SIDES, BELOW_SECTION_MARK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextPosition {
        int SIDES = 0, BOTTOM_SIDES = 1, BELOW_SECTION_MARK = 2;
    }
    private int mMin; // 首值（起始值）
    private int mMax; // 尾值（结束值）
    private float mProgress; // 实时值
    private int mTrackSize; // 下层track的高度
    private int mSecondTrackSize; // 上层track的高度
    private int mThumbRadius; // thumb的半径
    private int mThumbRadiusOnDragging; // 当thumb被拖拽时的半径
    private int mSectionCount; // min到max均分的份数
    private int mThumbColor; // thumb的颜色
    private int mTrackColor; // 下层track的颜色
    private int mSecondTrackColor; // 上层track的颜色
    private boolean isShowSectionText; // 是否显示section值文字
    private int mSectionTextSize; // section值文字大小
    private int mSectionTextColor; // section值文字颜色
    @TextPosition
    private int mSectionTextPosition; // section值文字位置
    private int mSectionTextInterval; // section值文字每间隔多少section显示
    private boolean isShowThumbText; // 是否显示实时值文字
    private int mThumbTextSize; // 实时值文字大小
    private int mThumbTextColor; // 实时值文字颜色
    private int mBubbleColor;// 气泡颜色
    private int mBubbleTextSize; // 气泡文字大小
    private int mBubbleTextColor; // 气泡文字颜色
    private boolean isShowSectionMark; // 是否显示份数
    private boolean isAutoAdjustSectionMark; // 是否自动滑到最近的整份数，以showSectionMark为前提
    private boolean isShowProgressInFloat; // 是否显示小数形式progress，所有小数均保留1位
    private boolean isTouchToSeek; // 是否点击快速seek
    private boolean isSeekBySection; // 是否以section为单位seek，仅适用于整型progress，可能progress非连续

    private int mDelta; // max - min
    private int mSectionValue; // (mDelta / mSectionCount)
    private float mThumbCenterX; // thumb的中心X坐标
    private float mTrackLength; // track的长度
    private float mSectionOffset; // 一个section的长度
    private boolean isThumbOnDragging; // thumb是否在被拖动
    private int mTextSpace; // 文字与其他的间距
    private OnProgressChangedListener mProgressListener; // progress变化监听

    private float mLeft; // 便于理解，假设显示SectionMark，该值为首个SectionMark圆心距自己左边的距离
    private float mRight; // 同上假设，该值为最后一个SectionMark圆心距自己左边的距离
    private Paint mPaint;
    private Rect mRectText;
    private WindowManager mWindowManager;

    private BubbleView mBubbleView; // 自定义气泡View
    private int mBubbleRadius; // 气泡半径
    private float mBubbleCenterRawSolidX; // 气泡在最左边的固定RawX
    private float mBubbleCenterRawSolidY; // 气泡的固定RawY
    private float mBubbleCenterRawX; // 气泡的实时RawX
    private WindowManager.LayoutParams mLayoutParams;
    private int[] mPoint = new int[2];
    private long mAnimDuration = 200;
    private boolean isTouchToSeekAnimEnd = true;
    private int mPreSecValue; // previous SectionValue

    public BubbleSeekBar(Context context) {
        this(context, null);
    }

    public BubbleSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BubbleSeekBar, defStyleAttr, 0);
        mMin = a.getInteger(R.styleable.BubbleSeekBar_bsb_min, 0);
        mMax = a.getInteger(R.styleable.BubbleSeekBar_bsb_max, 100);
        mProgress = a.getInteger(R.styleable.BubbleSeekBar_bsb_progress, mMin);
        mTrackSize = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_track_size, dp2px(2));
        mSecondTrackSize = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_second_track_size,
                mTrackSize + dp2px(2));
        mThumbRadius = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_thumb_radius,
                mSecondTrackSize + dp2px(2));
        mThumbRadiusOnDragging = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_thumb_radius,
                mSecondTrackSize * 2);
        mSectionCount = a.getInteger(R.styleable.BubbleSeekBar_bsb_section_count, 10);
        mTrackColor = a.getColor(R.styleable.BubbleSeekBar_bsb_track_color,
                ContextCompat.getColor(context, R.color.colorPrimary));
        mSecondTrackColor = a.getColor(R.styleable.BubbleSeekBar_bsb_second_track_color,
                ContextCompat.getColor(context, R.color.colorAccent));
        mThumbColor = a.getColor(R.styleable.BubbleSeekBar_bsb_thumb_color, mSecondTrackColor);
        isShowSectionText = a.getBoolean(R.styleable.BubbleSeekBar_bsb_show_section_text, false);
        mSectionTextSize = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_section_text_size, sp2px(14));
        mSectionTextColor = a.getColor(R.styleable.BubbleSeekBar_bsb_section_text_color, mTrackColor);
        isSeekBySection = a.getBoolean(R.styleable.BubbleSeekBar_bsb_seek_by_section, false);
        int pos = a.getInteger(R.styleable.BubbleSeekBar_bsb_section_text_position, 0);
        if (pos == 0) {
            mSectionTextPosition = SIDES;
        } else if (pos == 1) {
            mSectionTextPosition = TextPosition.BOTTOM_SIDES;
        } else if (pos == 2) {
            mSectionTextPosition = TextPosition.BELOW_SECTION_MARK;
        }
        mSectionTextInterval = a.getInteger(R.styleable.BubbleSeekBar_bsb_section_text_interval, 1);
        isShowThumbText = a.getBoolean(R.styleable.BubbleSeekBar_bsb_show_thumb_text, false);
        mThumbTextSize = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_thumb_text_size, sp2px(14));
        mThumbTextColor = a.getColor(R.styleable.BubbleSeekBar_bsb_thumb_text_color, mSecondTrackColor);
        mBubbleColor = a.getColor(R.styleable.BubbleSeekBar_bsb_bubble_color, mSecondTrackColor);
        mBubbleTextSize = a.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_bubble_text_size, sp2px(14));
        mBubbleTextColor = a.getColor(R.styleable.BubbleSeekBar_bsb_bubble_text_color, Color.WHITE);
        isShowSectionMark = a.getBoolean(R.styleable.BubbleSeekBar_bsb_show_section_mark, false);
        isAutoAdjustSectionMark = a.getBoolean(R.styleable.BubbleSeekBar_bsb_auto_adjust_section_mark, false);
        isShowProgressInFloat = a.getBoolean(R.styleable.BubbleSeekBar_bsb_show_progress_in_float, false);
        int duration = a.getInteger(R.styleable.BubbleSeekBar_bsb_anim_duration, -1);
        mAnimDuration = duration < 0 ? 200 : duration;
        isTouchToSeek = a.getBoolean(R.styleable.BubbleSeekBar_bsb_touch_to_seek, false);
        a.recycle();

        if (mMin > mMax) {
            int tmp = mMax;
            mMax = mMin;
            mMin = tmp;
        }
        mDelta = mMax - mMin;
        mSectionValue = mDelta / mSectionCount;

        if (mSectionCount <= 0) {
            mSectionCount = 10;
        }
        if (mSectionCount > mDelta) {
            isShowProgressInFloat = true;
        }
        if (isShowSectionText) {
            isShowSectionMark = true;
        }
        if (isAutoAdjustSectionMark && !isShowSectionMark) {
            isAutoAdjustSectionMark = false;
        }
        if (isSeekBySection && !isShowProgressInFloat && mDelta % mSectionCount == 0) {
            isSeekBySection = mSectionValue != 1;
            if (isSeekBySection) {
                mPreSecValue = mMin;
                isShowSectionMark = true;
                isAutoAdjustSectionMark = true;
                isTouchToSeek = false;
            }
        }


        if (mProgress < mMin) {
            mProgress = mMin;
        }
        if (mProgress > mMax) {
            mProgress = mMax;
        }
        if (mSecondTrackSize < mTrackSize) {
            mSecondTrackSize = mTrackSize + dp2px(2);
        }
        if (mThumbRadius <= mSecondTrackSize) {
            mThumbRadius = mSecondTrackSize + dp2px(2);
        }
        if (mThumbRadiusOnDragging <= mSecondTrackSize) {
            mThumbRadiusOnDragging = mSecondTrackSize * 2;
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mRectText = new Rect();

        mTextSpace = dp2px(2);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        // 初始化气泡View
        mBubbleView = new BubbleView(context);
        mBubbleView.setProgressText(isShowProgressInFloat ?
                String.valueOf(getProgressInFloat()) : String.valueOf(getProgress()));

        calculateRadiusOfBubble();
    }

    /**
     * 根据min、max计算气泡半径
     */
    private void calculateRadiusOfBubble() {
        mPaint.setTextSize(mBubbleTextSize);

        // 计算滑到两端气泡里文字需要显示的宽度，比较取最大值为气泡的半径
        String text = mMin < 0 ? "-" + mMin : "" + mMin;
        mPaint.getTextBounds(text, 0, text.length(), mRectText);
        int w1 = (mRectText.width() + mTextSpace * 2) >> 1;
        if (isShowProgressInFloat) {
            text = (mMin < 0 ? "-" + mMin : mMin) + ".0";
            mPaint.getTextBounds(text, 0, text.length(), mRectText);
            w1 = (mRectText.width() + mTextSpace * 2) >> 1;
        }

        text = mMax < 0 ? "-" + mMax : "" + mMax;
        mPaint.getTextBounds(text, 0, text.length(), mRectText);
        int w2 = (mRectText.width() + mTextSpace * 2) >> 1;
        if (isShowProgressInFloat) {
            text = (mMax < 0 ? "-" + mMax : mMax) + ".0";
            mPaint.getTextBounds(text, 0, text.length(), mRectText);
            w2 = (mRectText.width() + mTextSpace * 2) >> 1;
        }

        mBubbleRadius = dp2px(14); // 默认半径14dp
        mBubbleRadius = Math.max(mBubbleRadius, Math.max(w1, w2));
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = mThumbRadiusOnDragging * 2; // 默认高度为拖动时thumb圆的直径
        if (isShowThumbText) {
            mPaint.setTextSize(mThumbTextSize);
            mPaint.getTextBounds("j", 0, 1, mRectText); // “j”是字母和阿拉伯数字中最高的
            height += mRectText.height() + mTextSpace; // 如果显示实时进度，则原来基础上加上进度文字高度和间隔
        }
        if (isShowSectionText && mSectionTextPosition >= TextPosition.BOTTOM_SIDES) { // 如果Section值在track之下显示，比较取较大值
            mPaint.setTextSize(mSectionTextSize);
            mPaint.getTextBounds("j", 0, 1, mRectText);
            height = Math.max(height, mThumbRadiusOnDragging * 2 + mRectText.height() + mTextSpace);
        }
        setMeasuredDimension(resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec), height);

        mLeft = getPaddingLeft() + mThumbRadiusOnDragging;
        mRight = getMeasuredWidth() - getPaddingRight() - mThumbRadiusOnDragging;

        if (isShowSectionText) {
            mPaint.setTextSize(mSectionTextSize);
            if (mSectionTextPosition == SIDES) {

                String text = String.valueOf(mMin);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                mLeft += (mRectText.width() + mTextSpace);
                text = String.valueOf(mMax);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                mRight -= (mRectText.width() + mTextSpace);

            } else if (mSectionTextPosition >= TextPosition.BOTTOM_SIDES) {

                String text = String.valueOf(mMin);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                float max = Math.max(mThumbRadiusOnDragging, mRectText.width() / 2f);
                mLeft = getPaddingLeft() + max;

                text = String.valueOf(mMax);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                max = Math.max(mThumbRadiusOnDragging, mRectText.width() / 2f);
                mRight = getMeasuredWidth() - getPaddingRight() - max;
            }
        }

        mTrackLength = mRight - mLeft;
        mSectionOffset = mTrackLength * 1f / mSectionCount;

        mBubbleView.measure(widthMeasureSpec, heightMeasureSpec);

        locatePositionOnScreen();
    }

    /**
     * 气泡BubbleView实际是通过WindowManager动态添加的一个视图，因此与SeekBar唯一的位置联系就是它们在屏
     * 幕上的绝对坐标。
     * 先计算进度mProgress为零时BubbleView的中心坐标（mBubbleCenterRawSolidX，mBubbleCenterRawSolidY），
     * 然后根据进度来增量计算横坐标mBubbleCenterRawX，再动态设置LayoutParameter.x，就实现了气泡跟随滑动移动。
     */
    private void locatePositionOnScreen() {
        getLocationOnScreen(mPoint);

        mBubbleCenterRawSolidX = mPoint[0] + mLeft - mBubbleView.getMeasuredWidth() / 2f;
        mBubbleCenterRawX = mBubbleCenterRawSolidX + mTrackLength * (mProgress - mMin) / mDelta;
        mBubbleCenterRawSolidY = mPoint[1] - mBubbleView.getMeasuredHeight();
        mBubbleCenterRawSolidY -= dp2px(24);
        if (BuildUtils.isMIUI()) {
            mBubbleCenterRawSolidY += dp2px(4);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float xLeft = getPaddingLeft();
        float xRight = getMeasuredWidth() - getPaddingRight();
        float yTop = getPaddingTop() + mThumbRadiusOnDragging;

        if (isShowSectionText) {
            mPaint.setTextSize(mSectionTextSize);
            mPaint.setColor(mSectionTextColor);

            // 画Section值文字
            if (mSectionTextPosition == SIDES) {
                float y_ = yTop + mRectText.height() / 2f;

                String text = String.valueOf(mMin);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                canvas.drawText(text, xLeft + mRectText.width() / 2f, y_, mPaint);
                xLeft += mRectText.width() + mTextSpace;

                text = String.valueOf(mMax);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                canvas.drawText(text, xRight - mRectText.width() / 2f, y_, mPaint);
                xRight -= (mRectText.width() + mTextSpace);

            } else if (mSectionTextPosition >= TextPosition.BOTTOM_SIDES) {
                float y_ = yTop + mThumbRadiusOnDragging + mTextSpace;

                String text = String.valueOf(mMin);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                y_ += mRectText.height();
                xLeft = mLeft;
                if (mSectionTextPosition == TextPosition.BOTTOM_SIDES) {
                    canvas.drawText(text, xLeft, y_, mPaint);
                }

                text = String.valueOf(mMax);
                mPaint.getTextBounds(text, 0, text.length(), mRectText);
                xRight = mRight;
                if (mSectionTextPosition == TextPosition.BOTTOM_SIDES) {
                    canvas.drawText(text, xRight, y_, mPaint);
                }
            }
        }
        if (!isShowSectionText || mSectionTextPosition == TextPosition.SIDES) {
            xLeft += mThumbRadiusOnDragging;
            xRight -= mThumbRadiusOnDragging;
        }

        boolean isShowTextBelowSectionMark = isShowSectionText && mSectionTextPosition ==
                TextPosition.BELOW_SECTION_MARK && mSectionValue > 0;
        boolean conditionInterval = mSectionCount % 2 == 0;

        if (isShowTextBelowSectionMark || isShowSectionMark) {
            float r = (mThumbRadiusOnDragging - dp2px(2)) / 2f;
            float junction = mTrackLength / mDelta * Math.abs(mProgress - mMin) + mLeft; // 交汇点
            mPaint.setTextSize(mSectionTextSize);
            mPaint.getTextBounds("0123456789", 0, "0123456789".length(), mRectText);

            float x_;
            float y_ = yTop + mRectText.height() + mThumbRadiusOnDragging + mTextSpace;

            for (int i = 0; i <= mSectionCount; i++) {
                x_ = xLeft + i * mSectionOffset;
                mPaint.setColor(x_ <= junction ? mSecondTrackColor : mTrackColor);
                // 画section mark
                canvas.drawCircle(x_, yTop, r, mPaint);

                // 画section mark对应的进度值
                if (isShowTextBelowSectionMark) {
                    if (mSectionTextInterval > 1) {
                        if (conditionInterval && i % mSectionTextInterval == 0) {
                            mPaint.setColor(mTrackColor);
                            canvas.drawText(String.valueOf(mMin + mSectionValue * i), x_, y_, mPaint);
                        }
                    } else {
                        mPaint.setColor(mTrackColor);
                        canvas.drawText(String.valueOf(mMin + mSectionValue * i), x_, y_, mPaint);
                    }
                }
            }
        }

        if (!isThumbOnDragging) {
            mThumbCenterX = mTrackLength / mDelta * (mProgress - mMin) + xLeft;
        }

        if (isShowThumbText && !isThumbOnDragging && isTouchToSeekAnimEnd) {
            // 判断显示小数实时值、TextPosition.BOTTOM时，滑到首尾的情况
            isShowTextBelowSectionMark |= (mSectionTextPosition == TextPosition.BOTTOM_SIDES &&
                    (getProgress() == mMin || getProgress() == mMax));

            mPaint.setColor(mThumbTextColor);
            mPaint.setTextSize(isShowTextBelowSectionMark ? mSectionTextSize : mThumbTextSize);
            mPaint.getTextBounds("0123456789", 0, "0123456789".length(), mRectText);
            float y_ = yTop + mRectText.height() + mThumbRadiusOnDragging + mTextSpace;

            if (isShowProgressInFloat && !isShowTextBelowSectionMark) {
                canvas.drawText(String.valueOf(getProgressInFloat()), mThumbCenterX, y_, mPaint);
            } else {
                canvas.drawText(String.valueOf(getProgress()), mThumbCenterX, y_, mPaint);
            }
        }

        // 画下层track
        mPaint.setColor(mSecondTrackColor);
        mPaint.setStrokeWidth(mSecondTrackSize);
        canvas.drawLine(xLeft, yTop, mThumbCenterX, yTop, mPaint);

        // 画上层track
        mPaint.setColor(mTrackColor);
        mPaint.setStrokeWidth(mTrackSize);
        canvas.drawLine(mThumbCenterX, yTop, xRight, yTop, mPaint);

        // 画thumb
        mPaint.setColor(mThumbColor);
        canvas.drawCircle(mThumbCenterX, yTop, isThumbOnDragging ? mThumbRadiusOnDragging : mThumbRadius, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    float dx;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                isThumbOnDragging = isThumbTouched(event);
                if (isThumbOnDragging) {
                    showBubble();
                    invalidate();
                } else if (isTouchToSeek && isTrackTouched(event)) {
                    mThumbCenterX = event.getX();
                    if (mThumbCenterX < mLeft) {
                        mThumbCenterX = mLeft;
                    }
                    if (mThumbCenterX > mRight) {
                        mThumbCenterX = mRight;
                    }
                    mProgress = (mThumbCenterX - mLeft) * mDelta / mTrackLength + mMin;
                    mBubbleCenterRawX = mBubbleCenterRawSolidX + mTrackLength * (mProgress - mMin) / mDelta;

                    showBubble();
                    invalidate();
                }

                dx = mThumbCenterX - event.getX();

                break;
            case MotionEvent.ACTION_MOVE:
                if (isThumbOnDragging) {
                    mThumbCenterX = event.getX() + dx;
                    if (mThumbCenterX < mLeft) {
                        mThumbCenterX = mLeft;
                    }
                    if (mThumbCenterX > mRight) {
                        mThumbCenterX = mRight;
                    }
                    mProgress = (mThumbCenterX - mLeft) * mDelta / mTrackLength + mMin;

                    mBubbleCenterRawX = mBubbleCenterRawSolidX + mTrackLength * (mProgress - mMin) / mDelta;
                    mLayoutParams.x = (int) (mBubbleCenterRawX + 0.5f);
                    mWindowManager.updateViewLayout(mBubbleView, mLayoutParams);
                    mBubbleView.setProgressText(isShowProgressInFloat ?
                            String.valueOf(getProgressInFloat()) : String.valueOf(getProgress()));

                    invalidate();

                    if (mProgressListener != null) {
                        mProgressListener.onProgressChanged(getProgress(), getProgressInFloat());
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isAutoAdjustSectionMark) {
                    if (isTouchToSeek) {
                        mBubbleView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isTouchToSeekAnimEnd = false;
                                autoAdjustSection();
                            }
                        }, isThumbOnDragging ? 0 : 300);
                    } else {
                        autoAdjustSection();
                    }
                } else if (isThumbOnDragging || isTouchToSeek) {
                    mBubbleView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBubbleView.animate().alpha(0f).setDuration(mAnimDuration)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            hideBubble();

                                            isThumbOnDragging = false;
                                            invalidate();

                                            if (mProgressListener != null) {
                                                mProgressListener.onProgressChanged(getProgress(),
                                                        getProgressInFloat());
                                            }
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {
                                            hideBubble();

                                            isThumbOnDragging = false;
                                            invalidate();
                                        }
                                    }).start();

                        }
                    }, !isThumbOnDragging && isTouchToSeek ? 300 : 0);
                }

                if (mProgressListener != null) {
                    mProgressListener.getProgressOnActionUp(getProgress(), getProgressInFloat());
                }

                break;
        }

        return isThumbOnDragging || isTouchToSeek || super.onTouchEvent(event);
    }

    /**
     * 识别thumb是否被有效点击
     */
    private boolean isThumbTouched(MotionEvent event) {
        float x = mTrackLength / mDelta * (mProgress - mMin) + mLeft;
        float y = getMeasuredHeight() / 2f;
        return (event.getX() - x) * (event.getX() - x) + (event.getY() - y) * (event.getY() - y)
                <= (mLeft + dp2px(8)) * (mLeft + dp2px(8));
    }

    /**
     * 识别track是否被有效点击
     */
    private boolean isTrackTouched(MotionEvent event) {
        return event.getX() >= getPaddingLeft() && event.getX() <= getMeasuredWidth() - getPaddingRight()
                && event.getY() >= getPaddingTop() && event.getY() <= getPaddingTop() + mThumbRadiusOnDragging * 2;
    }

    /**
     * 显示气泡
     * 原理是利用WindowManager动态添加一个与Toast相同类型的BubbleView，消失时再移除
     */
    private void showBubble() {
        if (mBubbleView.getParent() != null) {
            return;
        }

        if (mLayoutParams == null) {
            mLayoutParams = new WindowManager.LayoutParams();
            mLayoutParams.gravity = Gravity.START | Gravity.TOP;
            mLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mLayoutParams.format = PixelFormat.TRANSLUCENT;
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
            // MIUI禁止了开发者使用TYPE_TOAST，Android 7.1.1 对TYPE_TOAST的使用更严格
            if (BuildUtils.isMIUI() || Build.VERSION.SDK_INT >= 25) {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
            } else {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        }
        mLayoutParams.x = (int) (mBubbleCenterRawX + 0.5f);
        mLayoutParams.y = (int) (mBubbleCenterRawSolidY + 0.5f);

        mBubbleView.setAlpha(0);
        mBubbleView.setVisibility(VISIBLE);
        mBubbleView.animate().alpha(1f).setDuration(mAnimDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mWindowManager.addView(mBubbleView, mLayoutParams);
                    }
                }).start();
        mBubbleView.setProgressText(isShowProgressInFloat ?
                String.valueOf(getProgressInFloat()) : String.valueOf(getProgress()));
    }

    /**
     * 自动滚向最近的分段处
     */
    private void autoAdjustSection() {
        int i;
        float x = 0;
        for (i = 0; i <= mSectionCount; i++) {
            x = i * mSectionOffset + mLeft;
            if (x <= mThumbCenterX && mThumbCenterX - x <= mSectionOffset) {
                break;
            }
        }

        BigDecimal bigDecimal = BigDecimal.valueOf(mThumbCenterX);
        float x_ = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        boolean onSection = x_ == x; // 就在section处，不作valueAnim，优化性能

        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator valueAnim = null;
        if (!onSection) {
            if (mThumbCenterX - x <= mSectionOffset / 2f) {
                valueAnim = ValueAnimator.ofFloat(mThumbCenterX, x);
            } else {
                valueAnim = ValueAnimator.ofFloat(mThumbCenterX, (i + 1) * mSectionOffset + mLeft);
            }
            valueAnim.setInterpolator(new LinearInterpolator());
            valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mThumbCenterX = (float) animation.getAnimatedValue();
                    mProgress = (mThumbCenterX - mLeft) * mDelta / mTrackLength + mMin;

                    mBubbleCenterRawX = mBubbleCenterRawSolidX + mThumbCenterX - mLeft;
                    mLayoutParams.x = (int) (mBubbleCenterRawX + 0.5f);
                    if (mBubbleView.getParent() != null) {
                        mWindowManager.updateViewLayout(mBubbleView, mLayoutParams);
                    }
                    mBubbleView.setProgressText(isShowProgressInFloat ?
                            String.valueOf(getProgressInFloat()) : String.valueOf(getProgress()));

                    invalidate();

                    if (mProgressListener != null) {
                        mProgressListener.onProgressChanged(getProgress(), getProgressInFloat());
                    }
                }
            });
        }

        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(mBubbleView, View.ALPHA, 0);

        if (onSection) {
            animatorSet.setDuration(mAnimDuration).play(alphaAnim);
        } else {
            animatorSet.setDuration(mAnimDuration).playTogether(valueAnim, alphaAnim);
        }
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                hideBubble();

                mProgress = (mThumbCenterX - mLeft) * mDelta / mTrackLength + mMin;
                isThumbOnDragging = false;
                isTouchToSeekAnimEnd = true;
                invalidate();

                if (mProgressListener != null) {
                    mProgressListener.getProgressOnFinally(getProgress(), getProgressInFloat());
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                hideBubble();

                mProgress = (mThumbCenterX - mLeft) * mDelta / mTrackLength + mMin;
                isThumbOnDragging = false;
                isTouchToSeekAnimEnd = true;
                invalidate();
            }
        });
        animatorSet.start();
    }

    /**
     * 隐藏气泡
     */
    private void hideBubble() {
        mBubbleView.setVisibility(GONE); // 防闪烁
        if (mBubbleView.getParent() != null) {
            mWindowManager.removeViewImmediate(mBubbleView);
        }
    }

    /**
     * 当外部容器是可滑动的控件时，监听滑动调用该方法来实时修正偏移
     */
    public void correctOffsetWhenContainerOnScrolling() {
        locatePositionOnScreen();

        if (mBubbleView.getParent() != null) {
            postInvalidate();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("save_instance", super.onSaveInstanceState());
        bundle.putFloat("progress", mProgress);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mProgress = bundle.getFloat("progress");
            super.onRestoreInstanceState(bundle.getParcelable("save_instance"));
            mBubbleView.setProgressText(isShowProgressInFloat ?
                    String.valueOf(getProgressInFloat()) : String.valueOf(getProgress()));

            return;
        }

        super.onRestoreInstanceState(state);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    public int getMin() {
        return mMin;
    }

    public int getMax() {
        return mMax;
    }

    public void setProgressRange(int min, int max) {
        setProgressRange(min, max, getProgressInFloat());
    }

    public void setProgressRange(int min, int max, float progress) {
        mMin = Math.min(min, max);
        mMax = Math.max(min, max);
        mDelta = mMax - mMin;
        if (progress > mMax) {
            mProgress = mMax;
        }
        if (progress < mMin) {
            mProgress = mMin;
        }
        calculateRadiusOfBubble();

        postInvalidate();
    }

    public int getProgress() {
        if (isSeekBySection && mSectionValue > 0) {
            float half = mSectionValue >> 1;

            if (mProgress >= mPreSecValue) { // increasing
                if (mProgress >= mPreSecValue + half) {
                    mPreSecValue += mSectionValue;
                    mProgress = mPreSecValue;
                    return Math.round(mProgress);
                } else {
                    mProgress = mPreSecValue;
                    return Math.round(mProgress);
                }
            } else { // reducing
                if (mProgress >= mPreSecValue - half) {
                    mProgress = mPreSecValue;
                    return Math.round(mProgress);
                } else {
                    mPreSecValue -= mSectionValue;
                    mProgress = mPreSecValue;
                    return Math.round(mProgress);
                }
            }
        }

        return Math.round(mProgress);
    }

    public float getProgressInFloat() {
        BigDecimal bigDecimal = BigDecimal.valueOf(mProgress);
        return bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public void setProgress(int progress) {
        setProgress(progress + 0.0f);
    }

    public void setProgress(float progress) {
        if (mProgress == progress || progress < mMin || progress > mMax) {
            return;
        }

        mProgress = progress;
        mBubbleCenterRawX = mBubbleCenterRawSolidX + mTrackLength * (mProgress - mMin) / mDelta;

        if (mProgressListener != null) {
            mProgressListener.onProgressChanged(getProgress(), getProgressInFloat());
            mProgressListener.getProgressOnFinally(getProgress(), getProgressInFloat());
        }

        postInvalidate();
    }

    public int getTrackSize() {
        return mTrackSize;
    }

    public void setTrackSize(int trackSize) {
        if (mTrackSize != trackSize) {
            mTrackSize = trackSize;
            if (mSecondTrackSize <= mTrackSize) {
                mSecondTrackSize = mTrackSize + dp2px(2);
            }
            if (mThumbRadius <= mSecondTrackSize) {
                mThumbRadius = mSecondTrackSize + dp2px(2);
            }
            if (mThumbRadiusOnDragging <= mSecondTrackSize) {
                mThumbRadiusOnDragging = mSecondTrackSize * 2;
            }

            postInvalidate();
        }
    }

    public int getSecondTrackSize() {
        return mSecondTrackSize;
    }

    public void setSecondTrackSize(int secondTrackSize) {
        if (mSecondTrackSize != secondTrackSize && secondTrackSize >= mTrackSize) {
            mSecondTrackSize = secondTrackSize;
            if (mThumbRadius <= mSecondTrackSize) {
                mThumbRadius = mSecondTrackSize + dp2px(2);
            }
            if (mThumbRadiusOnDragging <= mSecondTrackSize) {
                mThumbRadiusOnDragging = mSecondTrackSize * 2;
                requestLayout();
                return;
            }

            postInvalidate();
        }
    }

    public int getThumbRadius() {
        return mThumbRadius;
    }

    public void setThumbRadius(int thumbRadius) {
        if (mThumbRadius != thumbRadius) {
            mThumbRadius = thumbRadius;
            if (mThumbRadius <= mSecondTrackSize) {
                mThumbRadius = mSecondTrackSize + dp2px(2);
            }
            if (mThumbRadiusOnDragging <= mSecondTrackSize) {
                mThumbRadiusOnDragging = mSecondTrackSize * 2;
                requestLayout();
                return;
            }

            postInvalidate();
        }
    }

    public int getThumbRadiusOnDragging() {
        return mThumbRadiusOnDragging;
    }

    public void setThumbRadiusOnDragging(int thumbRadiusOnDragging) {
        if (mThumbRadiusOnDragging != thumbRadiusOnDragging) {
            mThumbRadiusOnDragging = thumbRadiusOnDragging;
            if (mThumbRadiusOnDragging <= mSecondTrackSize) {
                mThumbRadiusOnDragging = mSecondTrackSize * 2;
            }

            requestLayout();
        }
    }

    public int getSectionCount() {
        return mSectionCount;
    }

    public void setSectionCount(int sectionCount) {
        if (mSectionCount != sectionCount) {
            mSectionCount = sectionCount;
            if (mSectionCount <= 0 || mSectionCount > mMax - mMin) {
                mSectionCount = 10;
            }
            if (mSectionCount > mDelta) {
                isShowProgressInFloat = true;

                calculateRadiusOfBubble();
            }

            requestLayout();
        }
    }

    @ColorInt
    public int getTrackColor() {
        return mTrackColor;
    }

    public void setTrackColor(@ColorInt int trackColor) {
        if (mTrackColor != trackColor) {
            mTrackColor = trackColor;
            postInvalidate();
        }
    }

    @ColorInt
    public int getSecondTrackColor() {
        return mSecondTrackColor;
    }

    public void setSecondTrackColor(@ColorInt int secondTrackColor) {
        if (mSecondTrackColor != secondTrackColor) {
            mSecondTrackColor = secondTrackColor;
            postInvalidate();
        }
    }

    @ColorInt
    public int getThumbColor() {
        return mThumbColor;
    }

    public void setThumbColor(@ColorInt int thumbColor) {
        if (mThumbColor != thumbColor) {
            mThumbColor = thumbColor;
            postInvalidate();
        }
    }

    public boolean isShowSectionText() {
        return isShowSectionText;
    }

    public void setShowSectionText(boolean showSectionText) {
        if (isShowSectionText != showSectionText) {
            isShowSectionText = showSectionText;
            if (isShowSectionText) {
                isShowSectionMark = true;
            }
            requestLayout();
        }
    }

    public int getSectionTextSize() {
        return mSectionTextSize;
    }

    public void setSectionTextSize(int sectionTextSize) {
        if (mSectionTextSize != sectionTextSize) {
            mSectionTextSize = sectionTextSize;
            requestLayout();
        }
    }

    @ColorInt
    public int getSectionTextColor() {
        return mSectionTextColor;
    }

    public void setSectionTextColor(@ColorInt int sectionTextColor) {
        if (mSectionTextColor != sectionTextColor) {
            mSectionTextColor = sectionTextColor;
            postInvalidate();
        }
    }

    public int getSectionTextPosition() {
        return mSectionTextPosition;
    }

    public void setSectionTextPosition(@TextPosition int sectionTextPosition) {
        if (mSectionTextPosition != sectionTextPosition) {
            mSectionTextPosition = sectionTextPosition;
            requestLayout();
        }
    }

    public int getSectionTextInterval() {
        return mSectionTextInterval;
    }

    public void setSectionTextInterval(int sectionTextInterval) {
        if (sectionTextInterval < 1 || sectionTextInterval == mSectionTextInterval)
            return;

        mSectionTextInterval = sectionTextInterval;
        postInvalidate();
    }

    public boolean isShowThumbText() {
        return isShowThumbText;
    }

    public void setShowThumbText(boolean showThumbText) {
        if (isShowThumbText != showThumbText) {
            isShowThumbText = showThumbText;
            requestLayout();
        }
    }

    public int getThumbTextSize() {
        return mThumbTextSize;
    }

    public void setThumbTextSize(int thumbTextSize) {
        if (mThumbTextSize != thumbTextSize) {
            mThumbTextSize = thumbTextSize;
            requestLayout();
        }
    }

    @ColorInt
    public int getThumbTextColor() {
        return mThumbTextColor;
    }

    public void setThumbTextColor(@ColorInt int thumbTextColor) {
        if (mThumbTextColor != thumbTextColor) {
            mThumbTextColor = thumbTextColor;
            postInvalidate();
        }
    }

    public boolean isShowProgressInFloat() {
        return isShowProgressInFloat;
    }

    public void setShowProgressInFloat(boolean showProgressInFloat) {
        if (mSectionCount > mDelta) {
            isShowProgressInFloat = true;
            return;
        }

        if (isShowProgressInFloat != showProgressInFloat) {
            isShowProgressInFloat = showProgressInFloat;

            calculateRadiusOfBubble();

            postInvalidate();
        }
    }

    public boolean isTouchToSeek() {
        return isTouchToSeek;
    }

    public void setTouchToSeek(boolean touchToSeek) {
        isTouchToSeek = touchToSeek;
    }

    @ColorInt
    public int getBubbleColor() {
        return mBubbleColor;
    }

    public void setBubbleColor(@ColorInt int bubbleColor) {
        if (mBubbleColor != bubbleColor) {
            mBubbleColor = bubbleColor;
            mBubbleView.postInvalidate();
        }
    }

    public int getBubbleTextSize() {
        return mBubbleTextSize;
    }

    public void setBubbleTextSize(int bubbleTextSize) {
        if (mBubbleTextSize != bubbleTextSize) {
            mBubbleTextSize = bubbleTextSize;
            mBubbleView.postInvalidate();
        }
    }

    @ColorInt
    public int getBubbleTextColor() {
        return mBubbleTextColor;
    }

    public void setBubbleTextColor(@ColorInt int bubbleTextColor) {
        if (mBubbleTextColor != bubbleTextColor) {
            mBubbleTextColor = bubbleTextColor;
            mBubbleView.postInvalidate();
        }
    }

    public boolean isShowSectionMark() {
        return isShowSectionMark;
    }

    public void setShowSectionMark(boolean showSectionMark) {
        if (isShowSectionMark != showSectionMark) {
            isShowSectionMark = showSectionMark;

            if (isAutoAdjustSectionMark && !isShowSectionMark) {
                isAutoAdjustSectionMark = false;
            }
            postInvalidate();
        }
    }

    public boolean isAutoAdjustSectionMark() {
        return isAutoAdjustSectionMark;
    }

    public void setAutoAdjustSectionMark(boolean autoAdjustSectionMark) {
        if (isAutoAdjustSectionMark != autoAdjustSectionMark) {
            isAutoAdjustSectionMark = autoAdjustSectionMark;

            if (isAutoAdjustSectionMark && !isShowSectionMark) {
                isAutoAdjustSectionMark = false;
            }
            postInvalidate();
        }
    }

    public boolean isSeekBySection() {
        return isSeekBySection;
    }

    public void setSeekBySection(boolean seekBySection) {
        if (isSeekBySection != seekBySection) {
            isSeekBySection = seekBySection;

            if (isSeekBySection && !isShowProgressInFloat && mDelta % mSectionCount == 0) {
                mSectionValue = mDelta / mSectionCount;
                isSeekBySection = mSectionValue != 1;
                if (isSeekBySection) {
                    mPreSecValue = mMin;
                    isShowSectionMark = true;
                    isAutoAdjustSectionMark = true;
                    isTouchToSeek = false;
                }
            }
            postInvalidate();
        }
    }

    public OnProgressChangedListener getOnProgressChangedListener() {
        return mProgressListener;
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        mProgressListener = onProgressChangedListener;
    }

    /**
     * progress改变监听器
     */
    public interface OnProgressChangedListener {

        void onProgressChanged(int progress, float progressFloat);

        void getProgressOnActionUp(int progress, float progressFloat);

        void getProgressOnFinally(int progress, float progressFloat);
    }

    /**
     * progress改变监听
     * <br/>
     * 用法同{@link AnimatorListenerAdapter}
     */
    public static abstract class OnProgressChangedListenerAdapter implements OnProgressChangedListener {

        @Override
        public void onProgressChanged(int progress, float progressFloat) {
        }

        @Override
        public void getProgressOnActionUp(int progress, float progressFloat) {
        }

        @Override
        public void getProgressOnFinally(int progress, float progressFloat) {
        }
    }


    /*******************************************************************************************
     * ************************************  自定义气泡View  ************************************
     *******************************************************************************************/
    private class BubbleView extends View {

        private Paint mBubblePaint;
        private Path mBubblePath;
        private RectF mBubbleRectF;
        private Rect mRect;
        private String mProgressText = "";

        BubbleView(Context context) {
            this(context, null);
        }

        BubbleView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);

            mBubblePaint = new Paint();
            mBubblePaint.setAntiAlias(true);
            mBubblePaint.setTextAlign(Paint.Align.CENTER);

            mBubblePath = new Path();
            mBubbleRectF = new RectF();
            mRect = new Rect();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            setMeasuredDimension(3 * mBubbleRadius, 3 * mBubbleRadius);

            mBubbleRectF.set(getMeasuredWidth() / 2f - mBubbleRadius, 0,
                    getMeasuredWidth() / 2f + mBubbleRadius, 2 * mBubbleRadius);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            mBubblePath.reset();
            float x0 = getMeasuredWidth() / 2f;
            float y0 = getMeasuredHeight() - mBubbleRadius / 3f;
            mBubblePath.moveTo(x0, y0);
            float x1 = (float) (getMeasuredWidth() / 2f - Math.sqrt(3) / 2f * mBubbleRadius);
            float y1 = 3 / 2f * mBubbleRadius;
            mBubblePath.quadTo(
                    x1 - dp2px(2), y1 - dp2px(2),
                    x1, y1
            );
            mBubblePath.arcTo(mBubbleRectF, 150, 240);

            float x2 = (float) (getMeasuredWidth() / 2f + Math.sqrt(3) / 2f * mBubbleRadius);
            mBubblePath.quadTo(
                    x2 + dp2px(2), y1 - dp2px(2),
                    x0, y0
            );
            mBubblePath.close();

            mBubblePaint.setColor(mBubbleColor);
            canvas.drawPath(mBubblePath, mBubblePaint);

            mBubblePaint.setTextSize(mBubbleTextSize);
            mBubblePaint.setColor(mBubbleTextColor);
            mBubblePaint.getTextBounds(mProgressText, 0, mProgressText.length(), mRect);
            Paint.FontMetrics fm = mBubblePaint.getFontMetrics();
            float baseline = mBubbleRadius + (fm.descent - fm.ascent) / 2f - fm.descent;
            canvas.drawText(mProgressText, getMeasuredWidth() / 2f, baseline, mBubblePaint);
        }

        void setProgressText(String progressText) {
            if (progressText != null && !mProgressText.equals(progressText)) {
                mProgressText = progressText;
                invalidate();
            }
        }
    }

}