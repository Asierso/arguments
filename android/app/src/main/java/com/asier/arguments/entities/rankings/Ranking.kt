package com.asier.arguments.entities.rankings

data class Ranking (
    var id : String,
    var discussion : String,
    var ranking: HashMap<String,Int>,
    var xpPoints: HashMap<String,Int>,
    var paimonVote: String,
    var title: String
)