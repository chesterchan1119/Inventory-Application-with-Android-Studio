package com.example.infoday

import android.app.Application
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.infoday.ui.theme.InfoDayTheme
import kotlinx.coroutines.launch

@Entity(tableName = "event")
data class Event(
    @PrimaryKey val id: Int, val title: String, val deptId: String, var saved: Boolean
) {
    companion object {
        val data = listOf(
            Event(id = 1, title = "Career Talks", deptId = "COMS", saved = false),
            Event(id = 2, title = "Guided Tour", deptId = "COMS", saved = true),
            Event(id = 3, title = "MindDrive Demo", deptId = "COMP", saved = false),
            Event(id = 4, title = "Project Demo", deptId = "COMP", saved = false)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(snackbarHostState: SnackbarHostState, deptId: String?) {
    val context = LocalContext.current
    val eventViewModel: EventViewModel = viewModel(
        factory = EventViewModelFactory(context.applicationContext as Application)
    )
    val coroutineScope = rememberCoroutineScope();
    val events by eventViewModel.readAllData.observeAsState(listOf());
    LazyColumn {
        items(Event.data.filter { it.deptId == deptId }) { event ->
            ListItem(
                headlineText = { Text(event.title) },
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            eventViewModel.bookmarkEvent(event)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    "Event has been added to itinerary."
                                )
                            }
                        }

                    )
                }
            )
            Divider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventPreview() {
    InfoDayTheme {
        EventScreen( SnackbarHostState(), "COMP")
    }
}




