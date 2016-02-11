package com.example.lenovo.definedWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.lenovo.smartMooc.R;


public class changeColorIconWithText extends View {
    private int color = 0xFF45C01A;
    private Bitmap iconBitmap;
    private String text = "΢��";
    private int textSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
    // ��ͼ
    private Canvas canvas;
    private Bitmap bitmap;
    private Paint paint;
    private Paint textPaint;
    private float alpha;
    private Rect iconRect;
    private Rect textBonund;

    /**
     * ��ȡ�Զ������Ե�ֵ
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public changeColorIconWithText(Context context, AttributeSet attrs,
                                   int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        // �����Զ�������
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.changeColorIconWithText);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.changeColorIconWithText_mIcon:
                    BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
                    iconBitmap = drawable.getBitmap();
                    break;
                case R.styleable.changeColorIconWithText_mColor:
                    color = a.getColor(attr, 0xFF45C01A);
                    break;
                case R.styleable.changeColorIconWithText_mText:
                    text = a.getString(attr);
                    break;
                case R.styleable.changeColorIconWithText_mText_Size:
                    textSize = (int) a.getDimension(attr, TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                                    getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        textBonund = new Rect();
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(0Xff555555);
        textPaint.getTextBounds(text, 0, text.length(), textBonund);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // ͼƬ�����С���������
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - getPaddingTop()
                - getPaddingBottom() - textBonund.height());
        int left = getMeasuredWidth() / 2 - iconWidth / 2;
        int top = (getMeasuredHeight() - textBonund.height()) / 2 - iconWidth
                / 2;
        iconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawBitmap(iconBitmap, null, iconRect, null);//1
        int int_alpha = (int) Math.ceil(255 * alpha);// ����ȡ��
        // �ڴ�׼��bitmap��setAlpha����ɫ��xfermode��ͼ��
        setUpTargetBitmap(int_alpha);
        //�����ڴ��е�bitmap����1�е�ͼ����
        canvas.drawBitmap(bitmap, 0, 0, null);
        // �������֣�����ɫ����
        drawSourceText(canvas, int_alpha);
        drawTargetText(canvas, int_alpha);

    }

    // ��ɫ�ı�
    private void drawTargetText(Canvas canvas2, int int_alpha) {
        // TODO Auto-generated method stub
        textPaint.setColor(color);
        textPaint.setFakeBoldText(true);
        textPaint.setAlpha(int_alpha);
        int x = getMeasuredWidth() / 2 - textBonund.width() / 2;
        int y = iconRect.bottom + textBonund.height();
        canvas.drawText(text, x, y, textPaint);
    }

    // ����ԭ�ı�
    private void drawSourceText(Canvas canvas2, int int_alpha) {
        // TODO Auto-generated method stub
        textPaint.setColor(0xff333333);
        textPaint.setAlpha(255 - int_alpha);
        int x = getMeasuredWidth() / 2 - textBonund.width() / 2;
        int y = iconRect.bottom + textBonund.height();
        canvas.drawText(text, x, y, textPaint);
    }

    // ���ڴ��л��ƿɱ�ɫ��icon
    private void setUpTargetBitmap(int alpha) {
        // TODO Auto-generated method stub
        bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);// ȥ���
        paint.setDither(true);
        paint.setAlpha(alpha);
        canvas.drawRect(iconRect, paint);//��ɫͼ����
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setAlpha(255);
        //dit_in����������color��ɫ��icon
        canvas.drawBitmap(iconBitmap, null, iconRect, paint);
    }

    public changeColorIconWithText(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public changeColorIconWithText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public void setIconAlpha(float alpha) {
        this.alpha = alpha;
        invalidateView();
    }

    //�ػ�
    private void invalidateView() {
        // TODO Auto-generated method stub
        if (Looper.getMainLooper() == Looper.myLooper()) {
            //�����ui�߳�
            invalidate();
        } else {
            postInvalidate();
        }
    }

    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS = "status_alpha";

    //��ֹϵͳ����
    @Override
    protected Parcelable onSaveInstanceState() {
        // TODO Auto-generated method stub
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS, alpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // TODO Auto-generated method stub
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            alpha = bundle.getFloat(STATUS);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
