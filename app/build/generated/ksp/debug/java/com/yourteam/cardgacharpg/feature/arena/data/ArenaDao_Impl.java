package com.yourteam.cardgacharpg.feature.arena.data;

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
public final class ArenaDao_Impl implements ArenaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ArenaProfileEntity> __insertionAdapterOfArenaProfileEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTrophies;

  private final SharedSQLiteStatement __preparedStmtOfIncrementWeeklyCount;

  private final SharedSQLiteStatement __preparedStmtOfResetWeeklyReward;

  public ArenaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfArenaProfileEntity = new EntityInsertionAdapter<ArenaProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `arena_profile` (`id`,`trophies`,`weeklyArenaCount`,`lastRewardTimestamp`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ArenaProfileEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTrophies());
        statement.bindLong(3, entity.getWeeklyArenaCount());
        statement.bindLong(4, entity.getLastRewardTimestamp());
      }
    };
    this.__preparedStmtOfUpdateTrophies = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE arena_profile SET trophies = MAX(0, trophies + ?) WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementWeeklyCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE arena_profile SET weeklyArenaCount = weeklyArenaCount + 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfResetWeeklyReward = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE arena_profile SET weeklyArenaCount = 0, lastRewardTimestamp = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object ensureRow(final ArenaProfileEntity entity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfArenaProfileEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTrophies(final int delta, final int id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTrophies.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, delta);
        _argIndex = 2;
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
          __preparedStmtOfUpdateTrophies.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementWeeklyCount(final int id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementWeeklyCount.acquire();
        int _argIndex = 1;
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
          __preparedStmtOfIncrementWeeklyCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object resetWeeklyReward(final long timestamp, final int id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfResetWeeklyReward.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
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
          __preparedStmtOfResetWeeklyReward.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<ArenaProfileEntity> getProfile(final int id) {
    final String _sql = "SELECT * FROM arena_profile WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"arena_profile"}, new Callable<ArenaProfileEntity>() {
      @Override
      @Nullable
      public ArenaProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTrophies = CursorUtil.getColumnIndexOrThrow(_cursor, "trophies");
          final int _cursorIndexOfWeeklyArenaCount = CursorUtil.getColumnIndexOrThrow(_cursor, "weeklyArenaCount");
          final int _cursorIndexOfLastRewardTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastRewardTimestamp");
          final ArenaProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpTrophies;
            _tmpTrophies = _cursor.getInt(_cursorIndexOfTrophies);
            final int _tmpWeeklyArenaCount;
            _tmpWeeklyArenaCount = _cursor.getInt(_cursorIndexOfWeeklyArenaCount);
            final long _tmpLastRewardTimestamp;
            _tmpLastRewardTimestamp = _cursor.getLong(_cursorIndexOfLastRewardTimestamp);
            _result = new ArenaProfileEntity(_tmpId,_tmpTrophies,_tmpWeeklyArenaCount,_tmpLastRewardTimestamp);
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
  public Object get(final int id, final Continuation<? super ArenaProfileEntity> $completion) {
    final String _sql = "SELECT * FROM arena_profile WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ArenaProfileEntity>() {
      @Override
      @Nullable
      public ArenaProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTrophies = CursorUtil.getColumnIndexOrThrow(_cursor, "trophies");
          final int _cursorIndexOfWeeklyArenaCount = CursorUtil.getColumnIndexOrThrow(_cursor, "weeklyArenaCount");
          final int _cursorIndexOfLastRewardTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastRewardTimestamp");
          final ArenaProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpTrophies;
            _tmpTrophies = _cursor.getInt(_cursorIndexOfTrophies);
            final int _tmpWeeklyArenaCount;
            _tmpWeeklyArenaCount = _cursor.getInt(_cursorIndexOfWeeklyArenaCount);
            final long _tmpLastRewardTimestamp;
            _tmpLastRewardTimestamp = _cursor.getLong(_cursorIndexOfLastRewardTimestamp);
            _result = new ArenaProfileEntity(_tmpId,_tmpTrophies,_tmpWeeklyArenaCount,_tmpLastRewardTimestamp);
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
