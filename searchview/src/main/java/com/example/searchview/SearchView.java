package com.example.searchview;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class SearchView extends LinearLayout implements View.OnClickListener {
    private Context context;
    // 搜索框组件
    private EditText et_search; // 搜索按键
    private TextView tv_clear;  // 删除搜索记录按键
    private LinearLayout search_block,bottom,history_pop_up,mainarea;// 搜索框布局,底部选项框
    private TextView searchBack; // 返回按键

    // ListView列表 & 适配器
    private SearchListView listView;
    private BaseAdapter adapter;
    private List<String> list;
    private PopAdapter popAdapter;
    private ListView listView2;
    private View popview;
    private TextView t1,t2,t3,t4;
    private MyHistoryAdapter myHistoryAdapter;
    static RecyclerView recyclerView;
    static List<PageHistory> histories;
    URLSQLiteAdapter urlsqLiteAdapter;

    // 数据库变量
    // 用于存放历史搜索记录
    private RecordSQLiteOpenHelper helper ;
    private SQLiteDatabase db;

    // 回调接口
    private  ICallBack mCallBack;// 搜索按键回调接口
    private  bCallBack bCallBack; // 返回按键回调接口
    // 自定义属性设置
    // 1. 搜索字体属性设置：大小、颜色 & 默认提示
    private Float textSizeSearch;
    private int textColorSearch;
    private String textHintSearch;

    // 2. 搜索框设置：高度 & 颜色
    private int searchBlockHeight;
    private int searchBlockColor;


    /**
     * 构造函数
     * 作用：对搜索框进行初始化
     */
    public SearchView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(context, attrs); // ->>关注a
        init();// ->>关注b
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttrs(context, attrs);
        init();
    }
    /**
     * 关注a
     * 作用：初始化自定义属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {

        // 控件资源名称
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Search_View);

        // 搜索框字体大小（dp）
        textSizeSearch = typedArray.getDimension(R.styleable.Search_View_textSizeSearch, 20);

        // 搜索框字体颜色（使用十六进制代码，如#333、#8e8e8e）
        int defaultColor = context.getResources().getColor(R.color.colorText); // 默认颜色 = 灰色
        textColorSearch = typedArray.getColor(R.styleable.Search_View_textColorSearch, defaultColor);

        // 搜索框提示内容（String）
        textHintSearch = typedArray.getString(R.styleable.Search_View_textHintSearch);

        // 搜索框高度
        searchBlockHeight = typedArray.getInteger(R.styleable.Search_View_searchBlockHeight, 0);

        // 释放资源
        typedArray.recycle();
    }

    private String[] getCurrentUrl(){
        SharedPreferences sp=context.getSharedPreferences("olddata",Context.MODE_PRIVATE);
        String url=sp.getString("oldurl",null);
        String title=sp.getString("oldtitle",null);
        String info[]={url,title};
        return info;
    }
    /**
     * 关注b
     * 作用：初始化搜索框
     */
    private void init(){

        // 1. 初始化UI组件->>关注c
        initView();
        et_search.setText(getCurrentUrl()[0]);
        // 2. 实例化数据库SQLiteOpenHelper子类对象
        helper = new RecordSQLiteOpenHelper(context);

        // 3. 第1次进入时查询所有的历史搜索记录
        queryData("");

        /**
         * "清空搜索历史"按钮
         */
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("确定删除搜索历史吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 清空数据库->>关注2
                                deleteData();
                                // 模糊搜索空字符 = 显示所有的搜索历史（此时是没有搜索记录的）
                                queryData("");
                            }
                        })
                       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               dialogInterface.cancel();
                           }
                       }).show();

            }
        });

        /**
         * 监听输入键盘更换后的搜索按键
         * 调用时刻：点击键盘上的搜索键时
         */

        et_search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    // 1. 点击搜索按键后，根据输入的搜索字段进行查询
                    // 注：由于此处需求会根据自身情况不同而不同，所以具体逻辑由开发者自己实现，此处仅留出接口
                    if (!(mCallBack == null)){
                        mCallBack.SearchAciton(et_search.getText().toString());
                    }
                    // 2. 点击搜索键后，对该搜索字段在数据库是否存在进行检查（查询）->> 关注1
                    boolean hasData = hasData(et_search.getText().toString().trim());
                    // 3. 若存在，则不保存；若不存在，则将该搜索字段保存（插入）到数据库，并作为历史搜索记录
                    if (!hasData&&!et_search.getText().toString().equals("")) {
                        insertData(et_search.getText().toString().trim());
                        queryData("");
                    }
                }
                return false;
            }
        });


        /**
         * 搜索框的文本变化实时监听
         */
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            // 输入文本后调用该方法
            @Override
            public void afterTextChanged(Editable s) {
                // 每次输入后，模糊查询数据库 & 显示
                // 注：若搜索框为空,则模糊搜索空字符 = 显示所有的搜索历史
                String tempLink = et_search.getText().toString();
                queryData(tempLink); // ->>关注1

            }
        });


        /**
         * 搜索记录列表（ListView）监听
         * 即当用户点击搜索历史里的字段后,会直接将结果当作搜索字段进行搜索
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 获取用户点击列表里的文字,并自动填充到搜索框内
                TextView textView = view.findViewById(R.id.search_url);
                String name = textView.getText().toString();
                et_search.setText(name);
                if (!(mCallBack == null)){
                    mCallBack.SearchAciton(et_search.getText().toString());
                }

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view1, int position, long id) {
                popview = LayoutInflater.from(getContext()).inflate(R.layout.pop_wordbook, null);
                list=new ArrayList<>();
                list.add("删除");
                list.add("取消");
                listView2=popview.findViewById(R.id.lv_wordbook);
                popAdapter=new PopAdapter();
                listView2.setAdapter(popAdapter);
                final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.showAsDropDown(view1,600,-20,50);
                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(list.get(i).equals("删除")){
                            popupWindow.dismiss();
                            TextView textView = (TextView) view1.findViewById(R.id.search_url);
                            String name = textView.getText().toString();
                            deletePiece(name);
                            queryData("");
                            Toast.makeText(getContext(),"已删除",Toast.LENGTH_SHORT).show();
                        }else {
                            popupWindow.dismiss();
                        }
                    }
                });
                return true;
            }
        });

        /**
         * 点击返回按键后的事件
         */
        searchBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(bCallBack == null)){
                    bCallBack.BackAciton();
                }
            }
        });

    }

    /**
     * 关注c：绑定搜索框xml视图
     */
    private void initView(){

        // 1. 绑定R.layout.search_layout作为搜索框的xml文件
        LayoutInflater.from(context).inflate(R.layout.search_layout,this);

        // 2. 绑定搜索框EditText
        et_search = (EditText) findViewById(R.id.et_search2);
        et_search.setTextSize(textSizeSearch);
        et_search.setTextColor(textColorSearch);
        et_search.setHint(textHintSearch);

        // 3. 搜索框背景颜色
        search_block = (LinearLayout)findViewById(R.id.search_block);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) search_block.getLayoutParams();
        params.height = searchBlockHeight;
        search_block.setBackgroundColor(searchBlockColor);
        search_block.setLayoutParams(params);

        // 4. 历史搜索记录 = ListView显示
        listView = (SearchListView) findViewById(R.id.listView);

        // 5. 删除历史搜索记录 按钮
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        tv_clear.setVisibility(INVISIBLE);
        // 6. 返回按键
        searchBack = (TextView) findViewById(R.id.search_back);
        // 7.底部选项栏
        bottom=(LinearLayout) findViewById(R.id.bottom_hint);
        recyclerView=(RecyclerView) findViewById(R.id.rv_history);
        // 8.历史记录弹出窗口
        history_pop_up=(LinearLayout) findViewById(R.id.history_pop_up);
        //9.外布局
        mainarea=(LinearLayout) findViewById(R.id.main_seanchview);
        t1=(TextView) findViewById(R.id.urlhead);
        t2=(TextView)findViewById(R.id.urlhead2);
        t3=(TextView)findViewById(R.id.urlhead3);
        t4=(TextView)findViewById(R.id.usual);
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        et_search.setOnClickListener(this);
        mainarea.setOnClickListener(this);
        //设置历史记录适配器
        myHistoryAdapter=new MyHistoryAdapter();
        //设置历史记录数据库类适配器
        urlsqLiteAdapter=new URLSQLiteAdapter(getContext());
        myHistoryAdapter.setiCallBack(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                if(mCallBack!=null){
                    mCallBack.SearchAciton(string);
                }
            }
        });

    }

    /**
     * 关注1
     * 模糊查询数据 & 显示到ListView列表上
     */
    private void queryData(String tempLink) {
        // 1. 模糊搜索
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,link,title from records where link like '%" + tempLink + "%' order by id desc ", null);
        // 2. 创建adapter适配器对象 & 装入模糊搜索的结果0
        adapter = new SimpleCursorAdapter(context, R.layout.search_list_item, cursor, new String[] { "link","title" },
                new int[] { R.id.search_url,R.id.searh_title }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        System.out.println(cursor.getCount());
        // 当输入框为空 & 数据库中有搜索记录时，显示 "删除搜索记录"按钮

        if (tempLink.equals("") && cursor.getCount() != 0){
            tv_clear.setVisibility(VISIBLE);
        }
        else {
            tv_clear.setVisibility(INVISIBLE);
        };

    }
    private void deletePiece(String tempLink){
        db=helper.getWritableDatabase();
        db.execSQL("delete from records where link ='"+tempLink+"'");
        db.close();
    }

    /**
     * 关注2：清空数据库
     */
    private void deleteData() {

        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
        tv_clear.setVisibility(INVISIBLE);
    }

    /**
     *
     * 检查数据库中是否已经有该搜索记录
     */

    private boolean hasData(String tempLink) {
        // 从数据库中Record表里找到name=tempName的id
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,link from records where link =?", new String[]{tempLink});
        //  判断是否有下一个
        return cursor.moveToNext();

    }

    /**
     *
     * 插入数据到数据库，即写入搜索字段到历史搜索记录
     */
    private void insertData(String tempLink) {
        String webTitle=urlsqLiteAdapter.queryByUrl(tempLink);
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(link,title) values('" + tempLink + "','"+webTitle+"')");
        db.close();
    }
    /**
     * 点击键盘中搜索键后的操作，用于接口回调
     */
    public void setOnClickSearch(ICallBack mCallBack){
        this.mCallBack = mCallBack;

    }
    /**
     * 点击返回后的操作，用于接口回调
     */
    public void setOnClickBack(bCallBack bCallBack){
        this.bCallBack = bCallBack;

    }
    /**
     * 底部选项栏的属性
     */
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.urlhead) {
            et_search.append("www.");
        }else if(i==R.id.urlhead2){
            et_search.append(".com");
        }else if(i==R.id.urlhead3){
            et_search.append("http://");
        }else if(i==R.id.usual){
            histories=urlsqLiteAdapter.querAllHistory();
            recyclerView.setAdapter(myHistoryAdapter);
            LinearLayoutManager manager=new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            TranslateAnimation translateAnimation=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f);
            translateAnimation.setDuration(300);
            if(history_pop_up.getVisibility()==GONE) {
                history_pop_up.startAnimation(translateAnimation);
                history_pop_up.setVisibility(View.VISIBLE);
                listView.setVisibility(GONE);
                tv_clear.setVisibility(GONE);
            }else if(history_pop_up.getVisibility()==VISIBLE){
                history_pop_up.setVisibility(View.GONE);
                listView.setVisibility(VISIBLE);

            }
        }else if(i==R.id.et_search2){
            if(bottom.getVisibility()==GONE) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                alphaAnimation.setDuration(300);
                bottom.startAnimation(alphaAnimation);
                bottom.setVisibility(View.VISIBLE);
            }
        }else if(i==R.id.main_seanchview){
            bottom.setVisibility(View.GONE);
            history_pop_up.setVisibility(View.GONE);
        }
    }

    class PopAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            popview=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pop_wordbook_item,null);
            TextView textView=popview.findViewById(R.id.tv_delete);
            textView.setText(list.get(i));
            return popview;
        }
    }

}
