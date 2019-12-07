package com.app.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.app.weather.bean.DaoMaster;
import com.app.weather.bean.DaoSession;

/**
 * @author: 105032016090颜文君
 * @description: 封装db操作类
 * @data: 2019/5/26
 */

public class DaoUtils {
    private final static String dbName = "app_db";
    private static DaoUtils instance = null;
    private static MySQLiteOpenHelper helper;
    private static Context mContext;

    /**
     * 使用单例获取操作数据库对象
     * @param c
     * @return
     */
    public synchronized static DaoUtils getInstance(Context c) {
        if (instance == null) {
            synchronized (DaoUtils.class) {
                if (instance == null) {
                    instance = new DaoUtils(c);
                }
            }
        }
        return instance;
    }

    public DaoUtils(Context c) {
        this.mContext = c;
        helper = new MySQLiteOpenHelper(c, dbName, null);
    }

    public SQLiteDatabase getReadableDatabase() {
        if (helper == null) {
            helper = new MySQLiteOpenHelper(mContext, dbName, null);
        }
        return helper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     */
    public SQLiteDatabase getWritableDatabase() {
        if (helper == null) {
            helper = new MySQLiteOpenHelper(mContext, dbName, null);
        }
        return helper.getWritableDatabase();
    }

    public DaoMaster getReadDaoMaster() {
        return new DaoMaster(helper.getReadableDatabase());
    }

    public DaoSession getReadDaoSession() {
        return getReadDaoMaster().newSession();
    }

    /**
     * 创建新数据库
     * @return
     */
    public DaoMaster getWriDaoMaster() {
        return new DaoMaster(helper.getWritableDatabase());
    }


    public DaoSession getWriDaoSession() {
        return getWriDaoMaster().newSession();
    }
}
