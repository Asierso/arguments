package com.asier.arguments.entities

data class User(
    var firstname : String = "",
    var lastname : String = "",
    var username : String = "",
    var level : Int = 0,
    var xp: Int = 0,
    var description : String = "")