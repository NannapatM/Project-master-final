package co.project.client.data

import javax.inject.Inject
import javax.inject.Singleton

import co.project.client.data.local.DatabaseHelper
import co.project.client.data.remote.ServerService

@Singleton
class DataManager @Inject constructor(val serverService: ServerService,
                                      val databaseHelper: DatabaseHelper)
