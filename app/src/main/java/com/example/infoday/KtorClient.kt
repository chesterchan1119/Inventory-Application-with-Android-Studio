package com.example.infoday

import android.annotation.SuppressLint
import com.example.infoday.KtorClient.httpClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import org.w3c.dom.Text
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.*
import org.json.JSONObject
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.server.application.*
import io.ktor.server.auth.*


object KtorClient {

    var httpClient = HttpClient () {
        install(ContentNegotiation) {
            json() // enable the client to perform JSON serialization
        }
        install(Logging)
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }

        expectSuccess = true
    }


}


// Get
suspend fun getGames(page:Int): List<Game> {
    try {
        return httpClient.get("http://comp4107.azurewebsites.net/inventory?page=$page&type=game").body()
            ?: emptyList<Game>()
    } catch (e: Exception ){
        val emptyList = emptyList<Game>()
        Log.d("getGames()",e.toString())
//        emptyList.plus("The API is crashed. ")
        return emptyList
    }
}


suspend fun getGifts(page:Int): List<Gift> {
    try{return httpClient.get("http://comp4107.azurewebsites.net/inventory?page=$page&type=gift").body()?: emptyList<Gift>()
    }
    catch (e: Exception ){
        e.printStackTrace()
        Log.d("getGifts()",e.toString())
        return emptyList<Gift>()
    }
}
suspend fun getMaterials(page:Int): List<Material> {

    try{return httpClient.get("http://comp4107.azurewebsites.net/inventory?page=$page&type=material").body()?: emptyList<Material>()
    }
    catch (e: Exception ){
        e.printStackTrace()
        Log.d("getMaterials()",e.toString())
        return emptyList<Material>()
    }
}

suspend fun getBooks(page:Int): List<Book> {
    try{return httpClient.get("http://comp4107.azurewebsites.net/inventory?page=$page&type=book").body() ?: emptyList<Book>()
    }
    catch (e: Exception ){
        e.printStackTrace()
        Log.d("getBooks()",e.toString())
        return emptyList<Book>()
    }

}
object User {
    var token = ""
    var username = ""


    fun setAuthToken(newToken : String){
        token = newToken
    }
    fun getAuthToken() : String{
        return token
    }
    fun getUsernamee() : String {
        return username
    }
    fun setUsernamee(newUsername : String){
        username = newUsername
    }


}



@SuppressLint("SuspiciousIndentation")
@OptIn(InternalAPI::class)
suspend fun login(username :String, password : String) : String  {
    //val requestBody = """{"email": "sbeceril0@unc.edu","password": "123456"} """
    try{
        val response: String = httpClient.submitForm(
            url = "http://comp4107.azurewebsites.net/user/login",
            formParameters = Parameters.build {
                append("email", username)
                append("password", password)
            }
        ).body()
       var responseJson = JSONObject(response)
        var token = responseJson.getString("token")
        var firstName = responseJson.getString("firstname")
        var lastName = responseJson.getString("last_name")
        val fullName = firstName + " " + lastName
        User.setUsernamee(fullName)
        User.setAuthToken(token)
        return response
    }catch (e: Exception){
        val response = "Wrong Credentials"
        return response
    }
}
@SuppressLint("SuspiciousIndentation")
@OptIn(InternalAPI::class)
suspend fun  searchGame(keyword : String) : List<Any>{
    var baseUrl  = "http://comp4107.azurewebsites.net/inventory?type=game&keyword="
    var url = baseUrl + keyword

    val response = httpClient.get(url).body()?: emptyList<Game>()
    return response
}
@SuppressLint("SuspiciousIndentation")
@OptIn(InternalAPI::class)
suspend fun  searchGift(keyword : String) : List<Any>{
    var baseUrl  = "http://comp4107.azurewebsites.net/inventory?type=gift&keyword="
    var url = baseUrl + keyword

    val response = httpClient.get(url).body()?: emptyList<Gift>()
    return response
}
@SuppressLint("SuspiciousIndentation")
@OptIn(InternalAPI::class)
suspend fun  searchMaterial(keyword : String) : List<Any>{
    var baseUrl  = "http://comp4107.azurewebsites.net/inventory?type=material&keyword="
    var url = baseUrl + keyword

    val response = httpClient.get(url).body()?: emptyList<Material>()
    return response
}
@SuppressLint("SuspiciousIndentation")
@OptIn(InternalAPI::class)
suspend fun  searchBook(keyword : String) : List<Any>{
    var baseUrl  = "http://comp4107.azurewebsites.net/inventory?type=book&keyword="
    var url = baseUrl + keyword

    val response = httpClient.get(url).body()?: emptyList<Book>()
    return response
}



