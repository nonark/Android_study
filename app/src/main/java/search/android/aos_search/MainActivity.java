package search.android.aos_search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import search.android.adapter.SummaryPageAdapter;
import search.android.customview.SearchBar;
import search.android.navigation.PageNavigation;
import search.android.task.AsyncTaskCancelTimer;
import search.android.task.PageSearchTask;
import search.android.tools.WikiPageFinder;
import search.android.vo.SummaryPage;

public class MainActivity extends Activity {

    private SearchBar searchBar;
    private RecyclerView wikiPagesView;
    private SummaryPageAdapter adapter;
    private LinearLayout rootLayout;

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
                Intent intent = PageNavigation.moveSearchPage(getBaseContext(), searchText);
                startActivityForResult(intent, PageNavigation.OK);
                overridePendingTransition(R.anim.anim_hold, R.anim.left_slide);
            }
        });

        adapter.setHeaderItemClickedLListner(new SummaryPageAdapter.OnRecyclerViewItemClickedListener() {
            @Override
            public void onItemClicked(String searchText) {
                Intent intent = PageNavigation.moveWebviewPage(getBaseContext(), searchText);
                startActivityForResult(intent, PageNavigation.OK);
                overridePendingTransition(R.anim.anim_hold, R.anim.left_slide);
            }
        });

        wikiPagesView.setAdapter(adapter);
        wikiPagesView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        searchBar = (SearchBar) findViewById(R.id.searchBar);
        searchBar.setOnSearchBarClickedListener(new SearchBar.OnSearchBarClickedListener() {
            @Override
            public void onSearchButtonClicked(final String searchText) {
                final PageSearchTask task = new PageSearchTask(new WikiPageFinder());
                task.setOnPageSearchTaskListener(new PageSearchTask.OnPageSearchTaskListener() {

                    ProgressDialog dialog = new ProgressDialog(MainActivity.this);

                    @Override
                    public void onPreExecuted() {
                        dialog.setMessage("Loding...");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if(task.getStatus() != AsyncTask.Status.FINISHED) {
                                    task.cancel(true);
                                    Toast.makeText(getBaseContext(), "요청을 취소했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        dialog.show();
                    }

                    @Override
                    public void onPostExecuted(Object object) {
                        List<SummaryPage> wikiPages = (List<SummaryPage>) object;
                        adapter.setWikiPages(wikiPages);
                        adapter.notifyDataSetChanged();
                        dialog.cancel();

                        if(wikiPages.size() == 0) {
                            Toast.makeText(getBaseContext(), "검색어를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onProgressUpdate(String str) {
                        dialog.setMessage(str);
                    }

                    @Override
                    public void onCancelled() {
                        if(dialog.isShowing()) {
                            dialog.cancel();
                            Toast.makeText(getBaseContext(), "요청 시간을 초과했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                new AsyncTaskCancelTimer(task, 10000, 1000, true).start();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != PageNavigation.OK) {
            Toast.makeText(getBaseContext(), PageNavigation.statusMessage(resultCode), Toast.LENGTH_SHORT).show();
        }
    }
}