package co.project.client.ui.main

import co.project.client.data.model.Client
import co.project.client.ui.base.BaseMvp

interface MainMvp {
    interface View: BaseMvp.View {
        fun onConnected(client: Client)
        fun onServerConnectionResetted()
    }
    interface Presenter<in V: BaseMvp.View>: BaseMvp.Presenter<V> {
        fun connectToServer()
        fun resetServerConnection()
    }
}
