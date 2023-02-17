package com.example.ez_docs_app

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomePage(navController : NavHostController) {
    //todo : ajouter le vrai contenu
    LazyColumn(modifier = Modifier.fillMaxSize()){
        items(42) {
            Text(text = "Accueil ${it+1}")
        }

        item {
            //faire en sorte de pouvoir scroller plus pour que le contenu ne soit pas sous la navbar
            Spacer(modifier = Modifier.height((navBarHeight + navBarPaddingOnSides).dp))
        }
    }
}