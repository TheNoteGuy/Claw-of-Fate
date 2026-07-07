package com.yourteam.cardgacharpg.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.yourteam.cardgacharpg.feature.collection.data.CardDao;
import com.yourteam.cardgacharpg.feature.collection.data.CardDao_Impl;
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao;
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile CardDao _cardDao;

  private volatile InventoryDao _inventoryDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `cards` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `heroId` INTEGER NOT NULL, `name` TEXT NOT NULL, `rarity` TEXT NOT NULL, `element` TEXT NOT NULL, `role` TEXT NOT NULL, `level` INTEGER NOT NULL, `xp` INTEGER NOT NULL, `baseHp` INTEGER NOT NULL, `baseAtk` INTEGER NOT NULL, `baseDef` INTEGER NOT NULL, `baseSpd` INTEGER NOT NULL, `currentHp` INTEGER NOT NULL, `currentAtk` INTEGER NOT NULL, `currentDef` INTEGER NOT NULL, `currentSpd` INTEGER NOT NULL, `skill1Id` INTEGER NOT NULL, `skill2Id` INTEGER NOT NULL, `imageAssetName` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `inventory` (`itemType` TEXT NOT NULL, `amount` INTEGER NOT NULL, PRIMARY KEY(`itemType`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0930d4c4b4c11721a62ac3483e8d8c97')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `cards`");
        db.execSQL("DROP TABLE IF EXISTS `inventory`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCards = new HashMap<String, TableInfo.Column>(19);
        _columnsCards.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("heroId", new TableInfo.Column("heroId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("rarity", new TableInfo.Column("rarity", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("element", new TableInfo.Column("element", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("level", new TableInfo.Column("level", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("xp", new TableInfo.Column("xp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("baseHp", new TableInfo.Column("baseHp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("baseAtk", new TableInfo.Column("baseAtk", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("baseDef", new TableInfo.Column("baseDef", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("baseSpd", new TableInfo.Column("baseSpd", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("currentHp", new TableInfo.Column("currentHp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("currentAtk", new TableInfo.Column("currentAtk", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("currentDef", new TableInfo.Column("currentDef", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("currentSpd", new TableInfo.Column("currentSpd", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("skill1Id", new TableInfo.Column("skill1Id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("skill2Id", new TableInfo.Column("skill2Id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("imageAssetName", new TableInfo.Column("imageAssetName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCards = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCards = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCards = new TableInfo("cards", _columnsCards, _foreignKeysCards, _indicesCards);
        final TableInfo _existingCards = TableInfo.read(db, "cards");
        if (!_infoCards.equals(_existingCards)) {
          return new RoomOpenHelper.ValidationResult(false, "cards(com.yourteam.cardgacharpg.feature.collection.data.CardEntity).\n"
                  + " Expected:\n" + _infoCards + "\n"
                  + " Found:\n" + _existingCards);
        }
        final HashMap<String, TableInfo.Column> _columnsInventory = new HashMap<String, TableInfo.Column>(2);
        _columnsInventory.put("itemType", new TableInfo.Column("itemType", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventory.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysInventory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesInventory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoInventory = new TableInfo("inventory", _columnsInventory, _foreignKeysInventory, _indicesInventory);
        final TableInfo _existingInventory = TableInfo.read(db, "inventory");
        if (!_infoInventory.equals(_existingInventory)) {
          return new RoomOpenHelper.ValidationResult(false, "inventory(com.yourteam.cardgacharpg.feature.collection.data.InventoryEntity).\n"
                  + " Expected:\n" + _infoInventory + "\n"
                  + " Found:\n" + _existingInventory);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "0930d4c4b4c11721a62ac3483e8d8c97", "6d40f35e9434aef7d1dd0f6829f9fd9f");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "cards","inventory");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `cards`");
      _db.execSQL("DELETE FROM `inventory`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CardDao.class, CardDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(InventoryDao.class, InventoryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CardDao cardDao() {
    if (_cardDao != null) {
      return _cardDao;
    } else {
      synchronized(this) {
        if(_cardDao == null) {
          _cardDao = new CardDao_Impl(this);
        }
        return _cardDao;
      }
    }
  }

  @Override
  public InventoryDao inventoryDao() {
    if (_inventoryDao != null) {
      return _inventoryDao;
    } else {
      synchronized(this) {
        if(_inventoryDao == null) {
          _inventoryDao = new InventoryDao_Impl(this);
        }
        return _inventoryDao;
      }
    }
  }
}
