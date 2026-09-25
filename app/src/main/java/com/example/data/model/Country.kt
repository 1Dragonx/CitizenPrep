package com.example.data.model

enum class Country(
    val code: String,
    val displayName: String,
    val flagEmoji: String,
    val officialGuideName: String,
    val totalMockQuestions: Int,
    val passingScore: Int,
    val timeLimitMinutes: Int,
    val officialAuthority: String
) {
    CANADA(
        code = "CA",
        displayName = "Canada",
        flagEmoji = "🇨🇦",
        officialGuideName = "Discover Canada: The Rights and Responsibilities of Citizenship",
        totalMockQuestions = 20,
        passingScore = 15,
        timeLimitMinutes = 45,
        officialAuthority = "IRCC (Immigration, Refugees and Citizenship Canada)"
    ),
    USA(
        code = "US",
        displayName = "United States",
        flagEmoji = "🇺🇸",
        officialGuideName = "USCIS 2025 Civics Naturalization Test (128 Question Pool)",
        totalMockQuestions = 20,
        passingScore = 12,
        timeLimitMinutes = 30,
        officialAuthority = "USCIS (U.S. Citizenship and Immigration Services)"
    )
}
