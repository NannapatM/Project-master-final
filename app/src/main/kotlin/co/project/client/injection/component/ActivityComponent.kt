package co.project.client.injection.component

import dagger.Subcomponent
import co.project.client.injection.PerActivity
import co.project.client.injection.module.ActivityModule
import co.project.client.ui.compass.CompassActivity
import co.project.client.ui.main.MainActivity

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: CompassActivity)
}
