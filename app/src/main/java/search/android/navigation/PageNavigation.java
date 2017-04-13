package search.android.navigation;

import android.content.Context;
import android.content.Intent;

import search.android.aos_search.DetailActivity;
import search.android.aos_search.MainActivity;
import search.android.aos_search.WebviewActivity;

/**
 * Created by nhnent on 2017. 4. 12..
 */

public class PageNavigation {

    public static final int OK = 0;
    public static final int REQUEST_CANCLE = 1;
    public static final int NO_SEARCH_RESULT = 2;
    public static final int TIME_OUT = 3;
    public static final int NO_INTENT = 4;
    public static final int NO_INTERNET = 5;
    public static final int UNKOWN_ERROR = 100;

    public static Intent moveSearchPage(Context context, String title) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("Search", title);
        return intent;
    }

    public static Intent moveWebviewPage(Context context, String title) {
        Intent intent = new Intent(context, WebviewActivity.class);
        intent.putExtra("Search", title);
        return intent;
    }

    public static Intent moveMainPage(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //이전의 Activity를 제거
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //MainActivity가 새로 생기는 것을 방지
        return intent;
    }

    public static String statusMessage(int status) {

        switch(status) {
            case OK :
                return "정상작동";
            case REQUEST_CANCLE :
                return "요청을 취소했습니다.";
            case NO_SEARCH_RESULT :
                return "검색결과가 존재하지 않습니다.";
            case TIME_OUT:
                return "수행시간을 초과했습니다.";
            case NO_INTENT :
                return "데이터가 존재하지 않습니다.";
            case NO_INTERNET :
                return "인터넷이 연결되지 않았습니다.";
            default :
                return "알 수 없는 상태입니다.";
        }
    }

    public static Intent shareWikiPage(String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Wikipedia_URL");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.setType("text/plain");
        return intent;
    }
}
