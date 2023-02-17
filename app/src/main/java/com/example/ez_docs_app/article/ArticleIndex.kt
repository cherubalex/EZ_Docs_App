package com.example.ez_docs_app.article

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ez_docs_app.ImageAsset
import java.io.FileNotFoundException
import java.io.IOException

//filename est le nom du fichier tel que son chemin est "articles/{filename}"
//Cette fonction lit la première ligne du fichier, la partie avant le #.
fun getArticleName(filename : String, context: Context) : String {
    val inFileTitle: String?        //Correspond à la première ligne
     context.assets.open("articles/$filename").bufferedReader().use {
         inFileTitle = it.readLine()    //lire le titre depuis la première ligne
    }   //.use {} ferme automatiquement le bufferedReader

    if(inFileTitle == null) {
        return "null"
    }

    val headerList = inFileTitle.split("#")
    return headerList[0]
}


//filename est le nom du fichier tel que son chemin est "articles/{filename}"
//Cette fonction lit la première ligne du fichier, la partie après le #.
@Composable
fun ArticleIcone(filename : String, context: Context) {
    val inFileTitle: String?
    context.assets.open("articles/$filename").bufferedReader().use {
        inFileTitle = it.readLine()    //lire le titre depuis la première ligne
    }   //.use {} ferme automatiquement le bufferedReader

    if(inFileTitle != null) {
        val headerList = inFileTitle.split("#")
        if(headerList.size > 1) {
            ImageAsset(
                fileName = headerList[1].trim(' '),
                130,
                130,
                modifier = Modifier.clip(CircleShape).width(50.dp).height(50.dp)
            )
            return
        }
    }

    //S'il n'y a rien après le #, utiliser l'image par défaut.
    ImageAsset(
        fileName = "imgs/article.png",
        130,
        130,
        modifier = Modifier.clip(CircleShape).width(50.dp).height(50.dp)
    )
}


//fileName est le nom du fichier tel que son chemin est "articles/{filename}
fun getArticleWithName(fileName : String?, context: Context) : Article {
    if(fileName == null) {
        return Article("Article invalide", "L'article avec le nom $fileName n'existe pas :(")
    }

    val titreArticle: String?
    val contentArticle: String?

    try {
        context.assets.open("articles/$fileName").bufferedReader().use {
            titreArticle = it.readLine()    //lire le titre depuis la première ligne
            contentArticle = it.readText()  //lire le contenu
        }   //.use {} ferme automatiquement le bufferedReader
    }
    catch (e: FileNotFoundException) {
        return Article("Erreur", "Le fichier articles/$fileName n'existe pas.")
    }
    catch (e: IOException) {
        return Article("Erreur", "Une erreur à eu lieu lors de la lecture du fichier articles/$fileName.")
    }

    var titreFinal = "[Article sans titre]"     //supposer que la lecture du titre rate/soit vide
    if(!titreArticle.isNullOrBlank()) {      //vérifier si la lecture du titre est un succès
        titreFinal = titreArticle.split("#")[0]
    }

    if(contentArticle.isNullOrBlank()) {        //vérifier si la lecture de l'article est un succès
        return Article(titreFinal, "La lecture de l'article a raté :(")
    }
    return Article(titreFinal, contentArticle)
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleListItem(articleFileName : String, navController: NavController) {
    val context = LocalContext.current

    //fixme: Actuellemnt getArticleIcone et getArticleName ouvrent tous les deux le fichier.
    //       Ce serait mieux si le fichier était ouvert qu'une seul fois et traiter d'un coup.

    //infos sur ListItem : https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#ListItem(androidx.compose.ui.Modifier,kotlin.Function0,kotlin.Function0,kotlin.Boolean,kotlin.Function0,kotlin.Function0,kotlin.Function0)
    ListItem(
        icon = { ArticleIcone(articleFileName, context)},
        text = { Text( getArticleName(articleFileName, context)) },
        modifier = Modifier
            .clickable {
                navController.navigate("articles/$articleFileName")
            }
    )
}