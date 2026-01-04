package com.example.wotstats.view.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wotstats.R

@Composable
fun BottomNavButton(
    text: String,
    selected: Boolean
) {
    val borderColor = if (selected) colorResource(R.color.white) else colorResource(R.color.black)
    val background = if (selected) colorResource(R.color.black) else colorResource(R.color.white)
    val content = if (selected) colorResource(R.color.white) else colorResource(R.color.black)

    Box(
        modifier = Modifier
            .width(140.dp)
            .height(44.dp)
            .clip(RoundedCornerShape(22.dp))
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(22.dp)
            )
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = content,
            fontWeight = FontWeight.Medium
        )
    }
}