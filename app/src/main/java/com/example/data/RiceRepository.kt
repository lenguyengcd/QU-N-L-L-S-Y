package com.example.data

import kotlinx.coroutines.flow.Flow

class RiceRepository(private val dao: RiceCellDao) {
    val allCells: Flow<List<RiceCellEntity>> = dao.getAllCells()

    suspend fun initializeIfNeeded() {
        if (dao.getCount() == 0) {
            val initialCells = mutableListOf<RiceCellEntity>()
            for (i in 1..18) {
                initialCells.add(
                    RiceCellEntity(
                        id = i,
                        label = "$i",
                        entries = emptyList()
                    )
                )
            }
            // 19th cell labeled "Nền"
            initialCells.add(
                RiceCellEntity(
                    id = 19,
                    label = "Nền",
                    entries = emptyList()
                )
            )
            dao.insertAll(initialCells)
        }
    }

    suspend fun saveCell(id: Int, label: String, entries: List<RiceEntry>) {
        dao.updateCell(
            RiceCellEntity(
                id = id,
                label = label,
                entries = entries
            )
        )
    }

    suspend fun clearCell(id: Int, label: String) {
        dao.updateCell(
            RiceCellEntity(
                id = id,
                label = label,
                entries = emptyList()
            )
        )
    }
}
