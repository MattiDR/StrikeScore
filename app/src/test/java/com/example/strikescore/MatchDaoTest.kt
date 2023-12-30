package com.example.strikescore

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.room.Room
import com.example.strikescore.data.database.StrikeScoreDb
import com.example.strikescore.data.database.matches.MatchDao
import com.example.strikescore.data.database.matches.asDbMatch
import com.example.strikescore.data.database.matches.asDomainMatch
import com.example.strikescore.data.database.matches.asDomainMatches
import com.example.strikescore.fake.FakeMatchDataSource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
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
class MatchDaoTest {

    private lateinit var matchDao: MatchDao
    private lateinit var db : StrikeScoreDb
    private var date : String = "2021-08-14T14:00:00Z"
    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, StrikeScoreDb::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        matchDao = db.matchDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun DaoInsert_insertMatchIntoDb() = runBlocking{
        matchDao.insert(FakeMatchDataSource.matches[0].asDbMatch())
        val match = matchDao.getAllItems(date).map {
            it.asDomainMatches()
        }.first()[0]
        assertEquals(FakeMatchDataSource.matches[0], match)

    }

    @Test
    @Throws(Exception::class)
    fun DaoGetAllItems_getAllMatchesFromDb() = runBlocking{
        FakeMatchDataSource.matches.forEach {
            matchDao.insert(it.asDbMatch())
        }
        val matches = matchDao.getAllItems(date).map {
            it.asDomainMatches()
        }.first()
        assertTrue(matches.containsAll(FakeMatchDataSource.matches.filter { it.utcDate == date }.map { it }))
    }

    @Test
    @Throws(Exception::class)
    fun DaoGetById_getMatchFromDb() = runBlocking{
        FakeMatchDataSource.matches.forEach {
            matchDao.insert(it.asDbMatch())
        }
        val match = matchDao.getItem(1).map {
            it.asDomainMatch()
        }.first()
        assertEquals(FakeMatchDataSource.matches[0], match)
    }
}