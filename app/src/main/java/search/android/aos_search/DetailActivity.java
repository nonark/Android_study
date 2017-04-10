package search.android.aos_search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import search.android.adapter.SummaryPageAdapter;
import search.android.customview.StatusBar;
import search.android.task.WikiPageSearchTask;
import search.android.vo.SummaryPage;

/**
 * Created by nhnent on 2017. 4. 5..
 */

public class DetailActivity extends Activity {

    StatusBar statusBar;
    RecyclerView wikiPagesView;
    SummaryPageAdapter adapter;
    Context context;

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
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("Search", searchText);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_hold, R.anim.left_slide);
            }
        });

        //헤더를 클릭할 경우 Webview 페이지로 이동
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
        wikiPagesView.setItemAnimator(new DefaultItemAnimator());

        context = this;
        statusBar = (StatusBar) findViewById(R.id.statusBar);
        // 뒤로가기 버튼 클릭시 finish 호출
        statusBar.setOnBackButtonClickedListener(new StatusBar.OnStatusBarClickedListener() {
            @Override
            public void onStatusButtonClicked() {
                finish();
            }
        });

        // X 버튼을 누르면 현재까지 생성한 모든 Activity를 제거하고 MainActivity를 상단에 출력
        statusBar.setOnCloseButtonClickedListener(new StatusBar.OnStatusBarClickedListener() {
            @Override
            public void onStatusButtonClicked() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        //Intent 내의 값이 null이 아니면 주어진 검색어에 해당하는 리스트 출력
        Intent intent = getIntent();
        if (intent != null) {
            String searchText = intent.getStringExtra("Search");
            statusBar.setTitle(searchText);
            WikiPageSearchTask task = new WikiPageSearchTask(context, adapter);
            task.execute(searchText);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_hold, R.anim.right_slide);
    }
}
