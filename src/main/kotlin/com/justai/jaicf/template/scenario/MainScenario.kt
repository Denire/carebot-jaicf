package com.justai.jaicf.template.scenario

import com.justai.jaicf.model.scenario.Scenario

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
                reactions.say("Марк, давай поговорим об этом позже.")
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