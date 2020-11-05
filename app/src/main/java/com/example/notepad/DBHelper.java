package com.example.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "comment.db";
    private static final String TB_NAME = "comment";
    private static final String KIND_NAME = "KIND";

    public DBHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TB_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,COMMENT TEXT,TIME TIMESTAMP ,COLLECT TEXT,KIND TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + KIND_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,KIND TEXT)");
        //sqLiteDatabase.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public HashMap<String, String> add(HashMap<String, String> hm) {
        SQLiteDatabase wb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("COMMENT", hm.get("COMMENT"));
        hm.put("TIME", new Date().toString());
        values.put("TIME", hm.get("TIME"));
        values.put("COLLECT", hm.get("COLLECT"));
        values.put("KIND", hm.get("KIND"));
        wb.insert(TB_NAME, null, values);
        wb.close();
        return hm;
    }

    public HashMap<String, String> addKind(HashMap<String, String> hm) {
        SQLiteDatabase wb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("KIND", hm.get("KIND"));
        wb.insert(KIND_NAME, null, values);
        wb.close();
        return hm;
    }

    public HashMap<String, String> update(HashMap<String, String> hm) {
        SQLiteDatabase wb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("COMMENT", hm.get("COMMENT"));
        String temp = hm.get("TIME");
        hm.put("TIME", new Date().toString());
        values.put("TIME", hm.get("TIME"));
        values.put("COLLECT", hm.get("COLLECT"));
        values.put("KIND", hm.get("KIND"));
        wb.update(TB_NAME, values, "TIME = ?", new String[]{temp});
        wb.close();
        return hm;
    }

    public void delete(HashMap<String, String> hm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_NAME, "TIME = ?", new String[]{hm.get("TIME")});
        db.close();
    }

    public void deleteKind(HashMap<String, String> hm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("KIND", "未分类");
        db.update(TB_NAME, values, "KIND = ?", new String[]{hm.get("KIND")});
        db.delete(KIND_NAME, "KIND = ?", new String[]{hm.get("KIND")});
        db.close();
    }

    public ArrayList<HashMap<String, String>> listAll(String collect, String kind) {
        ArrayList<HashMap<String, String>> al = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TB_NAME, null, "COLLECT=? AND KIND=?", new String[]{collect, kind}, null, null, "TIME DESC");//(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("TIME", cursor.getString(cursor.getColumnIndex("TIME")));
                hm.put("COMMENT", cursor.getString(cursor.getColumnIndex("COMMENT")));
                al.add(hm);
                //Log.i(cursor.getString(cursor.getColumnIndex("COMMENT")), cursor.getString(cursor.getColumnIndex("TIME")));
            }
            cursor.close();
        }
        db.close();
        return al;
    }

    public ArrayList<HashMap<String, String>> listAllbyKind(String kind) {
        ArrayList<HashMap<String, String>> al = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TB_NAME, null, "KIND=?", new String[]{kind}, null, null, "TIME DESC");//(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("TIME", cursor.getString(cursor.getColumnIndex("TIME")));
                hm.put("COMMENT", cursor.getString(cursor.getColumnIndex("COMMENT")));
                al.add(hm);
                //Log.i(cursor.getString(cursor.getColumnIndex("COMMENT")), cursor.getString(cursor.getColumnIndex("TIME")));
            }
            cursor.close();
        }
        db.close();
        return al;
    }

    public ArrayList<HashMap<String, String>> listAllbyCollect(String kind) {
        ArrayList<HashMap<String, String>> al = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TB_NAME, null, "COLLECT=?", new String[]{kind}, null, null, "TIME DESC");//(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("TIME", cursor.getString(cursor.getColumnIndex("TIME")));
                hm.put("COMMENT", cursor.getString(cursor.getColumnIndex("COMMENT")));
                al.add(hm);
                Log.i(cursor.getString(cursor.getColumnIndex("COMMENT")), cursor.getString(cursor.getColumnIndex("TIME")));
            }
            cursor.close();
        }
        db.close();
        return al;
    }

    public ArrayList<HashMap<String, String>> listAll() {
        ArrayList<HashMap<String, String>> al = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TB_NAME, null, null, null, null, null, "TIME DESC");//(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("TIME", cursor.getString(cursor.getColumnIndex("TIME")));
                hm.put("COMMENT", cursor.getString(cursor.getColumnIndex("COMMENT")));
                hm.put("KIND", cursor.getString(cursor.getColumnIndex("KIND")));
                al.add(hm);
                Log.i(cursor.getString(cursor.getColumnIndex("COMMENT")), cursor.getString(cursor.getColumnIndex("TIME")));
            }
            cursor.close();
        }
        db.close();
        return al;
    }


    public ArrayList<HashMap<String, String>> listAllKind() {
        ArrayList<HashMap<String, String>> al = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(KIND_NAME, null, null, null, null, null, "ID DESC");//(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("KIND", cursor.getString(cursor.getColumnIndex("KIND")));
                Cursor cursor1 = db.query(TB_NAME, null, "KIND=?", new String[]{cursor.getString(cursor.getColumnIndex("KIND"))}, null, null, "ID DESC");//(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null, null, null)
                if (cursor1 != null) {
                    hm.put("NUMBER", String.valueOf(cursor1.getCount()));
                    cursor1.close();
                } else {
                    hm.put("NUMBER", "0");
                }
                al.add(hm);
                Log.i(cursor.getString(cursor.getColumnIndex("KIND")), "lala");
            }
            cursor.close();
        }
        db.close();
        return al;
    }

    public HashMap<String, String> listAllKind(String kind) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TB_NAME, null, "KIND = ?", new String[]{kind}, null, null, "ID DESC");//(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null, null, null)
        HashMap<String, String> hm = new HashMap<>();
        if (cursor != null) {
            hm.put("NUMBER", String.valueOf(cursor.getCount()));
            cursor.close();
        } else {
            hm.put("NUMBER", "0");
        }
        db.close();
        return hm;
    }

    public HashMap<String, String> getCount() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("COUNT", "0");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TB_NAME, null, null, null, null, null, "TIME DESC");//(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null, null, null)
        if (cursor != null) {
            hm.put("COUNT", String.valueOf(cursor.getCount()));
            cursor.close();
        }
        db.close();
        return hm;
    }
}
