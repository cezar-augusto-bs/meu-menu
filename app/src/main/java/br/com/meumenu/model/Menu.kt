package br.com.meumenu.model

data class Menu(
    var id: String? = "",
    var userId: String = "",
    var restaurantId : String = "",
    var categoryId : Int? = null,
    var name : String = "",
    var description : String = "",
    var price : Float = 0.0f
)
