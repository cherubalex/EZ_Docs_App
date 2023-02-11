package com.example.ez_docs_app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

// Lien intéressant sur "Arrangement" :
//  https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/Arrangement


///////////////
//Constances

const val navBarHeight = 70
const val navBarPaddingOnSides = 20     // 10 pixels de chaques côtés


//////////////////////////////////////
//Classes et fonctions utilitaires

class NavBarElementDescriptor(title : String, navigationDest : String, icon : ImageVector) {
    val elementTitle = title
    val elementDest = navigationDest
    val elementIcon = icon
}

//Permet d'obtenir une couleur si l'élément est sélectionné (ou pas).
@Composable
fun getColorForSelection(selected : Boolean) : Color {
    if(selected) return MaterialTheme.colors.primaryVariant //Sélectionné
    return MaterialTheme.colors.primary                     //Pas sélectionné
}

///////////////////////////
// Composants


//Pour être utilisé dans un Scaffold
@Composable
fun navBar(navController : NavHostController, content : List<NavBarElementDescriptor>) {
    //Stocke le nom de la destination cliqué la plus récemment (premier élément de la liste par défaut)
    var selectedElem = rememberSaveable{ mutableStateOf(content[0].elementDest) }

    navBarShape {
        for(element in content) {
            //Ajouter tous les éléments un par un
            navBarItem(
                navBarElem = element,   //Obtenir le titre de l'élément
                //Déterminer la couleur de l'élément à partir de
                backgroundCol = getColorForSelection(element.elementDest == selectedElem.value),
                //Si l'élément est clické, il faut naviguer vers la destination
                onclick = {
                    selectedElem.value = element.elementDest        //stocker l'élément sélectionné
                    navController.navigate(element.elementDest)     //changer de page
                }
            )
        }
    }
}

// Correspond au rectangle de la barre de navigation avec tout son contenu.
@Composable
fun navBarShape(content: @Composable () -> Unit) {

    //L'élément "Box" correspond à la forme oval de la barre de navigation
    Box(
        modifier = Modifier
            .padding((navBarPaddingOnSides / 2).dp)
            .clip(CircleShape)
            .fillMaxWidth()
            .height(navBarHeight.dp)
            .background(MaterialTheme.colors.primary)
            .clickable(enabled = false, onClick = { /* rien */ })       //faire en sorte que l'on ne puisse pas clicker sur ce qu'il y a derrière la navbar
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),      //Faire en sorte que la ligne prenne tout l'espace
            horizontalArrangement = Arrangement.SpaceAround //Espacer les éléments uniformément
        ) {
            content()       //Mettre le contenu dans la ligne
        }
    }
}

//Élément de la barre de navigation.
@Composable
fun navBarItem(navBarElem : NavBarElementDescriptor, backgroundCol : Color, onclick : () -> Unit) {
    Box(
        modifier = Modifier
            .padding(5.dp)      //5 de chaque côté
            .clip(CircleShape)
            .size((navBarHeight - 10).dp)
            .background(backgroundCol)
            .clickable { onclick() },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),  //Faire en sorte que la colonne prenne tout l'espace
            horizontalAlignment = Alignment.CenterHorizontally,  //centrer les éléments horizontalement
            verticalArrangement = Arrangement.Center            //centrer verticalement
        ) {
            Icon(
                navBarElem.elementIcon, contentDescription = null, tint = MaterialTheme.colors.onPrimary
            )
            Text(navBarElem.elementTitle, fontSize = 12.sp, color = MaterialTheme.colors.onPrimary)     //afficher le texte
        }
    }
}

///////////////////////
// Prévisualisations

@Preview
@Composable
fun navBarItemPreview() {
    navBarItem(
        navBarElem = NavBarElementDescriptor("Text", "idk", Icons.Default.Home),
        backgroundCol = MaterialTheme.colors.primary,
        onclick = {}
    )
}

@Preview(widthDp = 500)
@Composable
fun navBarPreview() {
    navBarShape {
        navBarItemPreview()
        navBarItemPreview()
        navBarItemPreview()
        navBarItemPreview()
        navBarItemPreview()
    }
}