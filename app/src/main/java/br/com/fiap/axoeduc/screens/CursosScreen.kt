package br.com.fiap.axoeduc.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.axoeduc.viewmodel.CursosViewModel

data class Curso(
    val titulo: String,
    val progresso: Float,
    val url: String
)

@Composable
fun CursosScreen(viewModel: CursosViewModel = viewModel()) {

    val listaDeCursos by viewModel.cursos.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Cursos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        items(listaDeCursos) { curso ->

            val textoBotao = when {
                curso.progresso >= 1f -> "Concluído"
                curso.progresso == 0f -> "Iniciar"
                else -> "Continuar"
            }

            CursoCard(
                titulo = curso.titulo,
                progresso = curso.progresso,
                textoProgresso = "${(curso.progresso * 100).toInt()}%",
                textoBotao = textoBotao,
                url = curso.url
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CursoCard(
    titulo: String,
    progresso: Float,
    textoProgresso: String,
    textoBotao: String,
    url: String
) {

    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4F67C6)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = titulo,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = textoProgresso,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(12.dp))

                LinearProgressIndicator(
                    progress = { progresso },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp),
                    color = Color.White,
                    trackColor = Color(0xFF8FA1E6)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { uriHandler.openUri(url) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (progresso >= 1f) Color(0xFF4CAF50) else Color.White
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = textoBotao,
                    color = if (progresso >= 1f) Color.White else Color(0xFF1D4ED8),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CursosScreenPreview() {
    CursosScreen()
}