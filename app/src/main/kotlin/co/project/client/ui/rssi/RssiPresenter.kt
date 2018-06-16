package co.project.client.ui.rssi

import android.hardware.Sensor
import android.hardware.SensorEvent
import co.project.client.data.DataManager
import co.project.client.data.model.Client
import co.project.client.injection.ConfigPersistent
import co.project.client.ui.base.BasePresenter
import co.project.client.ui.main.MainMvp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
/*
@ConfigPersistent
class RssiPresenter<V : RssiMvp.View> @Inject
constructor(private val dataManager : DataManager) : BasePresenter<V>(), RssiMvp.Presenter<V> {


}*/