package com.example.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.KeyboardHide
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.RiceCellEntity
import com.example.data.RiceEntry
import com.example.ui.theme.GreenCellBg
import com.example.ui.theme.GreenCellDark
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RiceAmberAccent
import com.example.ui.theme.RiceGreenDark
import com.example.ui.theme.RiceGreenLight
import com.example.ui.theme.RiceGreenPrimary
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.WhiteCellBg
import com.example.viewmodel.AVAILABLE_RICE_OWNERS
import com.example.viewmodel.AVAILABLE_RICE_VARIETIES
import com.example.viewmodel.RiceFieldViewModel

@Composable
fun RiceFieldScreen(
    viewModel: RiceFieldViewModel,
    modifier: Modifier = Modifier
) {
    val cells by viewModel.cells.collectAsStateWithLifecycle()
    val selectedCell by viewModel.selectedCell.collectAsStateWithLifecycle()

    val totalCells = cells.size
    val filledCellsCount = cells.count { it.hasRice }
    val emptyCellsCount = totalCells - filledCellsCount
    val totalQuantity = cells.sumOf { it.totalQuantity }

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF6F8F6)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = statusBarPadding, bottom = navBarPadding)
        ) {
            // Header Bar
            HeaderSection(
                totalCount = totalCells,
                filledCount = filledCellsCount,
                emptyCount = emptyCellsCount,
                totalQuantity = totalQuantity
            )

            // 19 Cells Grid
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(top = 6.dp, bottom = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("cells_grid")
                ) {
                    items(
                        items = cells,
                        key = { it.id },
                        span = { cell ->
                            // Cell 19 ("Nền") spans 2 columns so row 5 [17, 18, Nền] is balanced
                            if (cell.id == 19 || cell.label == "Nền") {
                                GridItemSpan(2)
                            } else {
                                GridItemSpan(1)
                            }
                        }
                    ) { cell ->
                        RiceCellCard(
                            cell = cell,
                            onClick = { viewModel.selectCell(cell) },
                            modifier = Modifier.testTag("cell_${cell.label}")
                        )
                    }
                }
            }

            // Legend Footer
            LegendFooter(filledCount = filledCellsCount, emptyCount = emptyCellsCount)
        }
    }

    // Edit Dialog when a cell is clicked
    selectedCell?.let { cell ->
        RiceCellEditDialog(
            cell = cell,
            allCells = cells,
            onDismiss = { viewModel.dismissDialog() },
            onSave = { entries ->
                viewModel.saveCellEntries(
                    id = cell.id,
                    label = cell.label,
                    entries = entries
                )
            },
            onClear = {
                viewModel.clearCell(id = cell.id, label = cell.label)
            },
            onTransferEntry = { fromCellId, toCellId, entry ->
                viewModel.transferEntry(fromCellId, toCellId, entry)
            }
        )
    }
}

