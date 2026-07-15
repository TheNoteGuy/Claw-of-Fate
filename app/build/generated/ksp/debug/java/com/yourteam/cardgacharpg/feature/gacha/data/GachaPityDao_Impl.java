package com.yourteam.cardgacharpg.feature.gacha.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GachaPityDao_Impl implements GachaPityDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GachaPityEntity> __insertionAdapterOfGachaPityEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdate;

  public GachaPityDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGachaPityEntity = new EntityInsertionAdapter<GachaPityEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `gacha_pity` (`id`,`pityCount`,`lastPullTimestamp`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GachaPityEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPityCount());
        statement.bindLong(3, entity.getLastPullTimestamp());
      }
    };
    this.__preparedStmtOfUpdate = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE gacha_pity SET pityCount = ?, lastPullTimestamp = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object ensureRow(final GachaPityEntity entity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGachaPityEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final int count, final long timestamp, final int id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdate.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, count);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdate.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<GachaPityEntity> observe(final int id) {
    final String _sql = "SELECT * FROM gacha_pity WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"gacha_pity"}, new Callable<GachaPityEntity>() {
      @Override
      @Nullable
      public GachaPityEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPityCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pityCount");
          final int _cursorIndexOfLastPullTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPullTimestamp");
          final GachaPityEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpPityCount;
            _tmpPityCount = _cursor.getInt(_cursorIndexOfPityCount);
            final long _tmpLastPullTimestamp;
            _tmpLastPullTimestamp = _cursor.getLong(_cursorIndexOfLastPullTimestamp);
            _result = new GachaPityEntity(_tmpId,_tmpPityCount,_tmpLastPullTimestamp);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object get(final int id, final Continuation<? super GachaPityEntity> $completion) {
    final String _sql = "SELECT * FROM gacha_pity WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<GachaPityEntity>() {
      @Override
      @Nullable
      public GachaPityEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPityCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pityCount");
          final int _cursorIndexOfLastPullTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPullTimestamp");
          final GachaPityEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpPityCount;
            _tmpPityCount = _cursor.getInt(_cursorIndexOfPityCount);
            final long _tmpLastPullTimestamp;
            _tmpLastPullTimestamp = _cursor.getLong(_cursorIndexOfLastPullTimestamp);
            _result = new GachaPityEntity(_tmpId,_tmpPityCount,_tmpLastPullTimestamp);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
