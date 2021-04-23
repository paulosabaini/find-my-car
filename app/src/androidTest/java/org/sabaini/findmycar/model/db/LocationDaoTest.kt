package org.sabaini.findmycar.model.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.sabaini.findmycar.MainCoroutineRuleAndroidTest

class LocationDaoTest {

    private lateinit var database: LocationDatabase
    private lateinit var locationDao: LocationDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRuleAndroidTest()

    @Before
    fun setup() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocationDatabase::class.java,
        ).build()
        locationDao = database.locationDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAndGetLastLocation() = runBlocking {
        val location = DatabaseLocation(1, 41.8901584, 12.4930752)
        locationDao.insert(location)
        val result = locationDao.getLastLocation()
        assertThat(result).isEqualTo(location)
    }

    @Test
    fun testInsertAndGetLastLocationWithMoreData() = runBlocking {
        val location1 = DatabaseLocation(1, 41.8901584, 12.4930752)
        val location2 = DatabaseLocation(2, 41.8901585, 12.4930753)
        val location3 = DatabaseLocation(3, 41.8901586, 12.4930754)
        locationDao.insert(location1)
        locationDao.insert(location2)
        locationDao.insert(location3)
        val result = locationDao.getLastLocation()
        assertThat(result).isEqualTo(location3)
    }
}