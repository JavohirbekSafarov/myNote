package com.javohirbekcoder.mynote.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

/*
Created by Javohirbek on 22.04.2023 at 21:30
*/

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DatabaseConfig.DATABASE_NAME, null, DatabaseConfig.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "create table ${DatabaseConfig.TABLE_NAME}(${BaseColumns._ID} integer primary key autoincrement," +
                    "${DatabaseConfig.TITLE_COL} text, ${DatabaseConfig.NOTE_COL} text, ${DatabaseConfig.DATE_COL} text, ${DatabaseConfig.COLOR_COL} integer)"
        db!!.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists ${DatabaseConfig.TABLE_NAME}")
    }

    fun insertNote(title: String, note: String, date: String, colorIndex: Int): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DatabaseConfig.TITLE_COL, title)
        contentValues.put(DatabaseConfig.NOTE_COL, note)
        contentValues.put(DatabaseConfig.DATE_COL, date)
        contentValues.put(DatabaseConfig.COLOR_COL, colorIndex)

        val isInserted = db.insert(DatabaseConfig.TABLE_NAME, null, contentValues)
        return !isInserted.equals(-1)
    }

    fun readNote(): Cursor {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor = db.rawQuery("select * from ${DatabaseConfig.TABLE_NAME}", null)
        return cursor
    }

    fun deleteNote(title :String):Boolean{
        val db :SQLiteDatabase = this.writableDatabase
        val deleteData = db.delete(DatabaseConfig.TABLE_NAME, "${DatabaseConfig.TITLE_COL} = ?", arrayOf(title))
        return deleteData != -1
    }

    fun updateNote(id: String, title: String, note: String, date: String, colorIndex: Int){
        val db:SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DatabaseConfig.TITLE_COL, title)
        contentValues.put(DatabaseConfig.NOTE_COL, note)
        contentValues.put(DatabaseConfig.DATE_COL, date)
        contentValues.put(DatabaseConfig.COLOR_COL, colorIndex)

        val updateData = db.update(DatabaseConfig.TABLE_NAME, contentValues, "${BaseColumns._ID} = ?", arrayOf(id))
    }
}