@Composable
private fun HeaderSection(
    totalCount: Int,
    filledCount: Int,
    emptyCount: Int,
    totalQuantity: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(RiceGreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Agriculture,
                            contentDescription = null,
                            tint = RiceGreenPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Bản Đồ 19 Ô Lúa",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "18 ô số & 1 ô Nền • Thêm từng dòng lúa riêng",
                            fontSize = 11.sp,
                            color = SlateTextSecondary
                        )
                    }
                }

                // Summary Pill
                Surface(
                    color = RiceGreenPrimary,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Grass,
                            contentDescription = null,
                            tint = PureWhite,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$filledCount/$totalCount ô",
                            color = PureWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (totalQuantity > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Tag,
                        contentDescription = null,
                        tint = RiceAmberAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tổng số lượng tất cả ô: $totalQuantity",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SlateTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun RiceCellCard(
    cell: RiceCellEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFilled = cell.hasRice
    val isGround = cell.label == "Nền"

    val animatedBgColor by animateColorAsState(
        targetValue = if (isFilled) GreenCellBg else WhiteCellBg,
        label = "cell_bg_anim"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(if (isGround) 116.dp else 124.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = animatedBgColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isFilled) 4.dp else 1.5.dp
        ),
        border = BorderStroke(
            width = if (isFilled) 1.5.dp else 1.dp,
            color = if (isFilled) GreenCellDark else SlateBorder
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            // Label tag (top row)
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (isFilled) Color.White.copy(alpha = 0.22f) else Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (isGround) "Nền" else "Ô ${cell.label}",
                        fontSize = if (isGround) 13.sp else 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFilled) PureWhite else SlateTextPrimary,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

                if (isFilled) {
                    Surface(
                        color = Color.White.copy(alpha = 0.22f),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "${cell.entries.size}",
                            color = PureWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            // Cell Center / Content
            if (isFilled) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (cell.entries.size == 1) {
                        val single = cell.entries.first()
                        Text(
                            text = single.variety,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PureWhite,
                            textAlign = TextAlign.Center
                        )
                        if (!single.owner.isNullOrBlank()) {
                            Surface(
                                color = Color(0xFFFEF3C7),
                                shape = RoundedCornerShape(3.dp),
                                border = BorderStroke(1.dp, RiceGreenPrimary),
                                modifier = Modifier.padding(top = 1.dp)
                            ) {
                                Text(
                                    text = single.owner,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.5.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tag,
                                contentDescription = "Số lượng",
                                tint = Color(0xFFFFE082),
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${single.quantity}",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFE082)
                            )
                        }
                        // Date/time added
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(top = 1.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Ngày giờ thêm",
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(9.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = single.formattedDateTime,
                                fontSize = 8.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Multiple lines: display stacked cleanly so each entry is visible
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            cell.entries.take(2).forEach { entry ->
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f, fill = false)
                                        ) {
                                            Text(
                                                text = entry.variety,
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Black,
                                                color = PureWhite
                                            )
                                            if (!entry.owner.isNullOrBlank()) {
                                                Text(
                                                    text = "•${entry.owner}",
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFFFFE082),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                        Text(
                                            text = ":${entry.quantity}",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = PureWhite
                                        )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(1.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Schedule,
                                                contentDescription = null,
                                                tint = Color.White.copy(alpha = 0.75f),
                                                modifier = Modifier.size(8.dp)
                                            )
                                            Text(
                                                text = entry.formattedDateTime,
                                                fontSize = 7.5.sp,
                                                color = Color.White.copy(alpha = 0.85f),
                                                maxLines = 1
                                            )
                                        }
                                        if (entry.weightKg != null && entry.weightKg > 0) {
                                            Text(
                                                text = "${entry.weightKg}kg",
                                                fontSize = 7.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFBAE6FD),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                    if (entry.totalPrice != null && entry.totalPrice!! > 0) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                text = "${java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN")).format(entry.totalPrice)}đ",
                                                fontSize = 7.5.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color(0xFFFDE047),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }

                            if (cell.entries.size > 2) {
                                Text(
                                    text = "+${cell.entries.size - 2} dòng",
                                    fontSize = 8.sp,
                                    color = Color(0xFFE2E8F0),
                                    textAlign = TextAlign.Center
                                )
                            }

                            // Total count pill
                            Surface(
                                color = Color.Black.copy(alpha = 0.22f),
                                shape = RoundedCornerShape(3.dp),
                                modifier = Modifier.padding(top = 1.dp)
                            ) {
                                Text(
                                    text = "Σ ${cell.totalQuantity}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFFFE082),
                                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 0.5.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                // Empty state (White background)
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Thêm lúa",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Trống",
                        fontSize = 11.sp,
                        color = SlateTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RiceCellEditDialog(
    cell: RiceCellEntity,
    allCells: List<RiceCellEntity> = emptyList(),
    onDismiss: () -> Unit,
    onSave: (entries: List<RiceEntry>) -> Unit,
    onClear: () -> Unit,
    onTransferEntry: ((fromCellId: Int, toCellId: Int, entry: RiceEntry) -> Unit)? = null
) {
    // Current working list of distinct rice entries/lines for this cell
    var currentEntries by remember(cell) {
        mutableStateOf(cell.entries)
    }

    var showEntryFormDialog by remember { mutableStateOf(false) }
    var entryToEdit by remember { mutableStateOf<RiceEntry?>(null) }
    var transferTargetEntry by remember { mutableStateOf<RiceEntry?>(null) }

    val scrollState = rememberScrollState()

    val safeDismiss = {
        onDismiss()
    }

    AlertDialog(
        onDismissRequest = safeDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        containerColor = PureWhite,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(vertical = 12.dp)
            .imePadding()
            .testTag("rice_edit_dialog"),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (currentEntries.isNotEmpty()) RiceGreenPrimary else Color(0xFFE2E8F0)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cell.label,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (currentEntries.isNotEmpty()) PureWhite else SlateTextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (cell.label == "Nền") "Danh Sách Lúa Ô Nền" else "Danh Sách Lúa Ô ${cell.label}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = SlateTextPrimary
                        )
                        Text(
                            text = if (currentEntries.isNotEmpty()) {
                                "Đang có ${currentEntries.size} dòng lúa • Tổng SL: ${currentEntries.sumOf { it.quantity }}"
                            } else {
                                "Chưa có lúa (Nền Trắng)"
                            },
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (currentEntries.isNotEmpty()) RiceGreenPrimary else SlateTextSecondary
                        )
                    }
                }
                IconButton(
                    onClick = safeDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Đóng",
                        tint = SlateTextSecondary
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Danh sách lúa đã chọn (${currentEntries.size}):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = SlateTextPrimary
                    )
                    if (currentEntries.isNotEmpty()) {
                        val totalKg = currentEntries.sumOf { it.weightKg?.toLong() ?: 0L }
                        val totalMoney = currentEntries.sumOf { it.totalPrice ?: 0L }

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                color = RiceGreenLight,
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, Color(0xFFA5D6A7))
                            ) {
                                Text(
                                    text = "SL: ${currentEntries.sumOf { it.quantity }}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    color = RiceGreenDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            if (totalKg > 0) {
                                Surface(
                                    color = Color(0xFFEFF6FF),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, Color(0xFF93C5FD))
                                ) {
                                    Text(
                                        text = "${java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN")).format(totalKg)}kg",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 11.sp,
                                        color = Color(0xFF1D4ED8),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            if (totalMoney > 0) {
                                Surface(
                                    color = Color(0xFFFEF3C7),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, Color(0xFFF59E0B))
                                ) {
                                    Text(
                                        text = "${java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN")).format(totalMoney)}đ",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 11.sp,
                                        color = Color(0xFFB45309),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (currentEntries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(BorderStroke(1.dp, SlateBorder), RoundedCornerShape(10.dp))
                            .padding(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Grass,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "Ô này chưa có dòng lúa nào.",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SlateTextPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Nhấn nút '+ Thêm dòng lúa mới' bên dưới để chọn Loại lúa, Chủ lúa, Số lượng, Số ký và Giá lúa.",
                                fontSize = 11.5.sp,
                                color = SlateTextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            color = Color(0xFFFEF9C3),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, Color(0xFFFDE047)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = Color(0xFF854D0E),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Chạm vào thẻ lúa bất kỳ để thay đổi lựa chọn trước đó",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF854D0E)
                                )
                            }
                        }

                        // NHÓM THEO LOẠI LÚA (GROUP BY THEO LOẠI LÚA)
                        val groupedEntries = currentEntries.groupBy { it.variety }

                        groupedEntries.forEach { (variety, entriesInGroup) ->
                            val groupTotalQty = entriesInGroup.sumOf { it.quantity }
                            val groupTotalKg = entriesInGroup.sumOf { it.weightKg?.toLong() ?: 0L }
                            val groupTotalMoney = entriesInGroup.sumOf { it.totalPrice ?: 0L }

                            // HEADER NHÓM LOẠI LÚA
                            Surface(
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Surface(
                                            color = RiceGreenPrimary,
                                            shape = RoundedCornerShape(4.dp),
                                            border = BorderStroke(1.dp, RiceGreenDark)
                                        ) {
                                            Text(
                                                text = variety,
                                                color = PureWhite,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text(
                                            text = "(${entriesInGroup.size} dòng)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = SlateTextSecondary
                                        )
                                    }

                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "SL: $groupTotalQty",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SlateTextPrimary
                                        )
                                        if (groupTotalKg > 0) {
                                            Text(
                                                text = "• ${java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN")).format(groupTotalKg)}kg",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1D4ED8)
                                            )
                                        }
                                        if (groupTotalMoney > 0) {
                                            Text(
                                                text = "• ${java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN")).format(groupTotalMoney)}đ",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color(0xFFB45309)
                                            )
                                        }
                                    }
                                }
                            }

                            // CÁC THẺ LÚA TRONG NHÓM (HIỂN THỊ 2 DÒNG)
                            entriesInGroup.forEach { entry ->
                                val globalIndex = currentEntries.indexOf(entry)
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            entryToEdit = entry
                                            showEntryFormDialog = true
                                        },
                                    shape = RoundedCornerShape(8.dp),
                                    color = RiceGreenLight,
                                    border = BorderStroke(1.dp, Color(0xFFA5D6A7))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 7.dp),
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        // DÒNG 1: LOẠI LÚA, CHỦ LÚA, SỐ LƯỢNG, THỜI GIAN NHẬP
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            FlowRow(
                                                modifier = Modifier.weight(1f),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                                            ) {
                                                // Số thứ tự
                                                Surface(
                                                    color = Color.White.copy(alpha = 0.95f),
                                                    shape = CircleShape
                                                ) {
                                                    Text(
                                                        text = "#${globalIndex + 1}",
                                                        color = SlateTextSecondary,
                                                        fontSize = 9.5.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                    )
                                                }

                                                // 1. LOẠI LÚA
                                                Surface(
                                                    color = RiceGreenPrimary,
                                                    shape = RoundedCornerShape(4.dp),
                                                    border = BorderStroke(1.dp, RiceGreenDark)
                                                ) {
                                                    Text(
                                                        text = entry.variety,
                                                        color = PureWhite,
                                                        fontSize = 11.5.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp)
                                                    )
                                                }

                                                // 2. CHỦ LÚA
                                                if (!entry.owner.isNullOrBlank()) {
                                                    Surface(
                                                        color = Color(0xFFFEF3C7),
                                                        border = BorderStroke(1.dp, RiceGreenPrimary),
                                                        shape = RoundedCornerShape(4.dp)
                                                    ) {
                                                        Text(
                                                            text = entry.owner,
                                                            color = Color(0xFF92400E),
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp),
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                    }
                                                }

                                                // 3. SỐ LƯỢNG
                                                Surface(
                                                    color = Color.White.copy(alpha = 0.85f),
                                                    shape = RoundedCornerShape(4.dp),
                                                    border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                                                ) {
                                                    Text(
                                                        text = "SL: ${entry.quantity}",
                                                        fontSize = 11.5.sp,
                                                        fontWeight = FontWeight.Black,
                                                        color = Color.Black,
                                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                                    )
                                                }

                                                // 4. THỜI GIAN NHẬP
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                                    modifier = Modifier.align(Alignment.CenterVertically)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Schedule,
                                                        contentDescription = "Thời gian",
                                                        tint = Color(0xFF64748B),
                                                        modifier = Modifier.size(11.dp)
                                                    )
                                                    Text(
                                                        text = entry.formattedDateTime,
                                                        fontSize = 10.5.sp,
                                                        color = Color(0xFF475569),
                                                        fontWeight = FontWeight.Medium,
                                                        maxLines = 1
                                                    )
                                                }
                                            }

                                            // Action buttons: Sửa & Xóa
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                IconButton(
                                                    onClick = {
                                                        entryToEdit = entry
                                                        showEntryFormDialog = true
                                                    },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = "Sửa dòng lúa",
                                                        tint = RiceGreenDark,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                                IconButton(
                                                    onClick = {
                                                        currentEntries = currentEntries.filter { it.id != entry.id }
                                                    },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Xóa dòng",
                                                        tint = Color(0xFFEF4444),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        }

                                        // DÒNG 2: SỐ KÝ, GIÁ TIỀN, THÀNH TIỀN
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            FlowRow(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                // SỐ KÝ
                                                if (entry.weightKg != null && entry.weightKg > 0) {
                                                    Surface(
                                                        color = Color(0xFFEFF6FF),
                                                        shape = RoundedCornerShape(4.dp),
                                                        border = BorderStroke(1.dp, Color(0xFF93C5FD))
                                                    ) {
                                                        Text(
                                                            text = "Số ký: ${java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN")).format(entry.weightKg)} kg",
                                                            color = Color(0xFF1D4ED8),
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                }

                                                // GIÁ TIỀN
                                                if (entry.price != null && entry.price > 0) {
                                                    Surface(
                                                        color = Color(0xFFDCFCE7),
                                                        shape = RoundedCornerShape(4.dp),
                                                        border = BorderStroke(1.dp, Color(0xFF86EFAC))
                                                    ) {
                                                        Text(
                                                            text = "Giá: ${java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN")).format(entry.price)} đ/kg",
                                                            color = Color(0xFF166534),
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                }

                                                // THÀNH TIỀN
                                                if (entry.totalPrice != null && entry.totalPrice!! > 0) {
                                                    Surface(
                                                        color = Color(0xFFFEF3C7),
                                                        shape = RoundedCornerShape(4.dp),
                                                        border = BorderStroke(1.dp, Color(0xFFF59E0B))
                                                    ) {
                                                        Text(
                                                            text = "Thành tiền: ${java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN")).format(entry.totalPrice)} đ",
                                                            color = Color(0xFFB45309),
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        // At "Nền": allow transferring entry to other cells
                                        if (cell.label == "Nền" && onTransferEntry != null) {
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Button(
                                                onClick = {
                                                    transferTargetEntry = entry
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                                shape = RoundedCornerShape(6.dp),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                modifier = Modifier.height(26.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                                    contentDescription = null,
                                                    tint = PureWhite,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Chuyển sang ô khác (1 - 18)",
                                                    fontSize = 10.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = PureWhite
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // NÚT MỞ POPUP THÊM DÒNG LÚA MỚI
                Button(
                    onClick = {
                        entryToEdit = null
                        showEntryFormDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RiceGreenPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("add_variety_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+ Thêm dòng lúa mới",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(currentEntries)
                },
                colors = ButtonDefaults.buttonColors(containerColor = RiceGreenPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("save_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Lưu vào ô",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (currentEntries.isNotEmpty()) {
                    TextButton(
                        onClick = {
                            currentEntries = emptyList()
                            onClear()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFDC2626)),
                        modifier = Modifier.testTag("clear_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Xóa hết")
                    }
                }
                TextButton(
                    onClick = safeDismiss,
                    modifier = Modifier.testTag("cancel_button")
                ) {
                    Text("Đóng")
                }
            }
        }
    )

    // POPUP RIÊNG: THÊM HOẶC CHỈNH SỬA DÒNG LÚA
    if (showEntryFormDialog) {
        RiceEntryFormDialog(
            entryToEdit = entryToEdit,
            onDismiss = {
                showEntryFormDialog = false
                entryToEdit = null
            },
            onSave = { variety, owner, quantity, weightKg, price ->
                if (entryToEdit == null) {
                    val newLine = RiceEntry(
                        variety = variety,
                        quantity = quantity,
                        owner = owner,
                        weightKg = weightKg,
                        price = price
                    )
                    currentEntries = currentEntries + newLine
                } else {
                    val targetId = entryToEdit!!.id
                    val updated = entryToEdit!!.copy(
                        variety = variety,
                        quantity = quantity,
                        owner = owner,
                        weightKg = weightKg,
                        price = price
                    )
                    currentEntries = currentEntries.map { if (it.id == targetId) updated else it }
                }
                showEntryFormDialog = false
                entryToEdit = null
            }
        )
    }

    if (transferTargetEntry != null) {
        val entryToTransfer = transferTargetEntry!!
        TransferEntryDialog(
            entry = entryToTransfer,
            targetCells = allCells.filter { it.label != "Nền" },
            onDismiss = { transferTargetEntry = null },
            onSelectTarget = { targetCell ->
                currentEntries = currentEntries.filter { it.id != entryToTransfer.id }
                onTransferEntry?.invoke(cell.id, targetCell.id, entryToTransfer)
                transferTargetEntry = null
            }
        )
    }
}

/**
 * POPUP RIÊNG: THÊM MỚI HOẶC CHỈNH SỬA DÒNG LÚA
 * - Loại lúa (ST, LL, NH, T8, 49, 54, HC)
 * - Chủ lúa (BẮT BUỘC)
 * - Số lượng (kiểu int) có viền xanh lá và bàn phím tự ẩn khi chạm ngoài
 * - Số ký (kiểu int, kg)
 * - Giá lúa (kiểu int, VNĐ)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RiceEntryFormDialog(
    entryToEdit: RiceEntry? = null,
    onDismiss: () -> Unit,
    onSave: (variety: String, owner: String, quantity: Int, weightKg: Int?, price: Int?) -> Unit
) {
    var selectedVariety by remember {
        mutableStateOf(entryToEdit?.variety ?: AVAILABLE_RICE_VARIETIES.first())
    }
    // Chủ lúa BẮT BUỘC: mặc định lấy từ thẻ đang sửa hoặc chủ đầu tiên trong danh sách
    var selectedOwner by remember {
        mutableStateOf(entryToEdit?.owner ?: AVAILABLE_RICE_OWNERS.first())
    }
    var quantityText by remember {
        mutableStateOf((entryToEdit?.quantity ?: 10).toString())
    }
    var weightKgText by remember {
        mutableStateOf(entryToEdit?.weightKg?.toString() ?: "")
    }
    var priceText by remember {
        mutableStateOf(entryToEdit?.price?.toString() ?: "")
    }
    var ownerError by remember { mutableStateOf(false) }
    var isInputError by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState.isScrollInProgress) {
        if (scrollState.isScrollInProgress) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }

    val safeDismiss = {
        keyboardController?.hide()
        focusManager.clearFocus()
        onDismiss()
    }

    AlertDialog(
        onDismissRequest = safeDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        containerColor = PureWhite,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(vertical = 12.dp)
            .imePadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
            .testTag("rice_entry_form_dialog"),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        })
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(RiceGreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (entryToEdit == null) Icons.Default.Add else Icons.Default.Edit,
                            contentDescription = null,
                            tint = RiceGreenPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (entryToEdit == null) "Thêm Dòng Lúa Mới" else "Chỉnh Sửa Dòng Lúa",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = SlateTextPrimary
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardHide,
                            contentDescription = "Ẩn bàn phím",
                            tint = RiceGreenDark
                        )
                    }
                    IconButton(
                        onClick = safeDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = SlateTextSecondary
                        )
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        })
                    }
                    .padding(vertical = 4.dp)
            ) {
                // SECTION 1: KHUNG CHỌN LOẠI LÚA VÀ CHỦ LÚA (BẮT BUỘC)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF9FCF9),
                    border = BorderStroke(1.5.dp, RiceGreenPrimary)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        // 1. LOẠI LÚA
                        Text(
                            text = "Loại lúa:",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RiceGreenDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            AVAILABLE_RICE_VARIETIES.forEach { variety ->
                                val isSelected = selectedVariety == variety
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                        selectedVariety = variety
                                    },
                                    label = {
                                        Text(
                                            text = variety,
                                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                            fontSize = 11.5.sp,
                                            color = if (isSelected) PureWhite else SlateTextPrimary
                                        )
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) RiceGreenDark else Color(0xFF86EFAC)
                                    ),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = RiceGreenPrimary,
                                        containerColor = PureWhite
                                    ),
                                    modifier = Modifier.height(28.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = Color(0xFFDCFCE7), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        // 2. CHỦ LÚA (BẮT BUỘC)
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Chủ lúa (Bắt buộc):",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (ownerError) MaterialTheme.colorScheme.error else RiceGreenDark
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "*",
                                color = Color(0xFFDC2626),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        if (ownerError) {
                            Text(
                                text = "⚠️ Vui lòng chọn Chủ lúa (bắt buộc)!",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            AVAILABLE_RICE_OWNERS.forEach { owner ->
                                val isSelected = selectedOwner == owner
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                        selectedOwner = owner
                                        ownerError = false
                                    },
                                    label = {
                                        Text(
                                            text = owner,
                                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                            fontSize = 11.sp,
                                            color = if (isSelected) Color(0xFF78350F) else SlateTextPrimary
                                        )
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) RiceGreenPrimary else SlateBorder
                                    ),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFFFDE68A),
                                        containerColor = PureWhite
                                    ),
                                    modifier = Modifier.height(28.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // SECTION 2: NHẬP SỐ LƯỢNG (Bọc viền xanh lá)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF9FCF9),
                    border = BorderStroke(1.5.dp, RiceGreenPrimary)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(RiceGreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Numbers,
                                    contentDescription = "Biểu tượng int",
                                    tint = RiceGreenPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Số lượng cho dòng này (kiểu int):",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp,
                                color = RiceGreenDark
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = quantityText,
                            onValueChange = { input ->
                                val filtered = input.filter { it.isDigit() }
                                quantityText = filtered
                                isInputError = filtered.isEmpty()
                            },
                            isError = isInputError,
                            supportingText = {
                                if (isInputError) {
                                    Text("Vui lòng nhập số nguyên hợp lệ (int > 0)")
                                }
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Tag,
                                    contentDescription = "Biểu tượng int",
                                    tint = if (isInputError) MaterialTheme.colorScheme.error else RiceGreenPrimary
                                )
                            },
                            trailingIcon = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (quantityText.isNotEmpty()) {
                                        IconButton(
                                            onClick = { quantityText = "" },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Xóa số lượng",
                                                tint = SlateTextSecondary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Ẩn bàn phím",
                                            tint = RiceGreenPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            ),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("quantity_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                cursorColor = Color.Black,
                                focusedBorderColor = RiceGreenDark,
                                unfocusedBorderColor = RiceGreenPrimary,
                                focusedLabelColor = RiceGreenPrimary,
                                focusedContainerColor = PureWhite,
                                unfocusedContainerColor = PureWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Nút chỉnh số nhanh: -10, -1, +1, +10
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf(-10, -1, 1, 10).forEach { delta ->
                                OutlinedButton(
                                    onClick = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                        val current = quantityText.toIntOrNull() ?: 0
                                        val newVal = (current + delta).coerceAtLeast(1)
                                        quantityText = newVal.toString()
                                        isInputError = false
                                    },
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(30.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, RiceGreenPrimary.copy(alpha = 0.6f)),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = PureWhite
                                    )
                                ) {
                                    Text(
                                        text = if (delta > 0) "+$delta" else "$delta",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // SECTION 3: NHẬP SỐ KÝ (Bọc viền xanh dương)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.5.dp, Color(0xFF3B82F6))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFDBEAFE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Scale,
                                        contentDescription = "Biểu tượng số ký",
                                        tint = Color(0xFF1D4ED8),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Số ký (kg):",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.5.sp,
                                    color = Color(0xFF1E40AF)
                                )
                            }
                            if (weightKgText.isNotBlank()) {
                                val wVal = weightKgText.toIntOrNull()
                                if (wVal != null && wVal > 0) {
                                    Surface(
                                        color = Color(0xFFDBEAFE),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "${java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN")).format(wVal)} kg",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 11.5.sp,
                                            color = Color(0xFF1E40AF),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = weightKgText,
                            onValueChange = { input ->
                                weightKgText = input.filter { it.isDigit() }
                            },
                            placeholder = {
                                Text("Nhập số ký (ví dụ: 1200)", fontSize = 13.sp, color = SlateTextSecondary)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Scale,
                                    contentDescription = "Số ký lúa",
                                    tint = Color(0xFF2563EB)
                                )
                            },
                            trailingIcon = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (weightKgText.isNotEmpty()) {
                                        IconButton(
                                            onClick = { weightKgText = "" },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Xóa số ký",
                                                tint = SlateTextSecondary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Ẩn bàn phím",
                                            tint = Color(0xFF2563EB),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            ),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("weight_kg_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                cursorColor = Color.Black,
                                focusedBorderColor = Color(0xFF1D4ED8),
                                unfocusedBorderColor = Color(0xFF3B82F6),
                                focusedLabelColor = Color(0xFF1D4ED8),
                                focusedContainerColor = PureWhite,
                                unfocusedContainerColor = PureWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Nút chỉnh số ký nhanh: +50, +100, +500, +1000
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf(50, 100, 500, 1000).forEach { delta ->
                                OutlinedButton(
                                    onClick = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                        val current = weightKgText.toIntOrNull() ?: 0
                                        val newVal = (current + delta).coerceAtLeast(0)
                                        weightKgText = newVal.toString()
                                    },
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(30.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, Color(0xFF3B82F6).copy(alpha = 0.5f)),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = PureWhite
                                    )
                                ) {
                                    Text(
                                        text = "+$delta",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1D4ED8)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // SECTION 4: NHẬP GIÁ LÚA (Bọc viền xanh lục/ngọc)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF9FCF9),
                    border = BorderStroke(1.5.dp, Color(0xFF059669))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFD1FAE5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Payments,
                                        contentDescription = "Biểu tượng giá lúa",
                                        tint = Color(0xFF047857),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Giá lúa (VNĐ):",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.5.sp,
                                    color = Color(0xFF065F46)
                                )
                            }
                            if (priceText.isNotBlank()) {
                                val pVal = priceText.toIntOrNull()
                                if (pVal != null && pVal > 0) {
                                    Surface(
                                        color = Color(0xFFD1FAE5),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "${java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN")).format(pVal)} đ",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 11.5.sp,
                                            color = Color(0xFF065F46),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { input ->
                                priceText = input.filter { it.isDigit() }
                            },
                            placeholder = {
                                Text("Nhập giá lúa (ví dụ: 8200)", fontSize = 13.sp, color = SlateTextSecondary)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = "Giá lúa",
                                    tint = Color(0xFF059669)
                                )
                            },
                            trailingIcon = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (priceText.isNotEmpty()) {
                                        IconButton(
                                            onClick = { priceText = "" },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Xóa giá",
                                                tint = SlateTextSecondary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Ẩn bàn phím",
                                            tint = Color(0xFF059669),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            ),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("price_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                cursorColor = Color.Black,
                                focusedBorderColor = Color(0xFF047857),
                                unfocusedBorderColor = Color(0xFF059669),
                                focusedLabelColor = Color(0xFF047857),
                                focusedContainerColor = PureWhite,
                                unfocusedContainerColor = PureWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Nút chọn giá nhanh phổ biến: 7.500, 8.000, 8.500, 9.000
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf(7500, 8000, 8500, 9000).forEach { presetPrice ->
                                OutlinedButton(
                                    onClick = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                        priceText = presetPrice.toString()
                                    },
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(30.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        if (priceText == presetPrice.toString()) Color(0xFF047857) else Color(0xFF059669).copy(alpha = 0.5f)
                                    ),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (priceText == presetPrice.toString()) Color(0xFFD1FAE5) else PureWhite
                                    )
                                ) {
                                    Text(
                                        text = "${presetPrice / 1000}k${if (presetPrice % 1000 != 0) (presetPrice % 1000) / 100 else ""}",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF065F46)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    if (selectedOwner.isBlank()) {
                        ownerError = true
                        return@Button
                    }
                    val qty = quantityText.toIntOrNull()
                    if (qty == null || qty <= 0) {
                        isInputError = true
                        return@Button
                    }
                    val weightKg = weightKgText.toIntOrNull()
                    val price = priceText.toIntOrNull()
                    onSave(selectedVariety, selectedOwner, qty, weightKg, price)
                },
                colors = ButtonDefaults.buttonColors(containerColor = RiceGreenPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("save_entry_button")
            ) {
                Icon(
                    imageVector = if (entryToEdit == null) Icons.Default.Add else Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (entryToEdit == null) "Thêm dòng lúa" else "Cập nhật thay đổi",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = safeDismiss
            ) {
                Text("Hủy")
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransferEntryDialog(
    entry: RiceEntry,
    targetCells: List<RiceCellEntity>,
    onDismiss: () -> Unit,
    onSelectTarget: (RiceCellEntity) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        containerColor = PureWhite,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(vertical = 12.dp)
            .imePadding()
            .testTag("transfer_dialog"),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Chuyển Thẻ Lúa Từ Nền",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = SlateTextPrimary
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Đóng",
                        tint = SlateTextSecondary
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Info of entry being transferred
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, SlateBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Thẻ lúa cần chuyển:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SlateTextSecondary
                        )
                        // DÒNG 1: LOẠI LÚA, CHỦ LÚA, SỐ LƯỢNG, THỜI GIAN
                        FlowRow(
                            verticalArrangement = Arrangement.Center,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                color = RiceGreenPrimary,
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.5.dp, RiceGreenDark)
                            ) {
                                Text(
                                    text = entry.variety,
                                    color = PureWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            if (!entry.owner.isNullOrBlank()) {
                                Surface(
                                    color = Color(0xFFFEF3C7),
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(1.5.dp, RiceGreenPrimary)
                                ) {
                                    Text(
                                        text = entry.owner,
                                        color = Color(0xFF92400E),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                            ) {
                                Text(
                                    text = "SL: ${entry.quantity}",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = entry.formattedDateTime,
                                    fontSize = 10.5.sp,
                                    color = SlateTextSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // DÒNG 2: SỐ KÝ, GIÁ TIỀN, THÀNH TIỀN
                        FlowRow(
                            verticalArrangement = Arrangement.Center,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (entry.weightKg != null && entry.weightKg > 0) {
                                Surface(
                                    color = Color(0xFFEFF6FF),
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(1.dp, Color(0xFF93C5FD))
                                ) {
                                    Text(
                                        text = "Số ký: ${java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN")).format(entry.weightKg)} kg",
                                        color = Color(0xFF1D4ED8),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            if (entry.price != null && entry.price > 0) {
                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(1.dp, Color(0xFF86EFAC))
                                ) {
                                    Text(
                                        text = "Giá: ${java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN")).format(entry.price)} đ/kg",
                                        color = Color(0xFF166534),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            if (entry.totalPrice != null && entry.totalPrice!! > 0) {
                                Surface(
                                    color = Color(0xFFFEF3C7),
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(1.dp, Color(0xFFF59E0B))
                                ) {
                                    Text(
                                        text = "Thành tiền: ${java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN")).format(entry.totalPrice)} đ",
                                        color = Color(0xFFB45309),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Chọn ô muốn chuyển đến (từ Ô 1 đến Ô 18):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Grid 3 columns of target cells (1 to 18)
                val sortedCells = targetCells.sortedBy { it.id }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    sortedCells.chunked(3).forEach { rowCells ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            rowCells.forEach { target ->
                                val isTargetFilled = target.hasRice
                                OutlinedButton(
                                    onClick = { onSelectTarget(target) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isTargetFilled) RiceGreenPrimary else SlateBorder
                                    ),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isTargetFilled) RiceGreenLight.copy(alpha = 0.55f) else PureWhite
                                    ),
                                    contentPadding = PaddingValues(2.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Ô ${target.label}",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            color = if (isTargetFilled) RiceGreenDark else SlateTextPrimary
                                        )
                                        Text(
                                            text = if (isTargetFilled) "${target.entries.size} dòng (Σ${target.totalQuantity})" else "Trống",
                                            fontSize = 9.sp,
                                            fontWeight = if (isTargetFilled) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isTargetFilled) RiceGreenDark else SlateTextSecondary
                                        )
                                    }
                                }
                            }
                            repeat(3 - rowCells.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = SlateTextSecondary)
            }
        }
    )
}

@Composable
private fun LegendFooter(
    filledCount: Int,
    emptyCount: Int
) {
    Surface(
        color = PureWhite,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Unselected indicator (White)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(WhiteCellBg)
                        .border(BorderStroke(1.dp, SlateBorder), RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Chưa có lúa ($emptyCount ô)",
                    fontSize = 12.sp,
                    color = SlateTextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            // Selected indicator (Green)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(GreenCellBg)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Có lúa ($filledCount ô)",
                    fontSize = 12.sp,
                    color = RiceGreenDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
