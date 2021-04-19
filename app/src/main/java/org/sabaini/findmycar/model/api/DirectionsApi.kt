package org.sabaini.findmycar.model.api

import retrofit2.http.GET
import retrofit2.http.Query

/*
 * Make a request to the Google Directions API to get the best route from origin to destination.
 */

interface DirectionsApi {

    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): Directions
}