package com.onlyburger.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Small colored pill for order status and payment status values. */
@Composable
fun StatusBadge(value: String) {
    val (background, foreground) = when (value) {
        "Pending" -> Color(0xFFFFF6E0) to Color(0xFF8A6100)
        "Approved" -> Color(0xFFEAFAF0) to Color(0xFF1D6B3F)
        "Rejected" -> Color(0xFFFDECEC) to Color(0xFF9A2020)
        "Paid" -> Color(0xFFE9F3FB) to Color(0xFF1F5E94)
        "Unpaid" -> Color(0xFFF1F1EE) to Color(0xFF555555)
        else -> Color(0xFFEFEFEC) to Color(0xFF444444)
    }
    Surface(color = background, shape = RoundedCornerShape(50)) {
        Text(
            text = value,
            color = foreground,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}
