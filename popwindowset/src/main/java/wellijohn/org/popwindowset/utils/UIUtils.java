package wellijohn.org.popwindowset.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.DisplayMetrics;
import android.view.WindowManager;


public class UIUtils {

    /**
     * @param paramContext context
     * @return 屏幕的高度
     */
    public static int getScreenHeight(Context paramContext) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) paramContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    public static Activity getActivityFromContextWrapper(ContextWrapper paramContext) {
        Context temp = paramContext;
        while (temp instanceof ContextWrapper) {
            if (temp instanceof Activity) {
                return (Activity) temp;
            }
            temp = ((ContextWrapper) temp).getBaseContext();
        }
        return null;
    }


}
