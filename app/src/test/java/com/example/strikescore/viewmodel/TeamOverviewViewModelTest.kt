package com.example.strikescore.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.test.platform.app.InstrumentationRegistry
import com.example.strikescore.data.TeamRepository
import com.example.strikescore.fake.FakeDataSource
import com.example.strikescore.fake.FakeTeamRepository
import com.example.strikescore.model.Team
import com.example.strikescore.network.team.ApiTeam
import com.example.strikescore.ui.teamScreen.TeamApiState
import com.example.strikescore.ui.teamScreen.TeamOverviewState
import com.example.strikescore.ui.teamScreen.TeamOverviewViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class TeamOverviewViewModelTest {
    private lateinit var viewModel: TeamOverviewViewModel
    private lateinit var apiState : TeamApiState


    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Before
    fun init(){
        viewModel = TeamOverviewViewModel(
            teamsRepository = FakeTeamRepository(),
        )

        apiState = viewModel.teamApiState
        when(apiState){
            is TeamApiState.Success -> return
            else -> {throw  AssertionError()}
        }
    }

    @Test
    fun getTeamTest() = runTest {
        Assert.assertEquals(FakeDataSource.teams, viewModel.uiListState.first().teamList)
    }


}

class TestDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}