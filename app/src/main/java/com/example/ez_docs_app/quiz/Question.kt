package com.example.ez_docs_app.quiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

class Question(val strQuestion: String, val reponses: List<String>) {
    //currentQuestion est dans le composable QuizPage
    //questionCount correspond au nombre de question dans la List<Question> de QuizPage
    //navController permet de retourner à la liste de questions quand l'utilisateur à répondu à toutes les questions du quiz.
    @Composable
    fun MakeComponant(currentQuestion : MutableState<Int>, score : MutableState<Int>) {
        //Afficher la question en titre
        Text(text = strQuestion, fontSize = 24.sp)

        //Conserver les états
        val checkboxesStates = mutableListOf<MutableState<Boolean>>()
        val colorStates = mutableListOf<MutableState<Int>>()        // 0 : defaut , 1 : vert , 2 : rouge
        val haveConfirmedOnce = rememberSaveable { mutableStateOf(false) }

        //afficher les propositions de réponses une par une
        for(i in 0..reponses.size-1) {
            //Initialiser l'état des checkbox et des couleurs de texte
            checkboxesStates.add(rememberSaveable { mutableStateOf(false) })
            colorStates.add(rememberSaveable { mutableStateOf(0) })

            //Afficher les réponses avec une checkbox à coté
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkboxesStates[i].value,
                    enabled = !haveConfirmedOnce.value,
                    onCheckedChange = {
                        checkboxesStates[i].value = it
                        println(i)
                    }
                )
                Text(
                    text = reponses[i].trim('#',),      //retirer le #
                    color = getColorFromInt(colorStates[i].value),
                    modifier = Modifier.clickable(enabled = !haveConfirmedOnce.value) { //rendre le texte clickable
                        checkboxesStates[i].value = !checkboxesStates[i].value
                    }
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            //Afficher le bouton confirmer si l'utilisateur n'a pas encore confirmer
            if(!haveConfirmedOnce.value) {
                Button(
                    onClick = {
                        var correct = true  //supposer que l'utilisateur a bon

                        //vérifier si les réponses sont bonnes
                        for (i in 0..colorStates.size - 1) {
                            if (reponses[i][0] == '#') {     //si c'est une bonne réponse...
                                if (checkboxesStates[i].value) {
                                    colorStates[i].value = 1    //la mettre en vert si elle est coché
                                } else {
                                    colorStates[i].value = 2    //la mettre en rouge si elle est pas coché
                                    correct = false
                                }
                            } else {  //Pas de # en premier charactère (pas la bonne réponse)
                                if (checkboxesStates[i].value) {
                                    colorStates[i].value = 2    //la mettre en rouge si elle est coché
                                    correct = false
                                } else {
                                    colorStates[i].value = 0    //sinon laisser sa couleur par défaut
                                }
                            }
                        }
                        if(correct) score.value += 1
                        haveConfirmedOnce.value = true
                    }   //onClick = {}
                ) {
                    Text(text = "Confirmer")
                }   //Button
            }
            //Afficher le bouton suivant si l'utilisateur a confirmé au moins une fois sa réponse
            else {
                Button(
                    onClick = {
                        println(checkboxesStates.size)
                        //"oublier" les trucs remember
                        haveConfirmedOnce.value = false
                        for (element in checkboxesStates) {
                            element.value = false
                        }
                        for (element in colorStates) {
                            element.value = 0
                        }

                        //passer à la question suivante
                        currentQuestion.value++
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                ) {
                    Text(text = "Suivant")
                }   //Button
            }   //else
        }   //Row
    }   //MakeComponant()


    //Permet d'obtenir les couleurs des réponses, après validation
    // 0 : defaut , 1 : vert , 2 : rouge
    @Composable
    private fun getColorFromInt(number : Int) : Color {
        if(number == 1) return Color.Green
        else if(number == 2) return Color.Red
        else return MaterialTheme.colors.onBackground
    }

}   //class Question