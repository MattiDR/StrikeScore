package com.example.strikescore.repository

import com.example.strikescore.fake.FakeTeamApiService
import com.example.strikescore.fake.FakeTeamRepository
import com.example.strikescore.model.Team
import com.example.strikescore.network.team.asDomainObjects
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ApiTeamRepositoryTest {
    @Test
    fun TeamRepository_getTeams_verifyTeamsList() =
        runTest {
            val repository = FakeTeamRepository().getTeams()

            var repositoryTeams : List<Team> = listOf()
            //add repository teams to repositoryTeams
            repository.collect { teams ->
                repositoryTeams = repositoryTeams.plus(teams)
            }



            assertEquals(FakeTeamApiService().getTeams().teams.asDomainObjects(), repositoryTeams)
            }
}