package com.justai.jaicf.template.controllers

import com.justai.jaicf.context.BotContext

class InitiativeController(context: BotContext) {
    val nexts = mutableListOf(
            Pair("Food", "Давай лучше поговорим про еду?"),
            Pair("Supermarket", "А давай сыграем в супермаркет?"),
            Pair("BadAdv", "А давай поиграем во вредные советы?"),
            Pair("Friend", "А давай сыграем в назойливого друга?"),
            Pair("Art", "Давай лучше поговорим про искусство?"),
            Pair("Music", "Давай лучше обсудим музыку?"),
            Pair("Cop", "А давай сыграем в полицейского?"))
    fun nextSelect() : Pair<String,String>  {
        return nexts.shuffled().take(1)[0]
    }
    var selected : Pair<String,String> by context.session
}