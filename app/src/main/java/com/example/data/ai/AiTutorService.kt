package com.example.data.ai

import com.example.data.model.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class TutorMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: Sender,
    val text: String,
    val languageCode: String = "en",
    val officialReference: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    enum class Sender { USER, TUTOR }
}

class AiTutorService {

    private val predefinedKnowledge = listOf(
        KnowledgeSnippet(
            keywords = listOf("parliament", "संसद", "house of commons", "senate", "mp"),
            country = Country.CANADA,
            english = "The Parliament of Canada consists of three parts: The Sovereign (represented by the Governor General), the Senate (105 appointed members who offer 'sober second thought'), and the House of Commons (elected Members of Parliament representing local ridings). The Prime Minister is the leader of the party that wins the most seats.",
            hindi = "कनाडा की संसद (Parliament) के 3 मुख्य भाग हैं:\n1. सॉवरेन (किंग चार्ल्स III, जिनका प्रतिनिधित्व गवर्नर जनरल करते हैं)\n2. सीनेट (105 नियुक्त सदस्य, जो कानूनों की समीक्षा करते हैं)\n3. हाउस ऑफ कॉमन्स (जनता द्वारा चुने गए सांसद यानी MPs)\n\nजिस राजनीतिक पार्टी के पास हाउस ऑफ कॉमन्स में सबसे ज्यादा सीटें होती हैं, उसका नेता प्रधानमंत्री (Prime Minister) बनता है।",
            french = "Le Parlement du Canada se compose de trois parties : le Souverain (représenté par le gouverneur général), le Sénat (105 membres nommés) et la Chambre des communes (députés élus).",
            source = "Discover Canada: How Canadians Govern Themselves"
        ),
        KnowledgeSnippet(
            keywords = listOf("habeas corpus", "हेबियस कॉर्पस", "detention", "arrest"),
            country = Country.CANADA,
            english = "Habeas corpus is an ancient Latin legal principle meaning 'you have the body.' It guarantees that if a person is arrested or detained by the government, they have the right to challenge their detention before a judge to ensure it is lawful.",
            hindi = "'Habeas Corpus' (हेबियस कॉर्पस) एक ऐतिहासिक कानूनी अधिकार है जिसका लैटिन में अर्थ है 'शरीर को पेश करना'। इसका मतलब है कि अगर सरकार किसी को हिरासत में लेती है, तो उस व्यक्ति को जज के सामने पेश होने और अपनी गैरकानूनी गिरफ्तारी को चुनौती देने का पूर्ण अधिकार है।",
            french = "L'habeas corpus est le droit pour une personne détenue de contester la légalité de son emprisonnement devant un tribunal.",
            source = "Discover Canada: Rights and Responsibilities"
        ),
        KnowledgeSnippet(
            keywords = listOf("charter", "rights", "अधिकार", "freedoms", "1982"),
            country = Country.CANADA,
            english = "The Canadian Charter of Rights and Freedoms, entrenched in the Constitution in 1982, protects 4 fundamental freedoms: conscience/religion, thought/speech/press, peaceful assembly, and association, plus democratic and equality rights.",
            hindi = "कनाडा का चार्टर ऑफ राइट्स एंड फ्रीडम्स (1982) 4 मौलिक स्वतंत्रताओं की गारंटी देता है:\n1. विचार और धर्म की स्वतंत्रता\n2. बोलने और प्रेस की स्वतंत्रता\n3. शांतिपूर्ण सभा करने का अधिकार\n4. संगठन/यूनियन बनाने का अधिकार।",
            french = "La Charte canadienne des droits et libertés (1982) protège les libertés fondamentales et les droits démocratiques.",
            source = "Discover Canada: Canadian Charter of Rights"
        ),
        KnowledgeSnippet(
            keywords = listOf("branch", "branches", "शाखाएं", "checks and balances", "three branches", "separation"),
            country = Country.USA,
            english = "The U.S. government is split into 3 branches to ensure checks and balances:\n1. Legislative (Congress: Senate & House of Reps) – Makes laws.\n2. Executive (President & Cabinet) – Carries out and enforces laws.\n3. Judicial (Supreme Court & Courts) – Evaluates and interprets laws.",
            hindi = "अमेरिकी सरकार की 3 शाखाएं (Branches) हैं ताकि शक्ति का संतुलन बना रहे:\n1. विधायी (Legislative/Congress): कानून बनाती है (सीनेट + हाउस ऑफ रिप्रेजेंटेटिव्स)\n2. कार्यपालिका (Executive/President): कानूनों को लागू करती है\n3. न्यायपालिका (Judicial/Supreme Court): कानूनों की व्याख्या और न्याय करती है।",
            french = "Le gouvernement américain comporte 3 branches : Législative (fait les lois), Exécutive (applique les lois) et Judiciaire (interprète les lois).",
            source = "USCIS Civics Study Guide (Item #13)"
        ),
        KnowledgeSnippet(
            keywords = listOf("constitution", "संविधान", "supreme law", "bill of rights"),
            country = Country.USA,
            english = "The U.S. Constitution (1787) is the supreme law of the land. Its first three words 'We the People' announce democratic popular sovereignty. The first 10 amendments form the 'Bill of Rights'.",
            hindi = "अमेरिकी संविधान (1787) देश का सर्वोच्च कानून है। इसके शुरुआती 3 शब्द 'We the People' (हम लोग) यह बताते हैं कि सत्ता जनता के हाथ में है। इसके पहले 10 संशोधनों को 'बिल ऑफ राइट्स' कहा जाता है।",
            french = "La Constitution des États-Unis est la loi suprême. Les dix premiers amendements forment le 'Bill of Rights'.",
            source = "USCIS 2025 Civics Questions #1 & #5"
        ),
        KnowledgeSnippet(
            keywords = listOf("first amendment", "पहला संशोधन", "1st amendment", "religion", "speech"),
            country = Country.USA,
            english = "The First Amendment guarantees five freedoms: Freedom of Religion, Freedom of Speech, Freedom of the Press, Right to Peacefully Assemble, and Right to Petition the Government.",
            hindi = "अमेरिकी संविधान का पहला संशोधन (1st Amendment) 5 मौलिक अधिकार देता है:\n1. धर्म की स्वतंत्रता\n2. वाणी/भाषण की स्वतंत्रता\n3. प्रेस की स्वतंत्रता\n4. शांतिपूर्ण सभा का अधिकार\n5. सरकार को याचिका देने का अधिकार।",
            french = "Le Premier Amendement garantit la liberté de religion, de parole, de presse, de réunion pacifique et de pétition.",
            source = "USCIS Civics Question #6"
        )
    )

