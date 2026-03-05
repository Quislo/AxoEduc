package br.com.fiap.axoeduc.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CursosScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))


        Text(
            text = "Cursos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(24.dp))


        CursoCard(
            titulo = "Meu Primeiro Orçamento",
            progresso = 0.25f,
            textoProgresso = "25%",
            textoBotao = "Continuar"
        )

        CursoCard(
            titulo = "Gestão pessoal",
            progresso = 1.0f,
            textoProgresso = "100%",
            textoBotao = "Continuar"
        )

        CursoCard(
            titulo = "Investimentos a longo prazo",
            progresso = 0.0f,
            textoProgresso = "0%",
            textoBotao = "Iniciar"
        )

        CursoCard(
            titulo = "Renda Extra sem sair de casa",
            progresso = 0.60f,
            textoProgresso = "60%",
            textoBotao = "Continuar"
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun CursoCard(titulo: String, progresso: Float, textoProgresso: String, textoBotao: String) {
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
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = textoBotao,
                    color = Color(0xFF1D4ED8),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}