package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RiceCellDao {
    @Query("SELECT * FROM rice_cells ORDER BY id ASC")
    fun getAllCells(): Flow<List<RiceCellEntity>>

    @Query("SELECT COUNT(*) FROM rice_cells")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cells: List<RiceCellEntity>)

    @Update
    suspend fun updateCell(cell: RiceCellEntity)
}
