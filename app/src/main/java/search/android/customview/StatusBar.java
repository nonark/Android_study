package search.android.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import search.android.aos_search.R;

/**
 * Created by nhnent on 2017. 4. 5..
 */

public class StatusBar extends LinearLayout {

    Button backButton;
    Button closeButton;
    TextView title;

    OnStatusBarClickedListener backButtonListener;
    OnStatusBarClickedListener closeButtonListener;

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

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(backButtonListener != null) {
                    backButtonListener.onStatusButtonClicked();
                }
            }
        });


        closeButton = (Button) findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(closeButtonListener != null) {
                    closeButtonListener.onStatusButtonClicked();
                }
            }
        });

        title = (TextView) findViewById(R.id.titleBar);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setOnBackButtonClickedListener(OnStatusBarClickedListener statusBarListener) {
        this.backButtonListener = statusBarListener;
    }

    public void setOnCloseButtonClickedListener(OnStatusBarClickedListener statusBarListener) {
        this.closeButtonListener = statusBarListener;
    }

    public interface OnStatusBarClickedListener {
        void onStatusButtonClicked();
    }
}
