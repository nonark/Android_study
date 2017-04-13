package search.android.tools;

import java.util.List;

import search.android.vo.SummaryPage;

/**
 * Created by nhnent on 2017. 4. 13..
 */

public interface PageFinder {
    SummaryPage getSummaryPage(String title);
    List<SummaryPage> findRelatedPages(String title);
    String getHtmlUrl(String title);
}
