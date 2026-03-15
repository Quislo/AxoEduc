package br.com.fiap.axoeduc.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.axoeduc.model.Certificado
import br.com.fiap.axoeduc.viewmodel.CertificadoViewModel

val AzulCard = Color(0xFF3F3DCE)

@Composable
fun CertificadosScreen(
    nomeUsuario: String,
    onProfileClick: () -> Unit,
    onCursosClick: () -> Unit,
    onFerramentasClick: () -> Unit,
    onCertificadosClick: () -> Unit
) {
    val viewModel: CertificadoViewModel = viewModel()
    val listaCertificados by viewModel.certificados.collectAsState()

    val context = LocalContext.current
    var certificadoParaVisualizar by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Text(
            text = "Certificados",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(listaCertificados) { certificado ->
                CardCertificado(
                    certificado = certificado,
                    onVisualizar = {
                        certificadoParaVisualizar = certificado.titulo
                    },
                    onImprimir = {
                        Toast.makeText(context, "Gerando PDF para impressão...", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    certificadoParaVisualizar?.let { tituloCurso ->
        CertificadoDialogBonitao(
            nomeAluno = nomeUsuario,
            nomeCurso = tituloCurso,
            onDismiss = { certificadoParaVisualizar = null }
        )
    }
}


@Composable
fun CardCertificado(
    certificado: Certificado,
    onVisualizar: () -> Unit,
    onImprimir: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AzulCard)
    ) {
        Column(
            modifier = Modifier.padding(20.dp).align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = certificado.titulo,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (certificado.desbloqueado) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onVisualizar, // Abre o popup
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9EA0FF))
                    ) {
                        Text("Visualizar", color = Color.Black)
                    }

                    Button(
                        onClick = onImprimir, 
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB8B9FF))
                    ) {
                        Text("Imprimir", color = Color.Black)
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {},
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text("🔒")
                    }
                }
            }
        }
    }
}

@Composable
fun CertificadoDialogBonitao(
    nomeAluno: String,
    nomeCurso: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().height(380.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFBF7)),
            border = BorderStroke(4.dp, Color(0xFFD4AF37))
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "🏆", fontSize = 54.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "CERTIFICADO DE CONCLUSÃO",
                    fontSize = 20.sp, fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF3F3DCE), textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Certificamos com honra que",
                    fontSize = 14.sp, color = Color.DarkGray, textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = nomeAluno,
                    fontSize = 22.sp, fontWeight = FontWeight.Bold,
                    color = Color.Black, textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "concluiu com êxito o curso:",
                    fontSize = 14.sp, color = Color.DarkGray, textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = nomeCurso,
                    fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    color = Color(0xFF3F3DCE), textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F3DCE))
                ) {
                    Text("Fechar")
                }
            }
        }
    }
}