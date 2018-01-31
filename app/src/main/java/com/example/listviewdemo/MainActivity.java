package com.example.listviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> l;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private int page = 3;
    private MyAdapter adapter;
    private int lastVisibleItem = 0;
    int myState;
    int count = 30;
    int first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l = new ArrayList<>();
        initView();
        initData(page);
        initRecyclerView();
        initListener();
    }

    private void initRecyclerView() {
        adapter = new MyAdapter(this, l);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.HORIZONTAL));
    }


    private void initListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                boolean down = recyclerView.canScrollVertically(-1);//表示手指向下滑动
                boolean up = recyclerView.canScrollVertically(1);//表示手指向上滑动

                if (myState == 2 && newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    l.clear();
                    page++;
                    initData(page);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "我是重新获取的数据（上拉）。。。", Toast.LENGTH_SHORT).show();
                    myState = 0;
                }

                if (adapter.getItemCount() < 30) {
                    myState = 1;
                }

                //上一页
                if (myState == 1 && newState == RecyclerView.SCROLL_STATE_IDLE && first == 0) {
                    page--;
                    if (page <= 0) {
                        Toast.makeText(MainActivity.this, "已经是第一页数据了", Toast.LENGTH_SHORT).show();
                    } else {
                        l.clear();
                        initData(page);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "我是重新获取的数据（下拉）。。。", Toast.LENGTH_SHORT).show();
                    }
                    myState = 0;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                first = linearLayoutManager.findFirstVisibleItemPosition();
                if (dy > 0) {
                    myState = 2;
                } else if (dy < 0) {
                    myState = 1;
                }
            }
        });
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
    }

    private void initData(int page) {
        for (int i = (page - 1) * count; i < page * count; i++) {
            l.add(i + " : item");
        }
    }
}
