package com.example.strikescore

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.strikescore.data.database.StrikeScoreDb
import com.example.strikescore.data.database.team.TeamDao
import com.example.strikescore.data.database.team.asDbTeam
import com.example.strikescore.data.database.team.asDomainTeam
import com.example.strikescore.data.database.team.asDomainTeams
import com.example.strikescore.fake.FakeTeamDataSource
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class TeamDaoTest {

    private lateinit var teamDao: TeamDao
    private lateinit var db : StrikeScoreDb
    private var name:String = "Arsenal FC"

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, StrikeScoreDb::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        teamDao = db.teamDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun DaoInsert_insertTeamIntoDb() = runBlocking{
        teamDao.insert(FakeTeamDataSource.teams[0].asDbTeam())
        val team = teamDao.getAllItems().map {
            it.asDomainTeams()
        }.first()
        TestCase.assertEquals(FakeTeamDataSource.teams[0], team[0])
    }

    @Test
    @Throws(Exception::class)
    fun DaoGetAllItems_getAllTeamsFromDb() = runBlocking{
        FakeTeamDataSource.teams.forEach {
            teamDao.insert(it.asDbTeam())
        }
        val teams = teamDao.getAllItems().map {
            it.asDomainTeams()
        }.first()
        TestCase.assertEquals(FakeTeamDataSource.teams, teams)
    }

    @Test
    @Throws(Exception::class)
    fun DaoGetByName_getTeamsFromDb() = runBlocking{
        FakeTeamDataSource.teams.forEach {
            teamDao.insert(it.asDbTeam())
        }
        val team = teamDao.getItem(name).first().asDomainTeam()
        TestCase.assertEquals(FakeTeamDataSource.teams.first { it.name == name }, team)
    }
}