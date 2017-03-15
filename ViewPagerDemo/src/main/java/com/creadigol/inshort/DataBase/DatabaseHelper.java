package com.creadigol.inshort.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.creadigol.inshort.Model.NewsModel;

import java.util.ArrayList;


/**
 * Created by ZAD on 10/26/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Created by ZAD on 10/26/2015.
     */

    //News
    public static final String TABLE_NEWS = "newsrecord"; // main recording table

    public static final String NEWS_ID = "id";
    public static final String NEWS_SERVER_ID = "sid";
    public static final String NEWS_LINK = "link";
    public static final String NEWS_TITLE = "title";
    public static final String NEWS_DESCRIPTION = "discription";
    public static final String NEWS_IMAGE = "image";
    public static final String NEWS_SERVER_IMAGE = "serverImage";
    public static final String NEWS_READ = "read";
    public static final String CREATED_DATE = "createddate";
    public static final String IS_BOOKMARKED = "bookmarked";
    public static final String LIKE = "like";

    private static final String TAG = "News";
    private static final String DATABASE_NAME = "NewsRecord";
    private static final int DATABASE_VERSION = 1;
    public static com.creadigol.inshort.DataBase.DatabaseHelper mInstance = null;
    private static Context mContext;
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new com.creadigol.inshort.DataBase.DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_NEWS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NEWS + " ( "
                + NEWS_ID + " INTEGER  PRIMARY KEY autoincrement, "
                + NEWS_SERVER_ID + " INTEGER, "
                + NEWS_TITLE + " VARCHAR, "
                + NEWS_DESCRIPTION + " VARCHAR,"
                + NEWS_READ + " INTEGER,"
                + CREATED_DATE + " VARCHAR,"
                + NEWS_IMAGE + " VARCHAR,"
                + NEWS_SERVER_IMAGE + " VARCHAR,"
                + NEWS_LINK + " VARCHAR,"
                + LIKE + " INTEGER,"
                + IS_BOOKMARKED + " INTEGER" + ");";
        db.execSQL(CREATE_NEWS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS + ";");


        onCreate(db);
    }


    public boolean insertNews(NewsModel newsModel) {
        boolean status = false;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int itemID = newsModel.getId();
        contentValues.put(NEWS_SERVER_ID, newsModel.getS_id());
        contentValues.put(NEWS_TITLE, newsModel.getTitle());
        contentValues.put(NEWS_DESCRIPTION, newsModel.getDiscription());
        contentValues.put(NEWS_LINK, newsModel.getLink());
        contentValues.put(CREATED_DATE, newsModel.getCreatedTime());
        contentValues.put(NEWS_READ, newsModel.getIsRead());
        contentValues.put(NEWS_IMAGE, newsModel.getImage_path());
        contentValues.put(NEWS_SERVER_IMAGE, newsModel.getServer_image_path());
        contentValues.put(LIKE, newsModel.getLike());
        contentValues.put(IS_BOOKMARKED, newsModel.getIsBookmarked());

        long check = db.insert(TABLE_NEWS, null, contentValues);
        if (check > -1) {
            Log.i("News Insert", "News inserted successfully" + contentValues);
            status = true;
        } else {
            Log.i("News Insert", "News insertion not successful");
        }

        return status;
    }

    /*Update BookMark*/
    public boolean UpdateBookmark(Integer id, Integer isBookMark) {
        boolean status = false;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(IS_BOOKMARKED, isBookMark);
        long check = db.update(TABLE_NEWS, contentValues, NEWS_ID + " ='" + id + "'", null);

        if (check > -1) {
            Log.i("Bookmarked ", "Bookmarked updated successfully");
            status = true;
        } else {
            Log.i("Bookmarked", "Bookmarked updated not successful");
        }

        return status;
    }


    /*Update LIKE*/
    public boolean UpdateLike(Integer id, Integer read) {
        boolean status = false;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NEWS_READ, read);
        long check = db.update(TABLE_NEWS, contentValues, NEWS_ID + " ='" + id + "'", null);

        if (check > -1) {
            Log.i("LIKE ", "LIKE updated successfully");
            status = true;
        } else {
            Log.i("LIKE", "LIKE updated not successful");
        }

        return status;
    }

    /*get All News*/


    public ArrayList<NewsModel> getAllNews() {
        String query = "select * from newsrecord " + " ORDER BY " + NEWS_ID + " DESC";

        ArrayList<NewsModel> newsModels = new ArrayList<NewsModel>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                int newsId = c.getInt(c.getColumnIndex(NEWS_ID));
                int newsServerId = c.getInt(c.getColumnIndex(NEWS_SERVER_ID));
                String newsTitle = c.getString(c.getColumnIndex(NEWS_TITLE));
                String newsDesc = c.getString(c.getColumnIndex(NEWS_DESCRIPTION));
                String newsImage = c.getString(c.getColumnIndex(NEWS_IMAGE));
                String newsServerImage = c.getString(c.getColumnIndex(NEWS_SERVER_IMAGE));
                String link = c.getString(c.getColumnIndex(NEWS_LINK));
                int newsRead = c.getInt(c.getColumnIndex(NEWS_READ));
                long newsDate = c.getLong(c.getColumnIndex(CREATED_DATE));
                int newsBookmark = c.getInt(c.getColumnIndex(IS_BOOKMARKED));
                int newsLike = c.getInt(c.getColumnIndex(LIKE));

                NewsModel news = new NewsModel();
                news.setId(newsId);
                news.setS_id(newsServerId);
                news.setTitle(newsTitle);
                news.setDiscription(newsDesc);
                news.setImage_path(newsImage);
                news.setServer_image_path(newsServerImage);
                news.setIsRead(newsRead);
                news.setLink(link);
                news.setCreatedTime(newsDate);
                news.setIsBookmarked(newsBookmark);
                news.setLike(newsLike);

                newsModels.add(news);
            }
        }
        return newsModels;

    }

    public boolean getAlreadyData(String serverId) {
        boolean newsModel = false;

        SQLiteDatabase sqldb = this.getReadableDatabase();
        String query = "select * from newsrecord where sid='" + serverId + "'";

        Cursor cursor = sqldb.rawQuery(query, null);
        if (cursor == null) {
            newsModel =false;
        } else if (cursor.getCount() == 0) {
            newsModel =false;
        } else if (cursor.getCount() != 0) {
            newsModel =true;
        }

        if (cursor != null) {
            cursor.close();
        }
        sqldb.close();
        return newsModel;
    }

    public ArrayList<NewsModel> getBookmarkData() {
        String query = "select * from newsrecord where bookmarked= 0 " + " ORDER BY " + NEWS_ID + " DESC";
Log.e("query ",""+query);
        ArrayList<NewsModel> newsModels = new ArrayList<NewsModel>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                int newsId = c.getInt(c.getColumnIndex(NEWS_ID));
                int newsServerId = c.getInt(c.getColumnIndex(NEWS_SERVER_ID));
                String newsTitle = c.getString(c.getColumnIndex(NEWS_TITLE));
                String newsDesc = c.getString(c.getColumnIndex(NEWS_DESCRIPTION));
                String newsImage = c.getString(c.getColumnIndex(NEWS_IMAGE));
                String newsServerImage = c.getString(c.getColumnIndex(NEWS_SERVER_IMAGE));
                String link = c.getString(c.getColumnIndex(NEWS_LINK));
                int newsRead = c.getInt(c.getColumnIndex(NEWS_READ));
                long newsDate = c.getLong(c.getColumnIndex(CREATED_DATE));
                int newsBookmark = c.getInt(c.getColumnIndex(IS_BOOKMARKED));
                int newsLike = c.getInt(c.getColumnIndex(LIKE));

                NewsModel news = new NewsModel();
                news.setId(newsId);
                news.setS_id(newsServerId);
                news.setTitle(newsTitle);
                news.setDiscription(newsDesc);
                news.setImage_path(newsImage);
                news.setServer_image_path(newsServerImage);
                news.setIsRead(newsRead);
                news.setLink(link);
                news.setCreatedTime(newsDate);
                news.setIsBookmarked(newsBookmark);
                news.setLike(newsLike);

                newsModels.add(news);
            }
        }
        return newsModels;

    }
}
