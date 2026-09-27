package com.example.data.model

data class StudentProfile(
    val name: String = "Alex Chen",
    val pronouns: String = "he/him",
    val handle: String = "@Hackfinix",
    val role: String = "Lab Lead",
    val institution: String = "CMRK Institute of Technology",
    val department: String = "Computer Science & Data Science",
    val classSection: String = "Section CSD-A",
    val rollNumber: String = "24CSD084",
    val semester: String = "Semester 3 (Fall '26)",
    val gradYear: String = "2030",
    val majorYear: String = "CSD ’30 • Computer Science & Data Science",
    val email: String = "alex.chen@cmrk.edu",
    val gpa: String = "3.92",
    val bio: String = "AI & Fullstack enthusiast • Building at @Hackfinix • CSD Lab Lead & NeuroSymbolic AI researcher. 🚀",
    val avatarUrl: String = StudyHiveAssets.ALEX_AVATAR,
    val bannerUrl: String = StudyHiveAssets.CAMPUS_BANNER,
    val postsCount: Int = 24,
    val connectedCount: Int = 380,
    val badgesCount: Int = 8,
    val isPrivate: Boolean = false,
    val showOnline: Boolean = true,
    val allowNonClassmateDMs: Boolean = true,
    val deadlineReminders: Boolean = true,
    val newGradeAlerts: Boolean = true,
    val socialMentions: Boolean = false
)

data class Classmate(
    val id: String,
    val name: String,
    val pronouns: String,
    val handle: String,
    val avatarUrl: String,
    val department: String,
    val classSection: String,
    val rollNumber: String,
    val isOnline: Boolean,
    val mutualCourses: String,
    val isConnected: Boolean = false,
    val bio: String = ""
)

data class StudyGroup(
    val id: String,
    val name: String,
    val courseCode: String,
    val section: String,
    val memberCount: Int,
    val activeCount: Int,
    val roomOrHall: String,
    val lastMessage: String,
    val lastMessageSender: String,
    val lastMessageTime: String,
    val hasActivePomodoro: Boolean = false,
    val isOfficial: Boolean = true
)

data class PortfolioItem(
    val id: String,
    val title: String,
    val meta: String,
    val date: String,
    val likesCount: Int,
    val imageUrl: String,
    val detailedSummary: String = ""
)

data class AcademicBadge(
    val id: String,
    val emoji: String,
    val title: String,
    val subtitle: String,
    val progressPercent: Int = 100,
    val isFull: Boolean = true
)

data class ClassRepository(
    val id: String,
    val title: String,
    val subtitle: String,
    val curatedFilesCount: String,
    val upvotes: String,
    val courseCode: String,
    val iconName: String
)

data class StudyInvite(
    val id: String,
    val studentName: String,
    val pronouns: String = "they/them",
    val avatarUrl: String,
    val timestamp: String,
    val mutualContext: String,
    val message: String,
    val isTeamInvite: Boolean = false
)

data class InterestTag(
    val id: String,
    val name: String,
    val iconName: String,
    val isSelected: Boolean = false
)

object StudyHiveAssets {
    const val LOGO = "https://lh3.googleusercontent.com/aida/AEtjO1WHPrQTmZp8mSlORf_Vy5EjdhcnVtaEVuasFk6IjOfaweOcHeVHOus7P5ue8gfAGTwx4A4RPLhAF7EP-iOSCuQ53RttaALfnlnse9L5ndBsbr6Yfcs8M7Qf_VdW7OO8QkfGW9ZzEQY4j1egClU2A8lv7CT2HHseQh9WcFsm98KJfaj5mv-cYY1lBc4qxxUH5yaIKrGKVVCegc94_MJXe2KtnUu5ctvM0dPAi1FZqhp4nEaO_JhP1vXy4Ps"
    const val ALEX_AVATAR = "https://lh3.googleusercontent.com/aida-public/AB6AXuD8Z4ilneXYN4IvIO4UU1E96Prs-fKQhL73XdWN7Ww0Gn70zOZqq678vuOrPuXCfr4RKT-Rv8T3ffzRRooxzv7VtGj1jfh8CCMQ5aEVM0NWVjK2Wdr8ArJVdiuG48F5YLp9ak6OXFl05spnCUTWFZLQzju7DNJxNA8Erj_uWoWMgdg0SnRxijqPl1pKYulZxsm1HK00Jkh1ITAjw7KYcu0HnXhNMiWaXGFj91qub9so5-t5NC__YdZe"
    const val CAMPUS_BANNER = "https://lh3.googleusercontent.com/aida-public/AB6AXuDcVRwQSdcwBVktFcPwZKdZRZvF6fRVVSUyzcgEfyyNyr8jjaa8gFStg0dPOlHiDfFqgmQF8wAk52RnRwmv2LkPTUohl3Id8r3y8JlxEE3rubZWbgPv51zg1WNXA9NuaiZvC1BbENj-O0L01o_n3UfhOZIHotTykkWvAFCaUDj4GKGNx21C8m7NCzzeT5jaHqgwyI7ui80N1TydgdKTjgjrhNX6AD5wq2uFncToonq3zNb29ufvpO79"
    
