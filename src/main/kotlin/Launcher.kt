fun main(args: Array<String>) {

    val categorizedInputs = listOf(
            CategorizedEmail("Hey there! I thought you might find this interesting. Click here.", isSpam = true),
            CategorizedEmail("Get viagra for a discount as much as 90%", isSpam = true),
            CategorizedEmail("Viagra prescription for less", isSpam = true),
            CategorizedEmail("Even better than Viagra, try this new prescription drug", isSpam = true),

            CategorizedEmail("Hey, I left my phone at home. Email me if you need anything", isSpam = false),
            CategorizedEmail("Please see attachment for notes on today's meeting. Interesting findings on your market research.", isSpam = false),
            CategorizedEmail("An item on your Amazon wish list received a discount", isSpam = false),
            CategorizedEmail("Your prescription drug order is ready", isSpam = false),
            CategorizedEmail("Your Amazon account password has been reset", isSpam = false),
            CategorizedEmail("Your Amazon order", isSpam = false)
    )

    val words = categorizedInputs.splitWords().distinct().toList()

    val probabilityOfSpam = categorizedInputs.count { it.isSpam }.toDouble() / categorizedInputs.count()

    println("The probability an email is spam is $probabilityOfSpam")

    val wordsBySpamProbability = words.asSequence()
            .map { word ->
                WordProbability(
                        word,
                        categorizedInputs.count { it.isSpam && word in it.words }.toDouble() / categorizedInputs.count { word in it.words }.toDouble() ,
                        categorizedInputs.count { !it.isSpam && word in it.words }.toDouble() / categorizedInputs.count { word in it.words }.toDouble()
                )
            }.toList()


    wordsBySpamProbability.sortedBy { it.probabilityIsSpam }.forEach { println(it) }
}

fun Iterable<CategorizedEmail>.splitWords() =  asSequence().flatMap {
                it.body.split(Regex("\\s")).asSequence()
            }.map { it.replace(Regex("[^A-Za-z]"),"").toLowerCase() }
            .filter { it.isNotEmpty() }




data class CategorizedEmail(val body: String, val isSpam: Boolean) {

    val words = body.split(Regex("\\s")).asSequence().map {
        it.replace(Regex("[^A-Za-z]"),"").toLowerCase()
    }.distinct().toSet()
}
class WordProbability(val word: String, val probabilityIsSpam: Double, val probabilityIsNotSpam: Double) {
    override fun toString() = "$word POS:$probabilityIsSpam NEG:$probabilityIsNotSpam"
}
