
//Helpful reference
//https://en.wikipedia.org/wiki/Naive_Bayes_spam_filtering


// globally all emails in my population, spam or not spam
val emailPopulation = listOf(
        CategorizedEmail("Hey there! I thought you might find this interesting. Click here.", isSpam = true),
        CategorizedEmail("Get viagra for a discount as much as 90%", isSpam = true),
        CategorizedEmail("Viagra prescription for less", isSpam = true),
        CategorizedEmail("Even better than Viagra, try this new prescription drug", isSpam = true),

        CategorizedEmail("Hey, I left my phone at home. Email me if you need anything. I'll be in a meeting for the afternoon.", isSpam = false),
        CategorizedEmail("Please see attachment for notes on today's meeting. Interesting findings on your market research.", isSpam = false),
        CategorizedEmail("An item on your Amazon wish list received a discount", isSpam = false),
        CategorizedEmail("Your prescription drug order is ready", isSpam = false),
        CategorizedEmail("Your Amazon account password has been reset", isSpam = false),
        CategorizedEmail("Your Amazon order", isSpam = false)
)


// Pr(S), the probability any given email is spam
val probabilityOfSpam = (emailPopulation.count { it.isSpam }.toDouble() / emailPopulation.count().toDouble()).also {
    println("The overall probability an email is spam is $it")
}

// Pr(H), the probability any given email is not spam (ham)
val probabilityOfHam = (emailPopulation.count { !it.isSpam }.toDouble() / emailPopulation.count().toDouble()).also {
    println("The overall probability an email is not spam is $it")
}

// words with their probability metrics
val wordsWithProbability = emailPopulation.splitWords().asSequence()
        .distinct()
        .map { word ->
            WordProbability(word = word)
        }.toList()


fun main(args: Array<String>) {

    wordsWithProbability.sortedByDescending { it.probabilityMsgIsSpamGivenWord }
            .forEach { println(it) }

    println("\r\nScore for an email containing words: discount viagra wholesale, hurry while this offer lasts")
    spamScoreForWords("discount", "viagra", "wholesale", "hurry", "while", "this", "offer", "lasts").also {
        println(it)
    }

    println("\r\nScore for an email containing words: interesting meeting on amazon cloud services discount program")
    spamScoreForWords("interesting", "meeting", "on", "amazon", "cloud", "services", "discount", "program").also {
        println(it)
    }
}

fun spamScoreForWords(vararg words: String) = words.asSequence().map { word ->
    wordsWithProbability.firstOrNull { it.word == word }?.probabilityMsgIsSpamGivenWord
}.filterNotNull().toList().let { scores ->
    scores.product() / (scores.product() + scores.map { 1.0 - it }.product() )
}

data class CategorizedEmail(val body: String, val isSpam: Boolean) {

    val words = body.split(Regex("\\s")).asSequence().map {
        it.replace(Regex("[^A-Za-z]"),"").toLowerCase()
    }.distinct().toSet()
}

class WordProbability(val word: String) {

    // Pr(W|S), probability word appears in spam message
    val probabilitySpamContainsWord = emailPopulation.count { it.isSpam && word in it.words }.toDouble() /
            emailPopulation.count { it.isSpam }.toDouble()

    //Pr(W|H), probability word appears in ham message
    val probabilityHamContainsWord = emailPopulation.count { !it.isSpam && word in it.words }.toDouble() /
            emailPopulation.count { !it.isSpam }.toDouble()

    // Pr(S|W), probability message is spam given word
    val probabilityMsgIsSpamGivenWord = (probabilitySpamContainsWord * probabilityOfSpam) / ((probabilitySpamContainsWord * probabilityOfSpam) + (probabilityHamContainsWord * probabilityOfHam))

    override fun toString(): String {
        return "word='$word', probabilitySpamContainsWord=$probabilitySpamContainsWord, probabilityHamContainsWord=$probabilityHamContainsWord, probabilityMsgIsSpamGivenWord=$probabilityMsgIsSpamGivenWord"
    }
}


fun Iterable<CategorizedEmail>.splitWords() =  asSequence().flatMap {
    it.body.split(Regex("\\s")).asSequence()
}.map { it.replace(Regex("[^A-Za-z]"),"").toLowerCase() }
.filter { it.isNotEmpty() }

fun Iterable<Double>.product() = reduce { acc, doub -> acc * doub }