package com.example.mgutierrezplaza.bbdd;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String name, byte[] image, byte[] image2, byte[] image3, byte[] image4, String audio){

        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO PRUEBA VALUES (NULL,?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindBlob(2, image);
        statement.bindBlob(3, image2);
        statement.bindBlob(4, image3);
        statement.bindBlob(5, image4);
        statement.bindString(6, audio);

        statement.executeInsert();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
