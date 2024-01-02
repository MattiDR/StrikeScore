package com.example.strikescore.fake

import com.example.strikescore.model.Team
import com.example.strikescore.network.team.ApiTeam

object FakeDataSource {

    val teams = listOf(
        Team(
            "Arsenal FC",
            "ARS",
            "https://crests.football-data.org/1.svg",
        ),
        Team(
            "Aston Villa FC",
            "AVL",
            "https://crests.football-data.org/2.svg",
        ),
    )

   val apiTeams = listOf(
       ApiTeam(
           "Arsenal FC",
           "ARS",
           "https://crests.football-data.org/1.svg",
       ),
         ApiTeam(
              "Aston Villa FC",
              "AVL",
              "https://crests.football-data.org/2.svg",
         ),
       )

}