package activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.excelergo.niceexp.R;
import com.example.searchview.ICallBack;
import com.example.searchview.SearchView;
import com.example.searchview.bCallBack;

import static fragment.Fragment2.webView;

public class SearchHistoryActivity extends AppCompatActivity {
    private SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);
        searchView=(SearchView)findViewById(R.id.search_view);
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                String searchUrl;
                if(string.startsWith("http://")||string.startsWith("https://")){
                    searchUrl=string;
                }else {
                    searchUrl="http://"+string;
                }

                webView.loadUrl(searchUrl);
                finish();
            }
        });

        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });

    }

}
