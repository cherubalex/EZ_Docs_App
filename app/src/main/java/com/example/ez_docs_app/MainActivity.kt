package com.example.ez_docs_app

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ez_docs_app.ui.theme.EZ_Docs_AppTheme
import com.example.ez_docs_app.ui.theme.Purple500
import java.util.*
import android.os.Bundle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EZ_Docs_AppTheme {
                MainApp()
            }   //EZ_Docs_AppTheme
        }   //setContent
    }   //onCreate
}   //class MainActivity


@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun MainApp() {
    //"Descriptions" de tous les éléments de la navbar + destinations
    val pagesDescriptors = listOf(
        NavBarElementDescriptor("Home", "home", Icons.Default.Home),
        NavBarElementDescriptor("Cours", "cours", Icons.Default.Info),
        NavBarElementDescriptor("Quiz", "quizlist", Icons.Default.Send)
    )

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
                Text(text = "EZ-Docs", fontSize = 24.sp)
            }
        },
        bottomBar = {
            navBar(navController, pagesDescriptors)
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            NavHost(navController, startDestination = "home") { //Permet de "choisir"/"délimiter" les pages de l'application
                composable("home") { HomePage(navController) }
                composable("cours") { CoursPage(navController) }
                composable("quizlist") { QuizListPage(navController) }

                composable("articles/{nomArticle}",
                    arguments = listOf(navArgument("nomArticle") { type = NavType.StringType })
                ) {
                    ArticlesPage(navController = navController, nomArticle = it.arguments?.getString("nomArticle"))
                }
            }   //Navhost
        }   //Surface
    }   //Scaffold
}