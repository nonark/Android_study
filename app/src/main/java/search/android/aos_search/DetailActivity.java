package search.android.aos_search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import search.android.adapter.SummaryPageAdapter;
import search.android.customview.StatusBar;
import search.android.navigation.PageNavigation;
import search.android.task.AsyncTaskCancelTimer;
import search.android.task.PageSearchTask;
import search.android.tools.WikiPageFinder;
import search.android.vo.SummaryPage;

/**
 * Created by nhnent on 2017. 4. 5..
 */

public class DetailActivity extends Activity {

    private StatusBar statusBar;
    private RecyclerView wikiPagesView;
    private SummaryPageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        wikiPagesView = (RecyclerView) findViewById(R.id.wikiPages);

        List<SummaryPage> wikiPages = new ArrayList<>();

        adapter = new SummaryPageAdapter(wikiPagesView.getContext(), wikiPages, R.layout.search_item_header ,R.layout.search_item);

        //연관검색어를 클릭할 경우 해당 검색어와 연관된 페이지로 이동
        adapter.setRelatedListner(new SummaryPageAdapter.OnRecyclerViewItemClickedListener() {
            @Override
            public void onItemClicked(String searchText) {
                Intent intent = PageNavigation.moveSearchPage(getBaseContext(), searchText);
                startActivityForResult(intent, PageNavigation.OK);
                overridePendingTransition(R.anim.anim_hold, R.anim.left_slide);
            }
        });

        //헤더를 클릭할 경우 Webview 페이지로 이동
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

        statusBar = (StatusBar) findViewById(R.id.statusBar);
        // 뒤로가기 버튼 클릭시 finish 호출
        statusBar.setOnBackButtonClickedListener(new StatusBar.OnStatusBarClickedListener() {
            @Override
            public void onStatusButtonClicked() {
                setResult(PageNavigation.OK);
                finish();
            }
        });

        // X 버튼을 누르면 현재까지 생성한 모든 Activity를 제거하고 MainActivity를 상단에 출력
        statusBar.setOnCloseButtonClickedListener(new StatusBar.OnStatusBarClickedListener() {
            @Override
            public void onStatusButtonClicked() {
                Intent intent = PageNavigation.moveMainPage(getBaseContext());
                startActivity(intent);
            }
        });

        //Intent 내의 값이 null이 아니면 주어진 검색어에 해당하는 리스트 출력
        Intent intent = getIntent();
        if (intent != null) {
            String searchText = intent.getStringExtra("Search");
            statusBar.setTitle(searchText);
            final PageSearchTask task = new PageSearchTask(new WikiPageFinder());
            task.setOnPageSearchTaskListener(new PageSearchTask.OnPageSearchTaskListener() {

                ProgressDialog dialog = new ProgressDialog(DetailActivity.this);

                @Override
                public void onPreExecuted() {
                    dialog.setMessage("Loding...");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if(task.getStatus() != AsyncTask.Status.FINISHED) {
                                task.cancel(true);
                                setResult(PageNavigation.REQUEST_CANCLE);
                                finish();
                            }
                        }
                    });
                    dialog.show();
                }

                @Override
                public void onProgressUpdate(String str) {
                    dialog.setMessage(str);
                }

                @Override
                public void onPostExecuted(Object object) {
                    List<SummaryPage> wikiPages = (List<SummaryPage>) object;
                    adapter.setWikiPages(wikiPages);
                    adapter.notifyDataSetChanged();
                    dialog.cancel();
                    if(wikiPages.size() == 0) {
                        setResult(PageNavigation.NO_SEARCH_RESULT);
                        finish();
                    }
                }

                @Override
                public void onCancelled() {
                    if(dialog.isShowing()) {
                        dialog.cancel();
                        setResult(PageNavigation.TIME_OUT);
                        finish();
                    }
                }
            });

            new AsyncTaskCancelTimer(task, 10000, 1000, true).start();
            task.execute(searchText);
        } else {
            setResult(PageNavigation.NO_INTENT);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != PageNavigation.OK) {
            Toast.makeText(getBaseContext(), PageNavigation.statusMessage(resultCode), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_hold, R.anim.right_slide);
    }
}
