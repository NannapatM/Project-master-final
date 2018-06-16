package co.project.client.injection.module

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import co.project.client.injection.ActivityContext
import co.project.client.injection.PerActivity
import co.project.client.ui.compass.CompassMvp
import co.project.client.ui.compass.CompassPresenter
import co.project.client.ui.main.MainMvp
import co.project.client.ui.main.MainPresenter

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    @PerActivity
    internal fun provideActivity(): Activity {
        return activity
    }

    @Provides
    @PerActivity
    @ActivityContext
    internal fun providesContext(): Context {
        return activity
    }

    @Provides
    @PerActivity
    @ActivityContext
    fun providesMainPresenter(presenter: MainPresenter<MainMvp.View>): MainMvp.Presenter<MainMvp.View> {
        return presenter
    }

    @Provides
    @PerActivity
    @ActivityContext
    fun providesCompassPresenter(presenter: CompassPresenter<CompassMvp.View>): CompassMvp.Presenter<CompassMvp.View> {
        return presenter
    }

}
