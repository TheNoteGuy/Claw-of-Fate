package com.yourteam.cardgacharpg.feature.collection.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.yourteam.cardgacharpg.core.database.Converters;
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
public final class InventoryDao_Impl implements InventoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<InventoryEntity> __insertionAdapterOfInventoryEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<InventoryEntity> __insertionAdapterOfInventoryEntity_1;

  private final SharedSQLiteStatement __preparedStmtOfAdjustAmount;

  public InventoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfInventoryEntity = new EntityInsertionAdapter<InventoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `inventory` (`itemType`,`amount`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final InventoryEntity entity) {
        final String _tmp = __converters.fromItemType(entity.getItemType());
        statement.bindString(1, _tmp);
        statement.bindLong(2, entity.getAmount());
      }
    };
    this.__insertionAdapterOfInventoryEntity_1 = new EntityInsertionAdapter<InventoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `inventory` (`itemType`,`amount`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final InventoryEntity entity) {
        final String _tmp = __converters.fromItemType(entity.getItemType());
        statement.bindString(1, _tmp);
        statement.bindLong(2, entity.getAmount());
      }
    };
    this.__preparedStmtOfAdjustAmount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE inventory SET amount = amount + ? WHERE itemType = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final InventoryEntity entity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfInventoryEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertIfAbsent(final InventoryEntity entity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfInventoryEntity_1.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object addAmount(final ItemType itemType, final int delta,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> InventoryDao.DefaultImpls.addAmount(InventoryDao_Impl.this, itemType, delta, __cont), $completion);
  }

  @Override
  public Object adjustAmount(final ItemType itemType, final int delta,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAdjustAmount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, delta);
        _argIndex = 2;
        final String _tmp = __converters.fromItemType(itemType);
        _stmt.bindString(_argIndex, _tmp);
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
          __preparedStmtOfAdjustAmount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<InventoryEntity> getByType(final ItemType itemType) {
    final String _sql = "SELECT * FROM inventory WHERE itemType = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromItemType(itemType);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"inventory"}, new Callable<InventoryEntity>() {
      @Override
      @Nullable
      public InventoryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfItemType = CursorUtil.getColumnIndexOrThrow(_cursor, "itemType");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final InventoryEntity _result;
          if (_cursor.moveToFirst()) {
            final ItemType _tmpItemType;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfItemType);
            _tmpItemType = __converters.toItemType(_tmp_1);
            final int _tmpAmount;
            _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
            _result = new InventoryEntity(_tmpItemType,_tmpAmount);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
