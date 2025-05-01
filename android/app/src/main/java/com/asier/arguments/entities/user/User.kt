package com.asier.arguments.entities.user

data class User(
    var firstname : String = "",
    var lastname : String = "",
    var username : String = "",
    var level : Int = 0,
    var xp: Int = 0,
    var description : String = "",
    var isActive : Boolean? = false,
    var history: HashMap<String,String>? = null)