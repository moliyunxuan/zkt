package com.example.zkt.data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UserId = new Property(0, Long.class, "userId", true, "_id");
        public final static Property Phone = new Property(1, String.class, "phone", false, "PHONE");
        public final static Property Pwd = new Property(2, String.class, "pwd", false, "PWD");
        public final static Property Sex = new Property(3, int.class, "sex", false, "SEX");
        public final static Property NickName = new Property(4, String.class, "nickName", false, "NICK_NAME");
        public final static Property RegetTime = new Property(5, long.class, "regetTime", false, "REGET_TIME");
    }


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: userId
                "\"PHONE\" TEXT," + // 1: phone
                "\"PWD\" TEXT," + // 2: pwd
                "\"SEX\" INTEGER NOT NULL ," + // 3: sex
                "\"NICK_NAME\" TEXT," + // 4: nickName
                "\"REGET_TIME\" INTEGER NOT NULL );"); // 5: regetTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(1, userId);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(2, phone);
        }
 
        String pwd = entity.getPwd();
        if (pwd != null) {
            stmt.bindString(3, pwd);
        }
        stmt.bindLong(4, entity.getSex());
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(5, nickName);
        }
        stmt.bindLong(6, entity.getRegetTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(1, userId);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(2, phone);
        }
 
        String pwd = entity.getPwd();
        if (pwd != null) {
            stmt.bindString(3, pwd);
        }
        stmt.bindLong(4, entity.getSex());
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(5, nickName);
        }
        stmt.bindLong(6, entity.getRegetTime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // userId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // phone
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // pwd
            cursor.getInt(offset + 3), // sex
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // nickName
            cursor.getLong(offset + 5) // regetTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setUserId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPhone(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPwd(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSex(cursor.getInt(offset + 3));
        entity.setNickName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRegetTime(cursor.getLong(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setUserId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getUserId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(User entity) {
        return entity.getUserId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
