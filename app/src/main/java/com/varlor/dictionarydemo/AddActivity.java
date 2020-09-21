package com.varlor.dictionarydemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etWord,etInterpret;
    private ImageButton btn_Save,btn_Cancel;
    private DBOpenHelper dbOpenHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        dbOpenHelper=new DBOpenHelper(AddActivity.this,
                "db_dict",null,1);//实例化DBOpenHelper对象
        etWord = findViewById(R.id.add_word);//获取添加单词编辑框
        etInterpret = findViewById(R.id.add_interpret);//获取添加解释的编辑框
        btn_Save = findViewById(R.id.save_btn);//获取保存按钮
        btn_Cancel = findViewById(R.id.cancel_btn1);//获取取消按钮
        btn_Save.setOnClickListener(this);
        btn_Cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_btn:
                String word = etWord.getText().toString();
                String interpret = etInterpret.getText().toString();
                if (word.equals("")||interpret.equals("")){
                    Toast.makeText(AddActivity.this,"填写的单词或解释为空！"
                    ,Toast.LENGTH_SHORT).show();
                }else{
                    insertData(dbOpenHelper.getReadableDatabase(),word,interpret);//插入生词
                    Toast.makeText(AddActivity.this,"添加生词成功！",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.cancel_btn1:
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
    //插入数据的方法
    private void insertData(SQLiteDatabase sqLiteDatabase,String word,String interpret){
        ContentValues contentValues = new ContentValues();
        contentValues.put("word",word);
        contentValues.put("detail",interpret);
        sqLiteDatabase.insert("tb_dict",null,contentValues);//执行插入操作
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbOpenHelper!=null){
            dbOpenHelper.close();
        }
    }
}
