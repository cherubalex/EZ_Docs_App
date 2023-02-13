package com.example.ez_docs_app.article

//text correspond au texte afficher dans l'article
//link correspond à la destination envoyé au NavHostController quand le lien est clické
//Cette classe est utilisé dans lineToAnnotatedString()
class HyperlinkObject(val text : String, val link : String) {
    fun calculateRawStringLenght() : Int {
        return  text.length + link.length + 4   // 4 : "[]()"
    }
}

//Entrée d'un lien présent dans l'article, possède le HyperlinkObject correspondant,
//l'index du premier charactère du lien et l'index du dernier charactère du lien.
//(En gros ça associe un entroit du ClickableText à un lien lors de la création de celui-ci.)
//Cette classe est utilisé dans lineToAnnotatedString() et dans Article.MakeLine()
class HyperlinkEntry(val hyperlink : HyperlinkObject, val startPos : Int, val endPos : Int)

// Traiter un lien interactif de la forme "... []() ..."
// startIndex doit correspondre à l'index de '[' dans la chaine.
fun parseHyperLink(s: String, startIndex : Int) : HyperlinkObject? {
    if(s[startIndex] != '[') {
        return null
    }

    val textEndIndex = searchCharFrom(s, ']', startIndex+1)
    if(textEndIndex == -1) {
        return null
    }

    //if(s[textEndIndex+1] != '(') {
    if(s.getOrNull(textEndIndex+1) != '(') {
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
