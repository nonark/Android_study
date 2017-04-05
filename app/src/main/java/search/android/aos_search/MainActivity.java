package search.android.aos_search;

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


                /*
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        //MalforemdURLException은 IOException의 서브클래스이므로 분리한다
                        try {
                            URL wikiUrl = new URL("https://www.mediawiki.org/api/rest_v1/page/html/"+searchText); //MalformedException
                            HttpsURLConnection wikiConnection = (HttpsURLConnection) wikiUrl.openConnection(); //IOException
                            wikiConnection.setRequestProperty("Accept", "text/html");
                            wikiConnection.setRequestProperty("charset", "utf-8");

                            if(wikiConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                                Log.d("res","OK : " +searchText);
                                //InputStream responseBody  = wikiConnection.getInputStream();
                                //InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                                //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("res","NO : " +searchText + ", " + wikiConnection.getResponseCode());
                                //Toast.makeText(getApplicationContext(), "검색결과가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }


                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });

                */

                List<SummaryPage> wikiPages = new ArrayList<>();

                wikiPages.add(new SummaryPage("", "a", "a1"));
                wikiPages.add(new SummaryPage("", "bb", "bb22"));
                wikiPages.add(new SummaryPage("", "ccc", "ccc333"));
                wikiPages.add(new SummaryPage("", "dddd", "dddd4444"));
                wikiPages.add(new SummaryPage("", "eeeee", "eeeee55555"));
                wikiPages.add(new SummaryPage("", "a", "a1"));

                adapter.setWikiPages(wikiPages);
                adapter.notifyDataSetChanged();

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
}
