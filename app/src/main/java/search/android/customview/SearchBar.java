package search.android.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import search.android.aos_search.R;

/**
 * Created by nhnent on 2017. 4. 5..
 */

public class SearchBar extends LinearLayout {


    private CustomEditText searchText;
    private View deleteButton;
    private View underLine;
    private Button searchButton;

    private OnSearchBarClickedListener searchBarListener;

    public SearchBar(Context context) {
        super(context);
        init(context);
    }

    public SearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.wiki_search_bar, this, true);

        searchText = (CustomEditText) findViewById(R.id.searchText);
        searchText.setRawInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS); //자동완성기능 제거
        searchText.setOnBackPressListner(new CustomEditText.OnBackPressListener() {
            @Override
            public void onBackPressed() {
                requestFocus();

            }
        });

        searchText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    underLine.setBackgroundResource(R.color.colorSelectUnderLine);

                    if(searchText.getText().length() > 0) {
                        deleteButton.setVisibility(View.VISIBLE);
                    }
                }
                else {

                    //키보드 숨기기
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);

                    underLine.setBackgroundResource(R.color.colorDefaultUnderLine);
                    deleteButton.setVisibility(View.GONE);
                }
            }
        });

        //키보드 상에서 검색버튼을 클릭 시 검색
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId) {
                    case EditorInfo.IME_ACTION_SEARCH :
                        searchBarListener.onSearchButtonClicked(searchText.getText().toString());
                        requestFocus();
                        break;

                    default :
                        return false;
                }

                return true;
            }
        });


        //검색창에 내용이 입력될 경우 이벤트
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            //길이를 확인해서 문자열이 입력되었으면 제거버튼(Delete Button) 활성화
            @Override
            public void afterTextChanged(Editable edit) {
                String str = edit.toString();
                if(str.length() > 0) {
                    deleteButton.setVisibility(View.VISIBLE);
                }
                else {
                    deleteButton.setVisibility(View.GONE);
                }
            }
        });

        deleteButton = findViewById(R.id.deleteButton);

        //삭제버튼을 클릭하면 입력된 모든 정보를 제거
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
            }
        });

        underLine = findViewById(R.id.underLine);
        searchButton = (Button) findViewById(R.id.searchButton);

        //검색버튼을 누르면 리스너를 통해서 검색어를 전달
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchBarListener != null) {
                    searchBarListener.onSearchButtonClicked(searchText.getText().toString());
                }
            }
        });

    }

    public void setOnSearchBarClickedListener(OnSearchBarClickedListener searchBarListener) {
        this.searchBarListener = searchBarListener;
    }

    public interface OnSearchBarClickedListener {
        void onSearchButtonClicked(String searchText);
    }
}
