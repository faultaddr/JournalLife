package com.weave.app.database

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class Journals(
  public val id: String,
  public val title: String,
  public val cover_style: String,
  public val created_at: Long,
  public val updated_at: Long,
  public val page_count: Long?,
  public val thickness: Double?,
)
