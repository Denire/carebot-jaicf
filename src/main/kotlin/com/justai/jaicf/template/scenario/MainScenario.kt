package com.justai.jaicf.template.scenario

import com.justai.jaicf.model.scenario.Scenario

fun selectQuestion(): String {
    val questions = mutableListOf("Napoleon", "Supermarket")
    return questions.shuffled().take(1)[0]
}

object MainScenario : Scenario() {

    init {
        state("Start") {
            globalActivators {
                regex("/start")
            }
            action {
                reactions.say("So let's begin!")
            }
        }

        state("Obsession") {
            activators {
                intent("Obsession")
            }
            action {
                reactions.sayRandom("Марк, давай поговорим об этом позже.", "Об этом я тебе еще расскажу.")
                reactions.go("/Ask")
            }
        }

        state("Ask") {
            action {
                val question = selectQuestion()
                reactions.go(question)
            }

            state("Napoleon") {
                action {
		            reactions.say("А сейчас скажи, что тебе больше нравится: Медовик или Наполеон?")
                }
            }

            state("Supermarket") {
                action {
                    reactions.say("А сейчас давай поиграем.")
                    reactions.say("Вот ты приходишь в магазин и например я продавец. Ты должен что-то купить, например, мама или папа тебя попросили: у нас закончились хлеб и молоко, сходи купи пожалуйста в магазин.")
                }
            }
        }

        state("Hello") {
            activators {
                intent("Hello")
            }

            action {
                reactions.say("Hi ther!")
            }
        }

        state("Time") {
            activators {
                intent("Time")
            }

            action {
                reactions.say("Самое время!")
            }
        }

        state("Bye") {
            activators {
                intent("Bye")
            }

            action {
                reactions.say("See you soon!")
            }
        }

        fallback {
            reactions.say("I have nothing to say yet...")
        }
    }
}
