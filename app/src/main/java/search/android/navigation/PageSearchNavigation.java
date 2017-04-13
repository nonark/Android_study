package search.android.navigation;

import android.content.Context;
import android.content.Intent;

import search.android.aos_search.MainActivity;
import search.android.tools.WikiPageFinder;

/**
 * Created by nhnent on 2017. 4. 12..
 */

public class PageSearchNavigation {

    public static Intent shareWikiPageIntent(String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Wikipedia_URL");
        intent.putExtra(Intent.EXTRA_TEXT, WikiPageFinder.getHtmlUrl(title));
        intent.setType("text/plain");
        return intent;
    }

    public static Intent moveMainPage(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //이전의 Activity를 제거
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //MainActivity가 새로 생기는 것을 방지
        return intent;
    }
/*
    public static Intent moveSearchPage(Context context, String title) {

    }

    public static Intent moveWebviewPage(Context context, String title) {

    }
    */
}
