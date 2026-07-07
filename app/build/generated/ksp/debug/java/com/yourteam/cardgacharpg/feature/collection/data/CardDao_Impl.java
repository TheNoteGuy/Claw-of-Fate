package com.yourteam.cardgacharpg.feature.collection.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.yourteam.cardgacharpg.core.database.Converters;
import com.yourteam.cardgacharpg.core.model.Element;
import com.yourteam.cardgacharpg.core.model.Rarity;
import com.yourteam.cardgacharpg.core.model.Role;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CardDao_Impl implements CardDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CardEntity> __insertionAdapterOfCardEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<CardEntity> __updateAdapterOfCardEntity;

  public CardDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCardEntity = new EntityInsertionAdapter<CardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `cards` (`id`,`heroId`,`name`,`rarity`,`element`,`role`,`level`,`xp`,`baseHp`,`baseAtk`,`baseDef`,`baseSpd`,`currentHp`,`currentAtk`,`currentDef`,`currentSpd`,`skill1Id`,`skill2Id`,`imageAssetName`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CardEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getHeroId());
        statement.bindString(3, entity.getName());
        final String _tmp = __converters.fromRarity(entity.getRarity());
        statement.bindString(4, _tmp);
        final String _tmp_1 = __converters.fromElement(entity.getElement());
        statement.bindString(5, _tmp_1);
        final String _tmp_2 = __converters.fromRole(entity.getRole());
        statement.bindString(6, _tmp_2);
        statement.bindLong(7, entity.getLevel());
        statement.bindLong(8, entity.getXp());
        statement.bindLong(9, entity.getBaseHp());
        statement.bindLong(10, entity.getBaseAtk());
        statement.bindLong(11, entity.getBaseDef());
        statement.bindLong(12, entity.getBaseSpd());
        statement.bindLong(13, entity.getCurrentHp());
        statement.bindLong(14, entity.getCurrentAtk());
        statement.bindLong(15, entity.getCurrentDef());
        statement.bindLong(16, entity.getCurrentSpd());
        statement.bindLong(17, entity.getSkill1Id());
        statement.bindLong(18, entity.getSkill2Id());
        statement.bindString(19, entity.getImageAssetName());
      }
    };
    this.__updateAdapterOfCardEntity = new EntityDeletionOrUpdateAdapter<CardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `cards` SET `id` = ?,`heroId` = ?,`name` = ?,`rarity` = ?,`element` = ?,`role` = ?,`level` = ?,`xp` = ?,`baseHp` = ?,`baseAtk` = ?,`baseDef` = ?,`baseSpd` = ?,`currentHp` = ?,`currentAtk` = ?,`currentDef` = ?,`currentSpd` = ?,`skill1Id` = ?,`skill2Id` = ?,`imageAssetName` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CardEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getHeroId());
        statement.bindString(3, entity.getName());
        final String _tmp = __converters.fromRarity(entity.getRarity());
        statement.bindString(4, _tmp);
        final String _tmp_1 = __converters.fromElement(entity.getElement());
        statement.bindString(5, _tmp_1);
        final String _tmp_2 = __converters.fromRole(entity.getRole());
        statement.bindString(6, _tmp_2);
        statement.bindLong(7, entity.getLevel());
        statement.bindLong(8, entity.getXp());
        statement.bindLong(9, entity.getBaseHp());
        statement.bindLong(10, entity.getBaseAtk());
        statement.bindLong(11, entity.getBaseDef());
        statement.bindLong(12, entity.getBaseSpd());
        statement.bindLong(13, entity.getCurrentHp());
        statement.bindLong(14, entity.getCurrentAtk());
        statement.bindLong(15, entity.getCurrentDef());
        statement.bindLong(16, entity.getCurrentSpd());
        statement.bindLong(17, entity.getSkill1Id());
        statement.bindLong(18, entity.getSkill2Id());
        statement.bindString(19, entity.getImageAssetName());
        statement.bindLong(20, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final CardEntity card, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCardEntity.insertAndReturnId(card);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<CardEntity> cards,
      final Continuation<? super List<Long>> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final List<Long> _result = __insertionAdapterOfCardEntity.insertAndReturnIdsList(cards);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final CardEntity card, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCardEntity.handle(card);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CardEntity>> getAll() {
    final String _sql = "SELECT * FROM cards ORDER BY level DESC, id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cards"}, new Callable<List<CardEntity>>() {
      @Override
      @NonNull
      public List<CardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHeroId = CursorUtil.getColumnIndexOrThrow(_cursor, "heroId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfRarity = CursorUtil.getColumnIndexOrThrow(_cursor, "rarity");
          final int _cursorIndexOfElement = CursorUtil.getColumnIndexOrThrow(_cursor, "element");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfXp = CursorUtil.getColumnIndexOrThrow(_cursor, "xp");
          final int _cursorIndexOfBaseHp = CursorUtil.getColumnIndexOrThrow(_cursor, "baseHp");
          final int _cursorIndexOfBaseAtk = CursorUtil.getColumnIndexOrThrow(_cursor, "baseAtk");
          final int _cursorIndexOfBaseDef = CursorUtil.getColumnIndexOrThrow(_cursor, "baseDef");
          final int _cursorIndexOfBaseSpd = CursorUtil.getColumnIndexOrThrow(_cursor, "baseSpd");
          final int _cursorIndexOfCurrentHp = CursorUtil.getColumnIndexOrThrow(_cursor, "currentHp");
          final int _cursorIndexOfCurrentAtk = CursorUtil.getColumnIndexOrThrow(_cursor, "currentAtk");
          final int _cursorIndexOfCurrentDef = CursorUtil.getColumnIndexOrThrow(_cursor, "currentDef");
          final int _cursorIndexOfCurrentSpd = CursorUtil.getColumnIndexOrThrow(_cursor, "currentSpd");
          final int _cursorIndexOfSkill1Id = CursorUtil.getColumnIndexOrThrow(_cursor, "skill1Id");
          final int _cursorIndexOfSkill2Id = CursorUtil.getColumnIndexOrThrow(_cursor, "skill2Id");
          final int _cursorIndexOfImageAssetName = CursorUtil.getColumnIndexOrThrow(_cursor, "imageAssetName");
          final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CardEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpHeroId;
            _tmpHeroId = _cursor.getInt(_cursorIndexOfHeroId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Rarity _tmpRarity;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfRarity);
            _tmpRarity = __converters.toRarity(_tmp);
            final Element _tmpElement;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfElement);
            _tmpElement = __converters.toElement(_tmp_1);
            final Role _tmpRole;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfRole);
            _tmpRole = __converters.toRole(_tmp_2);
            final int _tmpLevel;
            _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
            final int _tmpXp;
            _tmpXp = _cursor.getInt(_cursorIndexOfXp);
            final int _tmpBaseHp;
            _tmpBaseHp = _cursor.getInt(_cursorIndexOfBaseHp);
            final int _tmpBaseAtk;
            _tmpBaseAtk = _cursor.getInt(_cursorIndexOfBaseAtk);
            final int _tmpBaseDef;
            _tmpBaseDef = _cursor.getInt(_cursorIndexOfBaseDef);
            final int _tmpBaseSpd;
            _tmpBaseSpd = _cursor.getInt(_cursorIndexOfBaseSpd);
            final int _tmpCurrentHp;
            _tmpCurrentHp = _cursor.getInt(_cursorIndexOfCurrentHp);
            final int _tmpCurrentAtk;
            _tmpCurrentAtk = _cursor.getInt(_cursorIndexOfCurrentAtk);
            final int _tmpCurrentDef;
            _tmpCurrentDef = _cursor.getInt(_cursorIndexOfCurrentDef);
            final int _tmpCurrentSpd;
            _tmpCurrentSpd = _cursor.getInt(_cursorIndexOfCurrentSpd);
            final int _tmpSkill1Id;
            _tmpSkill1Id = _cursor.getInt(_cursorIndexOfSkill1Id);
            final int _tmpSkill2Id;
            _tmpSkill2Id = _cursor.getInt(_cursorIndexOfSkill2Id);
            final String _tmpImageAssetName;
            _tmpImageAssetName = _cursor.getString(_cursorIndexOfImageAssetName);
            _item = new CardEntity(_tmpId,_tmpHeroId,_tmpName,_tmpRarity,_tmpElement,_tmpRole,_tmpLevel,_tmpXp,_tmpBaseHp,_tmpBaseAtk,_tmpBaseDef,_tmpBaseSpd,_tmpCurrentHp,_tmpCurrentAtk,_tmpCurrentDef,_tmpCurrentSpd,_tmpSkill1Id,_tmpSkill2Id,_tmpImageAssetName);
            _result.add(_item);
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
  public Flow<List<CardEntity>> getByElement(final Element element) {
    final String _sql = "SELECT * FROM cards WHERE element = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromElement(element);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cards"}, new Callable<List<CardEntity>>() {
      @Override
      @NonNull
      public List<CardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHeroId = CursorUtil.getColumnIndexOrThrow(_cursor, "heroId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfRarity = CursorUtil.getColumnIndexOrThrow(_cursor, "rarity");
          final int _cursorIndexOfElement = CursorUtil.getColumnIndexOrThrow(_cursor, "element");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfXp = CursorUtil.getColumnIndexOrThrow(_cursor, "xp");
          final int _cursorIndexOfBaseHp = CursorUtil.getColumnIndexOrThrow(_cursor, "baseHp");
          final int _cursorIndexOfBaseAtk = CursorUtil.getColumnIndexOrThrow(_cursor, "baseAtk");
          final int _cursorIndexOfBaseDef = CursorUtil.getColumnIndexOrThrow(_cursor, "baseDef");
          final int _cursorIndexOfBaseSpd = CursorUtil.getColumnIndexOrThrow(_cursor, "baseSpd");
          final int _cursorIndexOfCurrentHp = CursorUtil.getColumnIndexOrThrow(_cursor, "currentHp");
          final int _cursorIndexOfCurrentAtk = CursorUtil.getColumnIndexOrThrow(_cursor, "currentAtk");
          final int _cursorIndexOfCurrentDef = CursorUtil.getColumnIndexOrThrow(_cursor, "currentDef");
          final int _cursorIndexOfCurrentSpd = CursorUtil.getColumnIndexOrThrow(_cursor, "currentSpd");
          final int _cursorIndexOfSkill1Id = CursorUtil.getColumnIndexOrThrow(_cursor, "skill1Id");
          final int _cursorIndexOfSkill2Id = CursorUtil.getColumnIndexOrThrow(_cursor, "skill2Id");
          final int _cursorIndexOfImageAssetName = CursorUtil.getColumnIndexOrThrow(_cursor, "imageAssetName");
          final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CardEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpHeroId;
            _tmpHeroId = _cursor.getInt(_cursorIndexOfHeroId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Rarity _tmpRarity;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfRarity);
            _tmpRarity = __converters.toRarity(_tmp_1);
            final Element _tmpElement;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfElement);
            _tmpElement = __converters.toElement(_tmp_2);
            final Role _tmpRole;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfRole);
            _tmpRole = __converters.toRole(_tmp_3);
            final int _tmpLevel;
            _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
            final int _tmpXp;
            _tmpXp = _cursor.getInt(_cursorIndexOfXp);
            final int _tmpBaseHp;
            _tmpBaseHp = _cursor.getInt(_cursorIndexOfBaseHp);
            final int _tmpBaseAtk;
            _tmpBaseAtk = _cursor.getInt(_cursorIndexOfBaseAtk);
            final int _tmpBaseDef;
            _tmpBaseDef = _cursor.getInt(_cursorIndexOfBaseDef);
            final int _tmpBaseSpd;
            _tmpBaseSpd = _cursor.getInt(_cursorIndexOfBaseSpd);
            final int _tmpCurrentHp;
            _tmpCurrentHp = _cursor.getInt(_cursorIndexOfCurrentHp);
            final int _tmpCurrentAtk;
            _tmpCurrentAtk = _cursor.getInt(_cursorIndexOfCurrentAtk);
            final int _tmpCurrentDef;
            _tmpCurrentDef = _cursor.getInt(_cursorIndexOfCurrentDef);
            final int _tmpCurrentSpd;
            _tmpCurrentSpd = _cursor.getInt(_cursorIndexOfCurrentSpd);
            final int _tmpSkill1Id;
            _tmpSkill1Id = _cursor.getInt(_cursorIndexOfSkill1Id);
            final int _tmpSkill2Id;
            _tmpSkill2Id = _cursor.getInt(_cursorIndexOfSkill2Id);
            final String _tmpImageAssetName;
            _tmpImageAssetName = _cursor.getString(_cursorIndexOfImageAssetName);
            _item = new CardEntity(_tmpId,_tmpHeroId,_tmpName,_tmpRarity,_tmpElement,_tmpRole,_tmpLevel,_tmpXp,_tmpBaseHp,_tmpBaseAtk,_tmpBaseDef,_tmpBaseSpd,_tmpCurrentHp,_tmpCurrentAtk,_tmpCurrentDef,_tmpCurrentSpd,_tmpSkill1Id,_tmpSkill2Id,_tmpImageAssetName);
            _result.add(_item);
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
  public Flow<List<CardEntity>> getByRarity(final Rarity rarity) {
    final String _sql = "SELECT * FROM cards WHERE rarity = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromRarity(rarity);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cards"}, new Callable<List<CardEntity>>() {
      @Override
      @NonNull
      public List<CardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHeroId = CursorUtil.getColumnIndexOrThrow(_cursor, "heroId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfRarity = CursorUtil.getColumnIndexOrThrow(_cursor, "rarity");
          final int _cursorIndexOfElement = CursorUtil.getColumnIndexOrThrow(_cursor, "element");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfXp = CursorUtil.getColumnIndexOrThrow(_cursor, "xp");
          final int _cursorIndexOfBaseHp = CursorUtil.getColumnIndexOrThrow(_cursor, "baseHp");
          final int _cursorIndexOfBaseAtk = CursorUtil.getColumnIndexOrThrow(_cursor, "baseAtk");
          final int _cursorIndexOfBaseDef = CursorUtil.getColumnIndexOrThrow(_cursor, "baseDef");
          final int _cursorIndexOfBaseSpd = CursorUtil.getColumnIndexOrThrow(_cursor, "baseSpd");
          final int _cursorIndexOfCurrentHp = CursorUtil.getColumnIndexOrThrow(_cursor, "currentHp");
          final int _cursorIndexOfCurrentAtk = CursorUtil.getColumnIndexOrThrow(_cursor, "currentAtk");
          final int _cursorIndexOfCurrentDef = CursorUtil.getColumnIndexOrThrow(_cursor, "currentDef");
          final int _cursorIndexOfCurrentSpd = CursorUtil.getColumnIndexOrThrow(_cursor, "currentSpd");
          final int _cursorIndexOfSkill1Id = CursorUtil.getColumnIndexOrThrow(_cursor, "skill1Id");
          final int _cursorIndexOfSkill2Id = CursorUtil.getColumnIndexOrThrow(_cursor, "skill2Id");
          final int _cursorIndexOfImageAssetName = CursorUtil.getColumnIndexOrThrow(_cursor, "imageAssetName");
          final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CardEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpHeroId;
            _tmpHeroId = _cursor.getInt(_cursorIndexOfHeroId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Rarity _tmpRarity;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfRarity);
            _tmpRarity = __converters.toRarity(_tmp_1);
            final Element _tmpElement;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfElement);
            _tmpElement = __converters.toElement(_tmp_2);
            final Role _tmpRole;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfRole);
            _tmpRole = __converters.toRole(_tmp_3);
            final int _tmpLevel;
            _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
            final int _tmpXp;
            _tmpXp = _cursor.getInt(_cursorIndexOfXp);
            final int _tmpBaseHp;
            _tmpBaseHp = _cursor.getInt(_cursorIndexOfBaseHp);
            final int _tmpBaseAtk;
            _tmpBaseAtk = _cursor.getInt(_cursorIndexOfBaseAtk);
            final int _tmpBaseDef;
            _tmpBaseDef = _cursor.getInt(_cursorIndexOfBaseDef);
            final int _tmpBaseSpd;
            _tmpBaseSpd = _cursor.getInt(_cursorIndexOfBaseSpd);
            final int _tmpCurrentHp;
            _tmpCurrentHp = _cursor.getInt(_cursorIndexOfCurrentHp);
            final int _tmpCurrentAtk;
            _tmpCurrentAtk = _cursor.getInt(_cursorIndexOfCurrentAtk);
            final int _tmpCurrentDef;
            _tmpCurrentDef = _cursor.getInt(_cursorIndexOfCurrentDef);
            final int _tmpCurrentSpd;
            _tmpCurrentSpd = _cursor.getInt(_cursorIndexOfCurrentSpd);
            final int _tmpSkill1Id;
            _tmpSkill1Id = _cursor.getInt(_cursorIndexOfSkill1Id);
            final int _tmpSkill2Id;
            _tmpSkill2Id = _cursor.getInt(_cursorIndexOfSkill2Id);
            final String _tmpImageAssetName;
            _tmpImageAssetName = _cursor.getString(_cursorIndexOfImageAssetName);
            _item = new CardEntity(_tmpId,_tmpHeroId,_tmpName,_tmpRarity,_tmpElement,_tmpRole,_tmpLevel,_tmpXp,_tmpBaseHp,_tmpBaseAtk,_tmpBaseDef,_tmpBaseSpd,_tmpCurrentHp,_tmpCurrentAtk,_tmpCurrentDef,_tmpCurrentSpd,_tmpSkill1Id,_tmpSkill2Id,_tmpImageAssetName);
            _result.add(_item);
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
  public Flow<CardEntity> getById(final int id) {
    final String _sql = "SELECT * FROM cards WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cards"}, new Callable<CardEntity>() {
      @Override
      @Nullable
      public CardEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHeroId = CursorUtil.getColumnIndexOrThrow(_cursor, "heroId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfRarity = CursorUtil.getColumnIndexOrThrow(_cursor, "rarity");
          final int _cursorIndexOfElement = CursorUtil.getColumnIndexOrThrow(_cursor, "element");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfXp = CursorUtil.getColumnIndexOrThrow(_cursor, "xp");
          final int _cursorIndexOfBaseHp = CursorUtil.getColumnIndexOrThrow(_cursor, "baseHp");
          final int _cursorIndexOfBaseAtk = CursorUtil.getColumnIndexOrThrow(_cursor, "baseAtk");
          final int _cursorIndexOfBaseDef = CursorUtil.getColumnIndexOrThrow(_cursor, "baseDef");
          final int _cursorIndexOfBaseSpd = CursorUtil.getColumnIndexOrThrow(_cursor, "baseSpd");
          final int _cursorIndexOfCurrentHp = CursorUtil.getColumnIndexOrThrow(_cursor, "currentHp");
          final int _cursorIndexOfCurrentAtk = CursorUtil.getColumnIndexOrThrow(_cursor, "currentAtk");
          final int _cursorIndexOfCurrentDef = CursorUtil.getColumnIndexOrThrow(_cursor, "currentDef");
          final int _cursorIndexOfCurrentSpd = CursorUtil.getColumnIndexOrThrow(_cursor, "currentSpd");
          final int _cursorIndexOfSkill1Id = CursorUtil.getColumnIndexOrThrow(_cursor, "skill1Id");
          final int _cursorIndexOfSkill2Id = CursorUtil.getColumnIndexOrThrow(_cursor, "skill2Id");
          final int _cursorIndexOfImageAssetName = CursorUtil.getColumnIndexOrThrow(_cursor, "imageAssetName");
          final CardEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpHeroId;
            _tmpHeroId = _cursor.getInt(_cursorIndexOfHeroId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Rarity _tmpRarity;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfRarity);
            _tmpRarity = __converters.toRarity(_tmp);
            final Element _tmpElement;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfElement);
            _tmpElement = __converters.toElement(_tmp_1);
            final Role _tmpRole;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfRole);
            _tmpRole = __converters.toRole(_tmp_2);
            final int _tmpLevel;
            _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
            final int _tmpXp;
            _tmpXp = _cursor.getInt(_cursorIndexOfXp);
            final int _tmpBaseHp;
            _tmpBaseHp = _cursor.getInt(_cursorIndexOfBaseHp);
            final int _tmpBaseAtk;
            _tmpBaseAtk = _cursor.getInt(_cursorIndexOfBaseAtk);
            final int _tmpBaseDef;
            _tmpBaseDef = _cursor.getInt(_cursorIndexOfBaseDef);
            final int _tmpBaseSpd;
            _tmpBaseSpd = _cursor.getInt(_cursorIndexOfBaseSpd);
            final int _tmpCurrentHp;
            _tmpCurrentHp = _cursor.getInt(_cursorIndexOfCurrentHp);
            final int _tmpCurrentAtk;
            _tmpCurrentAtk = _cursor.getInt(_cursorIndexOfCurrentAtk);
            final int _tmpCurrentDef;
            _tmpCurrentDef = _cursor.getInt(_cursorIndexOfCurrentDef);
            final int _tmpCurrentSpd;
            _tmpCurrentSpd = _cursor.getInt(_cursorIndexOfCurrentSpd);
            final int _tmpSkill1Id;
            _tmpSkill1Id = _cursor.getInt(_cursorIndexOfSkill1Id);
            final int _tmpSkill2Id;
            _tmpSkill2Id = _cursor.getInt(_cursorIndexOfSkill2Id);
            final String _tmpImageAssetName;
            _tmpImageAssetName = _cursor.getString(_cursorIndexOfImageAssetName);
            _result = new CardEntity(_tmpId,_tmpHeroId,_tmpName,_tmpRarity,_tmpElement,_tmpRole,_tmpLevel,_tmpXp,_tmpBaseHp,_tmpBaseAtk,_tmpBaseDef,_tmpBaseSpd,_tmpCurrentHp,_tmpCurrentAtk,_tmpCurrentDef,_tmpCurrentSpd,_tmpSkill1Id,_tmpSkill2Id,_tmpImageAssetName);
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
