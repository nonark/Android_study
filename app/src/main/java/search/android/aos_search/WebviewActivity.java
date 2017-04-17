package search.android.aos_search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import search.android.customview.StatusBar;
import search.android.navigation.PageNavigation;
import search.android.tools.WikiPageFinder;

/**
 * Created by nhnent on 2017. 4. 6..
 */

public class WebviewActivity extends Activity {

    private StatusBar statusBar;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        statusBar = (StatusBar) findViewById(R.id.statusBar);

        // 뒤로가기 버튼 클릭시 finish 호출
        statusBar.setOnBackButtonClickedListener(new StatusBar.OnStatusBarClickedListener() {
            @Override
            public void onStatusButtonClicked() {
                setResult(PageNavigation.OK);
                finish();
            }
        });

        // X 버튼을 누르면 현재까지 생성한 모든 Activity를 제거하고 MainActivity를 상단에 출력
        statusBar.setOnCloseButtonClickedListener(new StatusBar.OnStatusBarClickedListener() {
            @Override
            public void onStatusButtonClicked() {
                Intent intent = PageNavigation.moveMainPage(getBaseContext());
                startActivity(intent);
            }
        });

        //상단의 제목 클릭시 URL 공유
        statusBar.setOnTitleClickedListener(new StatusBar.OnStatusBarClickedListener() {
            @Override
            public void onStatusButtonClicked() {
                Intent intent = PageNavigation.shareWikiPage(new WikiPageFinder().getHtmlUrl(statusBar.getTitle()));
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                switch(errorCode) {
                    case ERROR_AUTHENTICATION:                // 서버에서 사용자 인증 실패
                    case ERROR_BAD_URL:                            // 잘못된 URL
                    case ERROR_CONNECT:                           // 서버로 연결 실패
                    case ERROR_FAILED_SSL_HANDSHAKE:     // SSL handshake 수행 실패
                    case ERROR_FILE:                                   // 일반 파일 오류
                    case ERROR_FILE_NOT_FOUND:                // 파일을 찾을 수 없습니다
                    case ERROR_HOST_LOOKUP:            // 서버 또는 프록시 호스트 이름 조회 실패
                    case ERROR_IO:                               // 서버에서 읽거나 서버로 쓰기 실패
                    case ERROR_PROXY_AUTHENTICATION:    // 프록시에서 사용자 인증 실패
                    case ERROR_REDIRECT_LOOP:                // 너무 많은 리디렉션
                    case ERROR_TOO_MANY_REQUESTS:      // 페이지 로드중 너무 많은 요청 발생
                    case ERROR_UNKNOWN:                         // 일반 오류
                    case ERROR_UNSUPPORTED_AUTH_SCHEME:  // 지원되지 않는 인증 체계
                    case ERROR_UNSUPPORTED_SCHEME:           // URI가 지원되지 않는 방식
                        setResult(PageNavigation.NO_SEARCH_RESULT);
                        break;

                    case ERROR_TIMEOUT:                           // 연결 시간 초과
                        setResult(PageNavigation.TIME_OUT);
                        break;

                    default :
                        setResult(PageNavigation.UNKOWN_ERROR);
                        break;
                }
                finish();
            }
        });

        //인터넷 연결 확인 - PERMISSION(ACCESS_NETWORK_STATE) 필요
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getActiveNetworkInfo();
        if(mobile == null) {
            setResult(PageNavigation.NO_INTERNET);
            finish();
        }

        //Intent 내의 값이 null이 아니면 주어진 URL에 해당하는 웹 표시
        Intent intent = getIntent();
        if (intent != null) {
            String searchText = intent.getStringExtra("Search");
            statusBar.setTitle(searchText);

            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(new WikiPageFinder().getHtmlUrl(searchText));
        } else {
            setResult(PageNavigation.NO_INTENT);
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
