package search.android.aos_search;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import search.android.adapter.SummaryPageAdapter;
import search.android.customview.SearchBar;
import search.android.tools.WikiPageFinder;
import search.android.vo.SummaryPage;

public class MainActivity extends AppCompatActivity {

    SearchBar searchBar;
    RecyclerView wikiPagesView;
    SummaryPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        adapter = new SummaryPageAdapter(wikiPages, R.layout.search_item);
        adapter.setRelatedListner(new SummaryPageAdapter.OnRecyclerViewItemClickedListener() {
            @Override
            public void onItemClicked(String searchText) {
                //oast.makeText(getApplicationContext(), searchText, Toast.LENGTH_SHORT).show();

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
