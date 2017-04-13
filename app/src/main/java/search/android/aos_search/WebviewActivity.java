package search.android.aos_search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import search.android.customview.StatusBar;
import search.android.tools.WikiPageFinder;

/**
 * Created by nhnent on 2017. 4. 6..
 */

public class WebviewActivity extends Activity {

    StatusBar statusBar;
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        statusBar = (StatusBar) findViewById(R.id.statusBar);

        // 뒤로가기 버튼 클릭시 finish 호출
        statusBar.setOnBackButtonClickedListener(new StatusBar.OnStatusBarClickedListener() {
            @Override
            public void onStatusButtonClicked() {
                finish();
            }
        });

        // X 버튼을 누르면 현재까지 생성한 모든 Activity를 제거하고 MainActivity를 상단에 출력
        statusBar.setOnCloseButtonClickedListener(new StatusBar.OnStatusBarClickedListener() {
            @Override
            public void onStatusButtonClicked() {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //이전의 Activity를 제거
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //MainActivity가 새로 생기는 것을 방지
                startActivityForResult(intent, 0);
            }
        });

        //상단의 제목 클릭시 URL 공유
        statusBar.setOnTitleClickedListener(new StatusBar.OnStatusBarClickedListener() {
            @Override
            public void onStatusButtonClicked() {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Wikipedia_URL");
                intent.putExtra(Intent.EXTRA_TEXT, new WikiPageFinder().getHtmlUrl(statusBar.getTitle()));
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        webView = (WebView) findViewById(R.id.webView);

        //Intent 내의 값이 null이 아니면 주어진 URL에 해당하는 웹 표시
        Intent intent = getIntent();
        if (intent != null) {
            String searchText = intent.getStringExtra("Search");
            statusBar.setTitle(searchText);

            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(new WikiPageFinder().getHtmlUrl(searchText));
        } else {
            Toast.makeText(getBaseContext(), "페이지를 열 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //finish 호출 시 애니메이션 적용
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_hold, R.anim.right_slide);
    }
}
