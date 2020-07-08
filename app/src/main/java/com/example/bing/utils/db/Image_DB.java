package com.example.bing.utils.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.example.bing.MyApp;
import com.example.bing.beans.ImageBean;
import com.example.bing.utils.Convert;
import com.example.bing.utils.L;
import java.util.ArrayList;
import java.util.List;

/**
 * Project    Bing
 * Path       com.example.bing.utils.db
 * Date       2020/07/07 - 18:04
 * Author     Payne.
 * About      类描述：
 */
public class Image_DB {


    private static final    String         TAG   = "DatabaseManager";
    // 静态引用
    private volatile static Image_DB       mInstance;
    // DatabaseHelper
    private                 DatabaseHelper dbHelper;
    private static final    String         TABLE = DatabaseHelper.TABLE_IMAGE;

    private Image_DB() {
        dbHelper = new DatabaseHelper(MyApp.instance);
    }

    /**
     * 获取单例引用
     *
     * @return
     */
    public static Image_DB getInstance() {
        Image_DB inst = mInstance;
        if (inst == null) {
            synchronized (Image_DB.class) {
                inst = mInstance;
                if (inst == null) {
                    mInstance = inst = new Image_DB();
                }
            }
        }

        return inst;
    }


    /**
     * 插入数据
     */
    public synchronized void insert(ImageBean bean) {

        try {
            if (queryByDay(bean.dateFormat) == null) {

                //获取写数据库
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //生成要修改或者插入的键值
                ContentValues cv = new ContentValues();
//            cv.put(DatabaseHelper.TE_ID, bean.id);//id自增
                cv.put(DatabaseHelper.TI_URL, bean.url);
                cv.put(DatabaseHelper.TI_COPYRIGHT, bean.copyright);
                cv.put(DatabaseHelper.TI_DATE_FORMAT, bean.dateFormat);

                // insert 操作
                db.insert(TABLE, null, cv);
                //关闭数据库
                db.close();
            }
        } catch (Exception e) {

        }
    }


    public synchronized ImageBean query(String imageUrl) {

        ImageBean bean = null;
        try {
            //指定要查询的是哪几列数据
            String[] columns = {
                    DatabaseHelper.TI_ID,
                    DatabaseHelper.TI_URL,
                    DatabaseHelper.TI_COPYRIGHT,
                    DatabaseHelper.TI_DATE_FORMAT
            };

            //获取可读数据库
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            //查询数据库
            Cursor cursor = null;
            try {
                cursor = db.query(TABLE, columns, DatabaseHelper.TI_URL + "=" + imageUrl,
                        null, null, null, null);//获取数据游标
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String url = cursor.getString(1);
                    String copyright = cursor.getString(2);
                    String dateFormat = cursor.getString(3);

                    bean = new ImageBean(id, url, dateFormat, copyright);

                }
                //关闭游标防止内存泄漏
                if (cursor != null) {
                    cursor.close();
                }
            } catch (SQLException e) {
                L.d("queryDatas  e=" + e.toString());
            }
            //关闭数据库
            db.close();

        } catch (Exception e) {
            L.d("queryDatas  e2=" + e.toString());
        }
        return bean;
    }


    public synchronized ImageBean queryByDay(String dateFormat) {

        ImageBean bean = null;
        try {
            //指定要查询的是哪几列数据
            String[] columns = {
                    DatabaseHelper.TI_ID,
                    DatabaseHelper.TI_URL,
                    DatabaseHelper.TI_COPYRIGHT,
                    DatabaseHelper.TI_DATE_FORMAT
            };

            //获取可读数据库
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            //查询数据库
            Cursor cursor = null;
            try {
                cursor = db.query(TABLE, columns, DatabaseHelper.TI_DATE_FORMAT + "=" + dateFormat,
                        null, null, null, null);//获取数据游标
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String url = cursor.getString(1);
                    String copyright = cursor.getString(2);
                    String dateFormatt = cursor.getString(3);

                    bean = new ImageBean(id, url, dateFormatt, copyright);

                }
                //关闭游标防止内存泄漏
                if (cursor != null) {
                    cursor.close();
                }
            } catch (SQLException e) {
                L.d("queryDatas  e=" + e.toString());
            }
            //关闭数据库
            db.close();

        } catch (Exception e) {
            L.d("queryDatas  e2=" + e.toString());
        }
        return bean;
    }


    /**
     * 查询所有数据
     */
    public synchronized List<ImageBean> queryAll() {

        List<ImageBean> res = new ArrayList<>();
        try {
            //指定要查询的是哪几列数据
            String[] columns = {
                    DatabaseHelper.TI_ID,
                    DatabaseHelper.TI_URL,
                    DatabaseHelper.TI_COPYRIGHT,
                    DatabaseHelper.TI_DATE_FORMAT
            };

            //获取可读数据库
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            //查询数据库
            Cursor cursor = null;
            try {
                cursor = db.query(TABLE, columns, "",
                        null, null, null, null);//获取数据游标
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String url = cursor.getString(1);
                    String copyright = cursor.getString(2);
                    String dateFormat = cursor.getString(3);

                    ImageBean bean = new ImageBean(id, url, dateFormat, copyright);

                    res.add(bean);
                }
                //关闭游标防止内存泄漏
                if (cursor != null) {
                    cursor.close();
                }
            } catch (SQLException e) {
                L.d("queryDatas  e=" + e.toString());
            }
            //关闭数据库
            db.close();
        } catch (Exception e) {
            L.d("queryDatas  e2=" + e.toString());
        }
        return res;
    }


}
