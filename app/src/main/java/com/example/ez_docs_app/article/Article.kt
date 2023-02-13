package com.example.ez_docs_app.article

import android.content.Context
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ez_docs_app.ImageAsset


//text correspond au texte afficher dans l'article
//link correspond à la destination envoyé au NavHostController quand le lien est clické
class HyperlinkObject(val text : String, val link : String) {
    fun calculateRawStringLenght() : Int {
        return  text.length + link.length + 4   // 4 : "[]()"
    }
}

// Retourne l'index de la première occurrence du charactère c dans la chaine s à partir de startIndex (inclus)
// Retourne -1 si c n'est pas présent dans s
fun searchCharFrom(s : String, c : Char, startIndex : Int) : Int {
    for(i in startIndex..s.length-1) {
        if(s[i] == c) {
            return i
        }
    }
    return -1
}

// Traiter un lien interactif de la forme "... []() ..."
// startIndex doit correspondre à l'index de '{' dans la chaine.
fun parseHyperLink(s: String, startIndex : Int) : HyperlinkObject? {
    if(s[startIndex] != '[') {
        return null
    }

    val textEndIndex = searchCharFrom(s, ']', startIndex+1)
    if(textEndIndex == -1) {
        return null
    }

    if(s[textEndIndex+1] != '(') {
        return null
    }
    val linkEndIndex = searchCharFrom(s, ')', textEndIndex+2)
    if(linkEndIndex == -1) {
        return null
    }

    val text = s.substring(startIndex+1, textEndIndex)      // [startIndex+1 ; textendindex[
    val link = s.substring(textEndIndex+2, linkEndIndex)    // [textEndIndex+2 ; linkEndIndex[
    return HyperlinkObject(text, link)
}

//Entrée d'un lien présent dans l'article, possède le HyperlinkObject correspondant,
//l'index du premier charactère du lien et l'index du dernier charactère du lien.
class HyperlinkEntry(val hyperlink : HyperlinkObject, val startPos : Int, val endPos : Int)


//note : La fonction écrit dans hyperlinkEntries
fun lineToAnnotatedString(line : String, hyperlinkEntries : MutableList<HyperlinkEntry>) : AnnotatedString {
    return buildAnnotatedString {
        var i = 0                       //index du charactère que l'on est en train de traiter dans la ligne
        var charCount = 0               //nombre de charactères de la ligne affiché sur la page
        var postApplySubTitle = false   //true si la ligne est un sous titre

        while(i < line.length) {
            val c = line[i]
            if(c == '[') {      //lien ?
                //traiter le lien
                val hyperlink = parseHyperLink(line, i)

                if (hyperlink == null) {    //echec du traitement du lien
                    append(c)       //ajouter le charactère comme si de rien était (sur un malentendu ça passe)
                    i++             //passer au charactère suivant
                    charCount++
                }
                else {      //Succès
                    //Ajouter le texte du lien
                    append(hyperlink.text)
                    addStyle(           //style du texte
                        style = SpanStyle(
                            textDecoration = TextDecoration.Underline
                        ),
                        charCount,                              //index du charactère de début dans le texte
                        charCount + hyperlink.text.length   //index du charactère de fin dans le texte
                    )

                    //entrée qui va être ajouté dans la liste de tous les liens clickables de l'article
                    val entry = HyperlinkEntry(
                        hyperlink,
                        charCount,                                  //index du charactère de début dans le texte
                        charCount + hyperlink.text.length    //index du charactère de fin dans le texte
                    )
                    hyperlinkEntries.add(entry)

                    i += hyperlink.calculateRawStringLenght()
                    charCount += hyperlink.text.length
                }
            }
            else if(c == '#' && i == 0) {   //sous titre
                postApplySubTitle = true
                i++
            }
            else {      //charactère normal
                append(c)
                i++         //passer au charactère suivant
                charCount++
            }
        }

        if(postApplySubTitle) {
            addStyle(
                style = SpanStyle(
                    fontSize = 20.sp
                ),
                0,
                charCount
            )
        }
    }
}


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
        //println(title)
        Text(text = title, fontSize = 26.sp)

        var rawTextIndex = 0    //Endroit du contenu que l'on est en trein de traiter

        //traiter l'article ligne par ligne
        while(rawTextIndex < rawContent.length) {
            //Obtenir la ligne
            var EOLIndex = searchCharFrom(rawContent, '\n', rawTextIndex)   //index de la fin de ligne de la ligne actuel
            if(EOLIndex == -1) EOLIndex = rawContent.length
            val currentLine = rawContent.substring(rawTextIndex, EOLIndex)

            //Traiter la ligne et la transformer en Composable
            ProcessRawLine(currentLine, navController)

            //passer à la ligne suivante (aka calculer l'index du premier charactère de la ligne suivante)
            rawTextIndex = EOLIndex + 1  //+1 pour skip le charactère '\n'
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
                for(entry in hyperlinkList) {
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