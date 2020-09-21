package com.varlor.dictionarydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView listView ;
    private EditText edSearch;
    private ImageButton btnSearch;
    private Button btn_add;
    private DBOpenHelper dbOpenHelper;//声明DBOpenHelper对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbOpenHelper=new DBOpenHelper(MainActivity.this,
                "db_dict",null,1);//实例化DBOpenHelper对象，用来创建数据库

        listView = findViewById(R.id.result_listView);
        edSearch = findViewById(R.id.search_et);
        btnSearch = findViewById(R.id.search_btn);
        btn_add = findViewById(R.id.btn_add);
        btnSearch.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_btn:

                String key = edSearch.getText().toString();//获取要查询的单词
                Cursor cursor = dbOpenHelper.getReadableDatabase().query(
                        "tb_dict",null,"word=?",new String[]{key},null,null,null
                );//查询操作
                ArrayList<Map<String, String>> resultList = new ArrayList<>();
                while(cursor.moveToNext()){
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("word",cursor.getString(1));
                    map.put("interpret",cursor.getString(2));
                    resultList.add(map);
                }
                if (resultList==null||resultList.size()==0){//如果数据库中没有数据
                    Toast.makeText(MainActivity.this,"很遗憾，没有相关记录!",Toast.LENGTH_SHORT).show();
                }else{
                    SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this,
                            resultList, R.layout.result_main,new String[]{"word","interpret"},new int[]{
                                    R.id.result_word,R.id.result_interpret
                    });
                    listView.setAdapter(simpleAdapter);
                }


                break;
            case R.id.btn_add:
                //单击添加生词按钮，跳转到添加生词界面
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbOpenHelper!=null){
            dbOpenHelper.close();
        }
    }
}