package local.mygpstracker.dao

import androidx.room.*
import local.mygpstracker.model.Coordinates

@Dao
interface CoordinatesDao {
    @Query("SELECT * FROM coordinates")
    fun getAll(): List<Coordinates>

    @Insert
    fun insertAll(vararg coordinates: Coordinates)
}