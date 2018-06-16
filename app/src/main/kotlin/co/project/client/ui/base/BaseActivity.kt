package co.project.client.ui.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.util.LongSparseArray
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import butterknife.Unbinder
import timber.log.Timber
import co.project.client.BoilerplateApplication
import co.project.client.R
import co.project.client.injection.component.ActivityComponent
import co.project.client.injection.component.ConfigPersistentComponent
import co.project.client.injection.component.DaggerConfigPersistentComponent
import co.project.client.injection.module.ActivityModule
import java.util.concurrent.atomic.AtomicLong

abstract class BaseActivity: AppCompatActivity(), BaseMvp.View {

     companion object {
        @JvmStatic private val KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID"
        @JvmStatic private val NEXT_ID = AtomicLong(0)
        @JvmStatic private val componentsMap = LongSparseArray<ConfigPersistentComponent>()
    }

    protected var unbinder: Unbinder? = null

    private var activityId: Long = 0
    private var progressDialog: ProgressDialog? = null
    protected lateinit var activityComponent: ActivityComponent

    @Suppress("UsePropertyAccessSyntax")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        activityId = savedInstanceState?.getLong(KEY_ACTIVITY_ID) ?: NEXT_ID.getAndIncrement()

        if (componentsMap[activityId] != null)
            Timber.i("Reusing ConfigPersistentComponent id=%d", activityId)

        componentsMap.get(activityId).let {
            if (it == null) {
                Timber.i("Creating new ConfigPersistentComponent id=%d", activityId)

                val component = DaggerConfigPersistentComponent.builder()
                        .applicationComponent((applicationContext as BoilerplateApplication).applicationComponent)
                        .build()

                componentsMap.put(activityId, component)
                // return
                component
            } else {
                // return
                it
            }
        }.also {
            activityComponent = it.activityComponent(ActivityModule(this))
        }
    }

    abstract override fun setup()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_ACTIVITY_ID, activityId)
    }

    override fun onDestroy() {
        if (!isChangingConfigurations) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", activityId)
            componentsMap.remove(activityId)
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        supportFragmentManager.let {
            if (it.backStackEntryCount > 0) {
                it.popBackStack()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun getContext(): Context = baseContext

    override fun showMessage(message: String?) {
        Toast.makeText(this, message ?: getString(R.string.some_error), Toast.LENGTH_SHORT).show()
    }

    override fun onError(message: String?) {
        Toast.makeText(this, message ?: getString(R.string.some_error), Toast.LENGTH_SHORT).show()
    }

    override fun isLoading() = progressDialog?.isShowing ?: false

    override fun showLoading() {
        if (!isLoading()) {
            progressDialog = ProgressDialog.show(this, null, getString(R.string.loading), true, false)
        }
    }

    override fun hideLoading() {
        if (isLoading()) {
            progressDialog?.cancel()
        }
    }
}
