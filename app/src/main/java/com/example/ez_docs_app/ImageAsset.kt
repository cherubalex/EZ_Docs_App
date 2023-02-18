package com.example.ez_docs_app

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import java.io.FileNotFoundException


fun LoadDrawableFromAsset(fileName: String, context: Context) : Drawable? {
    val fileNameTrim = fileName.trim()

    val drawableFromFile : Drawable?

    try {
        //Charger le fichier stockant l'image.
        val imgFile = context.assets.open(fileNameTrim)

        //Lire l'image depuis le fichier.
        drawableFromFile = Drawable.createFromStream(imgFile, null)

        //createFromStream() ne ferme pas automatiquement le fichier, donc il faut le faire manuellement.
        imgFile.close()
    }
    catch (e: FileNotFoundException) {
        return null
    }

    return drawableFromFile
}

//Affiche l'image avec une taille choisi "par défaut" par l'appelle de drawableFromFile.toBitmap()
//Cette taille par défaut correspond à la taille "intrinsèque" (aucune idée de ce que ça veut dire).
@Composable
fun ImageAsset(fileName : String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    //Charger l'image depuis le fichier
    val drawableFromFile = LoadDrawableFromAsset(fileName, context)

    //Vérifier si l'image a été chargé correctement.
    if(drawableFromFile != null) {
        //Convertir le Drawable en BitmapImage.
        val bitmapImage = drawableFromFile.toBitmap().asImageBitmap()

        //afficher l'image
        Image(bitmapImage, "description", modifier = modifier)   //affiche l'image avec une taille par "défaut".
        //Image(bitmap = (drawableFromFile as BitmapDrawable).bitmap.asImageBitmap(), "fd")     //affiche l'image dans sa vrai taille.
    }
    else {
        Text(text = "[Image $fileName n'a pas pu etre chargé]", color = Color.Red)
    }
}

//Change la résolution de l'image à la taille width*height puis affiche l'image.
@Composable
fun ImageAsset(
    fileName : String,
    width : Int,
    height : Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    //Charger l'image depuis le fichier
    val drawableFromFile = LoadDrawableFromAsset(fileName, context)

    //Vérifier si l'image a été chargé correctement.
    if(drawableFromFile != null) {
        //Convertir le Drawable en BitmapImage.
        val bitmapImage = drawableFromFile.toBitmap(width, height).asImageBitmap()

        //afficher l'image
        Image(bitmapImage, "description", modifier = modifier)   //affiche l'image avec une taille par "défaut".
        //Image(bitmap = (drawableFromFile as BitmapDrawable).bitmap.asImageBitmap(), "fd")     //affiche l'image dans sa vrai taille.
    }
    else {
        Box(modifier = modifier.background(Color.Red))
    }
}