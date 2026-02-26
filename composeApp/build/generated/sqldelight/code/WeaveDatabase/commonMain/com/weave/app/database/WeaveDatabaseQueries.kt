package com.weave.app.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Double
import kotlin.Long
import kotlin.String

public class WeaveDatabaseQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> getAllJournals(mapper: (
    id: String,
    title: String,
    cover_style: String,
    created_at: Long,
    updated_at: Long,
    page_count: Long?,
    thickness: Double?,
  ) -> T): Query<T> = Query(403_130_169, arrayOf("journals"), driver, "WeaveDatabase.sq",
      "getAllJournals",
      "SELECT journals.id, journals.title, journals.cover_style, journals.created_at, journals.updated_at, journals.page_count, journals.thickness FROM journals ORDER BY updated_at DESC") {
      cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4)!!,
      cursor.getLong(5),
      cursor.getDouble(6)
    )
  }

  public fun getAllJournals(): Query<Journals> = getAllJournals { id, title, cover_style,
      created_at, updated_at, page_count, thickness ->
    Journals(
      id,
      title,
      cover_style,
      created_at,
      updated_at,
      page_count,
      thickness
    )
  }

  public fun <T : Any> getJournalById(id: String, mapper: (
    id: String,
    title: String,
    cover_style: String,
    created_at: Long,
    updated_at: Long,
    page_count: Long?,
    thickness: Double?,
  ) -> T): Query<T> = GetJournalByIdQuery(id) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4)!!,
      cursor.getLong(5),
      cursor.getDouble(6)
    )
  }

  public fun getJournalById(id: String): Query<Journals> = getJournalById(id) { id_, title,
      cover_style, created_at, updated_at, page_count, thickness ->
    Journals(
      id_,
      title,
      cover_style,
      created_at,
      updated_at,
      page_count,
      thickness
    )
  }

  public fun <T : Any> getPagesByJournal(journal_id: String, mapper: (
    id: String,
    journal_id: String,
    date: Long,
    paper_style: String,
    background_color: Long?,
    created_at: Long,
    updated_at: Long,
  ) -> T): Query<T> = GetPagesByJournalQuery(journal_id) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getLong(4),
      cursor.getLong(5)!!,
      cursor.getLong(6)!!
    )
  }

  public fun getPagesByJournal(journal_id: String): Query<Pages> = getPagesByJournal(journal_id) {
      id, journal_id_, date, paper_style, background_color, created_at, updated_at ->
    Pages(
      id,
      journal_id_,
      date,
      paper_style,
      background_color,
      created_at,
      updated_at
    )
  }

  public fun <T : Any> getPageById(id: String, mapper: (
    id: String,
    journal_id: String,
    date: Long,
    paper_style: String,
    background_color: Long?,
    created_at: Long,
    updated_at: Long,
  ) -> T): Query<T> = GetPageByIdQuery(id) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getLong(4),
      cursor.getLong(5)!!,
      cursor.getLong(6)!!
    )
  }

  public fun getPageById(id: String): Query<Pages> = getPageById(id) { id_, journal_id, date,
      paper_style, background_color, created_at, updated_at ->
    Pages(
      id_,
      journal_id,
      date,
      paper_style,
      background_color,
      created_at,
      updated_at
    )
  }

  public fun <T : Any> getPageByDate(
    journal_id: String,
    date: Long,
    mapper: (
      id: String,
      journal_id: String,
      date: Long,
      paper_style: String,
      background_color: Long?,
      created_at: Long,
      updated_at: Long,
    ) -> T,
  ): Query<T> = GetPageByDateQuery(journal_id, date) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getLong(4),
      cursor.getLong(5)!!,
      cursor.getLong(6)!!
    )
  }

  public fun getPageByDate(journal_id: String, date: Long): Query<Pages> = getPageByDate(journal_id,
      date) { id, journal_id_, date_, paper_style, background_color, created_at, updated_at ->
    Pages(
      id,
      journal_id_,
      date_,
      paper_style,
      background_color,
      created_at,
      updated_at
    )
  }

  public fun <T : Any> getElementsByPage(page_id: String, mapper: (
    id: String,
    page_id: String,
    type: String,
    position_x: Double,
    position_y: Double,
    rotation: Double?,
    scale: Double?,
    z_index: Long?,
    data_: String,
  ) -> T): Query<T> = GetElementsByPageQuery(page_id) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getDouble(3)!!,
      cursor.getDouble(4)!!,
      cursor.getDouble(5),
      cursor.getDouble(6),
      cursor.getLong(7),
      cursor.getString(8)!!
    )
  }

  public fun getElementsByPage(page_id: String): Query<Page_elements> = getElementsByPage(page_id) {
      id, page_id_, type, position_x, position_y, rotation, scale, z_index, data_ ->
    Page_elements(
      id,
      page_id_,
      type,
      position_x,
      position_y,
      rotation,
      scale,
      z_index,
      data_
    )
  }

  public fun <T : Any> getAllMaterials(mapper: (
    id: String,
    name: String,
    category: String,
    resource_path: String,
    rarity: String?,
    is_unlocked: Long?,
    unlock_condition: String?,
    acquired_at: Long?,
  ) -> T): Query<T> = Query(-1_400_564_561, arrayOf("materials"), driver, "WeaveDatabase.sq",
      "getAllMaterials",
      "SELECT materials.id, materials.name, materials.category, materials.resource_path, materials.rarity, materials.is_unlocked, materials.unlock_condition, materials.acquired_at FROM materials ORDER BY category, rarity, name") {
      cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4),
      cursor.getLong(5),
      cursor.getString(6),
      cursor.getLong(7)
    )
  }

  public fun getAllMaterials(): Query<Materials> = getAllMaterials { id, name, category,
      resource_path, rarity, is_unlocked, unlock_condition, acquired_at ->
    Materials(
      id,
      name,
      category,
      resource_path,
      rarity,
      is_unlocked,
      unlock_condition,
      acquired_at
    )
  }

  public fun <T : Any> getMaterialsByCategory(category: String, mapper: (
    id: String,
    name: String,
    category: String,
    resource_path: String,
    rarity: String?,
    is_unlocked: Long?,
    unlock_condition: String?,
    acquired_at: Long?,
  ) -> T): Query<T> = GetMaterialsByCategoryQuery(category) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4),
      cursor.getLong(5),
      cursor.getString(6),
      cursor.getLong(7)
    )
  }

  public fun getMaterialsByCategory(category: String): Query<Materials> =
      getMaterialsByCategory(category) { id, name, category_, resource_path, rarity, is_unlocked,
      unlock_condition, acquired_at ->
    Materials(
      id,
      name,
      category_,
      resource_path,
      rarity,
      is_unlocked,
      unlock_condition,
      acquired_at
    )
  }

  public fun <T : Any> getUnlockedMaterials(mapper: (
    id: String,
    name: String,
    category: String,
    resource_path: String,
    rarity: String?,
    is_unlocked: Long?,
    unlock_condition: String?,
    acquired_at: Long?,
  ) -> T): Query<T> = Query(1_410_462_181, arrayOf("materials"), driver, "WeaveDatabase.sq",
      "getUnlockedMaterials",
      "SELECT materials.id, materials.name, materials.category, materials.resource_path, materials.rarity, materials.is_unlocked, materials.unlock_condition, materials.acquired_at FROM materials WHERE is_unlocked = 1 ORDER BY category, name") {
      cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4),
      cursor.getLong(5),
      cursor.getString(6),
      cursor.getLong(7)
    )
  }

  public fun getUnlockedMaterials(): Query<Materials> = getUnlockedMaterials { id, name, category,
      resource_path, rarity, is_unlocked, unlock_condition, acquired_at ->
    Materials(
      id,
      name,
      category,
      resource_path,
      rarity,
      is_unlocked,
      unlock_condition,
      acquired_at
    )
  }

  public fun <T : Any> getLockedMaterials(mapper: (
    id: String,
    name: String,
    category: String,
    resource_path: String,
    rarity: String?,
    is_unlocked: Long?,
    unlock_condition: String?,
    acquired_at: Long?,
  ) -> T): Query<T> = Query(-391_787_362, arrayOf("materials"), driver, "WeaveDatabase.sq",
      "getLockedMaterials",
      "SELECT materials.id, materials.name, materials.category, materials.resource_path, materials.rarity, materials.is_unlocked, materials.unlock_condition, materials.acquired_at FROM materials WHERE is_unlocked = 0 ORDER BY rarity DESC") {
      cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4),
      cursor.getLong(5),
      cursor.getString(6),
      cursor.getLong(7)
    )
  }

  public fun getLockedMaterials(): Query<Materials> = getLockedMaterials { id, name, category,
      resource_path, rarity, is_unlocked, unlock_condition, acquired_at ->
    Materials(
      id,
      name,
      category,
      resource_path,
      rarity,
      is_unlocked,
      unlock_condition,
      acquired_at
    )
  }

  public fun getJournalCount(): Query<Long> = Query(396_526_908, arrayOf("journals"), driver,
      "WeaveDatabase.sq", "getJournalCount", "SELECT COUNT(*) FROM journals") { cursor ->
    cursor.getLong(0)!!
  }

  public fun getPageCountByJournal(journal_id: String): Query<Long> =
      GetPageCountByJournalQuery(journal_id) { cursor ->
    cursor.getLong(0)!!
  }

  public fun getUnlockedMaterialCount(): Query<Long> = Query(836_520_289, arrayOf("materials"),
      driver, "WeaveDatabase.sq", "getUnlockedMaterialCount",
      "SELECT COUNT(*) FROM materials WHERE is_unlocked = 1") { cursor ->
    cursor.getLong(0)!!
  }

  public fun insertJournal(
    id: String,
    title: String,
    cover_style: String,
    created_at: Long,
    updated_at: Long,
    page_count: Long?,
    thickness: Double?,
  ) {
    driver.execute(1_317_657_708, """
        |INSERT INTO journals (id, title, cover_style, created_at, updated_at, page_count, thickness)
        |VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 7) {
          bindString(0, id)
          bindString(1, title)
          bindString(2, cover_style)
          bindLong(3, created_at)
          bindLong(4, updated_at)
          bindLong(5, page_count)
          bindDouble(6, thickness)
        }
    notifyQueries(1_317_657_708) { emit ->
      emit("journals")
    }
  }

  public fun updateJournal(
    title: String,
    cover_style: String,
    updated_at: Long,
    page_count: Long?,
    thickness: Double?,
    id: String,
  ) {
    driver.execute(333_214_812, """
        |UPDATE journals SET title = ?, cover_style = ?, updated_at = ?, page_count = ?, thickness = ?
        |WHERE id = ?
        """.trimMargin(), 6) {
          bindString(0, title)
          bindString(1, cover_style)
          bindLong(2, updated_at)
          bindLong(3, page_count)
          bindDouble(4, thickness)
          bindString(5, id)
        }
    notifyQueries(333_214_812) { emit ->
      emit("journals")
    }
  }

  public fun deleteJournal(id: String) {
    driver.execute(1_624_393_018, """DELETE FROM journals WHERE id = ?""", 1) {
          bindString(0, id)
        }
    notifyQueries(1_624_393_018) { emit ->
      emit("journals")
      emit("pages")
    }
  }

  public fun insertPage(
    id: String,
    journal_id: String,
    date: Long,
    paper_style: String,
    background_color: Long?,
    created_at: Long,
    updated_at: Long,
  ) {
    driver.execute(-1_121_433_222, """
        |INSERT INTO pages (id, journal_id, date, paper_style, background_color, created_at, updated_at)
        |VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 7) {
          bindString(0, id)
          bindString(1, journal_id)
          bindLong(2, date)
          bindString(3, paper_style)
          bindLong(4, background_color)
          bindLong(5, created_at)
          bindLong(6, updated_at)
        }
    notifyQueries(-1_121_433_222) { emit ->
      emit("pages")
    }
  }

  public fun updatePage(
    paper_style: String,
    background_color: Long?,
    updated_at: Long,
    id: String,
  ) {
    driver.execute(1_911_437_194, """
        |UPDATE pages SET paper_style = ?, background_color = ?, updated_at = ?
        |WHERE id = ?
        """.trimMargin(), 4) {
          bindString(0, paper_style)
          bindLong(1, background_color)
          bindLong(2, updated_at)
          bindString(3, id)
        }
    notifyQueries(1_911_437_194) { emit ->
      emit("pages")
    }
  }

  public fun deletePage(id: String) {
    driver.execute(-323_009_684, """DELETE FROM pages WHERE id = ?""", 1) {
          bindString(0, id)
        }
    notifyQueries(-323_009_684) { emit ->
      emit("page_elements")
      emit("pages")
    }
  }

  public fun insertElement(
    id: String,
    page_id: String,
    type: String,
    position_x: Double,
    position_y: Double,
    rotation: Double?,
    scale: Double?,
    z_index: Long?,
    data_: String,
  ) {
    driver.execute(1_074_285_617, """
        |INSERT INTO page_elements (id, page_id, type, position_x, position_y, rotation, scale, z_index, data)
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 9) {
          bindString(0, id)
          bindString(1, page_id)
          bindString(2, type)
          bindDouble(3, position_x)
          bindDouble(4, position_y)
          bindDouble(5, rotation)
          bindDouble(6, scale)
          bindLong(7, z_index)
          bindString(8, data_)
        }
    notifyQueries(1_074_285_617) { emit ->
      emit("page_elements")
    }
  }

  public fun updateElement(
    position_x: Double,
    position_y: Double,
    rotation: Double?,
    scale: Double?,
    z_index: Long?,
    data_: String,
    id: String,
  ) {
    driver.execute(89_842_721, """
        |UPDATE page_elements SET position_x = ?, position_y = ?, rotation = ?, scale = ?, z_index = ?, data = ?
        |WHERE id = ?
        """.trimMargin(), 7) {
          bindDouble(0, position_x)
          bindDouble(1, position_y)
          bindDouble(2, rotation)
          bindDouble(3, scale)
          bindLong(4, z_index)
          bindString(5, data_)
          bindString(6, id)
        }
    notifyQueries(89_842_721) { emit ->
      emit("page_elements")
    }
  }

  public fun deleteElement(id: String) {
    driver.execute(1_381_020_927, """DELETE FROM page_elements WHERE id = ?""", 1) {
          bindString(0, id)
        }
    notifyQueries(1_381_020_927) { emit ->
      emit("page_elements")
    }
  }

  public fun deleteElementsByPage(page_id: String) {
    driver.execute(2_070_145_018, """DELETE FROM page_elements WHERE page_id = ?""", 1) {
          bindString(0, page_id)
        }
    notifyQueries(2_070_145_018) { emit ->
      emit("page_elements")
    }
  }

  public fun insertMaterial(
    id: String,
    name: String,
    category: String,
    resource_path: String,
    rarity: String?,
    is_unlocked: Long?,
    unlock_condition: String?,
    acquired_at: Long?,
  ) {
    driver.execute(-749_478_254, """
        |INSERT INTO materials (id, name, category, resource_path, rarity, is_unlocked, unlock_condition, acquired_at)
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 8) {
          bindString(0, id)
          bindString(1, name)
          bindString(2, category)
          bindString(3, resource_path)
          bindString(4, rarity)
          bindLong(5, is_unlocked)
          bindString(6, unlock_condition)
          bindLong(7, acquired_at)
        }
    notifyQueries(-749_478_254) { emit ->
      emit("materials")
    }
  }

  public fun unlockMaterial(acquired_at: Long?, id: String) {
    driver.execute(2_115_452_957,
        """UPDATE materials SET is_unlocked = 1, acquired_at = ? WHERE id = ?""", 2) {
          bindLong(0, acquired_at)
          bindString(1, id)
        }
    notifyQueries(2_115_452_957) { emit ->
      emit("materials")
    }
  }

  private inner class GetJournalByIdQuery<out T : Any>(
    public val id: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("journals", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("journals", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_259_695_621,
        """SELECT journals.id, journals.title, journals.cover_style, journals.created_at, journals.updated_at, journals.page_count, journals.thickness FROM journals WHERE id = ?""",
        mapper, 1) {
      bindString(0, id)
    }

    override fun toString(): String = "WeaveDatabase.sq:getJournalById"
  }

  private inner class GetPagesByJournalQuery<out T : Any>(
    public val journal_id: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("pages", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("pages", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-2_018_715_744,
        """SELECT pages.id, pages.journal_id, pages.date, pages.paper_style, pages.background_color, pages.created_at, pages.updated_at FROM pages WHERE journal_id = ? ORDER BY date DESC""",
        mapper, 1) {
      bindString(0, journal_id)
    }

    override fun toString(): String = "WeaveDatabase.sq:getPagesByJournal"
  }

  private inner class GetPageByIdQuery<out T : Any>(
    public val id: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("pages", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("pages", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_645_253_083,
        """SELECT pages.id, pages.journal_id, pages.date, pages.paper_style, pages.background_color, pages.created_at, pages.updated_at FROM pages WHERE id = ?""",
        mapper, 1) {
      bindString(0, id)
    }

    override fun toString(): String = "WeaveDatabase.sq:getPageById"
  }

  private inner class GetPageByDateQuery<out T : Any>(
    public val journal_id: String,
    public val date: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("pages", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("pages", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-540_395_976,
        """SELECT pages.id, pages.journal_id, pages.date, pages.paper_style, pages.background_color, pages.created_at, pages.updated_at FROM pages WHERE journal_id = ? AND date = ?""",
        mapper, 2) {
      bindString(0, journal_id)
      bindLong(1, date)
    }

    override fun toString(): String = "WeaveDatabase.sq:getPageByDate"
  }

  private inner class GetElementsByPageQuery<out T : Any>(
    public val page_id: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("page_elements", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("page_elements", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-283_121_791,
        """SELECT page_elements.id, page_elements.page_id, page_elements.type, page_elements.position_x, page_elements.position_y, page_elements.rotation, page_elements.scale, page_elements.z_index, page_elements.data FROM page_elements WHERE page_id = ? ORDER BY z_index""",
        mapper, 1) {
      bindString(0, page_id)
    }

    override fun toString(): String = "WeaveDatabase.sq:getElementsByPage"
  }

  private inner class GetMaterialsByCategoryQuery<out T : Any>(
    public val category: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("materials", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("materials", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(369_885_725,
        """SELECT materials.id, materials.name, materials.category, materials.resource_path, materials.rarity, materials.is_unlocked, materials.unlock_condition, materials.acquired_at FROM materials WHERE category = ? ORDER BY rarity, name""",
        mapper, 1) {
      bindString(0, category)
    }

    override fun toString(): String = "WeaveDatabase.sq:getMaterialsByCategory"
  }

  private inner class GetPageCountByJournalQuery<out T : Any>(
    public val journal_id: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("pages", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("pages", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-801_500_892, """SELECT COUNT(*) FROM pages WHERE journal_id = ?""",
        mapper, 1) {
      bindString(0, journal_id)
    }

    override fun toString(): String = "WeaveDatabase.sq:getPageCountByJournal"
  }
}
