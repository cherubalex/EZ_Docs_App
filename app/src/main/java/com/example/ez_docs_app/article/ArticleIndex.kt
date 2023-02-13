package com.example.ez_docs_app.article

import android.content.Context
import java.io.FileNotFoundException
import java.io.IOException

//filename est le nom du fichier tel que son chemin est "articles/{filename}
//Cette fonction lit la première ligne du fichier
fun getArticleName(filename : String, context: Context) : String {
    val inFileTitle: String?
     context.assets.open("articles/$filename").bufferedReader().use {
         inFileTitle = it.readLine()    //lire le titre depuis la première ligne
    }   //.use {} ferme automatiquement le bufferedReader

    if(inFileTitle == null) {
        return "null"
    }
    return inFileTitle
}

//fileName est le nom du fichier tel que son chemin est "articles/{filename}
fun getArticleWithName(fileName : String?, context: Context) : Article {
    if(fileName == null) {
        return Article("Article invalide", "L'article avec le nom $fileName n'existe pas :(", context)
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
        return Article("Erreur", "Le fichier articles/$fileName n'existe pas.", context)
    }
    catch (e: IOException) {
        return Article("Erreur", "Une erreur à eu lieu lors de la lecture du fichier articles/$fileName.", context)
    }

    var titreFinal: String = "[Article sans titre]"     //supposer que la lecture du titre rate/soit vide
    if(!titreArticle.isNullOrBlank()) {      //vérifier si la lecture du titre est un succès
        titreFinal = titreArticle
    }

    if(contentArticle.isNullOrBlank()) {        //vérifier si la lecture de l'article est un succès
        return Article(titreFinal, "La lecture de l'article a raté :(", context)
    }
    return Article(titreFinal, contentArticle, context)
}