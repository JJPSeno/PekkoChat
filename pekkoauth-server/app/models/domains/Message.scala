package models.domains

import java.util.UUID

final case class Message(
  userId: UUID,
  username: String,
  message: Option[String] = None,
  photoType: Option[String] = None,
  photoBytes: Option[Array[Byte]] = None,
  id: UUID = UUID.randomUUID(),
)
