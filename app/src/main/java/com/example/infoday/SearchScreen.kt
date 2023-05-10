package com.example.infoday


import android.media.Image
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.infoday.ui.theme.InfoDayTheme
import io.ktor.client.plugins.auth.providers.*
import io.ktor.util.reflect.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

data class Search(val _id: String, val type: String) {
        companion object {
        val data = listOf(
            Search("GAME","Game" ),
            Search("GIFT","Gift"),
            Search( "MATERIAL", "Material"),
            Search( "BOOK", "Book")
        )
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(navController: NavHostController): Boolean {
    val padding = 12.dp
    var keyword by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }
    Row(){
        TextField(
            placeholder = {Text("Search...")},
            maxLines = 1,
            value = keyword,
            onValueChange = { keyword = it },
            shape = RoundedCornerShape(20.dp)
        )
        Spacer(Modifier.size(padding))
        Button(onClick = {
            coroutineScope.launch {

                when(ContentHolder.getSearchMode()){
                    "game" -> ContentHolder.setContent(searchGame(keyword))
                    "gift" -> ContentHolder.setContent(searchGift(keyword))
                    "material" -> ContentHolder.setContent(searchMaterial(keyword))
                     else -> ContentHolder.setContent(searchBook(keyword))
                }
                loading = true
                delay(4000)
                loading = false


            }

        }) {
            Text("Search")

        }

    }
    if(loading == true){
        loadingProgress(isDisplayed = loading)
    }
    return true
}

object ContentHolder {
    var resultList = listOf<Any>()
    var mode = ""

    fun setSearchMode(newMode : String){
        mode = newMode.lowercase()
    }
    fun getSearchMode() : String{
         return mode
    }
    fun setContent(newResultList: List<Any>) {
        resultList = newResultList
    }

    fun getContent() :  List<Any>{
        return resultList
    }

}




 //冇 Navcontroller >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchBar() {
//    val padding = 12.dp
//    var keyword by remember { mutableStateOf("") }
//    val coroutineScope = rememberCoroutineScope()
//    Row(){
//        TextField(
//            placeholder = {Text("Search...")},
//            maxLines = 1,
//            value = keyword,
//            onValueChange = { keyword = it },
//            shape = RoundedCornerShape(20.dp)
//        )
//        Spacer(Modifier.size(padding))
//        Button(onClick = {
//            coroutineScope.launch {
//
//            }
//        }) {
//            Text("Search")
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun filterBar(navController: NavHostController) {
    val padding = 12.dp
    var keyword by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var gameSelected by remember { mutableStateOf(false) }
    var giftSelected by remember { mutableStateOf(false) }
    var materialSelected by remember { mutableStateOf(false) }
    var bookSelected by remember { mutableStateOf(false) }
//    val buttonColor = if (selected) Color.Blue else Color.Yellow
//    LazyRow(){
//        items(Search.data){Search ->
    Spacer(Modifier.size(padding))
    Divider()
    Row(){
            Button(onClick = {
                coroutineScope.launch {
                   ContentHolder.setSearchMode("game")
                    gameSelected = !gameSelected
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = if (gameSelected) Color.Blue else Color.Yellow)
               )
            {
                Text(text = "Game", color = Color.Black)
            }
             Spacer(Modifier.size(15.dp))

                Button(onClick = {
                coroutineScope.launch {
                    ContentHolder.setSearchMode("gift")
                    giftSelected = !giftSelected
                }
            },      colors = ButtonDefaults.buttonColors(containerColor =  if (giftSelected) Color.Blue else Color.Yellow)
            )
            {
                Text(text = "Gift" , color = Color.Black)
            }

            Spacer(Modifier.size(15.dp))

            Button(onClick = {
                coroutineScope.launch {
                    ContentHolder.setSearchMode("material")
                    materialSelected = !materialSelected
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = if (materialSelected) Color.Blue else Color.Yellow)
            )
            {
                Text(text = "Material", color = Color.Black)
            }

             Spacer(Modifier.size(15.dp))

            Button(onClick = {
                coroutineScope.launch {
                    ContentHolder.setSearchMode("book")
                    bookSelected= !bookSelected
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = if (bookSelected) Color.Blue else Color.Yellow)
            )
            {
                Text(text = "Book", color = Color.Black)
            }
    }

}

// 冇 Navcontroller >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun filterBar() {
//    val padding = 12.dp
//    var keyword by remember { mutableStateOf("") }
//    val coroutineScope = rememberCoroutineScope()
//    LazyRow(){
//        items(Search.data){Search ->
//
//            Button(onClick = {
//                coroutineScope.launch {
//
//                }
//            }) {
//                Text(Search.type)
//            }
//            Spacer(Modifier.size(padding))
//            Divider()
//        }
//    }
//
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchResult(resultList : List<Any>) {
    var resultList by remember { mutableStateOf(listOf<Any>())};
    val padding = 20.dp

    LaunchedEffect(Unit){
        resultList = ContentHolder.getContent()
    }

    LazyColumn {

        items(resultList) { result ->
            if (result.toString().contains("type=game")) {
                result as Game
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Box(modifier = Modifier.height(200.dp)) {
                                //Image(painter = )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf( // Gradient of the Color Background
                                                    Color.Transparent,
                                                    Color.Black
                                                ),
                                                startY = 300f
                                            )
                                        )
                                )
                                Box( // The result title
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    contentAlignment = Alignment.CenterStart
                                )
                                {
                                    Text(
                                        result.title,
                                        style = TextStyle(
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = Bold
                                        )
                                    )
                                }
                                Box( // The result type
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    contentAlignment = Alignment.BottomStart
                                )
                                {
                                    Text(
                                        result.type,
                                        style = TextStyle(color = Color.White, fontSize = 16.sp)
                                    )
                                }
                            }
                        }

                Divider()
                Spacer(Modifier.size(padding))
            }
            else if(result.toString().contains("type=gift")){
                result as Gift
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Box(modifier = Modifier.height(200.dp)) {
                        //Image(painter = )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf( // Gradient of the Color Background
                                            Color.Transparent,
                                            Color.Black
                                        ),
                                        startY = 300f
                                    )
                                )
                        )
                        Box( // The result title
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            contentAlignment = Alignment.CenterStart
                        )
                        {
                            Text(
                                result.title,
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = Bold
                                )
                            )
                        }
                        Box( // The result type
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            contentAlignment = Alignment.BottomStart
                        )
                        {
                            Text(
                                result.type,
                                style = TextStyle(color = Color.White, fontSize = 16.sp)
                            )
                        }
                    }
            }

            Divider()
            Spacer(Modifier.size(padding))
            }
            else if(result.toString().contains("type=material")){
                result as Material
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Box(modifier = Modifier.height(200.dp)) {
                        //Image(painter = )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf( // Gradient of the Color Background
                                            Color.Transparent,
                                            Color.Black
                                        ),
                                        startY = 300f
                                    )
                                )
                        )
                        Box( // The result title
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            contentAlignment = Alignment.CenterStart
                        )
                        {
                            Text(
                                result.title,
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = Bold
                                )
                            )
                        }
                        Box( // The result type
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            contentAlignment = Alignment.BottomStart
                        )
                        {
                            Text(
                                result.type,
                                style = TextStyle(color = Color.White, fontSize = 16.sp)
                            )
                        }
                    }
                }

                Divider()
                Spacer(Modifier.size(padding))
            }

            else if(result.toString().contains("type=book")){
                result as Book
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Box(modifier = Modifier.height(200.dp)) {
                        //Image(painter = )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf( // Gradient of the Color Background
                                            Color.Transparent,
                                            Color.Black
                                        ),
                                        startY = 300f
                                    )
                                )
                        )
                        Box( // The result title
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            contentAlignment = Alignment.CenterStart
                        )
                        {
                            Text(
                                result.title,
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = Bold
                                )
                            )
                        }
                        Box( // The result type
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            contentAlignment = Alignment.BottomStart
                        )
                        {
                            Text(
                                result.type,
                                style = TextStyle(color = Color.White, fontSize = 16.sp)
                            )
                        }
                    }
                }

                Divider()
                Spacer(Modifier.size(padding))
            }
        }
    }

}




