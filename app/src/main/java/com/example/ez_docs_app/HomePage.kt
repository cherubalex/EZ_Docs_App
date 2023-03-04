package com.example.ez_docs_app

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ez_docs_app.article.Article

@Composable
fun HomePage(navController : NavHostController) {
    val context = LocalContext.current
    Article("Accueil", context.assets.open("accueil.md").bufferedReader().use { it.readText() }).MakeComponent(navController = navController)
}
