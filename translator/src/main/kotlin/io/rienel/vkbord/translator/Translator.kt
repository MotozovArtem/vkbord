package io.rienel.vkbord.translator

internal class Translator(
    private val languageManager: ILanguageManager,
    private val keyboardTable: KeyboardTable
): ITranslator {
    override fun translate(c: Char): Char = keyboardTable.englishToRussian[c] ?: '?'
}