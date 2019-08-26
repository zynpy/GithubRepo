package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class RepoFavoriteDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RepoFavoriteDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_FAVORITES = "favorites";
    private static final String ID = "id";
    private static final String USER_NAME = "user_name";
    private static final String REPO_NAME = "repo_name";

    public RepoFavoriteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_FAVORITES + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_NAME + " TEXT NOT NULL, "
                + REPO_NAME + " TEXT NOT NULL) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    public boolean insertMessages(Repository repository){
        SQLiteDatabase database = this.getWritableDatabase();
        try {
                ContentValues cv = new ContentValues();
                cv.put(USER_NAME, repository.getOwner());
                cv.put(REPO_NAME, repository.getRepoName());
                database.insert(TABLE_FAVORITES, null, cv);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        database.close();
        return true;
    }

    public boolean deleteRepository(Repository repository){
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(TABLE_FAVORITES,""+USER_NAME+"=? and "+REPO_NAME+"=?",new String[]{repository.getOwner(),repository.getRepoName()});
        return result > 0;
    }

    public ArrayList<String> getRepositories(String userName){
        ArrayList<String> repoNameList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            String columns[] = {REPO_NAME};
            Cursor cursor = database.query(TABLE_FAVORITES, columns,"user_name='"+userName+"' COLLATE NOCASE", null, null, null,null);
            while (cursor.moveToNext()){
                String repoName = cursor.getString(0);
                repoNameList.add(repoName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return repoNameList;
    }
}
