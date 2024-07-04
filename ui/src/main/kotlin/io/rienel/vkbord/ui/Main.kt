package io.rienel.vkbord.ui

import io.rienel.vkbord.translator.translatorModule
import org.koin.core.context.startKoin
import org.koin.logger.slf4jLogger

fun main() {

    startKoin {
        slf4jLogger()
        modules(
            appModule(),
            translatorModule()
        )
    }

    val app = App("0.1.0")
    app.start()
}