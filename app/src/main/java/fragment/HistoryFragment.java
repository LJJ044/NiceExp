package fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.excelergo.niceexp.R;
import java.util.List;
import adapter.MyHistoryAdapter;
import adapter.URLSQLiteAdapter;
import entity.PageHistory;
import interfaces.GoBackAction;

/**
 * 收藏页面
 */
public class HistoryFragment extends Fragment {
    private MyHistoryAdapter myHistoryAdapter;
    public static RecyclerView recyclerView;
    public static List<PageHistory> histories;
    private URLSQLiteAdapter adapter;
    public HistoryFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history,container,false);
        recyclerView=view.findViewById(R.id.rv_history);
        adapter=new URLSQLiteAdapter(getContext());
        histories=adapter.querAllHistory();
        myHistoryAdapter=new MyHistoryAdapter(getContext());
        recyclerView.setAdapter(myHistoryAdapter);
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        myHistoryAdapter.setGoBackAction(new GoBackAction() {
            @Override
            public void goBack(int i) {
                getActivity().finish();
            }
        });
        return view;
    }


}
