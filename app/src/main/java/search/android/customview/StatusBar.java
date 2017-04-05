package search.android.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import search.android.aos_search.R;

/**
 * Created by nhnent on 2017. 4. 5..
 */

public class StatusBar extends LinearLayout {
    public StatusBar(Context context) {
        super(context);
        init(context);
    }

    public StatusBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.wiki_status_bar, this, true);

    }
}
