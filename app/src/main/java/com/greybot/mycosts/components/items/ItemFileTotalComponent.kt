package com.greybot.mycosts.components.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.greybot.mycosts.models.AdapterItems
import com.greybot.mycosts.theme.MyCostsTypography

@Composable
fun ItemFileTotalComponent(model: AdapterItems.TotalItem) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shadowElevation = 2.dp,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .padding(start = 16.dp, end = 8.dp)
            ) {
                Text(
                    text = model.name1,
                    style = MyCostsTypography.labelLarge,
                    modifier = Modifier
                )
                Text(
                    text = model.value1,
                    style = MyCostsTypography.bodySmall,
                    modifier = Modifier
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = model.name2,
                    style = MyCostsTypography.labelLarge
                )
                Text(
                    text = model.value2,
                    style = MyCostsTypography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewItemFileTotalComponent() {
    ItemFileTotalComponent(
        model = AdapterItems.TotalItem("Order", "23.1 hrn", "Bay", "25.52 hrn"),
        )
}