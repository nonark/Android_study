package search.android.task;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import search.android.tools.PageFinder;
import search.android.vo.SummaryPage;

/**
 * Created by nhnent on 2017. 4. 7..
 */

public class PageSearchTask extends AsyncTask<String, String, Object> {

    private OnPageSearchTaskListener onPageSearchTaskListener;
    private PageFinder pageFinder;

    public PageSearchTask(PageFinder pageFinder) {
        this.pageFinder = pageFinder;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(onPageSearchTaskListener != null) {
            onPageSearchTaskListener.onPreExecuted();
        }
    }

    @Override
    protected Object doInBackground(String... params) {
        publishProgress("검색어 찾는 중...(1/2)");
        SummaryPage summaryPage = pageFinder.getSummaryPage(params[0]);

        publishProgress("연관 검색어 찾는 중...(2/2)");
        List<SummaryPage> wikiPages = pageFinder.findRelatedPages(params[0]);

        if(wikiPages == null) {
            wikiPages = new ArrayList<>();
        }

        if(summaryPage != null) {
            wikiPages.add(0, summaryPage);
        }

        return wikiPages;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if(onPageSearchTaskListener != null) {
            onPageSearchTaskListener.onProgressUpdate(values[0]);
        }
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
        if(onPageSearchTaskListener != null) {
            onPageSearchTaskListener.onCancelled();
        }
    }

    public void setOnPageSearchTaskListener(OnPageSearchTaskListener onPageSearchTaskListener) {
        this.onPageSearchTaskListener = onPageSearchTaskListener;
    }

    public interface OnPageSearchTaskListener{
        void onPreExecuted();
        void onPostExecuted(Object object);
        void onProgressUpdate(String str);
        void onCancelled();
    }
}