package com.example.zkt.data;





import com.example.zkt.base.BaseApplication;
import com.example.zkt.data.dao.Address;
import com.example.zkt.data.dao.AddressDao;
import com.example.zkt.data.dao.User;
import com.example.zkt.data.dao.UserDao;

import java.util.List;

/**
 *     desc   : 数据库管理类
 */

public class DataManager {

    /**
     * 添加数据
     *
     * @param user
     */
    public static void insertUser(User user) {
        BaseApplication.getDaoSession().getUserDao().insert(user);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteUser(Long id) {
        BaseApplication.getDaoSession().getUserDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param user
     */
    public static void updateUser(User user) {
        BaseApplication.getDaoSession().getUserDao().update(user);
    }

    /**
     * 查询条件为Type=Phone的数据
     *
     * @return
     */
    public static List<User> queryUser(String phone) {
        return BaseApplication.getDaoSession().getUserDao().queryBuilder().where
                (UserDao.Properties.Phone.eq(phone)).list();
    }


    /**
     * 添加数据
     *
     * @param address
     */
    public static void insertAddress(Address address) {
        BaseApplication.getDaoSession().getAddressDao().insert(address);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteAddress(Long id) {
        BaseApplication.getDaoSession().getAddressDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param address
     */
    public static void updateAddress(Address address) {
        BaseApplication.getDaoSession().getAddressDao().update(address);
    }

    /**
     * 查询条件为Type=UserId的数据
     *
     * @return
     */
    public static List<Address> queryAddress(Long userId) {
        return BaseApplication.getDaoSession().getAddressDao().queryBuilder().where
                (AddressDao.Properties.UserId.eq(userId)).list();
    }
}
