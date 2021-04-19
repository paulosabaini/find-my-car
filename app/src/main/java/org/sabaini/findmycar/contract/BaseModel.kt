package org.sabaini.findmycar.contract

interface BaseModel<L, D> {
    val locationDao: L
    val directionsApi: D
}