package co.project.client.data.local

import android.database.sqlite.SQLiteDatabase
import com.squareup.sqlbrite.BriteDatabase

import rx.Observable
import timber.log.Timber
import java.sql.SQLException
import javax.inject.Inject
import javax.inject.Singleton
import rx.Emitter.BackpressureMode

@Singleton
class DatabaseHelper @Inject constructor(val db: BriteDatabase) {

}
