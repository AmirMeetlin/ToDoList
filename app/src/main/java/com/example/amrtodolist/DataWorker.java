package com.example.amrtodolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amrtodolist.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataWorker extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    DataWorker(Context context) {
        super(context,"toDoDataBase",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE toDo(id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT, status INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS toDo");
        onCreate(db);
    }

    public void openDatabase(){
        db= this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put("task",task.getTask());
        cv.put("status",0);
        db.insert("toDo",null,cv);
    }

    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cursor=null;
        db.beginTransaction();
        try{
            cursor=db.query("toDo",null,null,null,null,null,null,null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        ToDoModel task=new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        task.setTask(cursor.getString(cursor.getColumnIndex("task")));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                        taskList.add(task);
                    }while (cursor.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cursor.close();
        }
        return taskList;
    }

    public void updateStatus(int id,int status){
        ContentValues cv = new ContentValues();
        cv.put("status",status);
        db.update("toDo",cv,"id=?",new String[]{String.valueOf(id)});
    }
    public void updateTask(int id,String task){
        ContentValues cv=new ContentValues();
        cv.put("task",task);
        db.update("toDo",cv,"id=?",new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete("toDo","id=?",new String[]{String.valueOf(id)});
    }

}
