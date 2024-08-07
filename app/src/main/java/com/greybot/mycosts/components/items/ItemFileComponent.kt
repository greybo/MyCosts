package com.greybot.mycosts.components.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.greybot.mycosts.models.AdapterItems
import com.greybot.mycosts.models.MeasureType
import com.greybot.mycosts.present.adapter.AdapterCallback

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemFileComponent(
    model: AdapterItems.RowItem,
    isDelete: Boolean,
    callback: (AdapterCallback) -> Unit
) {
//    val highlight = remember { mutableStateOf(false) }
//    val bgColor = if (highlight.value) Color.Gray else Color.White
    val bgColor = if (model.highlight) Color.Gray else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp)
        ) {
            Checkbox(
                checked = model.isBought,
                onCheckedChange = { callback.invoke(AdapterCallback.RowBuy(model)) },
                modifier = Modifier.size(25.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .combinedClickable(
                    onClick = {
                        if (model.highlight || isDelete) {
                            callback.invoke(AdapterCallback.FileHighlight(model))
                        } else callback.invoke(AdapterCallback.RowName(model))
                    },
                    onLongClick = { callback.invoke(AdapterCallback.FileHighlight(model)) }
                )
        ) {
            Text(
                text = model.name,
                modifier = Modifier.padding(top = 12.dp, end = 8.dp, bottom = 12.dp)
            )
        }
        Text(
            text = model.count.toString(),
            modifier = Modifier
                .padding( end = 8.dp)
                .fillMaxWidth(0.3f)
                .clickable { callback.invoke(AdapterCallback.RowCount(model)) }
        )
        Text(
            text = model.price.toString(),
            modifier = Modifier
                .padding(top = 12.dp, end = 16.dp, bottom = 12.dp)
                .fillMaxWidth(0.5f)
                .clickable { callback.invoke(AdapterCallback.RowPrice(model)) }
        )

    }
}

@Preview
@Composable
fun PreviewFileComponent() {
    ItemFileComponent(
        model = getFakeFile(),
        isDelete = false,
        callback = {}
    )
}

fun getFakeFile(): AdapterItems.RowItem {
    return AdapterItems.RowItem(
        "",
        "Test",
        "count: 0",
        MeasureType.None,
        0.0,
        0.0,
        isBought = true
    )
}
