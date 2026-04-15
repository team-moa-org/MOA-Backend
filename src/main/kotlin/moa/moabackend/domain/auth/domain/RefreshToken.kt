package moa.moabackend.domain.auth.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed

@RedisHash
class RefreshToken(

    @Id
    val token: String,

    @Indexed
    val userId: Long,

    @TimeToLive
    val ttl: Long
)
