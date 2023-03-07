package com.example.ez_docs_app.quiz

import android.content.Context

fun isReponseLine(line : String) : Boolean {
    if(line.isBlank()) {
        return false
    }

    if(line[0] != ' ') {
        return false
    }

    if(line.trim()[0] == '%') {
        return false
    }

    return true
}

fun isExplainationLine(line : String) : Boolean {
    if(line.isBlank()) {
        return false
    }

    return (line.trim()[0] == '%')
}

fun loadQuiz(quizFileName: String, context: Context) : List<Question> {
    val fileLines: List<String>
    context.assets.open(quizFileName).bufferedReader().use {
        fileLines = it.readText().split('\n')
    }

    val r = mutableListOf<Question>()

    var currentLine = 1     //1 pour skip la ligne du titre
    while(currentLine < fileLines.size) {
        //Skip toutes les lignes vides
        while (currentLine < fileLines.size && fileLines[currentLine].trim().isBlank()) {
            currentLine++
        }

        if(currentLine >= fileLines.size) {
            break
        }

        //Obtenir la question
        val strQuestion = fileLines[currentLine]
        currentLine++


        val reponses = mutableListOf<String>()

        //obtenir les réponses
        while(
            currentLine < fileLines.size &&
            isReponseLine(fileLines[currentLine]) &&
            !fileLines[currentLine].trim().isBlank()
        ) {
            reponses.add(fileLines[currentLine].trim())
            currentLine++
        }

        //obtenir l'explication (s'il y en a une)
        val explaination : String?
        if(currentLine < fileLines.size && isExplainationLine(fileLines[currentLine])) {
            explaination = fileLines[currentLine].trim(' ', '%')
            currentLine++
        }
        else {
            explaination = null
        }

        //Ajouter la question à la liste.
        r.add(Question(strQuestion, reponses, explaination))
    }

    return r
}
