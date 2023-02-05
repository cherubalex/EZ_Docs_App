package com.example.ez_docs_app

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ez_docs_app.article.getArticleWithName
import com.example.ez_docs_app.ui.theme.EZ_Docs_AppTheme
import com.example.ez_docs_app.ui.theme.Purple500
import java.util.*
import android.os.Bundle
import android.content.Context
import com.example.ez_docs_app.article.getArticleName

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
                            Text(text = "EZ-Docs", fontSize = 24.sp)
                        }
                    },
                    bottomBar = {
                        navBar(navController, pagesDescriptors)
                    }
                ) {
                    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                        NavHost(navController, startDestination = "home") { //Permet de "choisir"/"délimiter" les pages de l'application
                            composable("home") { HomePage(navController) }
                            composable("cours") { CoursPage(navController, applicationContext) }
                            composable("quiz") { QuizPage(navController) }

                            composable("articles/{nomArticle}",
                                arguments = listOf(navArgument("nomArticle") { type = NavType.StringType })
                            ) {
                                ArticlesPage(navController = navController, nomArticle = it.arguments?.getString("nomArticle"), applicationContext)
                            }
                        }   //Navhost
                    }   //Surface
                }   //Scaffold
            }   //EZ_Docs_AppTheme
        }   //setContent
    }   //onCreate
}   //class MainActivity

@Composable
fun HomePage(navController : NavHostController) {
    //todo : ajouter le vrai contenu
    Column {
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
fun QuizPage(navController : NavHostController) {
    //todo : ajouter le vrai contenu
    Text(text = "Quiz (à faire)")
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