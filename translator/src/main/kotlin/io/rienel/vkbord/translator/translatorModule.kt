package io.rienel.vkbord.translator

import org.koin.dsl.module

fun translatorModule() = module {
    single { KeyboardTable() }
    single<ILanguageManager> { LanguageManager() }
    single<ITranslator> { Translator(get(), get()) }
    single { CharFlow(get()) }
}