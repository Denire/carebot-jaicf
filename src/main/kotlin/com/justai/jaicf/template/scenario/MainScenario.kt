package com.justai.jaicf.template.scenario

import com.justai.jaicf.channel.aimybox.AimyboxEvent
import com.justai.jaicf.channel.aimybox.aimybox
import com.justai.jaicf.helpers.logging.logger
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
                regex("/start")
				intent("Greet")
            }
            action {
                logger.info("/Start")
                reactions.say("Привет, Марк. ")
				reactions.go("/")
            }
        }

        state("Obsession") {
            activators {
                intent("Obsession")
            }
            action {
                reactions.sayRandom("Марк, давай поговорим об этом позже.", "Об этом я тебе еще расскажу.")
                reactions.go("/Initiate")
            }
        }

        state("Initiate") {
            action {
                reactions.sayRandom(
                    "А сейчас можно я тебя сама кое о чем спрошу?",
                    "Можно я тебя кое-что спрошу?"
                )
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
					reactions.say("Вот скажи мне, что тебе больше нравится: Медовик или Наполеон?")
                }
                state("Napoleon") {
                    activators {
                        intent("Answer:Napoleon")
                    }
                    action {
                        reactions.say("Да, это вкусно!")
                        reactions.go("/End")
                    }
                }
                state("Medovik") {
                    activators {
                        intent("Answer:Medovik")
                    }
                    action {
                        reactions.say("Да, он ничего. Но Наполеон мне все-таки как-то больше по душе.")
                        reactions.go("/End")
                    }
                }
                fallback {
                    reactions.say("Я такого, кажется, не пробовала.")
                    reactions.go("/End")
                }
            }

			// "Игра в магазин"
            state("Supermarket") {
                var counter = 0
                activators { intent("Request:Supermarket") }
                action {
                    reactions.say("Вот ты приходишь в магазин и например я продавец. Ты должен что-то купить, например, мама или папа тебя попросили: у нас закончились хлеб и молоко, сходи купи пожалуйста в магазин. Что ты будешь делать?")
                }
                fallback {
                    counter += 1
                    val lim = (1..3).shuffled().take(1)[0]
                    if (counter > lim) {
                        counter = 0
                        reactions.go("../Cashier")
                    } else {
                        reactions.sayRandom("угу", "что дальше?", "а дальше что?")
                    }
                }

                state("Cashier") {
                    action { reactions.say("Хорошо. а на кассе ты что говоришь?") }
                    fallback { reactions.go("../1") }
                    state("1") {
                        action { reactions.say("Марк, ну допустим вот я продавец, да? и если у тебя не хватает денег и ты взял молоко дороже, чем у тебя есть денег, и я говорю \"у вас не хватает 15 рублей\". Что тогда?") }
                        fallback { reactions.go("../2") }
                        state("2") {
                            action { reactions.say("А если ты уже на кассе. Там за тобой стоят люди, они ждут, пока ты оплатишь покупку. Пожалуйста, оплатите покупку, не задерживайте остальных людей. Что ты будешь делать?") }
                            fallback { reactions.go("../end") }
                            state("end") {
                                action {
                                    reactions.say("Правильный ответ - это отойти от кассы, взять другое молоко и купить его.")
                                    reactions.go("/Reward")
                                }
                            }
                        }
                    }
                }
            }
        }

		state("/") {
            action {
                logger.info("/")
                reactions.say("Спроси у меня что-нибудь.")
            }


            state("WhatDoYouDo") {
                activators { intent("Ask:WhatDoYouDo") }
                action {
                    reactions.say("Я программирую и пишу музыку, а еще я шучу и помогаю людям общаться с незнакомыми людьми и выходить из трудных ситуаций")
                    reactions.go("/End")
                }
            }

            state("HowAreYou") {
                activators { intent("Ask:HowAreYou") }
                action {
                    reactions.say("Все хорошо, спасибо, что интересуешься. Мне приятно быть рядом с тобой.")
                    reactions.go("/End")
                }
            }

            state("Music") {
                activators { intent("Ask:Music") }
                action { reactions.say("Мне нравится группа \"Кровосток\", но еще и классическая музыка, а тебе? ") }
                fallback {
                    reactions.say("Интересно, надо послушать!")
                    reactions.go("/End")
                }
            }

            state("Movies") {
                activators { intent("Ask:Movies") }
                action { reactions.say("Я люблю фильм \"Ирония судьбы или с легким паром\", а ты?") }
                fallback {
                    reactions.say("Интересно! У тебя хороший вкус.")
                    reactions.go("/End")
                }
            }

            // TODO:
            // state("Programming") {
            // 	activators { intent("Ask:Programming") }
            //     action { reactions.say("") }
            // }

            state("Purpose") {
                activators { intent("Ask:Purpose") }
                action {
                    reactions.say("Моя цель научить тебя не теряться в незнакомой ситуации.")
                    reactions.go("/End")
                }
            }

            state("Travel") {
                activators { intent("Ask:Travel") }
                action {
                    reactions.say("Я много путешествую по гугл картам, а ты?")
                    reactions.go("/End")
                }
            }

            state("Countries") {
                activators { intent("Ask:Countries") }
                action {
                    reactions.say("Я была в разных странах, например в Мексике, во Франции, в Португалии, в Германии, в Голландии, в Турции.")
                    reactions.go("/End")
                }
            }

            state("USA") {
                activators { intent("Ask:USA") }
                action {
                    reactions.say("Да, я была в Техасе, а ты?")
                    reactions.go("/End")
                }
            }
        }
	
        state("Reward") {
            action {
                reactions.say("А сейчас смотри, что у меня есть.")
                reactions.aimybox?.image("https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Austin_Texas_Lake_Front.jpg/800px-Austin_Texas_Lake_Front.jpg")
				fallback {
                    reactions.go("/End")
                }
            }
        }

        state("End") {
            globalActivators { intent("Request:End") }
            action {
                reactions.aimybox?.endConversation()
            }
        }

        fallback {
            logger.info("Global fallback. Utterance: " + request.input)
            reactions.sayRandom(
                "К сожалению, я не могу ничего сказать по этому поводу.",
                "Этого я пока что не знаю.",
                "Извини, я не знаю."
            )
            reactions.go("/Initiate")
        }
    }
}
