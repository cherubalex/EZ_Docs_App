package com.example.ez_docs_app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ez_docs_app.quiz.loadQuiz

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuizListPage(navController : NavHostController) {
    val context = LocalContext.current

    val quizList: Array<String>? = context.assets.list("quiz")

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        //Vérifier s'il y a des articles dans le dossier
        if (quizList != null) {
            //Ajouter des liens vers les articles un par un dans des ListItems.
            items(quizList) { quizFileName ->
                val quizTitre = context.assets.open("quiz/$quizFileName").bufferedReader().use { it.readLine() }
                ListItem(
                    text = { Text(text = quizTitre) },       //todo : ajouter un titre aux quiz ?
                    modifier = Modifier
                        .clickable {
                            navController.navigate("quiz/$quizFileName")
                        }
                )
                Divider()           //Séparateurs entre les éléments de la liste.
            }
        } else {
            item {
                Text(text = "Aucun article n'a été trouvé.")
            }
        }

        item {
            //faire en sorte de pouvoir scroller plus pour que le contenu ne soit pas sous la navbar
            Spacer(modifier = Modifier.height((navBarHeight + navBarPaddingOnSides).dp))
        }
    }
}


@Composable
fun QuizPage(quizName: String?, navController: NavHostController, topTitle : MutableState<String>) {
    val context = LocalContext.current

    if (quizName.isNullOrBlank()) {
        Text(text = "Erreur")
        return
    }

    //fixme : ici le fichier du quiz est ouvert 2 fois, c'est pas optimal.
    //Recharger le titre
    val quizTitle = context.assets.open("quiz/$quizName").bufferedReader().use { it.readLine() }
    topTitle.value = quizTitle

    //Traiter le fichier texte et obtenir les questions
    val questions = loadQuiz("quiz/$quizName", LocalContext.current)

    //L'index de la question qui est affiché actuellement
    //(modifié depuis Question.MakeComposable())
    val currentQuestion = rememberSaveable { mutableStateOf(0) }
    //Compter les questions bonnes
    val score = rememberSaveable { mutableStateOf(0) }

    Column {
        //Afficher la question correspondant à l'index
        if(currentQuestion.value < questions.size) {
            questions[currentQuestion.value].MakeComponant(
                currentQuestion,
                score
            )
        }
        else {
            Text("Vous avez terminé le quiz avec un score de ${score.value}/${questions.size}.")

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        //Retourner là où était l'utilisateur précédemment
                        //La condition permet de vérifier si l'utilisateur était sur une page avant.
                        //Plus de détails : https://stackoverflow.com/a/75269457
                        if(navController.backQueue.size > 2) {
                            navController.popBackStack()
                        }
                    }
                ) {
                    Text(text = "Quitter le quiz")
                }
            }   //Row
        }
    }   //Column
}
