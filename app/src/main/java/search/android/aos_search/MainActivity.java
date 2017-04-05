package search.android.aos_search;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


//                List<SummaryPage> wikiPages = new ArrayList<>();
/*
                SummaryPage summaryPage = WikiPageFinder.findSummaryPage(searchText);
                if(summaryPage != null) {
                    wikiPages.add(summaryPage);
                }
*/
/*
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        SummaryPage summaryPage = WikiPageFinder.findSummaryPage(searchText);
                        if(summaryPage != null) {
                            Log.d("Success", summaryPage.getTitle() + " : " + summaryPage.getSummary());
                        }

                        List<SummaryPage> summaryPages = WikiPageFinder.findRelatedPages(searchText);
                        if(summaryPages != null) {
                            for(SummaryPage summaryPage : summaryPages) {
                                Log.d("Success", summaryPage.getTitle() + " : " + summaryPage.getSummary());
                            }
                        }
                    }
                });
                */

/*
                wikiPages.add(new SummaryPage("", "a", "a1"));
                wikiPages.add(new SummaryPage("", "bb", "bb22"));
                wikiPages.add(new SummaryPage("", "ccc", "ccc333"));
                wikiPages.add(new SummaryPage("", "dddd", "dddd4444"));
                wikiPages.add(new SummaryPage("", "eeeee", "eeeee55555"));
                wikiPages.add(new SummaryPage("", "a", "a1"));

                adapter.setWikiPages(wikiPages);
                adapter.notifyDataSetChanged();
*/
                Toast.makeText(getApplicationContext(), searchText, Toast.LENGTH_LONG).show();



            }
        });

        wikiPagesView = (RecyclerView) findViewById(R.id.wikiPages);

        List<SummaryPage> wikiPages = new ArrayList<>();

        adapter = new SummaryPageAdapter(wikiPages, R.layout.search_item);
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
