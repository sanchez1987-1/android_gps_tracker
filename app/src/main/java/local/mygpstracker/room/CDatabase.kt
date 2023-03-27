package local.mygpstracker

import android.content.Context
import androidx.room.*
import local.mygpstracker.dao.CoordinatesDao
import local.mygpstracker.model.Coordinates


@Database(entities = [Coordinates::class], version = 1, exportSchema = true)
abstract class CoordinatesDatabase : RoomDatabase() {
    abstract fun CoordinatesDao(): CoordinatesDao
    companion object {

        @Volatile
        private var INSTANCE: CoordinatesDatabase? = null

        fun getDatabase(context: Context): CoordinatesDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE                 ?: synchronized(this) {
                val instance                = Room.databaseBuilder(
                    context.applicationContext,
                    CoordinatesDatabase::class.java,
                    "database.db" //Название файла в файловой системе.
                )
                    .build()
                INSTANCE                    = instance
                // return instance
                instance
            }
        }
    }
}
