package search.android.task;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import search.android.tools.WikiPageFinder;
import search.android.vo.SummaryPage;

/**
 * Created by nhnent on 2017. 4. 7..
 */

public class PageSearchTask extends AsyncTask<String, Void, Object> {

    private OnPageSearchTaskListener onPageSearchTaskListener;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(onPageSearchTaskListener != null) {
            onPageSearchTaskListener.onPreExecuted();
        }
    }

    @Override
    protected Object doInBackground(String... params) {
        List<SummaryPage> wikiPages = WikiPageFinder.findRelatedPages(params[0]);

        if(wikiPages == null) {
            wikiPages = new ArrayList<>();
        }

        SummaryPage summaryPage = WikiPageFinder.getSummaryPage(params[0]);

        if(summaryPage != null) {
            wikiPages.add(0, summaryPage);
        }

        return wikiPages;
    }



    @Override
    protected void onPostExecute(Object pages) {
        super.onPostExecute(pages);
        if(onPageSearchTaskListener != null) {
            onPageSearchTaskListener.onPostExecuted(pages);
        }
    }

    @Override
    protected void onCancelled() {
        //super.onCancelled();
        onPageSearchTaskListener.onCancelled();
    }

    public void setOnPageSearchTaskListener(OnPageSearchTaskListener onPageSearchTaskListener) {
        this.onPageSearchTaskListener = onPageSearchTaskListener;
    }

    public interface OnPageSearchTaskListener{
        void onPreExecuted();
        void onPostExecuted(Object object);
        void onCancelled();
    }
}