package search.android.aos_search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import search.android.customview.SearchBar;

public class MainActivity extends AppCompatActivity {

    SearchBar searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = (SearchBar) findViewById(R.id.searchBar);
        searchBar.setOnSearchBarClickedListener(new SearchBar.OnSearchBarClickedListener() {
            @Override
            public void onSearchButtonClicked(String searchText) {
                Toast.makeText(getApplicationContext(), searchText, Toast.LENGTH_LONG).show();
            }
        });
    }
}
