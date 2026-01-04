package com.example.wotstats.view.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wotstats.R

@Composable
fun TierFilterButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val background = if (selected) colorResource(R.color.black) else colorResource(R.color.white)
    val content = if (selected) colorResource(R.color.white) else colorResource(R.color.black)
    val alpha = if (selected) 1f else 0.6f

    Box(
        modifier = Modifier
            .size(width = 48.dp, height = 32.dp)
            .alpha(alpha)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, colorResource(R.color.black), RoundedCornerShape(16.dp))
            .background(background)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = content,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}