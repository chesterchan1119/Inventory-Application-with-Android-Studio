package com.example.infoday

import android.media.Image
import androidx.compose.foundation.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infoday.ui.theme.InfoDayTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import android.util.Log

import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val dataStore = UserPreferences(LocalContext.current)
            val mode by dataStore.getMode.collectAsState(initial = false)

            InfoDayTheme(darkTheme = mode ?: false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//            Greeting("Android")
                    ScaffoldScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InfoDayTheme {
        ScaffoldScreen()
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldScreen() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Items", "Search", "Login")
    val icons = mapOf (
        "Items" to Icons.Filled.Home ,
        "Search" to Icons.Filled.Search,
        "Login" to Icons.Filled.Person)
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    var signedInState by remember { mutableStateOf(User.getAuthToken().length> 5) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Inventory Application")
                },
                navigationIcon = {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    if (navBackStackEntry?.arguments?.getBoolean("topLevel") == false) {
                        IconButton(
                            onClick = { navController.navigateUp() }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    } else {
                        null
                    }
                },
                actions = {
                    Row(){
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(containerColor = if (User.getAuthToken().length>5) Color.Yellow else Color.Transparent)
                            )
                            {
                                Text(text =  "Sign Out",
                                    color = Color.Black
                                )
                            }

                     }

                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { icons.get(item)?.let { Icon(it, contentDescription = item) } },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }},
        snackbarHost = {SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
            ) {
                when (selectedItem) {
                    0 -> ItemNav(navController)
                    1 -> SearchNav(navController)
                    2 -> InfoScreen()
                }
            }
        }
    )

}