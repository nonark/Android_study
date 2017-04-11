package search.android.aos_search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import search.android.adapter.SummaryPageAdapter;
import search.android.customview.SearchBar;
import search.android.task.WikiPageSearchTask;
import search.android.vo.SummaryPage;

public class MainActivity extends Activity {

    SearchBar searchBar;
    RecyclerView wikiPagesView;
    SummaryPageAdapter adapter;
    LinearLayout rootLayout;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wikiPagesView = (RecyclerView) findViewById(R.id.wikiPages);
        List<SummaryPage> wikiPages = new ArrayList<>();

        adapter = new SummaryPageAdapter(wikiPagesView.getContext(), wikiPages, R.layout.search_item_header ,R.layout.search_item);

        adapter.setRelatedListner(new SummaryPageAdapter.OnRecyclerViewItemClickedListener() {
            @Override
            public void onItemClicked(String searchText) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("Search", searchText);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_hold, R.anim.left_slide);
            }
        });

        adapter.setHeaderItemClickedLListner(new SummaryPageAdapter.OnRecyclerViewItemClickedListener() {
            @Override
            public void onItemClicked(String searchText) {
                Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
                intent.putExtra("Search", searchText);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_hold, R.anim.left_slide);
            }
        });

        wikiPagesView.setAdapter(adapter);
        wikiPagesView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        context = this;
        searchBar = (SearchBar) findViewById(R.id.searchBar);

        searchBar.setOnSearchBarClickedListener(new SearchBar.OnSearchBarClickedListener() {
            @Override
            public void onSearchButtonClicked(final String searchText) {
                WikiPageSearchTask task = new WikiPageSearchTask(context, adapter);
                task.execute(searchText);
            }
        });

        rootLayout = (LinearLayout) findViewById(R.id.rootView);
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBar.requestFocus();
            }
        });
        rootLayout.performClick();
    }
}
