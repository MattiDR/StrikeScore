package com.example.strikescore.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.test.platform.app.InstrumentationRegistry
import com.example.strikescore.data.TeamRepository
import com.example.strikescore.fake.FakeDataSource
import com.example.strikescore.fake.FakeTeamRepository
import com.example.strikescore.model.Team
import com.example.strikescore.network.team.ApiTeam
import com.example.strikescore.ui.teamScreen.TeamOverviewState
import com.example.strikescore.ui.teamScreen.TeamOverviewViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
//    private var teamRepository: TeamRepository = FakeTeamRepository(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)



    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getActorDetails should update uiActorDetailsState and set ActorApiState to Success`() = testScope.runTest {
//        viewModel = TeamOverviewViewModel(teamRepository)
        // Your test logic here
        val collectCompleted = CompletableDeferred<Unit>()
        var teamOverviewViewUiState: TeamOverviewState? = null
        val job = launch {
            teamOverviewViewUiState = viewModel.uiState.value
            collectCompleted.complete(Unit)
        }
        advanceUntilIdle()
        assertEquals((teamOverviewViewUiState as TeamOverviewState), FakeDataSource.teams)
        job.cancel()

    }

}