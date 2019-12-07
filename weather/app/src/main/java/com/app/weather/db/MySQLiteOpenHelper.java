package com.app.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.app.weather.bean.DaoMaster;

/**
 * @author: 105032016090颜文君
 * @description: 数据库设置类

 * @data: 2019/5/26
 */

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 自定义数据库版本升级过程
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

    }

}
