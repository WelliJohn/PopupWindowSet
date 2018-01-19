package wellijohn.org.popwindowset;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import wellijohn.org.popwindowset.utils.UIUtils;


/**
 * @author: JiangWeiwei
 * @time: 2017/6/7-19:06
 * @email: wellijohn1991@gmail.com
 * @desc: 下拉的popupwindow，内容view可以自定义
 */
public class DropDownPopupWindow extends PopupWindow implements View.OnClickListener {

    private static final String TAG = "DropDownPopupWindow";
    private int layerColor = Color.parseColor("#80000000");

    private View layerView;

    private View contentParentView;

    /**
     * contentViewHeight 不需要指定的时候可以赋值为<0
     */
    public DropDownPopupWindow(View contentView) {
        this(contentView, -1);
    }

    /**
     * contentViewHeight 不需要指定的时候可以赋值为<0
     */
    public DropDownPopupWindow(View contentView, int contentViewHeight) {
        this(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, contentViewHeight);
    }

    /**
     * contentViewHeight 不需要指定的时候可以赋值为<0
     */
    public DropDownPopupWindow(View contentView, int width, int height, int contentViewHeight) {
        this(contentView, width, height, true, contentViewHeight);
    }

    /**
     * contentViewHeight 不需要指定的时候可以赋值为<0
     */
    public DropDownPopupWindow(View contentView, int width, int height, boolean focusable, int contentViewHeight) {
        super(contentView, width, height, focusable);
        initPopView(contentView, contentViewHeight);
    }

    public DropDownPopupWindow setLayerColor(@ColorRes int layerColor) {
        this.layerColor = layerColor;
        return this;
    }

    private void setLayerView(View layerView) {
        this.layerView = layerView;
    }

    private void setContentParentView(View contentParentView) {
        this.contentParentView = contentParentView;
    }

    private void initPopView(View contentView, int contentViewHeight) {
        View popContentView = View.inflate(contentView.getContext(), R.layout.drop_down_popup_window, null);
        LinearLayout rootView = (LinearLayout) popContentView.findViewById(R.id.ll_popupwindow_rootview);
        LinearLayout contentParentView = (LinearLayout) popContentView.findViewById(R.id.ll_content_view);
        View layerView = popContentView.findViewById(R.id.view);
        contentParentView.addView(contentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //限制内容高度
        if (contentViewHeight > 0) {
            contentView.getLayoutParams().height = contentViewHeight;
        }

        layerView.setBackgroundColor(layerColor);
        setContentParentView(contentParentView);
        setLayerView(layerView);
        setContentView(popContentView);
        rootView.setOnClickListener(this);
//        setAnimationStyle(R.style.PopupAnimation);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_popupwindow_rootview) {
            if (isShowing()) dismiss();
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
        this.showAsDropDown(anchor, 0, 0, Gravity.TOP | Gravity.START);
    }


    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        this.showAsDropDown(anchor, xoff, yoff, Gravity.TOP | Gravity.START);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        show(getContentView().getContext(), anchor, xoff, yoff, getContentView(), layerView);
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }


    /**
     * @param context      Activity
     * @param attachOnView 显示在这个View的下方
     */
    private void show(final Context context, final View attachOnView, int xoff, final int yoff, final View contentView, final View layerView) {
        if (contentView != null && contentView.getParent() != null && contentView.getParent() instanceof ViewGroup) {
            ((ViewGroup) contentView.getParent()).removeAllViews();
        }

        contentParentView.post(new Runnable() {
            @Override
            public void run() {
                if (null != layerView) {
                    int location[] = new int[2];
                    int y;
                    attachOnView.getLocationOnScreen(location);
                    y = location[1];
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layerView.getLayoutParams();
                    lp.height = UIUtils.getScreenHeight(context) - (y + attachOnView.getHeight() + contentParentView.getHeight() + yoff);
                    layerView.setLayoutParams(lp);
                }
            }
        });

    }
}