    // Portfolio images
    const val PORTFOLIO_1 = "https://lh3.googleusercontent.com/aida-public/AB6AXuCpsJeOwswAiBfrqLfGIB8VsqNQar1ewoaBXtIntGaxNNzqxdNGFL4_t_gFqDOEdbTYQAnASjK5b9sgo61Irw4Yas9ohMe5GuJKNbrSXscL34_GJSwuWd4OWLVXp1Is77NtZOmBN1MuIzU06Ay1lC1Jm_AvnZDj3eGuLQbgJqzd6ablFOPf8DqU77-fnSMEUpKychP4s2_ZDqkk4H0C8gzHL_AZRFES4B5cKEc3pMLCvkg7sx6XCK3n"
    const val PORTFOLIO_2 = "https://lh3.googleusercontent.com/aida-public/AB6AXuDXJhu_5w2dswt6dFyvo7wrkxW-yObCe7J7rJbSxacZ3HSBACgMESRTAZmVoxqq5TWlMMq-0-tx768EvtrCsZ04Fg2KOrFahlG5CqCU3Dn8XDbkv0O-Jfv1V4Mfhzg-Y77gSQEyteLAnaO82Xy6DYUa0Q0AfLSnD-tvVrchfOS5OmlUBq8RWPX_ziGt7EUJPH7IF_jtySgMS9CUg1go1VfpH5ngDoKQviqtuoMKEiLEzQirpHKdClgX"
    const val PORTFOLIO_3 = "https://lh3.googleusercontent.com/aida-public/AB6AXuC5MltWQXZYkJ5AiBs9mfnYI9Z-tg4SxLGvJh7kYr02xzkb5JS-ED8QRzoNA0hqApqLHr6GFjrbsD8-a3g2tio0PAlU0QI3428890RTilg1k3Qvb0iMPzmYI8jVCy8AcH7R6uLDRdaqgNYhnTIVYUc_U0ULGAo4owoKs3avYi74wad_hGTLDXhh9bO9O806EHzT3AeAM24ixGCA0Wk7w1y8yjYUrSm7A55MTbhFcY_qHrh2HhCMMlVF"
    const val PORTFOLIO_4 = "https://lh3.googleusercontent.com/aida-public/AB6AXuAIlS5JlhBbtdo4ZGmUsa5oPmUzi0teY5NmB-RNtgwVAaq3hODApIFa5QOLqPYISTEb4PwrIHYy512T2I2u7ItGlPFLIM9t757G6ODwu-Pu9Sv3XAzPXFt6PUWWwrO_NKK1H4zhtKWeQWDVQNmH9OiiCsITp9Y6EmywhhAaj0XFp8SpCQTi3FrdX1j7Y6MUPzO9fGWKQZNn1yTaA4s3iaKAtazTvCH-MZ_Swlv1PifuQ1v9LqzY7yvA"
    const val PORTFOLIO_5 = "https://lh3.googleusercontent.com/aida-public/AB6AXuCb6EmV4qofBC0mSTeQit3uEUy4hDPelheeMZE17Mi1F1ffIPSeBGHkScbL8Mb6KUHoOegWAfuYY0oVlqqU8kJC59PggpkQPmjcaQzcg0nNRtAq9hf2KLcyqLi_78dBUXOpHaJrgAk2-vE_-MiedEaEXLxxkqw08rYHayOFZ8qKVqbM3TOHHJTuciKUgtfgwYH6XWx93Mx1RXARMI6bm00lJO_Yz4lmrogbTpjFYhTmT5QNzQlAKufZ"
    const val PORTFOLIO_6 = "https://lh3.googleusercontent.com/aida-public/AB6AXuAbat8Eqwg15SV4agGfET5u-BYEaadCI7--CDEDklbgQ1Oz1ZItxg8SY_PJufLm8RXPAJtm347mFIMUs7J0Zbbr9NFjKaxs1F0m3EpXm4yk69ypHwMqtmYeiwHVQeu1_LnG5Pa81xchjBiMobBYyQybLkd_CJr5TxPw03u2vQVQmI4FyJ3_hJI7KCeylVItb3t3l3U0ynEs9Cxaz7wEeqcG_RrITc-hq_edXL1ocDMuM5cdqRo34sbV"

