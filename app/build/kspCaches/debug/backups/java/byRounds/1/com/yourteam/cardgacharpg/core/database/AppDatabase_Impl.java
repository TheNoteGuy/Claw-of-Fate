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
import com.yourteam.cardgacharpg.feature.arena.data.ArenaDao;
import com.yourteam.cardgacharpg.feature.arena.data.ArenaDao_Impl;
import com.yourteam.cardgacharpg.feature.battle.data.FormationDao;
import com.yourteam.cardgacharpg.feature.battle.data.FormationDao_Impl;
import com.yourteam.cardgacharpg.feature.campaign.data.LevelProgressDao;
import com.yourteam.cardgacharpg.feature.campaign.data.LevelProgressDao_Impl;
import com.yourteam.cardgacharpg.feature.collection.data.CardDao;
import com.yourteam.cardgacharpg.feature.collection.data.CardDao_Impl;
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao;
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao_Impl;
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyDao;
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyDao_Impl;
import com.yourteam.cardgacharpg.feature.gacha.data.GachaPityDao;
import com.yourteam.cardgacharpg.feature.gacha.data.GachaPityDao_Impl;
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

  private volatile GachaPityDao _gachaPityDao;

  private volatile CurrencyDao _currencyDao;

  private volatile ArenaDao _arenaDao;

  private volatile LevelProgressDao _levelProgressDao;

  private volatile FormationDao _formationDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `cards` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `heroId` INTEGER NOT NULL, `name` TEXT NOT NULL, `rarity` TEXT NOT NULL, `element` TEXT NOT NULL, `role` TEXT NOT NULL, `level` INTEGER NOT NULL, `xp` INTEGER NOT NULL, `baseHp` INTEGER NOT NULL, `baseAtk` INTEGER NOT NULL, `baseDef` INTEGER NOT NULL, `baseSpd` INTEGER NOT NULL, `currentHp` INTEGER NOT NULL, `currentAtk` INTEGER NOT NULL, `currentDef` INTEGER NOT NULL, `currentSpd` INTEGER NOT NULL, `skill1Id` INTEGER NOT NULL, `skill2Id` INTEGER NOT NULL, `imageAssetName` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `inventory` (`itemType` TEXT NOT NULL, `amount` INTEGER NOT NULL, PRIMARY KEY(`itemType`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `gacha_pity` (`id` INTEGER NOT NULL, `pityCount` INTEGER NOT NULL, `lastPullTimestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `currency` (`id` INTEGER NOT NULL, `gems` INTEGER NOT NULL, `gold` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `arena_profile` (`id` INTEGER NOT NULL, `trophies` INTEGER NOT NULL, `weeklyArenaCount` INTEGER NOT NULL, `lastRewardTimestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `level_progress` (`levelId` INTEGER NOT NULL, `isUnlocked` INTEGER NOT NULL, `stars` INTEGER NOT NULL, PRIMARY KEY(`levelId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `formation` (`id` INTEGER NOT NULL, `slot0` INTEGER, `slot1` INTEGER, `slot2` INTEGER, `slot3` INTEGER, `slot4` INTEGER, `slot5` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'bd74989a9db6f87b9872c592211c29f9')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `cards`");
        db.execSQL("DROP TABLE IF EXISTS `inventory`");
        db.execSQL("DROP TABLE IF EXISTS `gacha_pity`");
        db.execSQL("DROP TABLE IF EXISTS `currency`");
        db.execSQL("DROP TABLE IF EXISTS `arena_profile`");
        db.execSQL("DROP TABLE IF EXISTS `level_progress`");
        db.execSQL("DROP TABLE IF EXISTS `formation`");
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
        final HashMap<String, TableInfo.Column> _columnsGachaPity = new HashMap<String, TableInfo.Column>(3);
        _columnsGachaPity.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGachaPity.put("pityCount", new TableInfo.Column("pityCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGachaPity.put("lastPullTimestamp", new TableInfo.Column("lastPullTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGachaPity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGachaPity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGachaPity = new TableInfo("gacha_pity", _columnsGachaPity, _foreignKeysGachaPity, _indicesGachaPity);
        final TableInfo _existingGachaPity = TableInfo.read(db, "gacha_pity");
        if (!_infoGachaPity.equals(_existingGachaPity)) {
          return new RoomOpenHelper.ValidationResult(false, "gacha_pity(com.yourteam.cardgacharpg.feature.gacha.data.GachaPityEntity).\n"
                  + " Expected:\n" + _infoGachaPity + "\n"
                  + " Found:\n" + _existingGachaPity);
        }
        final HashMap<String, TableInfo.Column> _columnsCurrency = new HashMap<String, TableInfo.Column>(3);
        _columnsCurrency.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrency.put("gems", new TableInfo.Column("gems", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCurrency.put("gold", new TableInfo.Column("gold", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCurrency = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCurrency = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCurrency = new TableInfo("currency", _columnsCurrency, _foreignKeysCurrency, _indicesCurrency);
        final TableInfo _existingCurrency = TableInfo.read(db, "currency");
        if (!_infoCurrency.equals(_existingCurrency)) {
          return new RoomOpenHelper.ValidationResult(false, "currency(com.yourteam.cardgacharpg.feature.gacha.data.CurrencyEntity).\n"
                  + " Expected:\n" + _infoCurrency + "\n"
                  + " Found:\n" + _existingCurrency);
        }
        final HashMap<String, TableInfo.Column> _columnsArenaProfile = new HashMap<String, TableInfo.Column>(4);
        _columnsArenaProfile.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsArenaProfile.put("trophies", new TableInfo.Column("trophies", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsArenaProfile.put("weeklyArenaCount", new TableInfo.Column("weeklyArenaCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsArenaProfile.put("lastRewardTimestamp", new TableInfo.Column("lastRewardTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysArenaProfile = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesArenaProfile = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoArenaProfile = new TableInfo("arena_profile", _columnsArenaProfile, _foreignKeysArenaProfile, _indicesArenaProfile);
        final TableInfo _existingArenaProfile = TableInfo.read(db, "arena_profile");
        if (!_infoArenaProfile.equals(_existingArenaProfile)) {
          return new RoomOpenHelper.ValidationResult(false, "arena_profile(com.yourteam.cardgacharpg.feature.arena.data.ArenaProfileEntity).\n"
                  + " Expected:\n" + _infoArenaProfile + "\n"
                  + " Found:\n" + _existingArenaProfile);
        }
        final HashMap<String, TableInfo.Column> _columnsLevelProgress = new HashMap<String, TableInfo.Column>(3);
        _columnsLevelProgress.put("levelId", new TableInfo.Column("levelId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLevelProgress.put("isUnlocked", new TableInfo.Column("isUnlocked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLevelProgress.put("stars", new TableInfo.Column("stars", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLevelProgress = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLevelProgress = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLevelProgress = new TableInfo("level_progress", _columnsLevelProgress, _foreignKeysLevelProgress, _indicesLevelProgress);
        final TableInfo _existingLevelProgress = TableInfo.read(db, "level_progress");
        if (!_infoLevelProgress.equals(_existingLevelProgress)) {
          return new RoomOpenHelper.ValidationResult(false, "level_progress(com.yourteam.cardgacharpg.feature.campaign.data.LevelProgressEntity).\n"
                  + " Expected:\n" + _infoLevelProgress + "\n"
                  + " Found:\n" + _existingLevelProgress);
        }
        final HashMap<String, TableInfo.Column> _columnsFormation = new HashMap<String, TableInfo.Column>(7);
        _columnsFormation.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormation.put("slot0", new TableInfo.Column("slot0", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormation.put("slot1", new TableInfo.Column("slot1", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormation.put("slot2", new TableInfo.Column("slot2", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormation.put("slot3", new TableInfo.Column("slot3", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormation.put("slot4", new TableInfo.Column("slot4", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormation.put("slot5", new TableInfo.Column("slot5", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFormation = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFormation = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFormation = new TableInfo("formation", _columnsFormation, _foreignKeysFormation, _indicesFormation);
        final TableInfo _existingFormation = TableInfo.read(db, "formation");
        if (!_infoFormation.equals(_existingFormation)) {
          return new RoomOpenHelper.ValidationResult(false, "formation(com.yourteam.cardgacharpg.feature.battle.data.FormationEntity).\n"
                  + " Expected:\n" + _infoFormation + "\n"
                  + " Found:\n" + _existingFormation);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "bd74989a9db6f87b9872c592211c29f9", "c97219dc74fc5915b70da69c4990cb5e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "cards","inventory","gacha_pity","currency","arena_profile","level_progress","formation");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `cards`");
      _db.execSQL("DELETE FROM `inventory`");
      _db.execSQL("DELETE FROM `gacha_pity`");
      _db.execSQL("DELETE FROM `currency`");
      _db.execSQL("DELETE FROM `arena_profile`");
      _db.execSQL("DELETE FROM `level_progress`");
      _db.execSQL("DELETE FROM `formation`");
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
    _typeConvertersMap.put(GachaPityDao.class, GachaPityDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CurrencyDao.class, CurrencyDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ArenaDao.class, ArenaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LevelProgressDao.class, LevelProgressDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FormationDao.class, FormationDao_Impl.getRequiredConverters());
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

  @Override
  public GachaPityDao gachaPityDao() {
    if (_gachaPityDao != null) {
      return _gachaPityDao;
    } else {
      synchronized(this) {
        if(_gachaPityDao == null) {
          _gachaPityDao = new GachaPityDao_Impl(this);
        }
        return _gachaPityDao;
      }
    }
  }

  @Override
  public CurrencyDao currencyDao() {
    if (_currencyDao != null) {
      return _currencyDao;
    } else {
      synchronized(this) {
        if(_currencyDao == null) {
          _currencyDao = new CurrencyDao_Impl(this);
        }
        return _currencyDao;
      }
    }
  }

  @Override
  public ArenaDao arenaDao() {
    if (_arenaDao != null) {
      return _arenaDao;
    } else {
      synchronized(this) {
        if(_arenaDao == null) {
          _arenaDao = new ArenaDao_Impl(this);
        }
        return _arenaDao;
      }
    }
  }

  @Override
  public LevelProgressDao levelProgressDao() {
    if (_levelProgressDao != null) {
      return _levelProgressDao;
    } else {
      synchronized(this) {
        if(_levelProgressDao == null) {
          _levelProgressDao = new LevelProgressDao_Impl(this);
        }
        return _levelProgressDao;
      }
    }
  }

  @Override
  public FormationDao formationDao() {
    if (_formationDao != null) {
      return _formationDao;
    } else {
      synchronized(this) {
        if(_formationDao == null) {
          _formationDao = new FormationDao_Impl(this);
        }
        return _formationDao;
      }
    }
  }
}
