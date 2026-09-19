package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Represents a single variety, quantity, and optional owner/person entry line within a cell.
 * Each entry has a unique id so multiple lines can exist independently.
 * Now includes timestamp (date & time added).
 */
data class RiceEntry(
    val variety: String,
    val quantity: Int,
    val owner: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val id: String = UUID.randomUUID().toString(),
    val price: Int? = null,
    val weightKg: Int? = null
) {
    /**
     * Thành tiền = Số ký * Giá tiền (Long để tránh tràn số)
     */
    val totalPrice: Long?
        get() = if (weightKg != null && price != null && weightKg > 0 && price > 0) {
            weightKg.toLong() * price.toLong()
        } else null

    val formattedDateTime: String
        get() = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date(timestamp))

    val formattedTimeOnly: String
        get() = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))

    val formattedDateOnly: String
        get() = SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date(timestamp))
}

/**
 * TypeConverter to persist a list of RiceEntry as a string in Room.
 * Supports multiple lines of rice with unique IDs, optional owner, timestamp, price, and weight.
 * Format: "id|variety|quantity|owner|timestamp|price|weightKg;..."
 */
class RiceConverters {
    @TypeConverter
    fun fromEntries(entries: List<RiceEntry>?): String {
        if (entries.isNullOrEmpty()) return ""
        return entries.joinToString(";") {
            "${it.id}|${it.variety}|${it.quantity}|${it.owner ?: ""}|${it.timestamp}|${it.price ?: ""}|${it.weightKg ?: ""}"
        }
    }

    @TypeConverter
    fun toEntries(data: String?): List<RiceEntry> {
        if (data.isNullOrBlank()) return emptyList()
        return data.split(";").mapNotNull { part ->
            val pieces = part.split("|")
            when {
                pieces.size >= 7 -> {
                    val id = pieces[0].trim()
                    val variety = pieces[1].trim()
                    val qty = pieces[2].trim().toIntOrNull()
                    val owner = pieces[3].trim().ifEmpty { null }
                    val time = pieces[4].trim().toLongOrNull() ?: System.currentTimeMillis()
                    val price = pieces[5].trim().toIntOrNull()
                    val weightKg = pieces[6].trim().toIntOrNull()
                    if (variety.isNotEmpty() && qty != null) {
                        RiceEntry(
                            variety = variety,
                            quantity = qty,
                            owner = owner,
                            timestamp = time,
                            id = id.ifEmpty { UUID.randomUUID().toString() },
                            price = price,
                            weightKg = weightKg
                        )
                    } else null
                }
                pieces.size == 6 -> {
                    val id = pieces[0].trim()
                    val variety = pieces[1].trim()
                    val qty = pieces[2].trim().toIntOrNull()
                    val owner = pieces[3].trim().ifEmpty { null }
                    val time = pieces[4].trim().toLongOrNull() ?: System.currentTimeMillis()
                    val price = pieces[5].trim().toIntOrNull()
                    if (variety.isNotEmpty() && qty != null) {
                        RiceEntry(
                            variety = variety,
                            quantity = qty,
                            owner = owner,
                            timestamp = time,
                            id = id.ifEmpty { UUID.randomUUID().toString() },
                            price = price,
                            weightKg = null
                        )
                    } else null
                }
                pieces.size == 5 -> {
                    val id = pieces[0].trim()
                    val variety = pieces[1].trim()
                    val qty = pieces[2].trim().toIntOrNull()
                    val owner = pieces[3].trim().ifEmpty { null }
                    val time = pieces[4].trim().toLongOrNull() ?: System.currentTimeMillis()
                    if (variety.isNotEmpty() && qty != null) {
                        RiceEntry(
                            variety = variety,
                            quantity = qty,
                            owner = owner,
                            timestamp = time,
                            id = id.ifEmpty { UUID.randomUUID().toString() }
                        )
                    } else null
                }
                pieces.size == 4 -> {
                    val id = pieces[0].trim()
                    val variety = pieces[1].trim()
                    val qty = pieces[2].trim().toIntOrNull()
                    val owner = pieces[3].trim().ifEmpty { null }
                    if (variety.isNotEmpty() && qty != null) {
                        RiceEntry(
                            variety = variety,
                            quantity = qty,
                            owner = owner,
                            timestamp = System.currentTimeMillis(),
                            id = id.ifEmpty { UUID.randomUUID().toString() }
                        )
                    } else null
                }
                pieces.size == 3 -> {
                    val id = pieces[0].trim()
                    val variety = pieces[1].trim()
                    val qty = pieces[2].trim().toIntOrNull()
                    if (variety.isNotEmpty() && qty != null) {
                        RiceEntry(
                            variety = variety,
                            quantity = qty,
                            owner = null,
                            timestamp = System.currentTimeMillis(),
                            id = id.ifEmpty { UUID.randomUUID().toString() }
                        )
                    } else null
                }
                else -> {
                    // Backward compatibility with legacy format "variety:quantity"
                    val oldPieces = part.split(":")
                    if (oldPieces.size == 2) {
                        val variety = oldPieces[0].trim()
                        val qty = oldPieces[1].trim().toIntOrNull()
                        if (variety.isNotEmpty() && qty != null) {
                            RiceEntry(
                                variety = variety,
                                quantity = qty,
                                owner = null,
                                timestamp = System.currentTimeMillis(),
                                id = UUID.randomUUID().toString()
                            )
                        } else null
                    } else null
                }
            }
        }
    }
}

/**
 * Entity representing one of the 19 cells in the rice field management app.
 * Cells are numbered 1 to 18, and the 19th cell is labeled "Nền".
 * Each cell can contain multiple lines of rice varieties with integer quantities and owners.
 */
@Entity(tableName = "rice_cells")
@TypeConverters(RiceConverters::class)
data class RiceCellEntity(
    @PrimaryKey val id: Int,
    val label: String,
    val entries: List<RiceEntry> = emptyList()
) {
    val hasRice: Boolean
        get() = entries.isNotEmpty()

    val totalQuantity: Int
        get() = entries.sumOf { it.quantity }
}
