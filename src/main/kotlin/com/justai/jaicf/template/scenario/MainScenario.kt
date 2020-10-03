package com.justai.jaicf.template.scenario

import com.justai.jaicf.channel.aimybox.AimyboxEvent
import com.justai.jaicf.channel.aimybox.aimybox
import com.justai.jaicf.model.scenario.Scenario

fun selectQuestion(): String {
    val questions = mutableListOf("Cakes", "Supermarket")
    return questions.shuffled().take(1)[0]
}

object MainScenario : Scenario() {

    init {
        state("Start") {
            globalActivators {
                event(AimyboxEvent.START)
            }
            action {
                reactions.say("Ну что, начнем! Спроси у меня что-нибудь.")
            }
        }

        state("Obsession") {
            activators {
                intent("Obsession")
            }
            action {
//                reactions.sayRandom("Марк, давай поговорим об этом позже.", "Об этом я тебе еще расскажу.")
                reactions.say("<phoneme alphabet=\"ipa\" ph=\"məlɐko\">молоко</phoneme> или <phoneme alphabet=\"ipa\" ph=\"məl\">молоко</phoneme>")
                reactions.aimybox?.say("молоко", "<phoneme alphabet=\"ipa\" ph=\"məlɐko\">молоко</phoneme> или <phoneme alphabet=\"ipa\" ph=\"məl\">молоко</phoneme>")
                reactions.aimybox?.image("https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Austin_Texas_Lake_Front.jpg/800px-Austin_Texas_Lake_Front.jpg")
                reactions.go("/Ask")
            }
        }

        state("Initiate") {
            action {
                reactions.say("А сейчас можно я тебя сама кое о чем спрошу?")
            }
            state("Yes") {
                activators {
                    intent("Answer:Yes")
                }
                action {
                    reactions.go("/Ask")
                }
            }
            state("No") {
                activators {
                    intent("Answer:No")
                }
                action {
                    reactions.say("Ну ладно.")
                }
            }
        }

        state("Ask") {
            action {
                val question = selectQuestion()
                reactions.go(question)
            }

            state("Cakes") {
                action {
		            reactions.say("А сейчас скажи, что тебе больше нравится: Медовик или Наполеон?")
                }

                state("Napoleon") {
                    activators {
                        intent("Answer:Napoleon")
                    }
                    action {
                        reactions.say("Ммм да, это вкусно!")
                    }
                }
            }

            state("Supermarket") {
                var counter = 0
                action {
                    reactions.say("Вот ты приходишь в магазин и например я продавец. Ты должен что-то купить, например, мама или папа тебя попросили: у нас закончились хлеб и молоко, сходи купи пожалуйста в магазин.")
                }

                fallback {
                    counter += 1
                    println(counter)
                    if (counter > 3) {
                        reactions.go("../Cashier")
                    } else {
                        reactions.sayRandom("угу", "мхм", "что дальше?", "а дальше что?")
                    }

                }

                state("Cashier") {
                    action { reactions.say("Хорошо. а на кассе ты что говоришь?") }
                    fallback { reactions.go("../1") }
                    state("1") {
                        action { reactions.say("Марк, ну допустим вот я продавец, да? и если у тебя не хватает денег и ты взял молоко дороже, чем у тебя есть денег, и я говорю \"у вас не хватает ээ 15 рублей\".") }
                        fallback { reactions.go("../2") }
                        state("2") {
                            action { reactions.say("А если ты уже на кассе. Там за тобой стоят люди, они ждут, пока ты оплатишь покупку. Пожалуйста, оплатите покупку, не задерживайте остальных людей.") }
                            fallback { reactions.go("../end") }
                            state("end") {
                                action { reactions.say("Правильный ответ - это отойти от кассы, взять другое молоко и купить его.") }
                            }
                        }
                    }
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

        fallback {
            reactions.say("К сожалению, я не могу ничего сказать по этому поводу. ")
            reactions.go("/Initiate")
        }
    }
}
