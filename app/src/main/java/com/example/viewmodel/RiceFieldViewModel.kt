package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.RiceCellEntity
import com.example.data.RiceDatabase
import com.example.data.RiceEntry
import com.example.data.RiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

val AVAILABLE_RICE_VARIETIES = listOf("ST", "LL", "NH", "T8", "49", "54", "HC")

val AVAILABLE_RICE_OWNERS = listOf(
    "PHUONGTT",
    "HANHTT",
    "2XOAN",
    "7NO",
    "4DAN",
    "PHUONGDAN",
    "C.LAN",
    "6GONG",
    "D.THOI",
    "D.ĐUNG",
    "GAI7"
)

class RiceFieldViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RiceRepository

    val cells: StateFlow<List<RiceCellEntity>>

    private val _selectedCell = MutableStateFlow<RiceCellEntity?>(null)
    val selectedCell: StateFlow<RiceCellEntity?> = _selectedCell.asStateFlow()

    init {
        val db = RiceDatabase.getDatabase(application)
        repository = RiceRepository(db.riceCellDao())

        cells = repository.allCells.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.initializeIfNeeded()
        }
    }

    fun selectCell(cell: RiceCellEntity) {
        _selectedCell.value = cell
    }

    fun dismissDialog() {
        _selectedCell.value = null
    }

    fun saveCellEntries(id: Int, label: String, entries: List<RiceEntry>) {
        viewModelScope.launch {
            repository.saveCell(
                id = id,
                label = label,
                entries = entries
            )
            _selectedCell.value = null
        }
    }

    fun clearCell(id: Int, label: String) {
        viewModelScope.launch {
            repository.clearCell(id = id, label = label)
            _selectedCell.value = null
        }
    }

    fun transferEntry(fromCellId: Int, toCellId: Int, entry: RiceEntry) {
        viewModelScope.launch {
            val currentCells = cells.value
            val sourceCell = currentCells.find { it.id == fromCellId } ?: return@launch
            val targetCell = currentCells.find { it.id == toCellId } ?: return@launch

            // Remove entry from sourceCell
            val updatedSourceEntries = sourceCell.entries.filter { it.id != entry.id }
            repository.saveCell(sourceCell.id, sourceCell.label, updatedSourceEntries)

            // Add entry to targetCell
            val updatedTargetEntries = targetCell.entries + entry
            repository.saveCell(targetCell.id, targetCell.label, updatedTargetEntries)

            // Update _selectedCell if source is still open
            if (_selectedCell.value?.id == fromCellId) {
                _selectedCell.value = sourceCell.copy(entries = updatedSourceEntries)
            }
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RiceFieldViewModel::class.java)) {
                return RiceFieldViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