    suspend fun getTutorResponse(
        query: String,
        country: Country,
        preferredLanguage: String
    ): TutorMessage = withContext(Dispatchers.Default) {
        val lowerQuery = query.lowercase().trim()

        // Match against known syllabus snippets
        val match = predefinedKnowledge.firstOrNull { snippet ->
            snippet.keywords.any { keyword -> lowerQuery.contains(keyword.lowercase()) }
        }

        if (match != null) {
            val responseText = when (preferredLanguage.lowercase()) {
                "hi", "hindi" -> match.hindi
                "fr", "french" -> match.french
                else -> match.english
            }
            return@withContext TutorMessage(
                sender = TutorMessage.Sender.TUTOR,
                text = responseText,
                languageCode = preferredLanguage,
                officialReference = match.source
            )
        }

        // Contextual AI Civics advice
        val fallbackText = when (preferredLanguage.lowercase()) {
            "hi", "hindi" -> {
                if (country == Country.CANADA) {
                    "कनाडा नागरिकता परीक्षा की तैयारी के लिए 'Discover Canada' आधिकारिक गाइड सबसे महत्वपूर्ण है। यह परीक्षा 20 बहुविकल्पीय प्रश्नों की होती है जिसमें पास होने के लिए 15 सही उत्तर चाहिए। आप संसद, इतिहास (1867 कन्फेडरेशन), और चार्टर ऑफ राइट्स से जुड़े सवाल पूछ सकते हैं!"
                } else {
                    "अमेरिकी नागरिकता (Civics Test) के लिए 2025 क्वेश्चन पूल में 128 प्रश्न हैं। इंटरव्यू में अधिकारी आपसे 20 प्रश्न तक पूछते हैं और पास होने के लिए कम से कम 12 सही उत्तर जरूरी हैं। आप संविधान, कांग्रेस, या इतिहास के बारे में कोई भी सवाल पूछ सकते हैं!"
                }
            }
            "fr", "french" -> {
                if (country == Country.CANADA) {
                    "Pour le test canadien, l'étude du guide officiel 'Découvrir le Canada' est essentielle. Le test compte 20 questions et requiert 15 bonnes réponses (75%). Posez une question sur le Parlement, la Confédération ou la Charte!"
                } else {
                    "Pour le test civique américain de 2025, 12 réponses correctes sur 20 questions sont requises. N'hésitez pas à poser une question sur la Constitution ou les branches du gouvernement!"
                }
            }
            else -> {
                if (country == Country.CANADA) {
                    "For the Canadian citizenship test based on the official 'Discover Canada' guide, you must answer at least 15 out of 20 questions correctly (45-minute limit). Feel free to ask about Parliament, Confederation (1867), the Charter of Rights, Canadian symbols, or geography!"
                } else {
                    "For the U.S. naturalization civics test (2025 128-question pool), an applicant must answer 12 out of 20 questions correctly. Ask about the U.S. Constitution, the 3 branches of government, the Bill of Rights, or key historical milestones!"
                }
            }
        }

        TutorMessage(
            sender = TutorMessage.Sender.TUTOR,
            text = fallbackText,
            languageCode = preferredLanguage,
            officialReference = if (country == Country.CANADA) "Discover Canada Official Study Guide" else "USCIS 2025 Civics Naturalization Test"
        )
    }

    private data class KnowledgeSnippet(
        val keywords: List<String>,
        val country: Country,
        val english: String,
        val hindi: String,
        val french: String,
        val source: String
    )
}
