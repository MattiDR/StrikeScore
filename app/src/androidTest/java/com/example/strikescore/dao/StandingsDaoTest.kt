package com.example.strikescore.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.strikescore.data.database.StrikeScoreDb
import com.example.strikescore.data.database.matches.asDbMatch
import com.example.strikescore.data.database.matches.asDomainMatches
import com.example.strikescore.data.database.standings.StandingsDao
import com.example.strikescore.data.database.standings.asDbStandings
import com.example.strikescore.data.database.standings.asDomainStandings
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

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
        standingsDao.insert(FakeDataSource.standings[0].asDbStandings())
        val standings = standingsDao.getAllItems()
        assertEquals(FakeDataSource.standings[0], standings.first()[0].asDomainStandings())
    }

    @Test
    @Throws(Exception::class)
    fun DaoGetAllItems_getAllStandingsFromDb() = runBlocking{
        FakeDataSource.standings.forEach {
            standingsDao.insert(it.asDbStandings())
        }
        val standings = standingsDao.getAllItems()
        assertEquals(FakeDataSource.standings, standings.first().asDomainStandings())
    }

    @Test
    @Throws(Exception::class)
    fun DaoGetById_getStandingsFromDb() = runBlocking{
        FakeDataSource.standings.forEach {
            standingsDao.insert(it.asDbStandings())
        }
        val standings = standingsDao.getItem(1)
        assertEquals(FakeDataSource.standings[0], standings.first().asDomainStandings())
    }

    @Test
    @Throws(Exception::class)
    fun DaoDelete_deleteStandingsFromDb() = runBlocking{
        FakeDataSource.standings.forEach {
            standingsDao.insert(it.asDbStandings())
        }
        standingsDao.delete(FakeDataSource.standings[0].asDbStandings())
        val standings = standingsDao.getAllItems()
        assertEquals(FakeDataSource.standings.subList(1, FakeDataSource.standings.size), standings.first().asDomainStandings())
    }

}