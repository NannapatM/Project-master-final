package co.project.client.injection.component

import android.app.Application
import android.content.Context
import dagger.Component
import co.project.client.data.DataManager
import co.project.client.data.SyncService
import co.project.client.data.local.DatabaseHelper
import co.project.client.data.remote.ServerService
import co.project.client.injection.ApplicationContext
import co.project.client.injection.module.ApplicationModule
import co.project.client.injection.module.DataModule

import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, DataModule::class))
interface ApplicationComponent {
    fun inject(syncService: SyncService)

    @ApplicationContext fun context(): Context
    fun application(): Application
    fun serverService(): ServerService
    fun databaseHelper(): DatabaseHelper
    fun dataManager(): DataManager
}
