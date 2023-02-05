package com.example.ez_docs_app.article

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

class Article(private val title : String, private val rawContent: String) {
    //contient la liste des liens présents dans l'article
    private val hyperlinkList = mutableListOf<HyperlinkEntry>()

    //traiter le texte brut de l'article
    private val annotatedContent : AnnotatedString = buildAnnotatedString {
        var i = 0               //nombre de charactères de rawContent traité
        var charCount = 0       //nombre de charactères affiché sur la page

        while(i < rawContent.length) {
            val c = rawContent[i]       //récupérer le charactère

            if(c == '[') {      //lien ?
                //traiter le lien
                val hyperlink = parseHyperLink(rawContent, i)

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
                    hyperlinkList.add(entry)

                    i += hyperlink.calculateRawStringLenght()
                    charCount += hyperlink.text.length
                }
            }
            else if(c == '#') {      //titre
                if(i > 0 && rawContent[i-1] == '\n') {
                    val endLineIndex = searchCharFrom(rawContent, '\n', i)
                    val lineLenght = endLineIndex - i       //taille de la ligne dans le rawContent

                    // i+1 pour ne pas inclure le '#'
                    //endLineIndex est exclut de l'interval, donc le '\n' n'est pas pris en compte.
                    val lineContent = rawContent.substring(i + 1, endLineIndex)
                    append(lineContent)
                    addStyle(
                        style = SpanStyle(
                            fontSize = 20.sp
                        ), charCount, charCount + lineLenght - 1
                    )

                    i += lineLenght
                    charCount += lineLenght - 1    //-1 pour ne las compter le '#'
                }
                else {
                    append(c)
                    i++         //passer au charactère suivant
                    charCount++
                }
            }
            else {      //charactère normal
                append(c)
                i++         //passer au charactère suivant
                charCount++
            }
        }
    }

    //créé un composable à partir de l'article.
    //note : le composable créé en lui-même n'est pas scrollable, il faut donc que le parent
    //       de celui-ci gère le scrolling.
    @Composable
    fun MakeComponent(navController: NavHostController) {
        println(title)
        Text(text = title, fontSize = 26.sp)
        //Text(text = content)
        ClickableText(
            text = annotatedContent,
            onClick = {     //"it" correspond à l'index du caractère qui a été clické
                println("clicked $it")
                for(entry in hyperlinkList) {   //regarder dans la liste des liens si un lien a été clické
                    if(entry.startPos <= it && it <= entry.endPos) {        //si cette condition est rempli, un lien a été cliké
                        println("Ce lien a été clické : ${entry.hyperlink.link}")
                        navController.navigate(entry.hyperlink.link)        //naviguer vers la déstination du lien
                    }
                }
            },
            style = TextStyle(color = MaterialTheme.colors.onBackground)
        )

    }
}