package com.example.dtbssql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Db_operations extends SQLiteOpenHelper {
    public Db_operations(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "books.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE BOOKS(ID INTEGER PRIMARY KEY AUTOINCREMENT, BOOKID TEXT UNIQUE, BOOKTITLE TEXT, BOOKAUTHOR TEXT, BOOKCATEGORY TEXT, BOOKYEARPUB TEXT)");
        db.execSQL("CREATE TABLE USERS(ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT UNIQUE, USERNAME TEXT UNIQUE, PASSWORD TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS BOOKS");
        db.execSQL("DROP TABLE IF EXISTS USERS");
        onCreate(db);
    }

    public void insert_user(String email, String username, String pwd)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", email);
        contentValues.put("USERNAME", username);
        contentValues.put("PASSWORD", pwd);
        this.getWritableDatabase().insertOrThrow("USERS","",contentValues);
    }


    public boolean SearchEmail(String email, String pwd){
        String sql = "SELECT EMAIL FROM USERS WHERE EMAIL = '"+email+"' AND PASSWORD = '"+pwd+"'";
        Cursor cursor = this.getReadableDatabase().rawQuery(sql,null);
        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean SearchUsername(String username, String pwd){
        String sql = "SELECT USERNAME FROM USERS WHERE USERNAME = '"+username+"'  AND PASSWORD = '"+pwd+"'";
        Cursor cursor = this.getReadableDatabase().rawQuery(sql,null);
        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public Cursor viewAUserThruUsername(String username){
        return this.getReadableDatabase().rawQuery("SELECT USERNAME FROM USERS WHERE USERNAME = '"+username+"'", null);
    }

    public Cursor viewAUserThruEmail(String email){
        return this.getReadableDatabase().rawQuery("SELECT USERNAME FROM USERS WHERE EMAIL = '"+email+"'", null);
    }

    //BOOKS SECTION DATABASE
    public void insert_book(String book_id, String book_title, String book_author, String book_category, String book_year_pub)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("BOOKID", book_id);
        contentValues.put("BOOKTITLE", book_title);
        contentValues.put("BOOKAUTHOR", book_author);
        contentValues.put("BOOKCATEGORY", book_category);
        contentValues.put("BOOKYEARPUB", book_year_pub);
        //this.getWritableDatabase().insertOrThrow("BOOKS","",contentValues);
        db.insert("BOOKS", null, contentValues);
    }

    public boolean SearchBooks(String book_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM BOOKS WHERE BOOKID = '"+book_id+"'",null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //Search By ID
    public Cursor searchByID(String id){
        return this.getReadableDatabase().rawQuery("SELECT * FROM BOOKS WHERE BOOKID like '%"+id+"%'", null);
    }

    //Search By TITLE
    public Cursor searchByTitle(String title){
        return this.getReadableDatabase().rawQuery("SELECT * FROM BOOKS WHERE BOOKTITLE like '%"+title+"%'", null);
    }

    //Search By CATEGORY
    public Cursor searchByCategory(String category){
        return this.getReadableDatabase().rawQuery("SELECT * FROM BOOKS WHERE BOOKCATEGORY = '"+category+"'", null);
    }
    //Search By PUBLISHED YEAR
    public Cursor searchByYearPub(String yearPub){
        return this.getReadableDatabase().rawQuery("SELECT * FROM BOOKS WHERE BOOKYEARPUB like '%"+yearPub+"%'", null);
    }

    public void update_book(String bookid, String booktitle, String bookauthor, String bookcategory, String bookyearpublished){
        this.getWritableDatabase().execSQL("UPDATE BOOKS SET BOOKTITLE = '"+booktitle+"', BOOKAUTHOR = '"+bookauthor+"', BOOKCATEGORY = '"+bookcategory+"', BOOKYEARPUB = '"+bookyearpublished+"' WHERE BOOKID = '"+bookid+"'");
    }

    public void delete_by_book_number(String bookid){
        this.getWritableDatabase().delete("BOOKS","BOOKID = '"+bookid+"'", null);
    }

    public void delete_by_book_title(String booktitle){
        this.getWritableDatabase().delete("BOOKS","BOOKTITLE = '"+booktitle+"'", null);
    }

    public Cursor getAllData(){
        return this.getReadableDatabase().rawQuery("SELECT * FROM BOOKS", null);
    }
}
