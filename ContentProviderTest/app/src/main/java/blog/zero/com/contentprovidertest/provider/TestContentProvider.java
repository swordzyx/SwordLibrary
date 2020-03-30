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
        switch (matcher.match(uri)){
            case 1:

                break;
            case 2:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
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
        switch (matcher.match(uri)){
            case 1:
                assert(database != null);
                database.delete(TestContract.TABLE1.TABLE_NAME, selection, selectionArgs);
                break;
            case 2:
                break;
            default:
                break;
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
