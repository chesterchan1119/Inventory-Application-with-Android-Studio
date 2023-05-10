package com.example.infoday

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.font.FontWeight.Companion.Light
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Entity
import androidx.room.PrimaryKey
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.infoday.ui.theme.InfoDayTheme
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import coil.compose. rememberImagePainter
import kotlinx.coroutines.delay

@Serializable
@Entity(tableName = "game")

data class Game (
    @PrimaryKey val _id: String,
    val title: String,
    val image: String,
    val quantity: Int,
    val description: String,
    val category: String,
    val publisher: String,
    val location: String,
    val remark: String,
     val type: String,
    val borrower: String
){
    companion object {
//        val data = listOf(
//            Game(id = 0, title = "First Game", author = "Game", year = "2023",isbn ="Hello", description ="Hello", category = "GAME", publisher = "Hello", location = "Hello", remark ="Hello"
//                , type ="Hello", borrower = "Hello"),
//            Game(id = 0, title = "Second Game", author = "Game", year = "2023",isbn ="Hello", description ="Hello", category = "GAME", publisher = "Hello", location = "Hello", remark ="Hello"
//            , type ="Hello", borrower = "Hello")
//        )
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(games: List<Game>) {
    var gameList by remember { mutableStateOf(listOf<Game>())};
    val coroutineScope = rememberCoroutineScope()
    var resultMessage by remember  { mutableStateOf("") }
    var showBorrowedAlert by remember {mutableStateOf(false)}
    var loading by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val isAtBottom = scrollState.isAtBottom()
    var apiResponse by remember { mutableStateOf(games)}
    var count by remember{mutableStateOf(2)}
    val coroutineScope2 = rememberCoroutineScope()

//    LaunchedEffect(Unit) {
//        gameList = getGames();
//    }
    LazyColumn (state = scrollState){
        items(apiResponse){game ->
            var isBorrowed by remember { if(game.borrower==User.getUsernamee())mutableStateOf(true)else{mutableStateOf(false)} }
            ListItem(
                headlineText = { Text(game.title) },
                modifier = Modifier.clickable {},
                trailingContent = { }
            )
            Card(onClick = {  }) {
                Box(Modifier
                    .fillMaxSize()
                    .height(150.dp)
                    .background(Color.DarkGray)
                    )

                {
                    AsyncImage(
                        model = game.image.replace("http","https"),
                        contentDescription = null,
                        modifier = Modifier
                                .fillMaxSize()
                            .fillParentMaxSize()
                    )

//                    Text(resultMessage,
//                        fontSize =  5.em,
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .padding(10.dp)
//                            .height(75.dp),
//                        color = Color.Red
//                    )

                if (!isBorrowed) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (User.getAuthToken().length > 5) {
                                    resultMessage = borrow(game._id)
                                }
                                isBorrowed = !isBorrowed
                                showBorrowedAlert = !showBorrowedAlert
                            }

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (User.getAuthToken().length > 5) Color.Blue else Color.LightGray),
//                            else if (User.getAuthToken().length >5 && User.getUsernamee() == book.borrower) Color.White
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Borrow",
                            color = if (User.getUsernamee() != game.borrower) Color.White else Color.Black,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }else if (isBorrowed){
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (User.getAuthToken().length > 5) {
                                    resultMessage = returnItem(game._id)
                                }
                                isBorrowed = !isBorrowed
                                showBorrowedAlert = !showBorrowedAlert
                            }

                        },
                        colors = ButtonDefaults.buttonColors(containerColor =  if (User.getAuthToken().length > 5) Color.Yellow else Color.LightGray),
//                            else if (User.getAuthToken().length >5 && User.getUsernamee() == book.borrower) Color.White
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Return",
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
             }
            }
        Divider()
        Spacer(Modifier.size(20.dp))
        LaunchedEffect(isAtBottom) {
                // do your stuff
                if(isAtBottom){

                    delay(2000)
                    loading = true
                    delay(2000)
                    loading = false
                    coroutineScope2.launch {
                        apiResponse =   apiResponse + getGames(count)
                        count++
                    }
                }


        }
        }
    }
    if(loading == true) {
        Box(
            modifier = Modifier
                .fillMaxHeight(1f)
                .zIndex(4f)
                .padding(0.dp, 500.dp, 0.dp, 0.dp)
        ) {
            loadingProgress(isDisplayed = loading)

        }

    }
     if(showBorrowedAlert){
        if(alertDialog(resultMessage)){
            showBorrowedAlert = false
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    InfoDayTheme(){
//        GameScreen(rememberNavController(navigators = n))
    }
   
}




