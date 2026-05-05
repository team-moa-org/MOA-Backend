package moa.moabackend.domain.user.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "tbl_user",
    indexes = [
        Index(name = "idx_user_email", columnList = "email")
    ]
)
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false, unique = true, length = 255)
    val email: String,

    @Column(nullable = false, length = 50)
    val name: String,

    @Column(nullable = false, length = 255)
    val password: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    val role: Role,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column
    var profileImageUrl: String? = null,

    @Column(length = 50)
    var farmName: String? = null,

    @Column(length = 20)
    var phoneNumber: String? = null,

    @Column
    var deletedAt: LocalDateTime? = null

) {
    fun softDelete() {
        this.deletedAt = LocalDateTime.now()
    }

    fun isDeleted(): Boolean = deletedAt != null
}
