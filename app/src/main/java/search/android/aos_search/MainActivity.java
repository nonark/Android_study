package search.android.aos_search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import search.android.adapter.SummaryPageAdapter;
import search.android.customview.SearchBar;
import search.android.tools.WikiPageFinder;
import search.android.vo.SummaryPage;

public class MainActivity extends Activity {

    SearchBar searchBar;
    RecyclerView wikiPagesView;
    SummaryPageAdapter adapter;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");

        searchBar = (SearchBar) findViewById(R.id.searchBar);
        searchBar.setOnSearchBarClickedListener(new SearchBar.OnSearchBarClickedListener() {
            @Override
            public void onSearchButtonClicked(final String searchText) {

                WikiPageSearchTask task = new WikiPageSearchTask(adapter);
                task.execute(searchText);

            }
        });

        wikiPagesView = (RecyclerView) findViewById(R.id.wikiPages);

        List<SummaryPage> wikiPages = new ArrayList<>();

        adapter = new SummaryPageAdapter(wikiPages, R.layout.search_item_header ,R.layout.search_item);
        adapter.setRelatedListner(new SummaryPageAdapter.OnRecyclerViewItemClickedListener() {
            @Override
            public void onItemClicked(String searchText) {
                //Toast.makeText(getApplicationContext(), searchText, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("Search", searchText);
                startActivity(intent);
            }
        });
        adapter.setHeaderItemClickedLListner(new SummaryPageAdapter.OnRecyclerViewItemClickedListener() {
            @Override
            public void onItemClicked(String searchText) {
                Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
                intent.putExtra("Search", searchText);
                startActivity(intent);
            }
        });

        wikiPagesView.setAdapter(adapter);
        wikiPagesView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        wikiPagesView.setItemAnimator(new DefaultItemAnimator());


    }

    private class WikiPageSearchTask extends AsyncTask<String, Void, Void> {

        private SummaryPageAdapter summaryPageAdapter;
        private List<SummaryPage> wikiPages;

        public WikiPageSearchTask(SummaryPageAdapter summaryPageAdapter) {
            super();
            this.summaryPageAdapter = summaryPageAdapter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            wikiPages = null;
        }

        @Override
        protected Void doInBackground(String... params) {
            wikiPages = WikiPageFinder.findRelatedPages(params[0]);

            if(wikiPages == null) {
                wikiPages = new ArrayList<>();
            }

            SummaryPage summaryPage = WikiPageFinder.findSummaryPage(params[0]);

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
                Toast.makeText(getApplicationContext(), "검색어를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
