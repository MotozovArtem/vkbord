package io.rienel.vkbord.translator

import kotlinx.coroutines.flow.StateFlow

interface ILanguageManager {
    val to: StateFlow<Language>

    fun changeFrom(lang: Language)
    fun changeTo(lang: Language)
}