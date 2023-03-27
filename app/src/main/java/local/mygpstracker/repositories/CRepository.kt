package local.mygpstracker.repositories

import androidx.annotation.WorkerThread
import local.mygpstracker.dao.CoordinatesDao
import local.mygpstracker.model.Coordinates

class CRepository (
    private val daoObjects : CoordinatesDao
)
{
    /****************************************************************************************************
     * Получение списка всех элементов.                                                                 *
     ***************************************************************************************************/
    fun getAll()                            = daoObjects.getAll()

    /****************************************************************************************************
     * Сохранение нового элемента в БД.                                                                 *
     * @param item - объект для сохранения.                                                             *
     ***************************************************************************************************/
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(
        item                                : Coordinates
    ) {
        daoObjects.insertAll(item)
    }
    /****************************************************************************************************
     * Удаление существующего объекта.                                                                  *
     * @param item - объект для удаления.                                                               *
     ***************************************************************************************************/
//    @WorkerThread
//    suspend fun delete(
//        item                            : Coordinates
//    ) {
//        daoObjects.delete(item)
//    }
}