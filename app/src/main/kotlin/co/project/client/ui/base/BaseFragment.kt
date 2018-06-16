package co.project.client.ui.base

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment(), BaseMvp.View {

     override fun setup() {
         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
     }

     override fun showMessage(message: String?) {
         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
     }

     override fun onError(message: String?) {
         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
     }

     override fun isLoading(): Boolean {
         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
     }

     override fun showLoading() {
         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
     }

     override fun hideLoading() {
         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
     }

}