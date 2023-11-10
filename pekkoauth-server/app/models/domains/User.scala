package models.domains

import java.util.UUID

final case class User(  
  val id: UUID = UUID.randomUUID(),
  val username: String,
  val password: String
)
