package com.example.ez_docs_app

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

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


///////////////////////////
// Composants


//Pour être utilisé dans un Scaffold
@Composable
fun navBar(navController : NavHostController, content : List<NavBarElementDescriptor>) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    ) {
        //Stocke la position actuelle
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.value?.destination?.route

        for(element in content) {
            BottomNavigationItem(
                selected = (currentRoute == element.elementDest),
                alwaysShowLabel = true,
                selectedContentColor = MaterialTheme.colors.onPrimary,
                unselectedContentColor = MaterialTheme.colors.onPrimary.copy(0.5f),
                label = { Text(element.elementTitle)},
                icon = { Icon(imageVector = element.elementIcon, contentDescription = "") },
                onClick = { navController.navigate(element.elementDest) }
            )
        }
    }

}
