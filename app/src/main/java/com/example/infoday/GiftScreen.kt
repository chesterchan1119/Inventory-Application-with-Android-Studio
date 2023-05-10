package com.example.infoday

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Entity
import androidx.room.PrimaryKey
import coil.compose.AsyncImage
import com.example.infoday.ui.theme.InfoDayTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "gift")
data class Gift(
    val _id: String,
    val title: String,
    val image: String,
    val donatedBy: String,
    val description: String,
    val category: String,
    val amount: Int,
    val unitPrice: Int,
    val location: String,
    val remark: String,
    val type: String,
    val remaining: Int
){
    companion object {
//        val data =
    //listOf(
//            Gift(_id = "0", title = "First Gift", image = "Game1", donatedBy = "2023", description ="Hello", category = "GIFT", amount= 1, unitPrice = 1, location = "", remark ="Hello"
//                , type ="Hello", remaining = 1),
//            Gift(_id = "1", title = "Second Gift", image = "Game2", donatedBy = "2023", description ="Hello", category = "GIFT", amount= 1, unitPrice = 1, location = "", remark ="Hello"
//                , type ="Hello", remaining = 1)
//        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftScreen(gifts: List<Gift>) {
    var giftList by remember { mutableStateOf(listOf<Gift>())};
    val coroutineScope = rememberCoroutineScope()
    var resultMessage by remember  { mutableStateOf("") }
    var showConsumedAlert by remember {mutableStateOf(false)}
    var loading by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val isAtBottom = scrollState.isAtBottom()
    var apiResponse by remember { mutableStateOf(gifts)}
    var count by remember{mutableStateOf(2)}
    val coroutineScope2 = rememberCoroutineScope()

    LazyColumn(state = scrollState) {
        items(apiResponse){gift ->
            ListItem(
                headlineText = { Text(gift.title) },
                modifier = Modifier.clickable {},
                trailingContent = { }
            )
            Card(onClick = {},) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .height(150.dp)
                        .background(Color.DarkGray)
                )

                    {
                        AsyncImage(
                            model = gift.image.replace("http","https"),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .fillParentMaxSize()
                        )

//                        Text(resultMessage,
//                            fontSize =  5.em,
//                            modifier = Modifier
//                                .align(Alignment.Center)
//                                .padding(10.dp)
//                                .height(75.dp),
//                            color = Color.Red
//                        )
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    if (User.getAuthToken().length>5){
                                        resultMessage = consume(gift._id)
                                    }
                                    showConsumedAlert = !showConsumedAlert
                                }

                            },
                            colors = ButtonDefaults.buttonColors(containerColor = if (User.getAuthToken().length>5) Color.Blue else Color.LightGray),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        ){
                            Text("Consume",
                                color = Color.White
                            )
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
                        apiResponse =   apiResponse + getGifts(count)
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
    if(showConsumedAlert){
        if(alertDialog(resultMessage)){
            showConsumedAlert = false
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GiftPreview() {
//    InfoDayTheme {
//        GiftScreen()
//    }
//}




