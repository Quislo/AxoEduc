package br.com.fiap.axoeduc.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.axoeduc.model.Certificado
import br.com.fiap.axoeduc.viewmodel.CertificadoViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

val AzulCard = Color(0xFF3F3DCE)

@Composable
fun CertificadosScreen(
    onProfileClick: () -> Unit,
    onCursosClick: () -> Unit,
    onFerramentasClick: () -> Unit,
    onCertificadosClick: () -> Unit
) {

    val viewModel: CertificadoViewModel = viewModel()

    val listaCertificados by viewModel.certificados.collectAsState()

    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {

        Text(
            text = "Certificados",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            items(listaCertificados) { certificado ->
                CardCertificado(certificado)            }

        }
    }
}


@Composable
fun CardCertificado(certificado: Certificado) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AzulCard
        )
    ) {

        Column(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            Text(
                text = certificado.titulo,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (certificado.desbloqueado) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF9EA0FF)
                        )
                    ) {
                        Text("Visualizar", color = Color.Black)
                    }

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB8B9FF)
                        )
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
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        ),
                        modifier = Modifier
                            .width(150.dp)
                    ) {
                        Text("🔒")
                    }
                }


            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CertificadosScreenPreview() {

    CertificadosScreen(
        onProfileClick = {},
        onCursosClick = {},
        onFerramentasClick = {},
        onCertificadosClick = {}
    )
}