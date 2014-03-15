/**
 * @Author marvin
 * @Email marvinlix@gmail.com
 * @Version 0.0.1
 * @CreateTime 2012-7-10
 * 
 */

package com.pybeta.util;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

public class DMToast {

    private static Toast sCurrentToast = null;

    public static Toast makeText(Context context, CharSequence text, int duration) {
        if (sCurrentToast == null) {
            sCurrentToast = Toast.makeText(context, text, duration);
        }
        sCurrentToast.setText(text);
        sCurrentToast.setDuration(duration);
        return sCurrentToast;
    }

    public static Toast makeText(Context context, int resId, int duration) throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }
    
}
