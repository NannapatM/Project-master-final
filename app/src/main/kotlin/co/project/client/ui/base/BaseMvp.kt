package co.project.client.ui.base

import android.content.Context

/**
 * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
interface BaseMvp {
    interface Presenter<in V: View> {
        fun attachView(mvpView: V)
        fun detachView()
    }
    interface View {
        fun setup()
        fun getContext(): Context?
        fun showMessage(message: String?)
        fun onError(message: String?)
        fun isLoading(): Boolean
        fun showLoading()
        fun hideLoading()
    }
}
