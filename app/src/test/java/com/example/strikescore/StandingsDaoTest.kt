package com.example.strikescore

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.strikescore.data.database.StrikeScoreDb
import com.example.strikescore.data.database.matches.MatchDao
import com.example.strikescore.data.database.matches.asDbMatch
import com.example.strikescore.data.database.standings.StandingsDao
import com.example.strikescore.data.database.standings.asDbStandings
import com.example.strikescore.data.database.standings.asDomainStandings
import com.example.strikescore.data.database.team.asDbTeam
import com.example.strikescore.data.database.team.asDomainTeam
import com.example.strikescore.fake.FakeMatchDataSource
import com.example.strikescore.fake.FakeStandingDataSource
import com.example.strikescore.fake.FakeTeamDataSource
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class StandingsDaoTest {

    private lateinit var standingsDao: StandingsDao
    private lateinit var db : StrikeScoreDb
    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, StrikeScoreDb::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        standingsDao = db.standingsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun DaoInsert_insertStandingsIntoDb() =  runBlocking{
        standingsDao.insert(FakeStandingDataSource.standings[0].asDbStandings())
        val standings = standingsDao.getAllItems()
        assertEquals(FakeStandingDataSource.standings[0], standings.first()[0].asDomainStandings())
    }

    @Test
    @Throws(Exception::class)
    fun DaoGetAllItems_getAllStandingsFromDb() = runBlocking{
        FakeStandingDataSource.standings.forEach {
            standingsDao.insert(it.asDbStandings())
        }
        val standings = standingsDao.getAllItems()
        assertEquals(FakeStandingDataSource.standings, standings.first().asDomainStandings())
    }

    @Test
    @Throws(Exception::class)
    fun DaoGetById_getStandingsFromDb() = runBlocking{
        FakeStandingDataSource.standings.forEach {
            standingsDao.insert(it.asDbStandings())
        }
        val standings = standingsDao.getItem(1)
        assertEquals(FakeStandingDataSource.standings[0], standings.first().asDomainStandings())
    }

}