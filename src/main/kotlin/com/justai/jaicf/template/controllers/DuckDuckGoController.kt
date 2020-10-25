package com.justai.jaicf.template.controllers

import com.justai.jaicf.context.BotContext
import com.justai.jaicf.template.extra.DuckDuckGoClient

class DuckDuckGoController(context: BotContext)  {
    var abstract: String by context.session

    fun query(s: String): String? {
        val cl = DuckDuckGoClient()
        return cl.getDefinition(s)
    }
}