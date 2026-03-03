package br.com.fiap.axoeduc.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun BottomMenuPreview() {
    BottomMenu(
        onCursosClick = {},
        onFerramentasClick = {},
        onCertificadosClick = {}
    )
}

@Composable
fun BottomMenu(
    onCursosClick: () -> Unit,
    onFerramentasClick: () -> Unit,
    onCertificadosClick: () -> Unit
) {
    val backgroundColor = Color(0xFF4F67C6)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MenuItem(
                icon = Icons.Default.School,
                text = "Cursos",
                onClick = onCursosClick
            )
            MenuItem(
                icon = Icons.Default.Build,
                text = "Ferramentas",
                onClick = onFerramentasClick
            )
            MenuItem(
                icon = Icons.Default.Badge,
                text = "Certificados",
                onClick = onCertificadosClick
            )
        }
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}