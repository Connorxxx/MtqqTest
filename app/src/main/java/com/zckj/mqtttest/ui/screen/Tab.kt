package com.zckj.mqtttest.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TabTest() {
    var state by remember { mutableStateOf(0) }
    val titles = remember { mutableStateListOf("TAB 1", "TAB 2", "TAB 3") }
    Column {
        Row(Modifier.fillMaxWidth()) {
            ScrollableTabRow(
                selectedTabIndex = state,
                edgePadding = 3.dp,
                modifier = Modifier.weight(3f)
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = state == index,
                        onClick = { state = index },
                        text = { Text(text = title) })
                }
            }
            Button(
                onClick = {
                    titles.add("TAB ${titles.size + 1}")
                    state = titles.size - 1
                },
                Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = "add")
            }
        }
        Another(hiltViewModel(key = "$state"))
    }
}