@SuppressLint("SuspiciousIndentation")
@OptIn(InternalAPI::class)
suspend fun consume(itemId: String) : String{
    var baseUrl  = "http://comp4107.azurewebsites.net/user/consume/"
    var url = baseUrl + itemId

    var logInClient = HttpClient () {
        install(ContentNegotiation) {
            json() // enable the client to perform JSON serialization
        }
        install(Logging)
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
        install(Auth) {
            bearer {
                loadTokens {
                    // Load tokens from a local storage and return them as the 'BearerTokens' instance
                    BearerTokens(User.getAuthToken(), User.getAuthToken())
                }
            }
        }

        expectSuccess = true
    }

    try{
        val response : String  = logInClient.post(url).body()
        var responseJson = JSONObject(response)
        var message = responseJson.getString("message")
        return message

    }catch(e : Exception){
        e.printStackTrace()
        Log.d("consume()",e.toString())
        val response = e.toString()
        if(response.contains("message")){
            return "Item not available for consume."
        }else{
            return "You have to login before consume"
        }

    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(InternalAPI::class)
suspend fun borrow(itemId: String) : String{
    var baseUrl  = "http://comp4107.azurewebsites.net/user/borrow/"
    var url = baseUrl + itemId

    var logInClient = HttpClient () {
        install(ContentNegotiation) {
            json() // enable the client to perform JSON serialization
        }
        install(Logging)
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
        install(Auth) {
            bearer {
                loadTokens {
                    // Load tokens from a local storage and return them as the 'BearerTokens' instance
                    BearerTokens(User.getAuthToken(), User.getAuthToken())
                }
            }
        }

        expectSuccess = true
    }
    try{
        val response : String  = logInClient.post(url).body()
        var responseJson = JSONObject(response)
        var message = responseJson.getString("message")
        return message

    }catch(e : Exception){
        e.printStackTrace()
        Log.d("borrow()",e.toString())
        val response = e.toString()
        if(response.contains("message")){
            return "Item not available for borrowing."
        }else{
            return "You have to login to borrow resources from the system"
        }

    }

}
@SuppressLint("SuspiciousIndentation")
@OptIn(InternalAPI::class)
suspend fun returnItem(itemId: String) : String{
    var baseUrl  = "http://comp4107.azurewebsites.net/user/return/"
    var url = baseUrl + itemId

    var logInClient = HttpClient () {
        install(ContentNegotiation) {
            json() // enable the client to perform JSON serialization
        }
        install(Logging)
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
        install(Auth) {
            bearer {
                loadTokens {
                    // Load tokens from a local storage and return them as the 'BearerTokens' instance
                    BearerTokens(User.getAuthToken(), User.getAuthToken())
                }
            }
        }

        expectSuccess = true
    }
    try{
        val response : String  = logInClient.post(url).body()
        var responseJson = JSONObject(response)
        var message = responseJson.getString("message")
        return message

    }catch(e : Exception){
        e.printStackTrace()
        Log.d("returnItem()",e.toString())
        val response = e.toString()
        if(response.contains("message")){
            return "Item not available for returning."
        }else{
            return "You have to login to return resources from the system"
        }

    }

}




@Composable
fun loadingProgress( isDisplayed: Boolean ){
    if(isDisplayed){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp),
            horizontalArrangement = Arrangement.Center
        ){
            CircularProgressIndicator(
                color = Color.Yellow
            )

        }
    }
}

@Composable
fun alertDialog(message: String) : Boolean {
    val context = LocalContext.current
    val openDialog = remember{mutableStateOf(true )}
    var finished : Boolean by remember { mutableStateOf(false)}

    if(openDialog.value)
    {
        AlertDialog(
            onDismissRequest = { openDialog.value = false},
            title = { Text(text = "Notification", color = Color.Black, fontWeight = FontWeight.ExtraBold) },
            text = { Text(text = message, color = Color.Black) },
            confirmButton = {
                TextButton(onClick = {
                    openDialog.value = false
                    Toast.makeText(context, "Confirm Button Clicked", Toast.LENGTH_SHORT).show()
                    finished = !finished

                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow)
                ){
                    Text(text="Confirm", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDialog.value = false
                    Toast.makeText(context, "Dismiss Button Clicked", Toast.LENGTH_SHORT).show()
                    finished = !finished
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Yellow)
                ){
                    Text(text = "Dismiss", color = Color.Black)
                }
            },
            containerColor = Color.LightGray,
            textContentColor = Color.Black,

            )

    }
    return finished

}








@Serializable
data class HttpBinResponse(
    val args: Map<String, String>,
    val data: String,
    val files: Map<String, String>,
    val form: Map<String, String>,
    val headers: Map<String, String>,
    val json: String?,
    val origin: String,
    val url: String
)

