package com.app.weather.db;

import android.content.Context;

import com.app.weather.bean.Citys;

import java.util.List;

/**
 * @author: 105032016090颜文君
 * @description: Citys数据对应接口
 * @data: 2019/5/26
 */
public class CitysDao {

    private Context mContext;

    public CitysDao(Context context) {
        mContext = context;
    }

    /**
     * 添加新城市
     * @param citys
     * @return
     */
    public long add(Citys citys) {
        return DaoUtils.getInstance(mContext).getWriDaoSession().insert(citys);
    }

    /**
     * 查询所有城市
     * @return
     */
    public List<Citys> findAllCity() {
        return DaoUtils.getInstance(mContext).getReadDaoSession().queryBuilder(Citys.class).list();
    }

    /**
     * 删除城市
     * @param citys
     */
    public void delete(Citys citys) {
        DaoUtils.getInstance(mContext).getWriDaoSession().delete(citys);
    }
}
