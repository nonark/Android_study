package search.android.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import search.android.adapter.SummaryPageAdapter;
import search.android.tools.WikiPageFinder;
import search.android.vo.SummaryPage;

/**
 * Created by nhnent on 2017. 4. 7..
 */

public class WikiPageSearchTask extends AsyncTask<String, Void, Void> {

    private SummaryPageAdapter summaryPageAdapter;
    private List<SummaryPage> wikiPages;
    private ProgressDialog dialog;
    private Context context;

    public WikiPageSearchTask(Context context, SummaryPageAdapter summaryPageAdapter) {
        super();
        this.summaryPageAdapter = summaryPageAdapter;
        this.context = context;
        this.dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Loding...");
        dialog.show();
        wikiPages = null;
    }

    @Override
    protected Void doInBackground(String... params) {
        wikiPages = WikiPageFinder.findRelatedPages(params[0]);

        if(wikiPages == null) {
            wikiPages = new ArrayList<>();
        }

        SummaryPage summaryPage = WikiPageFinder.getSummaryPage(params[0]);

        if(summaryPage != null) {
            wikiPages.add(0, summaryPage);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        summaryPageAdapter.setWikiPages(wikiPages);
        summaryPageAdapter.notifyDataSetChanged();
        dialog.cancel();
        if(wikiPages.size() == 0) {
            Toast.makeText(context, "검색어를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}