package org.sabaini.findmycar.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.sabaini.findmycar.MainCoroutineRule
import org.sabaini.findmycar.model.api.*
import org.sabaini.findmycar.model.db.DatabaseLocation
import org.sabaini.findmycar.model.db.LocationDao

@RunWith(MockitoJUnitRunner::class)
class ModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var locationDao: LocationDao

    @Mock
    private lateinit var directionsApi: DirectionsApi
    private lateinit var model: Model
    private lateinit var data: MutableList<DatabaseLocation>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        model = Model(locationDao, directionsApi)
        data = mutableListOf()
    }

    @Test
    fun testGetLastLocation() = runBlocking {
        val location = DatabaseLocation(1, 41.8901584, 12.4930752)
        data.add(location)
        Mockito.`when`(locationDao.getLastLocation()).then { data.last() }
        val lastLocation = model.getLastLocation()
        assertThat(lastLocation).isEqualTo(location)
    }

    @Test
    fun testInsertLocation() = runBlocking {
        val location = DatabaseLocation(null, 41.8901584, 12.4930752)
        Mockito.`when`(locationDao.insert(location)).then { data.add(location) }
        model.insertLocation(41.8901584, 12.4930752)
        assertThat(data[0]).isEqualTo(location)
    }

    @Test
    fun testGetDirections() = runBlocking {
        val directions = Directions(
            listOf(Routes(listOf(Legs(listOf(Steps(Polyline("0,0")))))))
        )
        Mockito.`when`(
            directionsApi.getDirections(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString()
            )
        ).thenAnswer { directions }

        val response = model.getDirections("", "", "")
        assertThat(response).isEqualTo(directions)
    }
}