package com.example.infoday

import android.content.Intent
import android.icu.text.CaseMap.Title
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infoday.ui.theme.InfoDayTheme
import io.ktor.client.plugins.*
import io.ktor.http.LinkHeader.Parameters.Title
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoGreeting(islogedIn: Boolean) {
    val padding = 16.dp;


    Column(horizontalAlignment = Alignment.CenterHorizontally) {

            if (!islogedIn){
                Text(text = "Inventory System Login", fontSize = 30.sp)
                Spacer(Modifier.size(padding))
                Image(
                    painter = painterResource(id = R.drawable.inventory_icon),
                    contentDescription = stringResource(id = R.string.inventory_icon)
                )
            }
            else if (islogedIn) {
                ListItem(
                    headlineText = {
                        Text(
                            User.getUsernamee(),
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .padding(16.dp)
                                .padding(8.dp),
                            fontSize = 25.sp
                        )
                    },
                    leadingContent = {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = null,
                        )
                    },
                  trailingContent =  {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = null,
                        )

                    }
                )
                ListItem(
                    headlineText = {
                        Text(
                            User.getAuthToken(),
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .padding(16.dp)
                                .padding(8.dp)
                        )
                    },
                    leadingContent = {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = null
                        )
                    }
                )
            }



        Spacer(Modifier.size(padding))

    }
}

@Preview(showBackground = true)
@Composable
fun InfoPreview() {
    InfoDayTheme {
        InfoScreen()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun signInForm(snackbarHostState: SnackbarHostState) : Boolean{
    val padding = 12.dp
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var token by remember  { mutableStateOf("") }
    var loginMessage by remember  { mutableStateOf("") }
    var showSignedInAlert by remember {mutableStateOf(false)}



    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            placeholder = {Text("Username...")},
            maxLines = 1,
            value = username,
            onValueChange = { username = it }
        )
        Spacer(Modifier.size(padding))
        TextField(
            placeholder = {Text("Password...")},
            maxLines = 1,
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { password = it },
        )
        Spacer(Modifier.size(padding))

        Button(onClick = {
            coroutineScope.launch {

                token = login(username, password);

                if(token.contains("token")){
                    loginMessage = "You have successfully log in"
                }
                else {
                    loginMessage = "Credentials inputted are incorrect."
                }
                showSignedInAlert = true
                delay(5000)
            }

        }) {
            Text("Login")
        }
    }
    if(showSignedInAlert){
        if(alertDialog(loginMessage)){
            if(token.contains("token")){
                return true
            }
            showSignedInAlert = false
        }

    }
    return false
}




@Composable
fun InfoScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    var islogedIn by remember { if(User.getAuthToken().length>5)mutableStateOf(true)else{mutableStateOf(false)} }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        InfoGreeting(islogedIn)
        if(!islogedIn){
            islogedIn = signInForm(snackbarHostState)
        }
        SettingList()

    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingList() {
//    var checked by remember { mutableStateOf(true) }
    val dataStore  = UserPreferences(LocalContext.current)
    val checked by dataStore.getMode.collectAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()
    ListItem(
        headlineText = { Text("Dark Mode") },
        leadingContent = {
            Icon(
                Icons.Filled.Settings,
                contentDescription = null
            )
        },
        trailingContent = {
            Switch(
                modifier = Modifier.semantics { contentDescription = "Demo" },

                checked = checked ?: true,
                onCheckedChange = {
                    coroutineScope.launch {
                        dataStore.saveMode(it)
                    }
                })
        }
    )
}





