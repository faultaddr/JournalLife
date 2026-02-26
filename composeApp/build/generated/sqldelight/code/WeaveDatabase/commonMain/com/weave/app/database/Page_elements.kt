package com.weave.app.database

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class Page_elements(
  public val id: String,
  public val page_id: String,
  public val type: String,
  public val position_x: Double,
  public val position_y: Double,
  public val rotation: Double?,
  public val scale: Double?,
  public val z_index: Long?,
  public val data_: String,
)
