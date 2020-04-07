package blog.zero.com.contentprovidertest.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import blog.zero.com.contentprovidertest.db.TestContract;
import blog.zero.com.contentprovidertest.db.TestDatabase;
import blog.zero.com.contentprovidertest.util.Logger;
import blog.zero.com.contentprovidertest.util.SelectionBuilder;

public class TestContentProvider extends ContentProvider {
    final static String AUTHORITY = "blog.zero.com.contentprovidertest.provider";
    final static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private TestDatabase testDatabaseHelper;

    static {
        matcher.addURI(AUTHORITY, "table1", 1);
        matcher.addURI(AUTHORITY, "table1/#", 2); //这个 Uri 模式就表示请求的数据为 table1 中的某一行数据。
    }

    @Override
    public boolean onCreate() {
        testDatabaseHelper = new TestDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SelectionBuilder sb = null;
        SQLiteDatabase database = testDatabaseHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (matcher.match(uri)){
            case 1:
                assert (database != null);
                cursor = new SelectionBuilder(TestContract.TABLE1.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .query(database, projection, sortOrder);
                break;
            case 2:
                assert (database != null);
                cursor = new SelectionBuilder(TestContract.TABLE1.TABLE_NAME)
                        .where("_ID=?", new String[]{uri.getLastPathSegment()})
                        .where(selection, selectionArgs)
                        .query(database, projection, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknow Uri: " + uri);
        }
        Logger.debug("query count: " + cursor.getCount());
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(matcher.match(uri)){
            case 1:
                return TestContract.TABLE1.CONTENT_TYPE;
            case 2:
                return TestContract.TABLE1.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknow Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase database = testDatabaseHelper.getWritableDatabase();
        //database must not be null
        assert database != null;
        Uri result = null;
        switch (matcher.match(uri)){
            case 1:
                long id = database.insertOrThrow(TestContract.TABLE1.TABLE_NAME, null, values);
                result = Uri.parse(TestContract.TABLE1.CONTENT_URI + "/" + id);
                break;
            case 2:
                throw  new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknow Uri: " + uri);
        }
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = testDatabaseHelper.getWritableDatabase();
        SelectionBuilder sb = null;
        int deleteId = -1;
        switch (matcher.match(uri)){
            case 1:
                assert(database != null);
                deleteId = database.delete(TestContract.TABLE1.TABLE_NAME, selection, selectionArgs);
                break;
            case 2:
                assert(database != null);
                deleteId = new SelectionBuilder(TestContract.TABLE1.TABLE_NAME)
                        .where("_ID=?", new String[]{uri.getLastPathSegment()})
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            default:
                throw new IllegalArgumentException("Unknow Uri: " + uri);
        }
        Logger.debug("delete column: " + deleteId);
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = testDatabaseHelper.getWritableDatabase();
        int updateId = -1;
        switch (matcher.match(uri)){
            case 1:
                assert (database != null);
                updateId = new SelectionBuilder(TestContract.TABLE1.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case 2:
                assert (database != null);
                updateId = new SelectionBuilder(TestContract.TABLE1.TABLE_NAME)
                        .where("_ID=?", new String[]{uri.getLastPathSegment()})
                        .update(database, values);
                break;
            default:
                throw new IllegalArgumentException("Unknow Uri: " + uri);
        }
        Logger.debug("update column: " + updateId);
        return updateId;
    }
}
