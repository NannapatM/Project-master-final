package co.project.client

import android.app.Application
import android.support.annotation.VisibleForTesting
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber
import co.project.client.injection.component.ApplicationComponent
import co.project.client.injection.component.DaggerApplicationComponent
import co.project.client.injection.module.ApplicationModule

open class BoilerplateApplication: Application() {

    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this))
            return
        LeakCanary.install(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        initDaggerComponent()
    }

    private fun initDaggerComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}