@Composable
fun SearchNav(navController: NavHostController) {
        val padding = 12.dp
        val games = produceState(
            initialValue = listOf<Game>(),
            producer = {
                value = getGames(1)
            }
        )
        val gifts = produceState(
            initialValue = listOf<Gift>(),
            producer = {
                value = getGifts(1)
            }
        )
        val materials = produceState(
            initialValue = listOf<Material>(),
            producer = {
                value = getMaterials(1)
            }
        )
        val books = produceState(
            initialValue = listOf<Book>(),
            producer = {
                value = getBooks(1)
            }
        )
        val resultList = produceState(
            initialValue = listOf<Any>(),
            producer = {
                value = ContentHolder.getContent()
            }
        )
    val resultList2 by remember { mutableStateOf(ContentHolder.getContent())}
    var isSearched by remember { if(ContentHolder.getContent().size>1)mutableStateOf(true)else {mutableStateOf(false)} }
        NavHost(
            navController = navController,
            startDestination = "SEARCH",

            ) {
            composable("SEARCH") {
                Column() {
                    filterBar(navController)
                    Spacer(Modifier.size(padding))
                    isSearched = SearchBar(navController)
                    Spacer(Modifier.size(padding))
                    Column() {
                        if(isSearched == true){
                             searchResult(resultList.value)
                        }
                    }

                }

             }
//        composable("Info") { InfoScreen() }
            composable("GAME") { GameScreen(games.value)}
            composable("GIFT") { GiftScreen(gifts.value)}
            composable("MATERIAL") { MaterialScreen(materials.value)}
            composable("BOOK") { BookScreen(books.value)}
    }
}
@Composable
@Preview(showBackground = true)
fun SearchScreenPreview() {
    val padding = 12.dp
    val games = produceState(
        initialValue = listOf<Game>(),
        producer = {
            value = getGames(1)
        }
    )
    val gifts = produceState(
        initialValue = listOf<Gift>(),
        producer = {
            value = getGifts(1)
        }
    )
    Column() {
//        filterBar()
        Spacer(Modifier.size(padding))
//        SearchBar()
        Spacer(Modifier.size(padding))
    }


}




