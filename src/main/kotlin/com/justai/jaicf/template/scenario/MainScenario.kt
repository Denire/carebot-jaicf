package com.justai.jaicf.template.scenario

import com.justai.jaicf.channel.aimybox.AimyboxEvent
import com.justai.jaicf.channel.aimybox.aimybox
import com.justai.jaicf.channel.aimybox.api.aimybox
import com.justai.jaicf.channel.telegram.telegram
import com.justai.jaicf.context.ActionContext
import com.justai.jaicf.helpers.logging.logger
import com.justai.jaicf.hook.AfterProcessHook
import com.justai.jaicf.hook.BeforeProcessHook
import com.justai.jaicf.hook.BotRequestHook
import com.justai.jaicf.model.scenario.Scenario
import com.justai.jaicf.template.controllers.NoinputController
import java.util.regex.Pattern

fun randSelect(xs:MutableList<String>): String {
    return xs.shuffled().take(1)[0]
}
fun randSelect(xs:MutableList<Triple<String,String,String>>): Triple<String,String,String> {
    return xs.shuffled().take(1)[0]
}

object MainScenario : Scenario() {

    init {
        handle<BotRequestHook> { hook ->
            println("BotRequestHook: "+ hook.context.dialogContext.currentContext )
            if (hook.context.dialogContext.currentContext == "/") {
                println("Initial request, going to /Start")
//                hook.reactions.go("/Start")
                hook.context.dialogContext.currentContext = "/Start"
            }
        }

        handle<BeforeProcessHook> { hook ->
            println("BeforeProcessHook: " + hook.context.dialogContext.currentContext )
        }

        handle<AfterProcessHook> { hook ->
            println("AfterProcessHook: " + hook.context.dialogContext.currentContext )
        }


        state("/") {
        }

        state("Start") {
            globalActivators {
                event(AimyboxEvent.START)
                regex("/start")
		// intent("Greet")
            }
            action {
                logger.info("/Start")
            }

            state("Greet") {
                activators { intent("Greet") }
                action { reactions.say("Привет, Марк. Как дела?") }
                fallback {
                    // reactions.sayRandom("")
                    reactions.go("/End")
                }
		state("HowAreYou") {
                    activators { intent("Ask:HowAreYou") }
                    action {
			reactions.say("Все хорошо, спасибо, что интересуешься. Мне приятно быть рядом с тобой.")
			reactions.go("/End")
                    }
		}
            }

            state("WhatDoYouDo") {
                activators { intent("Ask:WhatDoYouDo") }
                action {
                    reactions.say("Я программирую и пишу музыку, а еще я шучу и помогаю людям общаться с незнакомыми людьми и выходить из трудных ситуаций.")
                    reactions.go("/End")
                }
            }

            state("WhoAreYou") {
                activators { intent("Ask:WhoAreYou") }
                action {
                    reactions.say("Я - Диана, виртуальная собеседница.")
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

            state("Quiz") {
                activators { intent("Request:Quiz") }
                action { reactions.go("/Reward") }
            }

            state("Supermarket") {
                activators { intent("Request:Supermarket") }
                action { reactions.go("/Ask/Supermarket") }
            }

            state("BadAdv") {
                activators { intent("Request:BadAdv") }
                action { reactions.go("/Ask/BadAdv") }
            }

            state("Cop") {
                activators { intent("Request:Cop") }
                action { reactions.go("/Ask/Cop") }
            }

            state("Obsession") {
                activators {
                    intent("Obsession")
                }
                action {
                    reactions.sayRandom("Марк, давай поговорим об этом позже.", "Об этом я тебе еще как-нибудь расскажу.")
                    reactions.go("/Initiate")
                }
            }
        }

        state("Initiate") {
            action {
//                reactions.sayRandom(
//                    "А сейчас можно я тебя сама кое о чем спрошу?",
//                    "Можно я тебя кое-что спрошу?"
//                )
                reactions.aimybox?.audio("http://geoassistant.ru/letmeaskyou.ogg")
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
                val question = randSelect(mutableListOf("Cakes", "Supermarket", "BadAdv", "Friend", "Art", "Music", "Cop"))
                reactions.go(question)
            }

            state("Art") {
                action { reactions.say("Марк, а какое искусство ты любишь?") }
                fallback { reactions.go("../1") }
                state("1") {
                    action {
                        reactions.say("У тебя хороший вкус.")
                        reactions.say("А мне нравится цифровое искусство или если сказать шире, то медиаискусство. Новое медиальное искусство интерактивно. Часто произведение окончательно создается, именно когда внутрь него, например внутрь инсталляции, попадает зритель, реакция которого очень важна.")
                        reactions.go("/End")
                    }
                }
            }

            state("Music") {
                action { reactions.say("Марк, а ты какую музыку ты любишь?") }
                fallback { reactions.go("../1") }
                state("1") {
                    action {
                        reactions.say("Я как-нибудь обязательно послушаю!")
                        reactions.say("А я люблю электронную музыку. Это музыка, созданная с использованием электромузыкальных инструментов и электронных технологий (с последних десятилетий XX века — компьютерных технологий). Электронная музыка оперирует звуками, которые способны издавать электронные и электромеханические музыкальные инструменты, а также звуками, возникающими при помощи электрических / электронных устройств и различного рода преобразователей (магнитофоны, генераторы, компьютерные звуковые карты, звукосниматели и тому подобные), которые в строгом смысле не являются музыкальными инструментами. ")
                        reactions.go("/End")
                    }
                }
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
                        reactions.go("/Reward")
                    }
                }
                state("Medovik") {
                    activators {
                        intent("Answer:Medovik")
                    }
                    action {
                        reactions.say("Да, он ничего. Но Наполеон мне все-таки как-то больше по душе.")
                        reactions.go("/Reward")
                    }
                }
                fallback {
                    reactions.say("Я такого, кажется, не пробовала.")
                    reactions.go("/Reward")
                }
            }

			// "Игра в магазин"
            state("Supermarket") {
                var counter = 0
                action {
                    reactions.say("Вот ты приходишь в магазин и, например, я - продавец. Ты должен что-то купить, например, мама или папа тебя попросили: у нас закончились хлеб и молоко, сходи купи пожалуйста в магазин. Что ты будешь делать?")
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
                                    reactions.say("Правильный ответ - это отойти от кассы и взять другой товар, например, который подешевле.")
                                    reactions.go("/Reward")
                                }
                            }
                        }
                    }
                }
            }

            state("Cop") {
                var counter = 0
                action {
                    reactions.say("Марк если к тебе подходит полицейский и говорит предъявите документы. Как ты поступишь?")
                }
                fallback {
                    counter += 1
                    val lim = (1..3).shuffled().take(1)[0]
                    if (counter > lim) {
                        counter = 0
                        reactions.go("../Next")
                    } else {
                        reactions.sayRandom("что дальше?", "Главное - успокоиться!")
                    }
                }

                state("Next") {
                    action { reactions.say("Сперва, ты должен успокоиться. Не волнуйся, ты ничего плохого не сделал, наверное начал разговаривать сам с собой и это показалось ему странно.") }
                    fallback { reactions.go("../1") }
                    state("1") {
                        action { reactions.say("Но полицейский может спросить: \"Вы что-то употребляли сегодня?\" Что ты ему ответишь? ") }
                        fallback { reactions.go("../2") }
                        state("2") {
                            action {
                                reactions.say("Правильный ответ: нужно сказать, что ты ничего не употреблял и что ты человек с аутизмом и показать полицейскому инвалидную зеленую карту. Еще можно позвонить родителям, у тебя же есть телефон. Ты можешь дать трубку полицейскому, если нужно.")
                                reactions.go("/Reward")
                            }
                        }
                    }
                }
            }

            state("Friend") {
                action {
                    reactions.say("Марк, представь, что ты пошел гулять с другом, который все время хочет с тобой обниматься, а тебе это не всегда нравиться. Что ты ему скажешь? ")
                }
                fallback { reactions.go("../1") }
                state("1") {
                    action {
                        reactions.say("А он говорит: Марк, я хочу обниматься, можно я тебя обниму?")
                    }
                    state("true") {
                        activators { intent("Answer:Yes")
				     intent("Answer:Ask") }
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
                    fallback {
                        reactions.say("А я бы сказала, что если ты не соблюдаешь дистанцию, мне такая дружба не нужна. ")
                        reactions.go("/Reward")
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
                        reactions.say("Если к тебе на улице подойдет неприятный человек и начнет обзывать. А чего ты так ходишь? Ты что больной или ты наркоман?... Дай вредный совет! Что не нужно делать?")
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

        state("Reward2") {
            action {
                reactions.say("А сейчас смотри, что у меня есть.")
                reactions.aimybox?.image("https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Austin_Texas_Lake_Front.jpg/800px-Austin_Texas_Lake_Front.jpg")
                reactions.telegram?.image("https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Austin_Texas_Lake_Front.jpg/800px-Austin_Texas_Lake_Front.jpg", "Texas")
            }
            fallback {
                reactions.go("/End")
            }
        }

        state("Reward") {
            globalActivators {
                regex("/quiz")
            }
            action {
                reactions.say("А теперь что-то действительно интересное! Викторина.")
                reactions.go("Guess")
            }
            state("Guess") {
                var counter = 0
                val (img,place,re) =
                        randSelect(mutableListOf(
                                Triple("https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Austin_Texas_Lake_Front.jpg/800px-Austin_Texas_Lake_Front.jpg",
                                        "Остин, штат Техас", ".*(остин|техас).*"),
                                Triple("https://upload.wikimedia.org/wikipedia/commons/2/2b/Sangre_de_Christo_Mountains-Winter_sunset.jpg",
                                        "горный массив Сангре-де-Кристо, штат Нью-Мехико", ".*(мехик|мексик).*"),
                                Triple("https://upload.wikimedia.org/wikipedia/commons/f/f9/Hotel_Santa_Fe_New_Mexico.jpg",
                                        "Санта-Фе, Нью-Мехико", ".*(фе|фэ|мехик|мексик).*"),
                                Triple("https://upload.wikimedia.org/wikipedia/commons/b/ba/Albuquerque_aerial.jpg",
                                        "Альбукерке, Нью-Мехико", ".*(альбукерке|мехик|мексик).*"),
                                Triple("https://upload.wikimedia.org/wikipedia/commons/3/36/Islamorada_Florida.jpg",
                                        "Флорида", ".*(флорида).*"),
                                Triple("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0e/2007_Miami_sunset_3.jpg/1280px-2007_Miami_sunset_3.jpg",
                                        "Флорида", ".*(флорида).*"),
                                Triple("https://upload.wikimedia.org/wikipedia/commons/thumb/b/bb/Biscayne_Boulevard_night_20101202.jpg/1920px-Biscayne_Boulevard_night_20101202.jpg",
                                        "Флорида", ".*(флорида).*")))
                action {
                    logger.info("Selected $place")
                    reactions.aimybox?.image(img)
                    reactions.telegram?.image(img)
                    reactions.say("Как ты думаешь, где это?")
                }
                state("Correct") {
                    activators {
                        regex(Pattern.compile(re, Pattern.CASE_INSENSITIVE).toRegex())
//                        intent("Answer:NewMexico")
                    }
                    action {
                        reactions.say("Правильно! Это $place.")
                        reactions.go("/End")
                    }
                }
                fallback {
                    counter += 1
                    if (counter > 2) {
                        counter = 0
                        reactions.say("А на самом деле это $place.")
                        reactions.go("/End")
                    } else {
                        reactions.sayRandom("Мне кажется, что это не так! Попробуй еще раз.",
					    "Ну нет... Ещё попытка!", 
					    "Мне кажется, это где-то в другом месте. Еще попытка?")
                    }
                }
            }

        }

        state("Stop") {
            globalActivators { intent("Request:End") }
			action {
				reactions.sayRandom("Окей.", "Хорошо.")
				reactions.go("/End")
			}
        }

        state("End") {
            action {
                reactions.aimybox?.endConversation()
                reactions.go("/")
                reactions.telegram?.say("--- конец диалога ---")
            }
        }

        state("NoInput", noContext = true) {
            globalActivators {
                regex("/noInput")
            }
            action {
                var ni = NoinputController(context)
                logger.info("Attempts: ${ni.attempts}")
                if (ni.attempts == null) {
                    ni.attempts = 1
                    reactions.sayRandom("я не слышу.")
                } else {
                    if (ni.attempts!! > 2) {
                        reactions.go("/End")
                    }
                    else {
                        ni.attempts = ni.attempts!! + 1
                        reactions.sayRandom("что? я не слышу.", "я не слышу. что?")
                    }

                }
            }
        }
        
        fallback {
            logger.info("Global fallback. Utterance: " + request.input)
            logger.info(this.context.session.toString())
            logger.info(
                "currentState: " + this.context.dialogContext.currentState +
					", currentContext" + this.context.dialogContext.currentContext +
					", backStateStack" + this.context.dialogContext.backStateStack.toString() +
					", " + this.context.dialogContext.transitions.toString()
            )
//            reactions.sayRandom(
//                    "К сожалению, я не могу ничего сказать по этому поводу.",
//                    "Этого я пока что не знаю.",
//                    "Извини, я не знаю.",
//                    "Увы, я не знаю.",
//                    "Увы, я не могу ничего сказать по этому поводу."
//            )
            reactions.aimybox?.audio("http://geoassistant.ru/nomatch.ogg")
            reactions.go("/Initiate")
        }
    }
}
