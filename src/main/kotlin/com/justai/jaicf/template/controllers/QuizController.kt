package com.justai.jaicf.template.controllers

import com.justai.jaicf.context.BotContext


class QuizController(context: BotContext) {
    val images:MutableList<Triple<String,String,String>> = mutableListOf(
            Triple("https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Austin_Texas_Lake_Front.jpg/800px-Austin_Texas_Lake_Front.jpg",
                    "Остин, штат Техас", "Answer:Texas"),
            Triple("https://upload.wikimedia.org/wikipedia/commons/4/47/Texas_State_Capitol_Night.jpg",
                    "Капитолий штата Техас в Остине", "Answer:Texas"),
            Triple("https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Johnson_Space_Center_Main_Entry_Gate_%281%29.jpg/1024px-Johnson_Space_Center_Main_Entry_Gate_%281%29.jpg",
                    "Космический центр имени Линдона Джонсона, город Хьюстон, штат Техас", "Answer:Texas"),
            Triple("https://upload.wikimedia.org/wikipedia/commons/2/2b/Sangre_de_Christo_Mountains-Winter_sunset.jpg",
                    "горный массив Сангре-де-Кристо, штат Нью-Мехико", "Answer:NewMexico"),
            Triple("https://upload.wikimedia.org/wikipedia/commons/f/f9/Hotel_Santa_Fe_New_Mexico.jpg",
                    "Санта-Фе, Нью-Мехико", "Answer:NewMexico"),
            Triple("https://upload.wikimedia.org/wikipedia/commons/b/ba/Albuquerque_aerial.jpg",
                    "Альбукерке, Нью-Мехико", "Answer:NewMexico"),
            Triple("https://upload.wikimedia.org/wikipedia/commons/3/36/Islamorada_Florida.jpg",
                    "поселок Исламорада во Флориде", "Answer:Florida"),
            Triple("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0e/2007_Miami_sunset_3.jpg/1280px-2007_Miami_sunset_3.jpg",
                    "Майами, штат Флорида", "Answer:Florida"),
            Triple("https://upload.wikimedia.org/wikipedia/commons/thumb/b/bb/Biscayne_Boulevard_night_20101202.jpg/1920px-Biscayne_Boulevard_night_20101202.jpg",
                    "Майами, штат Флорида", "Answer:Florida")
    )
    var asked: MutableList<String>? by context.session
    fun quizSelect() : Triple<String,String,String> {
        return images.shuffled().take(1)[0]
    }
    var selected : Triple<String,String,String> by context.session
    var attempt : Int? by context.session

}


