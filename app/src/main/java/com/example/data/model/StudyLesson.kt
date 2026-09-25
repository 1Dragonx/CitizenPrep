package com.example.data.model

data class StudyLesson(
    val topicId: String,
    val country: Country,
    val title: String,
    val summary: String,
    val keyFacts: List<String>,
    val importantDatesOrNumbers: List<Pair<String, String>>,
    val memoryTip: String
) {
    companion object {
        val ALL_LESSONS = listOf(
            // Canada Lessons
            StudyLesson(
                topicId = "ca_rights",
                country = Country.CANADA,
                title = "Rights and Responsibilities of Citizenship",
                summary = "Canadian citizenship brings duties alongside cherished rights. The Canadian Charter of Rights and Freedoms (entrenched in 1982) protects fundamental freedoms, democratic rights, mobility rights, and equality before the law.",
                keyFacts = listOf(
                    "Four Fundamental Freedoms: Conscience/religion, thought/speech/press, peaceful assembly, association.",
                    "Habeas corpus: Ancient common-law right to challenge unlawful detention before a judge.",
                    "Official languages: English and French have equality of status across federal institutions.",
                    "Citizenship Responsibilities: Obeying the law, taking responsibility for oneself & family, serving on a jury, voting in elections, helping in the community, protecting the environment."
                ),
                importantDatesOrNumbers = listOf(
                    "1982" to "Canadian Charter of Rights and Freedoms entrenched in Constitution",
                    "1969" to "Official Languages Act passed establishing English and French",
                    "1215" to "Magna Carta signed in England, foundation of modern rights"
                ),
                memoryTip = "Remember 'ROVE': Respect laws, Obey, Vote, Environmental care & Community Service."
            ),
            StudyLesson(
                topicId = "ca_history",
                country = Country.CANADA,
                title = "Canadian History & Confederation",
                summary = "Canada's history spans thousands of years of Indigenous heritage, French and British colonization, the struggle for responsible government, Confederation in 1867, and world war sacrifices that cemented Canadian national identity.",
                keyFacts = listOf(
                    "Founding Peoples: Aboriginal (First Nations, Métis, Inuit), French, and British.",
                    "War of 1812: Canadians, British, and Indigenous allies repelled American invasion attempts.",
                    "Fathers of Confederation established the Dominion of Canada on July 1, 1867 under the British North America Act.",
                    "Original 4 provinces: Ontario, Quebec, Nova Scotia, and New Brunswick.",
                    "Vimy Ridge (April 1917): Crucial WWI victory where Canadian Corps fought together as a unified national force."
                ),
                importantDatesOrNumbers = listOf(
                    "July 1, 1867" to "Confederation Day (now celebrated as Canada Day)",
                    "1812" to "War of 1812 defends Canadian borders",
                    "April 1917" to "Battle of Vimy Ridge in France"
                ),
                memoryTip = "1867 = 1 July, 4 initial provinces: Ontario, Quebec, Nova Scotia, New Brunswick."
            ),
            StudyLesson(
                topicId = "ca_gov",
                country = Country.CANADA,
                title = "How Canadians Govern Themselves",
                summary = "Canada is a constitutional monarchy, a parliamentary democracy, and a federal state. Power is shared between the federal government in Ottawa, 10 provincial governments, and 3 territorial administrations.",
                keyFacts = listOf(
                    "Three Branches: Executive (King/Governor General, PM & Cabinet), Legislative (Parliament: Senate & House of Commons), Judicial (Supreme Court & independent courts).",
                    "Head of State is Sovereign King Charles III, represented by the Governor General.",
                    "Head of Government is the Prime Minister, leading the political party with the most seats in House of Commons.",
                    "The Senate has 105 appointed members who provide 'sober second thought' to legislation.",
                    "Three levels: Federal (defense, foreign policy, citizenship, money), Provincial (health, education, highways), Municipal (snow removal, policing, fire)."
                ),
                importantDatesOrNumbers = listOf(
                    "3" to "Branches of Government (Executive, Legislative, Judicial)",
                    "3" to "Levels of Government (Federal, Provincial, Municipal)",
                    "105" to "Appointed Senators in the Senate"
                ),
                memoryTip = "King = Head of State. Prime Minister = Head of Government."
            ),
            StudyLesson(
                topicId = "ca_elections",
                country = Country.CANADA,
                title = "Federal Elections & The Secret Ballot",
                summary = "Canadians elect representatives to the House of Commons through free, fair elections conducted by secret ballot. The party that elects the most Members of Parliament (MPs) forms the government.",
                keyFacts = listOf(
                    "Voting is conducted by Secret Ballot: no one has the right to know or influence your vote.",
                    "Voter Qualifications: Canadian citizen, at least 18 years old on election day, registered on the voters list.",
                    "Electoral Districts: Also called 'ridings' or 'constituencies', each electing 1 MP.",
                    "Elections Canada is the non-partisan independent agency managing federal elections.",
                    "Opposition Parties: Parties not in power; the party with the second most seats becomes the Official Opposition."
                ),
                importantDatesOrNumbers = listOf(
                    "18" to "Minimum age to vote in Canadian federal elections",
                    "338+" to "Electoral ridings / seats in House of Commons",
                    "4 years" to "Maximum fixed-date federal election term (unless dissolved earlier)"
                ),
                memoryTip = "Secret Ballot means total privacy: never show marked ballot to anyone."
            ),
            StudyLesson(
                topicId = "ca_geography",
                country = Country.CANADA,
                title = "Geography & The 5 Regions",
                summary = "Canada is the second largest country on earth by area, bordered by three oceans: Pacific, Atlantic, and Arctic. It comprises 10 provinces and 3 territories organized into 5 distinct geographic regions.",
                keyFacts = listOf(
                    "5 Distinct Regions: Atlantic Provinces, Central Canada, Prairie Provinces, West Coast, Northern Territories.",
                    "Capital: Ottawa, Ontario, selected by Queen Victoria in 1857.",
                    "Central Canada (Ontario & Quebec) is the industrial and manufacturing heartland containing over half the population.",
                    "Prairie Provinces (Manitoba, Saskatchewan, Alberta) rich in energy, agriculture, and grain.",
                    "Three Oceans: Pacific Ocean (West), Atlantic Ocean (East), Arctic Ocean (North)."
                ),
                importantDatesOrNumbers = listOf(
                    "10" to "Provinces",
                    "3" to "Territories (Yukon, NWT, Nunavut)",
                    "3" to "Oceans bordering Canada"
                ),
                memoryTip = "10 + 3 = 13 total jurisdictions. Ottawa is on the border of Ontario and Quebec."
            ),
            StudyLesson(
                topicId = "ca_symbols",
                country = Country.CANADA,
                title = "Canadian National Symbols",
                summary = "Canadian symbols reflect its natural beauty, heritage, and values of peace, order, and good government.",
                keyFacts = listOf(
                    "The Maple Leaf: Adopted as a symbol by French Canadians in the 1700s, featured on the flag since 1965.",
                    "The Beaver: Official animal symbol adopted in 1975, honoring centuries of fur trade history.",
                    "Fleur-de-lis: Symbol of French royalty, present on the Quebec provincial flag.",
                    "National Anthem: 'O Canada', officially adopted in 1980.",
                    "Royal Motto: 'A Mari Usque Ad Mare' ('From Sea to Sea').",
                    "Victoria Cross: The highest honour for military valour awarded to 96 Canadians."
                ),
                importantDatesOrNumbers = listOf(
                    "1965" to "Red and White Maple Leaf flag raised for first time",
                    "1980" to "'O Canada' proclaimed national anthem",
                    "1975" to "Beaver recognized as official symbol"
                ),
                memoryTip = "Anthem = 'O Canada'. Motto = 'From Sea to Sea' (A Mari Usque Ad Mare)."
            ),

            // USA Lessons
            StudyLesson(
                topicId = "us_gov_principles",
                country = Country.USA,
                title = "Principles of American Democracy",
                summary = "The United States is a constitutional federal republic governed by the rule of law. The Constitution of 1787 is the supreme law of the land, establishing government of the people, by the people, and for the people.",
                keyFacts = listOf(
                    "The Constitution is the supreme law of the land: no state or federal law may contradict it.",
                    "'We the People': The first three words announce popular sovereignty and consent of the governed.",
                    "Rule of Law: Everyone must follow the law; leaders must obey the law; no one is above the law.",
                    "An amendment is a formal change or addition to the Constitution; 27 have been ratified."
                ),
                importantDatesOrNumbers = listOf(
                    "1787" to "Constitutional Convention in Philadelphia",
                    "27" to "Total Amendments to the U.S. Constitution",
                    "3" to "Opening words: 'We the People'"
                ),
                memoryTip = "Rule of law means: No one—not even the President—is above the law."
            ),
            StudyLesson(
                topicId = "us_constitution",
                country = Country.USA,
                title = "Constitution & The Bill of Rights",
                summary = "The Bill of Rights consists of the first 10 amendments ratified in 1791, safeguarding core liberties from governmental intrusion.",
                keyFacts = listOf(
                    "First Amendment guarantees 5 rights: Freedom of Speech, Freedom of Religion, Freedom of the Press, Right to Peacefully Assemble, Right to Petition the Government.",
                    "Second Amendment protects the right to keep and bear arms.",
                    "Fourth Amendment prohibits unreasonable searches and seizures.",
                    "Fifth Amendment guarantees due process and protection against self-incrimination."
                ),
                importantDatesOrNumbers = listOf(
                    "1791" to "Bill of Rights ratified",
                    "10" to "Amendments in the original Bill of Rights",
                    "5" to "Rights guaranteed in the First Amendment (Speech, Religion, Press, Assembly, Petition)"
                ),
                memoryTip = "1st Amendment = 'RAPPS': Religion, Assembly, Press, Petition, Speech."
            ),
            StudyLesson(
                topicId = "us_branches",
                country = Country.USA,
                title = "Three Branches of U.S. Government",
                summary = "To prevent tyranny, power is divided into three separate branches with checks and balances against one another.",
                keyFacts = listOf(
                    "Legislative Branch (Congress): Makes laws. Consists of the Senate (100 members) and House of Representatives (435 members).",
                    "Executive Branch (President, Vice President, Cabinet): Enforces and administers laws. President is Commander in Chief.",
                    "Judicial Branch (Supreme Court & federal courts): Interprets laws, resolves disputes, decides if laws violate the Constitution.",
                    "Checks and Balances: President can veto bills from Congress; Congress can override veto with 2/3 vote; Supreme Court can declare laws unconstitutional."
                ),
                importantDatesOrNumbers = listOf(
                    "100" to "U.S. Senators (2 per state)",
                    "435" to "Voting U.S. Representatives (based on state population)",
                    "9" to "Justices on the Supreme Court"
                ),
                memoryTip = "Legislative makes, Executive enforces, Judicial interprets."
            ),
            StudyLesson(
                topicId = "us_elections",
                country = Country.USA,
                title = "Elections and Terms of Office",
                summary = "Federal elections are held every two years on the Tuesday following the first Monday in November.",
                keyFacts = listOf(
                    "President: Elected for a 4-year term, limited to 2 terms by the 22nd Amendment.",
                    "U.S. Senator: Serves 6-year terms; represents all people of their state.",
                    "U.S. Representative: Serves 2-year terms; represents citizens in their congressional district.",
                    "Electoral College: Electors chosen to officially cast votes for President based on state election outcomes.",
                    "Voting age: 18 years old (26th Amendment)."
                ),
                importantDatesOrNumbers = listOf(
                    "4 years" to "Presidential term (max 2 terms)",
                    "6 years" to "U.S. Senator term",
                    "2 years" to "U.S. Representative term",
                    "18" to "Minimum voting age"
                ),
                memoryTip = "Rep = 2 yrs, Pres = 4 yrs, Senator = 6 yrs (2, 4, 6 cadence!)."
            ),
            StudyLesson(
                topicId = "us_history",
                country = Country.USA,
                title = "American History & Independence",
                summary = "From thirteen British colonies declaring independence in 1776, through the Civil War that ended slavery, to 20th century global leadership and civil rights.",
                keyFacts = listOf(
                    "July 4, 1776: Declaration of Independence adopted, authored primarily by Thomas Jefferson.",
                    "George Washington: Commander in Chief during Revolutionary War and 1st U.S. President.",
                    "Civil War (1861-1865): Fought over slavery and states' rights. Abraham Lincoln preserved the Union and issued the Emancipation Proclamation.",
                    "Civil Rights Movement: Martin Luther King, Jr. and civil rights activists fought for equality and racial justice."
                ),
                importantDatesOrNumbers = listOf(
                    "July 4, 1776" to "Declaration of Independence adopted",
                    "1861-1865" to "American Civil War",
                    "1863" to "Emancipation Proclamation issued by Lincoln"
                ),
                memoryTip = "1776 = Independence. 1860s = Civil War & Lincoln. 1960s = Civil Rights & MLK Jr."
            )
        )

        fun getLessonsFor(country: Country): List<StudyLesson> {
            return ALL_LESSONS.filter { it.country == country }
        }
    }
}
