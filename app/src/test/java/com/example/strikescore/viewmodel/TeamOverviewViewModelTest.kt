package com.example.strikescore.viewmodel


import com.example.strikescore.fake.FakeDataSource
import com.example.strikescore.fake.FakeTeamRepository
import com.example.strikescore.ui.teamScreen.TeamApiState
import com.example.strikescore.ui.teamScreen.TeamOverviewViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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