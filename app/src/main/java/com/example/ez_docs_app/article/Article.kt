package com.example.ez_docs_app.article

import android.content.Context
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ez_docs_app.ImageAsset


class Article(
    private val title : String,
    private val rawContent: String,
    private val context: Context
    ) {
    //créé un composable à partir de l'article.
    //note : le composable créé en lui-même n'est pas scrollable, il faut donc que le parent
    //       de celui-ci gère le scrolling.
    @Composable
    fun MakeComponent(navController: NavHostController) {
        //Afficher le titre de l'article
        Text(text = title, fontSize = 26.sp)

        val rawContentLines = rawContent.split("\n")
        //traiter l'article ligne par ligne
        for(currentLine in rawContentLines) {
            //Traiter la ligne et la transformer en Composable
            ProcessRawLine(currentLine, navController)
        }
    }


    //Transformer une ligne du RawContent en Composable
    //Appelé par MakeComponent()
    @Composable
    private fun ProcessRawLine(rawLine : String, navController: NavHostController) {
        //Détermider de quel type de ligne il s'agit
        if(rawLine.getOrNull(0) == '&') {     //caractére marquant un élément spécial
            //"Découper" la ligne à chaque espace.
            val splittedLine = rawLine.split(" ")

            if(splittedLine[0] == "&IMG") {     //image
                MakeImage(splittedLine, navController = navController)
            }
            else {      //Élément inconnu, l'insérer sous forme de texte.
                MakeLine(lineString = rawLine, navController = navController)
            }
        }
        else {          //Texte (élément par défaut)
            MakeLine(lineString = rawLine, navController = navController)
        }
    }


    //Traiter une ligne (ou un paragraphe) et la transformer en Composable (ClickableText)
    //Appelé par ProcessRawLine()
    @Composable
    private fun MakeLine(lineString : String, navController: NavHostController) {
        //Pour stocker les liens présents dans la ligne
        val hyperlinkList = mutableListOf<HyperlinkEntry>()

        //Composer le AnnotatedString pour le ClickableText
        val annotatedLine = lineToAnnotatedString(lineString, hyperlinkList)

        //Afficher le texte.
        ClickableText(
            text = annotatedLine,
            onClick = {
                println("clicked on index $it")
                for(entry in hyperlinkList) {       //ajouter les liens
                    if(entry.startPos <= it && it <= entry.endPos) {        //si cette condition est rempli, un lien a été cliké
                        println("Ce lien a été clické : ${entry.hyperlink.link}")
                        navController.navigate(entry.hyperlink.link)        //naviguer vers la déstination du lien
                    }
                }
            },
            style = TextStyle(color = MaterialTheme.colors.onBackground)
        )
    }


    //Traiter une image et la transformer en Composable (Image)
    //Appelé par ProcessRawLine()
    @Composable
    private fun MakeImage(splittedLine : List<String>, navController: NavHostController) {
        if(splittedLine.size < 2) {
            Text("&IMG : nombre d'arguments invalide")
            return
        }

        val filePath = splittedLine[1]

        if(splittedLine.size == 4) {
            val width = splittedLine[2].toIntOrNull()
            val height = splittedLine[3].toIntOrNull()

            if(width == null || height == null) {
                ImageAsset(fileName = filePath, context)
            }
            else {
                ImageAsset(fileName = filePath, width, height, context)
            }
            return
        }

        ImageAsset(fileName = filePath, context)
    }
}