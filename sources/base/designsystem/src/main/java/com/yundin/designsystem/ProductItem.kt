package com.yundin.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.yundin.designsystem.theme.TangoDemoTheme
import java.math.BigDecimal

@Composable
fun ProductItem(
    name: String,
    imageUrl: String,
    manufacturer: String,
    price: BigDecimal
) {
    ProductItemContent(
        name = name,
        manufacturer = manufacturer,
        price = price.setScale(2).toPlainString()
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null
        ) {
            val state = painter.state
            if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                ProductPlaceholder()
            } else {
                SubcomposeAsyncImageContent()
            }
        }
    }
}

@Composable
fun ProductItemContent(
    name: String,
    manufacturer: String,
    price: String,
    imageSlot: @Composable (ColumnScope) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            imageSlot(this)
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.h6,
                )
                Text(
                    text = manufacturer,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.alpha(0.5f)
                )
                Text(
                    text = price,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}

@Composable
private fun ProductPlaceholder() {
    Box(
        modifier = Modifier.background(Color.Gray).fillMaxWidth().aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(0.8f).aspectRatio(1f),
            painter = painterResource(id = R.drawable.ic_placeholder_icon),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun ProductItemPreview() {
    TangoDemoTheme {
        Surface {
            ProductItem(
                name = "Product name",
                imageUrl = "",
                manufacturer = "Product manufacturer",
                price = BigDecimal.ONE
            )
        }
    }
}
