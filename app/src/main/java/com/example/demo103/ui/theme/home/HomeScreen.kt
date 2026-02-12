package com.example.demo103.ui.theme.home

import android.R.attr.padding
import android.icu.util.DateInterval
import android.text.format.DateUtils.isToday
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.util.Date
import java.util.Locale


@Composable
fun HomeScreen(
    modifier: Modifier= Modifier,
) {
    val currentDate = LocalDate.now()

    val monthName = currentDate.month.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
    Surface(
        modifier=Modifier.fillMaxSize(),
        color = Color.Black
        ) {
Column(
    modifier= Modifier.fillMaxSize()
        .background(Color.Black)
){
    Box(
        modifier=Modifier
            .fillMaxSize()
        .padding(top = 30.dp, start = 8.dp, end = 8.dp),
        contentAlignment = Alignment.TopStart

    ){
    Text(
        text=monthName,
        color = Color.White,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
}

DateRowList(

)

}
    }

}

@Composable
fun DateRowList(){
    val dateList = remember{
        (7 downTo 0).map{LocalDate.now().minusDays(it.toLong()) }
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)

    ) {
        items(
            dateList
        ) { date ->
            DateItem(
                day = date.dayOfMonth.toString(),
                isToday = date == LocalDate.now()
            )
        }
    }
}

@Composable
fun DateItem(day: String, isToday: Boolean) {
    Card(
        modifier = Modifier.size(60.dp),
        colors = CardDefaults.cardColors(
            // Highlight today's date with a different color
            containerColor = if (isToday) Color(0xFF6200EE) else Color.DarkGray
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

