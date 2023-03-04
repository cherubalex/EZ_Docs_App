package com.example.ez_docs_app.article

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ez_docs_app.ImageAsset
import com.example.ez_docs_app.navBarHeight
import com.example.ez_docs_app.navBarPaddingOnSides


class Article(
    private val title : String,
    private val rawContent: String
    ) {
    //créé un composable à partir de l'article.
    //note : le composable créé en lui-même n'est pas scrollable, il faut donc que le parent
    //       de celui-ci gère le scrolling.
    @Composable
    fun MakeComponent(navController: NavHostController) {
        //"Découper" l'article ligne par ligne.
        val rawContentLines = rawContent.split("\n")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp)
        ) {
            //Afficher le titre de l'article
            item { Text(text = title, fontSize = 26.sp) }

            //traiter l'article ligne par ligne
            items(rawContentLines) { currentLine ->
                ProcessRawLine(currentLine, navController)
            }

            item {
                //faire en sorte de pouvoir scroller plus pour que le contenu ne soit pas sous la navbar
                Spacer(modifier = Modifier.height((navBarHeight + navBarPaddingOnSides).dp))
            }
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
        val annotatedLine = lineToAnnotatedString(lineString, hyperlinkList, LocalContext.current)

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
                ImageAsset(fileName = filePath)
            }
            else {
                //ImageAsset(fileName = filePath, width, height)
                ImageAsset(
                    fileName = filePath,
                    width, height,
                    modifier = Modifier
                        .width(width.dp).height(height.dp)
                )
            }
            return
        }

        ImageAsset(fileName = filePath)
    }
}