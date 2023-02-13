package com.example.ez_docs_app

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ez_docs_app.article.getArticleName
import com.example.ez_docs_app.article.getArticleWithName

//Affiche la liste de tous les articles disponibles dans assets/articles.
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CoursPage(navController : NavHostController, context: Context) {
    //Obtenir la liste de tous les articles.
    //todo : gèrer IOExeption ?
    val articlesList: Array<out String>? = context.assets.list("articles")

    Column {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .weight(weight = 2f, fill = false)
                .padding(bottom = (navBarHeight + navBarPaddingOnSides).dp) //Pour ne pas que le bas de la page soit sous la navbar.
        ) {
            if (articlesList != null) {
                //Ajouter des liens vers les articles un par un dans des ListItems.
                for(articleFileName in articlesList) {
                    //https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#ListItem(androidx.compose.ui.Modifier,kotlin.Function0,kotlin.Function0,kotlin.Boolean,kotlin.Function0,kotlin.Function0,kotlin.Function0)
                    ListItem(
                        text = { Text( getArticleName(articleFileName, context)) },
                        modifier = Modifier.clickable {
                            navController.navigate("articles/$articleFileName")
                        }
                    )
                    Divider()       //Séparateurs entre les éléments de la liste.
                }
            }
            else {
                Text(text = "Aucun article n'a été trouvé.")
            }
        }
    }
}


//Affiche le contenu d'un article.
//nomArticle correspond au nom du fichier tel que son chemin est "assets/articles/{nomArticle}"
@Composable
fun ArticlesPage(navController : NavHostController, nomArticle : String?, context: Context) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .weight(weight = 2f, fill = false)
                .padding(start = 10.dp, end = 10.dp, bottom = (navBarHeight + navBarPaddingOnSides).dp) //pour ne pas que le bas de la page soit sous la navbar
        ) {
            if(nomArticle.isNullOrBlank()) {
                getArticleWithName("null", context).MakeComponent(navController)
            }
            else {
                getArticleWithName(nomArticle, context).MakeComponent(navController)
            }
        }
    }
}