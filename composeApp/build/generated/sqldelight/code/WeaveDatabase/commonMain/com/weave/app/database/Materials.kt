package com.weave.app.database

import kotlin.Long
import kotlin.String

public data class Materials(
  public val id: String,
  public val name: String,
  public val category: String,
  public val resource_path: String,
  public val rarity: String?,
  public val is_unlocked: Long?,
  public val unlock_condition: String?,
  public val acquired_at: Long?,
)
