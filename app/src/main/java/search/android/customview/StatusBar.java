package search.android.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import search.android.aos_search.R;

import static search.android.aos_search.R.id.searchText;

/**
 * Created by nhnent on 2017. 4. 5..
 */

public class StatusBar extends Bar {

    private Button backButton;
    private Button closeButton;
    private TextView title;

    private OnStatusBarClickedListener backButtonListener;
    private OnStatusBarClickedListener closeButtonListener;
    private OnStatusBarClickedListener titleListener;

    public StatusBar(Context context) {
        super(context);
        init(context, null);
    }

    public StatusBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.status_bar, this, true);

        //Status Bar에서 사용하는 기능은 비활성화
        RelativeLayout searchTextArea = (RelativeLayout) findViewById(R.id.searchTextArea);
        searchTextArea.setVisibility(View.GONE);

        //View 연결
        title = (TextView) findViewById(R.id.titleBar);
        backButton = (Button) findViewById(R.id.backButton);
        closeButton = (Button) findViewById(R.id.rightButton);

        //View Image Setting
        backButton.setBackgroundResource(R.drawable.btn_back);
        closeButton.setBackgroundResource(R.drawable.ic_close);

        //Custom Attribute Setting
        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.Bar);
        int fontColor = tArray.getColor(R.styleable.Bar_fontColor, 0);
        int fontSize = tArray.getDimensionPixelSize(R.styleable.Bar_fontSize, (int) title.getTextSize());

        //Custom Attribute Setting
        title.setTextColor(fontColor);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(backButtonListener != null) {
                    backButtonListener.onStatusButtonClicked();
                }
            }
        });

        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(closeButtonListener != null) {
                    closeButtonListener.onStatusButtonClicked();
                }
            }
        });

        title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titleListener != null) {
                    titleListener.onStatusButtonClicked();
                }
            }
        });

    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
    public String getTitle() { return this.title.getText().toString(); }

    public void setOnTitleClickedListener(OnStatusBarClickedListener statusBarListener) {
        this.titleListener = statusBarListener;
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
