package com.example.bing.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.bing.utils.L;

/**
 * Project    Bing
 * Path       com.example.bing.utils.db
 * Date       2020/07/07 - 17:55
 * Author     Payne.
 * About      类描述：
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String  DB_NAME = "bing.db";//数据库名字
    private              Context mContext;


    /**
     * image数据表
     * id 唯一id
     * String url;//地址，需要拼接
     * String dateFormat;//时间
     * String copyright;//描述和版权
     */

    public static final String TABLE_IMAGE = "_image_tab";//image数据表

    public static final String TI_ID          = "_img_id";//id 唯一id
    public static final String TI_URL         = "_img_url";//地址，需要拼接
    public static final String TI_DATE_FORMAT = "_img_date_format";//时间
    public static final String TI_COPYRIGHT   = "_img_copyright";//描述和版权


    private static final int DB_VERSION = 1;   // 数据库版本

    public DatabaseHelper(Context context) {

        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类

        super(context, DB_NAME, null, DB_VERSION);

        mContext = context;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        L.d(" DatabaseHelper  onCreate ");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_IMAGE
                + " (" + TI_ID + " INTEGER PRIMARY KEY, "
                + TI_URL + " varchar(20),"
                + TI_DATE_FORMAT + " varchar(20),"
                + TI_COPYRIGHT + " varchar(20))"
        );


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        L.d(" onUpgrade  old = " + oldVersion + "   new = " + newVersion);

        //数据库升级
//        if (2 == newVersion && 1 == oldVersion) {
//            db.execSQL("ALTER TABLE  " + TABLE_Theme + " ADD " + Columns_crc32
//                    + " VARCHAR(20)"); //往表中增加一列
//        }

    }


}

