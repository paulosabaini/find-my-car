package org.sabaini.findmycar.mvp

interface FindMyCarContract {
    interface View : BaseView<FindMyCarPresenter> {
        fun getLocationPermission()
        fun showLocationText(text: String)
        fun showSnackBar(text: String)
    }

    interface Presenter : BasePresenter {
        fun saveLocation()
        fun showLocation()
        fun traceLocation()
    }
}