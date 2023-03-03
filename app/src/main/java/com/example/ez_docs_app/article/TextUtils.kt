package com.example.ez_docs_app.article

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

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

//La fonction prend en entrée une chaine de charactère et
//retourne un AnnotatedString (chaine de caractère contenant des informations sur le style).
//L'AnnotatedString peut être utilisé dans un ClickableText.
//note : La fonction écrit dans hyperlinkEntries afin d'y mettre les liens "[]()",
//cette liste doit être initialisé (et vide) avant l'appelle de la fonction.
fun lineToAnnotatedString(line : String, hyperlinkEntries : MutableList<HyperlinkEntry>) : AnnotatedString {
    return buildAnnotatedString {
        var i = 0                       //index du charactère que l'on est en train de traiter dans la ligne
        var charCount = 0               //nombre de charactères de la ligne affiché sur la page
        var postApplySubTitle = false   //true si la ligne est un sous titre

        var boldBegin = -1              //si un premier '*' est rencontré, l'index du charactère est affecté à cet variable.
        var doBold = false              //true si un premier '*' est rencontré. Repassé sur false quand un deuxième est rencontré.

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
            else if(c == '*') {             //gras
                if(doBold == false) {       //premier '*' rencontré
                    boldBegin = charCount   //enregistrer la position dans le texte
                    doBold = true
                }
                else {                      //deuxième '*' rencontré
                    addStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        boldBegin,
                        charCount
                    )
                    doBold = false
                }
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