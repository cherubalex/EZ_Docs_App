package com.example.ez_docs_app.article

class ArticleIndexEntry(val displayName : String, val internalName : String)

val articlesIndex = listOf<ArticleIndexEntry>(
    ArticleIndexEntry("Article test", "testarticle"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine"),
    ArticleIndexEntry("Chocolatine", "chocolatine")

)

fun getArticleWithName(name : String?) : Article {
    if(name == "testarticle") {
        return Article(
            "Titre",
            "Ceci est le contenu de l'article.\n" +
                    "J'aime les pates et {chocolatine}[article/chocolatine], supérieur aux pains aux chocolats qui eux sont médiocres"
        )
    }
    else if(name == "chocolatine") {
        return Article(
            "Chocolatine",
            "Ceci est un article ayant pour but de faire l'éloge des chocolatines."
        )
    }


    return Article("Article invalide", "L'article avec le nom $name n'existe pas :(")
}