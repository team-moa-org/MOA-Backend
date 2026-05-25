package moa.moabackend.domain.grouppurchase.domain

enum class Category(val displayName: String) {
    FRUIT("과일"),
    VEGETABLE("채소"),
    GRAIN("곡물"),
    MEAT("육류"),
    SEAFOOD("수산물"),
    OTHER("기타")
}
