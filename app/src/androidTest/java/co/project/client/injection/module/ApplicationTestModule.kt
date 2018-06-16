package co.project.client.injection.module

import android.app.Application
import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import dagger.Module
import dagger.Provides
import co.project.client.data.DataManager
import co.project.client.data.local.DatabaseHelper
import co.project.client.injection.ApplicationContext
import javax.inject.Singleton

@Module
class ApplicationTestModule(val application: Application) {

    @Provides
    @Singleton
    internal fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    @ApplicationContext
    internal fun provideContext(): Context {
        return application
    }

    /***** MOCKS *****/

    @Provides
    fun ribotsService(): RibotsService {
        return mock()
    }

    @Provides
    fun databaseHelper(): DatabaseHelper {
        return mock()
    }

    @Provides
    fun dataManager(): DataManager {
        return mock()
    }
}
