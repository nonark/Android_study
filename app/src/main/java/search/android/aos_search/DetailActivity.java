package search.android.aos_search;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import search.android.adapter.SummaryPageAdapter;
import search.android.customview.StatusBar;
import search.android.tools.WikiPageFinder;
import search.android.vo.SummaryPage;

/**
 * Created by nhnent on 2017. 4. 5..
 */

public class DetailActivity extends AppCompatActivity {

    StatusBar statusBar;
    RecyclerView wikiPagesView;
    SummaryPageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        statusBar = (StatusBar) findViewById(R.id.statusBar);
        statusBar.setOnBackButtonClickedListener(new StatusBar.OnStatusBarClickedListener() {
            @Override
            public void onStatusButtonClicked() {
                finish();
            }
        });

        statusBar.setOnCloseButtonClickedListener(new StatusBar.OnStatusBarClickedListener() {
            @Override
            public void onStatusButtonClicked() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        wikiPagesView = (RecyclerView) findViewById(R.id.wikiPages);

        List<SummaryPage> wikiPages = new ArrayList<>();

        adapter = new SummaryPageAdapter(wikiPages, R.layout.search_item);
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

        Intent intent = getIntent();
        if (intent != null) {
            String searchText = intent.getStringExtra("Search");
            statusBar.setTitle(searchText);
            WikiPageSearchTask task = new WikiPageSearchTask(adapter);
            task.execute(searchText);
        }
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
            wikiPages = null;
        }

        @Override
        protected Void doInBackground(String... params) {
            wikiPages = WikiPageFinder.findRelatedPages(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(wikiPages != null) {
                summaryPageAdapter.setWikiPages(wikiPages);
            } else {
                summaryPageAdapter.setWikiPages(new ArrayList<SummaryPage>());
            }
            summaryPageAdapter.notifyDataSetChanged();
        }
    }
}
