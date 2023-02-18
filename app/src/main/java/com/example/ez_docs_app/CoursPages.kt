package com.example.ez_docs_app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ez_docs_app.article.ArticleListItem
import com.example.ez_docs_app.article.getArticleWithName

//Affiche la liste de tous les articles disponibles dans assets/articles.
@Composable
fun ArticleListPage(navController : NavHostController) {
    val context = LocalContext.current

    //Obtenir la liste de tous les noms de fichier des articles.
    //todo : gèrer IOExeption ?
    val articlesList: Array<String>? = context.assets.list("articles")

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        //Vérifier s'il y a des articles dans le dossier
        if (articlesList != null) {
            //Ajouter des liens vers les articles un par un dans des ListItems.
            items(articlesList) { articleFileName ->
                ArticleListItem(articleFileName, navController)
                Divider()           //Séparateurs entre les éléments de la liste.
            }
        } else {
            item {
                Text(text = "Aucun article n'a été trouvé.")
            }
        }

        item {
            //faire en sorte de pouvoir scroller plus pour que le contenu ne soit pas sous la navbar
            Spacer(modifier = Modifier.height((navBarHeight + navBarPaddingOnSides).dp))
        }
    }
}


//Affiche le contenu d'un article.
//nomArticle correspond au nom du fichier tel que son chemin est "assets/articles/{nomArticle}"
@Composable
fun ArticlesPage(navController : NavHostController, nomArticle : String?) {
    val context = LocalContext.current
    if(nomArticle.isNullOrBlank()) {
        getArticleWithName("null", context).MakeComponent(navController)
    }
    else {
        getArticleWithName(nomArticle, context).MakeComponent(navController)
    }
}