package com.justai.jaicf.template

import com.justai.jaicf.BotEngine
import com.justai.jaicf.activator.caila.CailaIntentActivator
import com.justai.jaicf.activator.caila.CailaNLUSettings
import com.justai.jaicf.activator.catchall.CatchAllActivator
import com.justai.jaicf.activator.event.BaseEventActivator
import com.justai.jaicf.activator.regex.RegexActivator
import com.justai.jaicf.activator.selection.ActivationSelector
import com.justai.jaicf.activator.selection.isFrom
import com.justai.jaicf.activator.selection.isFromRoot
import com.justai.jaicf.activator.selection.isTo
import com.justai.jaicf.context.ActivatorContext
import com.justai.jaicf.context.BotContext
import com.justai.jaicf.context.manager.InMemoryBotContextManager
import com.justai.jaicf.context.manager.mongo.MongoBotContextManager
import com.justai.jaicf.model.activation.Activation
import com.justai.jaicf.model.transition.Transition
import com.justai.jaicf.template.scenario.MainScenario
import com.mongodb.client.MongoClients
import java.util.*

private val contextManager = System.getenv("MONGODB_URI")?.let { url ->
    val client = MongoClients.create(url)
    MongoBotContextManager(client.getDatabase("jaicf").getCollection("contexts"))
} ?: InMemoryBotContextManager

val accessToken: String = System.getenv("JAICP_API_TOKEN") ?: Properties().run {
    load(CailaNLUSettings::class.java.getResourceAsStream("/jaicp.properties"))
    getProperty("apiToken")
}

private val cailaNLUSettings = CailaNLUSettings(
    accessToken = accessToken,
    confidenceThreshold = 0.3
)

class ContextFirstActivationSelectorPrime : ActivationSelector {
    override fun selectActivation(
            botContext: BotContext,
            activations: List<Pair<Transition, ActivatorContext>>
    ): Activation? {
        val current = botContext.dialogContext.currentContext

        val toChildren = activations.filter { it.first.isFrom(current) }.maxBy { it.second.confidence }
        val toCurrent = activations.filter { it.first.isTo(current) }.maxBy { it.second.confidence }
        val fromRoot = activations.filter { it.first.isFromRoot }.maxBy { it.second.confidence }

        val best = toChildren ?: toCurrent ?: fromRoot
        return best?.let { Activation(it.first.toState, it.second) }
    }

}

val templateBot = BotEngine(
    scenario = MainScenario,
    defaultContextManager = contextManager,
        activationSelector = ContextFirstActivationSelectorPrime(),
    activators = arrayOf(
        RegexActivator,
        CailaIntentActivator.Factory(cailaNLUSettings),
        CatchAllActivator,
        BaseEventActivator
    )
)

