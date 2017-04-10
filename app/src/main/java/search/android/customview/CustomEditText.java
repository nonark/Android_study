package search.android.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by nhnent on 2017. 4. 10..
 */

public class CustomEditText extends EditText {

    OnBackPressListener listener;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && listener != null) {
            listener.onBackPressed();
            return true;
        }

        return super.onKeyPreIme(keyCode, event);
    }

    public void setOnBackPressListner(OnBackPressListener listner) {
        this.listener = listner;
    }

    public interface OnBackPressListener {
        void onBackPressed();
    }
}
