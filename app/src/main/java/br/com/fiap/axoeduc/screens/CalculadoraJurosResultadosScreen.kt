package br.com.fiap.axoeduc.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.axoeduc.R
import br.com.fiap.axoeduc.components.CustomTopBar
import br.com.fiap.axoeduc.components.BottomMenu
import br.com.fiap.axoeduc.model.CalculadoraJuros
import br.com.fiap.axoeduc.repository.CalculadoraRepository
import kotlin.math.pow
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.axoeduc.viewmodel.CalculadoraViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalculadoraScreenResultadosPreview() {
    CalculadoraJurosResultadoScreen (
        onProfileClick = { },
        onCursosClick = { },
        onFerramentasClick = { },
        onCertificadosClick = { },
    )
}

@Composable
fun CalculadoraJurosResultadoScreen(
    onProfileClick: () -> Unit,
    onCursosClick: () -> Unit,
    onFerramentasClick: () -> Unit,
    onCertificadosClick: () -> Unit
) {

    val viewModel: CalculadoraViewModel = viewModel()

    Scaffold(
        topBar = {
            CustomTopBar(onProfileClick = onProfileClick)
        },
        bottomBar = {
            BottomMenu(
                onCursosClick = onCursosClick,
                onFerramentasClick = onFerramentasClick,
                onCertificadosClick = onCertificadosClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.calculadora_screen),
                contentDescription = "Cofrinho",
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Calcular juros",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            CampoNumero(
                "N° de Meses",
                viewModel.meses.value
            ) { viewModel.onMesesChange(it) }

            CampoNumero(
                "Taxa de Juros Mensal (%)",
                viewModel.taxa.value
            ) { viewModel.onTaxaChange(it) }

            CampoNumero(
                "Valor Financiado",
                viewModel.valorFinanciado.value
            ) { viewModel.onValorFinanciadoChange(it) }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { viewModel.calcularJuros() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Juros Final")
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (viewModel.jurosFinal.value != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Juros Total: R$ %.2f"
                            .format(viewModel.jurosFinal.value),
                        modifier = Modifier.padding(20.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CampoNumero(
    label: String,
    valor: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp)
    )
}