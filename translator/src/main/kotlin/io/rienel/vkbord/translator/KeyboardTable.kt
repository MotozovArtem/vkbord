package io.rienel.vkbord.translator

class KeyboardTable {
    val russianToEnglish = mapOf(
        'й' to 'q', 'ц' to 'w', 'у' to 'e', 'к' to 'r', 'е' to 't', 'н' to 'y', 'г' to 'u', 'ш' to 'i', 'щ' to 'o', 'з' to 'p',
        'х' to '[', 'ъ' to ']', 'ф' to 'a', 'ы' to 's', 'в' to 'd', 'а' to 'f', 'п' to 'g', 'р' to 'h', 'о' to 'j', 'л' to 'k',
        'д' to 'l', 'ж' to ';', 'э' to '\'', 'я' to 'z', 'ч' to 'x', 'с' to 'c', 'м' to 'v', 'и' to 'b', 'т' to 'n', 'ь' to 'm',
        'б' to ',', 'ю' to '.', 'ё' to '`'
    )

    val englishToRussian = russianToEnglish.entries.associate { (k, v) -> v to k }


}