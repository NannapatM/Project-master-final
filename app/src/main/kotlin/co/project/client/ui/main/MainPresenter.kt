package co.project.client.ui.main

import javax.inject.Inject

import co.project.client.data.DataManager
import co.project.client.data.model.Client
import co.project.client.injection.ConfigPersistent
import co.project.client.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@ConfigPersistent
class MainPresenter<V : MainMvp.View> @Inject
constructor(private val dataManager : DataManager) : BasePresenter<V>(), MainMvp.Presenter<V> {
    override fun connectToServer() {
        view.showLoading()
        dataManager.serverService
                .connect()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorReturn { err ->
                    err.printStackTrace()
                    Client(err.message ?: err.toString(),0.0,0.0)
                }
                .subscribe { client ->
                    view.onConnected(client)
                    view.hideLoading()
                }
    }

    override fun resetServerConnection() {
        view.showLoading()
        dataManager.serverService
                .reset()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorComplete { err ->
                    err.printStackTrace()
                    view.onError(err.message)
                    true
                }
                .subscribe {
                    view.onServerConnectionResetted()
                    view.hideLoading()
                }
    }

}