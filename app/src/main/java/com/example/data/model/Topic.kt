package com.example.data.model

data class Topic(
    val id: String,
    val country: Country,
    val title: String,
    val subtitle: String,
    val iconName: String,
    val totalQuestionsEstimate: Int = 15
) {
    companion object {
        val CANADA_TOPICS = listOf(
            Topic("ca_rights", Country.CANADA, "Rights & Responsibilities", "Charter of Rights, democratic freedoms, citizenship duties", "Gavel"),
            Topic("ca_history", Country.CANADA, "Canadian History", "Indigenous peoples, French/British heritage, Confederation 1867", "HistoryEdu"),
            Topic("ca_gov", Country.CANADA, "Government", "Constitutional monarchy, Parliament, 3 branches and 3 levels", "AccountBalance"),
            Topic("ca_elections", Country.CANADA, "Elections", "Secret ballot, electoral districts, voting rights & process", "HowToVote"),
            Topic("ca_geography", Country.CANADA, "Geography", "5 regions, 10 provinces & 3 territories, oceans and capital", "Public"),
            Topic("ca_economy", Country.CANADA, "Economy", "Natural resources, manufacturing, trade partners, USMCA", "TrendingUp"),
            Topic("ca_symbols", Country.CANADA, "Canadian Symbols", "Maple Leaf, Beaver, Fleur-de-lis, Coat of Arms, O Canada", "MilitaryTech"),
            Topic("ca_modern", Country.CANADA, "Modern Canada", "Bilingualism, multiculturalism, Canadian justice & society", "Diversity3"),
            Topic("ca_people", Country.CANADA, "Important People & Events", "Sir John A. Macdonald, Terry Fox, Vimy Ridge, Victoria Cross", "Person")
        )

        val USA_TOPICS = listOf(
            Topic("us_gov_principles", Country.USA, "American Government", "Principles of democracy, rule of law, supreme law of the land", "Gavel"),
            Topic("us_constitution", Country.USA, "Constitution & Bill of Rights", "Structure, 27 amendments, 1st Amendment freedoms", "MenuBook"),
            Topic("us_rights", Country.USA, "Rights & Responsibilities", "Voting, federal juries, allegiance, passport privileges", "VerifiedUser"),
            Topic("us_history", Country.USA, "American History", "Colonial period, 1776 Independence, Civil War, 20th century", "HistoryEdu"),
            Topic("us_branches", Country.USA, "Government Branches", "Executive (President), Legislative (Congress), Judicial (Courts)", "AccountBalance"),
            Topic("us_elections", Country.USA, "Elections & System", "Presidential 4-yr terms, Senate 6-yr, House 2-yr, Electoral College", "HowToVote"),
            Topic("us_geography", Country.USA, "Geography & Borders", "50 states, Atlantic/Pacific oceans, major rivers & territories", "Public"),
            Topic("us_figures", Country.USA, "Historical Figures", "Washington, Jefferson, Lincoln, Susan B. Anthony, MLK Jr.", "Person"),
            Topic("us_officials", Country.USA, "Current Officials & Symbols", "President, Vice President, Chief Justice, Anthem, Holidays", "Flag")
        )

        fun getTopicsFor(country: Country): List<Topic> {
            return if (country == Country.CANADA) CANADA_TOPICS else USA_TOPICS
        }
    }
}
