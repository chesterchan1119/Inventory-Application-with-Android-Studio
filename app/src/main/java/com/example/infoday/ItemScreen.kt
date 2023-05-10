package com.example.infoday

import android.R
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import coil.compose.AsyncImage
import kotlinx.coroutines.launch


@kotlinx.serialization.Serializable
data class Item(val _id: String, val type : String, val image: String) {

    companion object {
        val data = listOf(
            Item("GAME", "Game", "https://images.pexels.com/photos/3165335/pexels-photo-3165335.jpeg"),
            Item("GIFT", "Gift","https://pbs.twimg.com/media/FdIKwCmX0AEyBnN.jpg" ),
            Item( "MATERIAL", "Material","https://lbrcontent.affino.com/AcuCustom/Sitename/DAM/012/Metaverse_Sports_2021.jpg"),
            Item("BOOK", "Book","https://cdna.artstation.com/p/assets/covers/images/011/844/864/large/ivan-friz-fairy-tail-artcraft-book-2d-game-art.jpg?1531737438")
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ItemScreenPreview() {
    ItemScreen(rememberNavController())
}


@Composable
@Preview(showBackground = true)
fun ItemPreview() {
    ItemNav(rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val padding = 12.dp
    var selected by remember { mutableStateOf(false) }
    LazyColumn {
        items(Item.data) { Item ->

            ListItem(
                headlineText = { Text(Item.type) },
                modifier = Modifier.clickable {
                    navController.navigate(Item._id)
                },
                leadingContent = {
//                    Icon(
//                        Icons.Filled.ThumbUp,
//                        contentDescription = null
//                    )
                },
                trailingContent = {
                    Icon(
                        Icons.Filled.ThumbUp,
                        contentDescription = null,
                        modifier = Modifier.clickable{
                            coroutineScope.launch {
                                selected= !selected
                            }
                        },
                        tint = if (selected) Color.Blue else Color.White)


                }
            )

            Card(onClick = {}) {

                Box(
                    Modifier
                        .fillMaxSize()
                        .height(150.dp)
                        .background(Color.DarkGray)
                )
                {
//
                    AsyncImage(
                        model = Item.image,
                        modifier = Modifier
                            .fillMaxSize()
                            .fillParentMaxSize()
                            ,
                        contentDescription = "Translated description of what the image contains"
                    )

                }

            }

            Spacer(Modifier.size(padding))
            Spacer(Modifier.size(padding))
            Spacer(Modifier.size(padding))
            Divider()
        }
    }
}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Banner(navController: NavHostController) {
//    LazyColumn {
//        items(Item.data) { Item ->
//        }}}




@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ItemNav(navController: NavHostController) {
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
    val books= produceState(
        initialValue = listOf<Book>(),
        producer = {
            value = getBooks(1)
        }
    )
    NavHost(
        navController = navController,
        startDestination = "ITEM",

        ) {
        composable("ITEM") { ItemScreen(navController) }
//        composable("Info") { InfoScreen() }
        composable("GAME") { GameScreen(games.value)}
        composable("GIFT") { GiftScreen(gifts.value)}
        composable("MATERIAL") { MaterialScreen(materials.value)}
        composable("BOOK") { BookScreen(books.value)}
    }
}