    // Peer avatars
    const val SOPHIA_AVATAR = "https://lh3.googleusercontent.com/aida-public/AB6AXuD8TKLfDHk07c2Tq7fd5ZFTRrSlB2-ytc-_T7oGCO6ybXg54_xD59v-kpbTI-QnUSGRQpIF8ofxg7Kxbsc1Ue5bDy3VYl1-pEm1ivwpvRTDIwhTIUJ9yBTBR6EjDGooglCfByMmnBOAaAP92Xi7rSSd4KrXHpx4i2T0oElsSrGPmIRUQn0X5vr_Mn4hHdNLQa3oMbOz5ji9AjBKE6gdXHdewKBY3Y6KeaXbL3VlzeCtZWgfz-KEoWrq"
    const val ARJUN_AVATAR = "https://lh3.googleusercontent.com/aida-public/AB6AXuBUffrVXyVe2w9PDsv5D0wQoAzVZvFe5zNMSVIw_5rgpR9uRW39NIc2Vqx3jTRpfTaqwEryOqYQk3yzR9kDqHLqKrrWP-nd8p-psGNsKxJxAkEYHMvmYJIBtJ8GmS40iJrRCd4NBAglsd6lyerPAvO_ps8omdDYp_38MHe8IQ6wDSDTAokA1pR530m8OkjA8r-xdXVQH74LXugoK1rMnRyTJukKkJyfpBCzzAJwodv5Ych3c7LZRNRx"
    const val DAVID_AVATAR = "https://lh3.googleusercontent.com/aida-public/AB6AXuCpfxqWHuew3BRcI44GwAYuE5mpryzims-EaQ_6-22wDYmFct_3mkxsgfvivl5XRmWUu7Ae4Umxuu2nW6U189AgIGRv03Ip8sbhxegvh5VsAfWVdev4baC3YvFOKVGdnlqJmKMU6HyTjtd7ZYKny28POOVOULk3tgJJbpk9YD0l8iJNr2iJUX0thErPJudyj63UAxj9rD78In46rsbsoN46Y7SEMNwW7N0ZcklQvx1yzi4doqCCutic"
    const val CHLOE_AVATAR = "https://lh3.googleusercontent.com/aida-public/AB6AXuApwSugGuyjvILSncdx-kmmG_-eSbnfuqBFjU6Cad8LLk5EXaFPXCHNbwYLW4KKuWV0bKld2NEQoBlkqv6lMNQ5LreSZWlajw7NegsKq_J5syczhumASK1KuEGHhF9seGLatuBVqTripkASGV3irDQAG2xZT-Ged12IyotgCXkhIMlfK3DxfP7xI_YGxdYXi-ZEKsIpgS9JTEYL_UwCZk3C5AXErTy7QpzJ9W51N3_OkIb-wSclxCTQ"
    const val LIAM_AVATAR = "https://lh3.googleusercontent.com/aida-public/AB6AXuCNyf9MyPOSid7ChlAQRZ12eIEweNnX44lxmz3HMi9MKg1bql0szCC5YCXl_HHbYdQMHWPJNGKYFhXynFnVUvFIW9PLvE6XbqP5HCxgZ_NZB8-zToPV7WSomNeNSCdpVWheRmlsgMYVwGP_Sn_tCnc7LqVW5nqbG9h1ZCn2PD_U1oaR4pGqs9BBL1NRJxLcfNDaI4b0_ykXLJaf8Q7nlJcFXP1QYrTG0S86gf-5g08gQsUfVEubiPYQ"
    const val POST_SAMPLE_IMG = "https://lh3.googleusercontent.com/aida-public/AB6AXuDbI3rQOuDuMj3oY-N1yNRpyluUeTRel-83nVyuxW74guGD19f-cXEf3SPR64xRZkxwpuriVfEN1ADd9ytyGfzvDF4Keh6cWrjEQBSoAvwCAfYWtFjzCTyFpOMwC2ktPLkD-aBnB8GDy2jg2cLbgdn2Mq-Gat-Cj03faAHDoO7h2uBFEFCqZd6lsRD0Zp9KWmgCPMWUD-bdhV3i7DHmZmKXM8_LlHrgPX6altxwYE9pumV6S69lcbDH"
    const val EMILY_AVATAR = "https://lh3.googleusercontent.com/aida-public/AB6AXuBGFloQvQli-ISwSHPdDgvU5iBxnMoMRAHjPwsdSreEKFkCPYn-MJ9bKbJOWIBsQpO75Qrd1rtIFsjLyWkVdaeXZ98vlMVjDEiWOl4zujUIzSnuVYaJ45NdjxfQe2vxenG-oLkuo-RYwBqPVpX-zx6FeXFJQ_Y2Agix9bscAgCKApkWydY3UxzR3GmW_Inof6UjW7rUhFi6_q4aUyvGFtrsIiegxxwcdke6kf0FAynK10pMFqS1YBa_"
    const val RAHUL_AVATAR = "https://lh3.googleusercontent.com/aida-public/AB6AXuDc339Q9K97Opo4pM0gyQu-nvcKUUiAocefZYhDfvkbPho9U7XUQS5ORY9nslvKTiJU9KMcJ6EEPUUVYHZaKVr9i_OLSdQvyQvkLshqrTTR3X2Uzix-FO5G5oXZNqAa7Upylv4G2FS0RX7On-PGEKGUMm1Skh5nz9nsOU5pygUHP6UX1-LgmoEQ5GXZgHMt2opqprpEA51vCj_SrbkZEwsv2lCtrTfpbrz1YQKJgOY7qUQ2FQB8cCtG"
}
