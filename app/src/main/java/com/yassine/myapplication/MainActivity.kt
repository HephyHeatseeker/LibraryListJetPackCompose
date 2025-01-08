package com.yassine.myapplication

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.yassine.myapplication.ui.theme.MyApplicationTheme
import repository.Repository
import room.BookEntity
import room.BooksDB
import viewmodel.BookViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yassine.myapplication.screens.UpdateScreen


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 35.dp ),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val mContext = LocalContext.current
                    val db = BooksDB.getInstance(mContext)
                    val repository = Repository(db)
                    val myViewModel = BookViewModel(repository = repository )



                    // navigation
                    var navController = rememberNavController()

                    NavHost(navController = navController
                            , startDestination = "MainScreen"
                    ){
                        composable("MainScreen"){
                            MainScreen(viewModel = myViewModel , navController)
                        }
                        composable("UpdateScreen/{bookId}"){
                            UpdateScreen(viewModel = myViewModel ,bookId = it.arguments?.getString("bookId"))
                        }


                    }

                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: BookViewModel, navController: NavHostController) {

    var inputBook: String by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            top     = 22.dp,
            start   = 6.dp,
            end     = 6.dp
        )
    ) {
        Text(
            text     = "Insert Books in ROOM DB",
            fontSize = 22.sp
        )

        OutlinedTextField(
            value = inputBook,
            onValueChange = { enteredText -> inputBook = enteredText },
            label = { Text(text = "MOVIE TITLE") },
            placeholder = { Text("Title EX: AVENGERS: END GAME") }
        )
        Button(
            onClick = {
                if (inputBook.isNotEmpty()) {
                    viewModel.addBook(BookEntity(0, inputBook))
                    inputBook = "" // Clear the text field after submission
                }
            },

            colors = ButtonDefaults.buttonColors(Color.Blue)

            ) {
            Text(text = "Insert book into db")
        }

        // books list
        BooksList(viewModel= viewModel,navController)
    }
}


@Composable
fun BookCard(viewModel: BookViewModel, book : BookEntity,navController: NavHostController){

    Card(
        modifier =  Modifier.padding(8.dp)
                            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text= "" + book.id,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 12.dp,end=12.dp),
                color    = Color.Magenta
            )
            Text(
                text = book.title,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxSize(0.7f),
                color    = Color.Black

            )

            Row (
                horizontalArrangement = Arrangement.End
            ){
                IconButton(
                    onClick = { viewModel.deleteBook(book = book )}
                ) {
                    Icon(
                        imageVector         = Icons.Default.Delete ,
                        contentDescription  = "Delete"
                    )
                }

                IconButton(
                    onClick = {
                        navController.navigate("UpdateScreen/${book.id}")
                    }
                ) {
                    Icon(
                        imageVector         = Icons.Default.Edit ,
                        contentDescription  = "Edit"
                    )
                }
            }


        }
    }

}


@Composable
fun BooksList(viewModel: BookViewModel,navController: NavHostController){

    val books by viewModel.books.collectAsState(initial = emptyList())

    Column(
       modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "My Library",
            fontSize = 24.sp,
            color    = Color.Red

        )


        LazyColumn {

                items(items = books ){
                    item -> BookCard(
                            viewModel = viewModel,
                            book = item,
                            navController = navController
                            )
                }

            }
        }
}