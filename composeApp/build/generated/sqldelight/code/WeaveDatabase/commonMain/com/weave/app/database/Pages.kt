package com.weave.app.database

import kotlin.Long
import kotlin.String

public data class Pages(
  public val id: String,
  public val journal_id: String,
  public val date: Long,
  public val paper_style: String,
  public val background_color: Long?,
  public val created_at: Long,
  public val updated_at: Long,
)
