package com.example.ez_docs_app

import android.annotation.SuppressLint
import android.icu.text.CaseMap.Title
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ez_docs_app.ui.theme.EZ_Docs_AppTheme
import com.example.ez_docs_app.ui.theme.Purple500
import java.util.*

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //"Descriptions" de tous les éléments de la navbar + destinations
        val pagesDescriptors = listOf(
            NavBarElementDescriptor("Home", "home", Icons.Default.Home),
            NavBarElementDescriptor("Cours", "cours", Icons.Default.Info),
            NavBarElementDescriptor("Quiz", "quiz", Icons.Default.Send)
        )


        setContent {
            EZ_Docs_AppTheme {
                //Permet de gérer la navigation dans l'application grace à la methode .navigate("dest")
                val navController = rememberNavController()

                //Permet de changer le titre
                val topTitle = remember { mutableStateOf("Accueil") }

                //Pour plus d'info : https://developer.android.com/jetpack/compose/layouts/material?hl=fr#scaffold
                Scaffold(
                    topBar = {
                        TopAppBar(
                            elevation = 4.dp,
                            backgroundColor = Purple500
                        ) {
                            Text(text = "hello", fontSize = 24.sp)
                        }
                    },
                    bottomBar = {
                        navBar(navController, pagesDescriptors)
                    }
                ) {
                    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                        NavHost(navController, startDestination = "home") { //Permet de "choisir"/"délimiter" les pages de l'application
                            composable("home") { HomePage(navController) }
                            composable("cours") { CoursPage(navController) }
                            composable("quiz") { QuizPage(navController) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomePage(navController : NavHostController) {
    //todo : ajouter le vrai contenu
    //todo 2 : Actuellement une partie du contenu est caché par la navbar, même quand on scroll à fond.
    Column() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .weight(weight = 2f, fill = false)
                .padding(bottom = (navBarHeight + navBarPaddingOnSides).dp) //pour ne pas que le bas de la page soit sous la navbar
        ) {
            for (i in 0..42) {
                Text(text = "Accueil $i")
            }
        }
    }
}

@Composable
fun CoursPage(navController : NavHostController) {
    //todo : ajouter le vrai contenu
    Text(text = "Cours")
}

@Composable
fun QuizPage(navController : NavHostController) {
    //todo : ajouter le vrai contenu
    Text(text = "Pages")
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EZ_Docs_AppTheme {
        Greeting("Android")
    }
}