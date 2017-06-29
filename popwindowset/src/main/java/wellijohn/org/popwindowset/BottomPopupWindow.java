package wellijohn.org.popwindowset;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import wellijohn.org.popwindowset.utils.UIUtils;

/**
 * @author: JiangWeiwei
 * @time: 2016/7/6-17:07
 * @email: wellijohn1991@gmail.com
 * @desc: 底部滚动选择器，内容view可自行定义
 */
public class BottomPopupWindow extends PopupWindow {

    private OnConfirmClickListener onConfirmClickListener;


    public BottomPopupWindow(final Context context, View contentView) {
        this(context, contentView, false);
    }


    /**
     * @param context
     * @param contentView
     * @param isDefault   为true的时候，默认是全布局都添加上去，如果是false，头部会有取消和确定的按钮
     */
    public BottomPopupWindow(final Context context, View contentView, boolean isDefault) {
        super(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (!isDefault) {
            View view = View.inflate(context, R.layout.pop_window_wheel_view, null);
            TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
            TextView btnSubmit = (TextView) view.findViewById(R.id.btn_submit);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBtnClick(v);
                }
            });
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBtnClick(v);
                }
            });
            LinearLayout mLlSelectContent = (LinearLayout) view.findViewById(R.id.ll_select_content);
            mLlSelectContent.addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            setContentView(view);
        } else {
            setContentView(contentView);
        }
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setBackgroundAlpha(context, 0.5f);
        setAnimationStyle(R.style.PopupWindow_Bottom_Animation);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(context, 1.0f);
            }
        });
    }


    /**
     * 设置内容布局
     *
     * @param contentView 显示的contentView
     * @return 当前的popupwindow
     */
    public BottomPopupWindow setContent(View contentView) {
        super.setContentView(contentView);
        return this;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    private void setBackgroundAlpha(Context mContext, float bgAlpha) {
        Activity activity = null;
        if (mContext instanceof Activity) {
            activity = (Activity) mContext;
        } else if (mContext instanceof ContextWrapper) {
            activity = UIUtils.getActivityFromContextWrapper((ContextWrapper) mContext);
        }
        if (activity == null) return;
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }


    private void setBtnClick(View view) {
        dismiss();
        if (view.getId() == R.id.btn_submit && null != onConfirmClickListener) {
            onConfirmClickListener.onConfirm();
        }
    }


    public void showAtBottom(@NonNull View rootView) {
        showAtLocation(rootView, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
    }


    public void setOnConfirmClickListener(OnConfirmClickListener paramOnConfirmClickListener) {
        this.onConfirmClickListener = paramOnConfirmClickListener;
    }

    interface OnConfirmClickListener {
        void onConfirm();
    }


}
