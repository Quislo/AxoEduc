package br.com.fiap.axoeduc.screens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.axoeduc.R
import br.com.fiap.axoeduc.model.Cofrinho
import br.com.fiap.axoeduc.viewmodel.CofrinhoViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CofrinhoScreenPreview() {
    CofrinhoScreen(
        onProfileClick = {},
        onCursosClick = {},
        onFerramentasClick = {},
        onCertificadosClick = {}
    )
}

@Composable
fun CofrinhoScreen(
    onProfileClick: () -> Unit,
    onCursosClick: () -> Unit,
    onFerramentasClick: () -> Unit,
    onCertificadosClick: () -> Unit,
    viewModel: CofrinhoViewModel = viewModel()
) {

    val reservas by viewModel.reservas.collectAsState(initial = emptyList())

    var abrirCriar by remember { mutableStateOf(false) }
    var abrirDeposito by remember { mutableStateOf(false) }
    var abrirRetirada by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.cofrinho_screen),
            contentDescription = "Cofrinho",
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Cofrinho",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            BotaoAcao("Criar reserva", Icons.Default.Add) {
                abrirCriar = true
            }

            BotaoAcao("Depositar", Icons.Default.AttachMoney) {
                abrirDeposito = true
            }

            BotaoAcao("Retirar", Icons.Default.Remove) {
                abrirRetirada = true
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Seus cofrinhos",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {

            items(reservas) { reserva ->

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE0E0E0)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = reserva.nome,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "R$ ${reserva.valorAtual}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Meta: R$ ${reserva.meta}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }

    if (abrirCriar) {
        DialogCriarReserva(
            onDismiss = { abrirCriar = false },
            onCriar = { nome, meta, valor ->
                viewModel.criarReserva(nome, meta, valor)
                abrirCriar = false
            }
        )
    }

    if (abrirDeposito) {
        DialogDepositar(
            reservas = reservas,
            onDepositar = { cofrinho, valor ->
                viewModel.depositar(cofrinho, valor)
                abrirDeposito = false
            },
            onDismiss = { abrirDeposito = false }
        )
    }

    if (abrirRetirada) {
        DialogRetirar(
            reservas = reservas,
            onRetirar = { cofrinho, valor ->
                viewModel.retirar(cofrinho, valor)
                abrirRetirada = false
            },
            onDismiss = { abrirRetirada = false }
        )
    }
}

@Composable
fun BotaoAcao(
    texto: String,
    icone: ImageVector,
    onClick: () -> Unit
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD6DFF5)
        ),
        modifier = Modifier
            .size(110.dp)
            .clickable { onClick() }
            .padding(5.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = icone,
                contentDescription = texto,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(texto)
        }
    }
}

@Composable
fun DialogDepositar(
    reservas: List<Cofrinho>,
    onDepositar: (Cofrinho, Double) -> Unit,
    onDismiss: () -> Unit
) {

    var valor by remember { mutableStateOf("") }
    var reservaSelecionada by remember { mutableStateOf<Cofrinho?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Depositar") },
        text = {

            Column {

                reservas.forEach { reserva ->

                    Button(
                        onClick = { reservaSelecionada = reserva },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(reserva.nome)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = valor,
                    onValueChange = { valor = it },
                    label = { Text("Valor") }
                )
            }
        },
        confirmButton = {

            Button(
                onClick = {

                    reservaSelecionada?.let {

                        val deposito = valor.toDoubleOrNull() ?: 0.0
                        onDepositar(it, deposito)
                    }
                }
            ) {
                Text("Depositar")
            }
        }
    )
}

@Composable
fun DialogRetirar(
    reservas: List<Cofrinho>,
    onRetirar: (Cofrinho, Double) -> Unit,
    onDismiss: () -> Unit
) {

    var valor by remember { mutableStateOf("") }
    var reservaSelecionada by remember { mutableStateOf<Cofrinho?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Retirar") },
        text = {

            Column {

                reservas.forEach { reserva ->

                    Button(
                        onClick = { reservaSelecionada = reserva },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(reserva.nome)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = valor,
                    onValueChange = { valor = it },
                    label = { Text("Valor") }
                )
            }
        },
        confirmButton = {

            Button(
                onClick = {

                    reservaSelecionada?.let {

                        val retirada = valor.toDoubleOrNull() ?: 0.0
                        onRetirar(it, retirada)
                    }
                }
            ) {
                Text("Retirar")
            }
        }
    )
}

@Composable
fun DialogCriarReserva(
    onDismiss: () -> Unit,
    onCriar: (String, Double, Double) -> Unit
) {

    var nome by remember { mutableStateOf("") }
    var meta by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Criar Cofrinho") },

        text = {

            Column {

                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome do cofrinho") }
                )

                OutlinedTextField(
                    value = meta,
                    onValueChange = { meta = it },
                    label = { Text("Meta") }
                )

                OutlinedTextField(
                    value = valor,
                    onValueChange = { valor = it },
                    label = { Text("Valor inicial") }
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    onCriar(
                        nome,
                        meta.toDoubleOrNull() ?: 0.0,
                        valor.toDoubleOrNull() ?: 0.0
                    )
                }
            ) {
                Text("Criar")
            }
        }
    )
}