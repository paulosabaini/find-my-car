package org.sabaini.findmycar.model.api

/*
 * Objects that represent the result JSON from the Directions API.
 */

data class Directions(
    val routes: List<Routes>
)

data class Routes(
    val legs: List<Legs>
)

data class Legs(
    val steps: List<Steps>
)

data class Steps(
    val polyline: Polyline
)

data class Polyline(
    val points: String
)