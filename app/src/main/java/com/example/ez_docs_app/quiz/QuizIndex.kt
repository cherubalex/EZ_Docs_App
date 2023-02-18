package com.example.ez_docs_app.quiz

import android.content.Context

fun isReponseLine(line : String) : Boolean {
    if(line.isBlank()) {
        return false
    }

    if(line[0] != ' ') {
        return false
    }

    return true
}

fun loadQuiz(quizFileName: String, context: Context) : List<Question> {
    val fileLines: List<String>
    context.assets.open(quizFileName).bufferedReader().use {
        fileLines = it.readText().split('\n')
    }

    val r = mutableListOf<Question>()

    var currentLine = 0
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

        //Ajouter la question à la liste.
        r.add(Question(strQuestion, reponses))
    }

    return r
}
