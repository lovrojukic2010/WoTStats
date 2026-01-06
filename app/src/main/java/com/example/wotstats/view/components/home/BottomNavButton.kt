package com.example.wotstats.view.components.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wotstats.R
import com.example.wotstats.view.navigation.Screen

@Composable
fun BottomNavButton(
    text: String,
    selected: Boolean,
    navController: NavController
) {
    val borderColor = if (selected) colorResource(R.color.white) else colorResource(R.color.black)
    val background = if (selected) colorResource(R.color.black) else colorResource(R.color.white)
    val content = if (selected) colorResource(R.color.white) else colorResource(R.color.black)

    Button(
        modifier = Modifier
            .width(140.dp)
            .height(44.dp)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(22.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            contentColor = content,
            containerColor = background
        ),
        onClick = {
            if (!selected) {
                if (text == "TANKS") {
                    navController.navigate(Screen.HomeScreen.route)
                } else {
                    navController.navigate(Screen.FavoritesScreen.route)
                }
            }
        },
    ) {
        Text(
            text = text,
            color = content,
            fontWeight = FontWeight.Medium
        )
    }
}