package com.justai.jaicf.template.scenario

import com.justai.jaicf.channel.aimybox.AimyboxEvent
import com.justai.jaicf.channel.aimybox.aimybox
import com.justai.jaicf.channel.telegram.telegram
import com.justai.jaicf.helpers.logging.logger
import com.justai.jaicf.model.scenario.Scenario

fun randSelect(xs:MutableList<String>): String {
    return xs.shuffled().take(1)[0]
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
                reactions.say("Привет, Марк. Спроси у меня что-нибудь.")
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
                    intent("Answer:Ask")
                }
                action {
                    reactions.sayRandom("Отлично", "Супер")
                    reactions.go("/Ask")
                }
            }
            state("No") {
                activators {
                    intent("Answer:No")
                }
                action {
                    reactions.say("Ну ладно.")
                    reactions.go("/End")
                }
            }
            fallback {
                reactions.go("/Ask")
            }
        }

        state("Ask") {
            activators {
                intent("Request:AskMe")
            }
            action {
                val question = randSelect(mutableListOf("Cakes", "Supermarket", "BadAdv"))
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
                globalActivators { intent("Request:Supermarket") }
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
                        reactions.sayRandom("угу", "что дальше?", "а дальше что?", "Главное - успокоиться!")
                    }
                }

                state("Cashier") {
                    action { reactions.say("Хорошо. А на кассе ты что говоришь?") }
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

            state("Friend") {
                action {
                    reactions.say("Марк, представь, что ты пошел гулять с другом, который все время хочет с тобой обниматься, а тебе это не всегда нравиться, что ты ему скажешь? ")
                }
                fallback { reactions.go("../1") }
                state("1") {
                    action {
                        reactions.say("А он говорит: Марк, я хочу обниматься, можно я тебя обниму?")
                    }
                    state("true") {
                        activators { intent("Answer:Yes") }
                        action {
                            reactions.say("Хорошо, но можно еще сказать, что мне не нужна твоя дружба если ты не соблюдаешь дистанцию. ")
                            reactions.go("/Reward")
                        }
                    }
                    state("false") {
                        activators { intent("Answer:No") }
                        action {
                            reactions.say("А если он скажет: а тогда я обижусь!")
                        }
                        fallback {
                            reactions.say("А я бы сказала, что если ты не соблюдаешь дистанцию, мне такая дружба не нужна. ")
                            reactions.go("/Reward")
                        }
                    }

                }
            }


            state("BadAdv") {
                action {
                    reactions.sayRandom("Давай придумаем вредный совет!", "Давай придумаем совет, только очень вредный!")
                    val question = randSelect(mutableListOf("Metro", "Gopnik"))
                    reactions.go(question)
                }
                state("Gopnik") {
                    action {
                        reactions.say("Если к тебе на улице подойдет неприятный человек и начнет обзывать. \"А чего ты так ходишь? Ты что больной или ты наркоман?\" Дай вредный совет! Что не нужно делать?")
                    }
                    fallback {
                        reactions.say("Но только нельзя забывать, что это вредный совет! Я придумала вот такой: позвать его в гости!")
                        reactions.go("/Reward")
                    }
                }
                state("Metro") {
                    action {
                        reactions.say("Тебе куда-то нужно ехать, а на карте метрополитена закончились деньги. Дай мне вредный совет, что не нужно делать в такой ситуации. ")
                    }
                    fallback {
                        reactions.say("Но не забывай, что это вредный совет! А я придумала вот такой совет: перепрыгнуть через турникет.")
                        reactions.go("/Reward")
                    }
                }

            }
        }




	
        state("Reward") {
            action {
                reactions.say("А сейчас смотри, что у меня есть.")
                reactions.aimybox?.image("https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Austin_Texas_Lake_Front.jpg/800px-Austin_Texas_Lake_Front.jpg")
                reactions.telegram?.image("https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Austin_Texas_Lake_Front.jpg/800px-Austin_Texas_Lake_Front.jpg", "Texas")
            }
            fallback {
                reactions.go("/End")
            }
        }

        state("End") {
            globalActivators { intent("Request:End") }
            action {
                reactions.aimybox?.endConversation()
                reactions.telegram?.say("--- конец диалога ---")
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
