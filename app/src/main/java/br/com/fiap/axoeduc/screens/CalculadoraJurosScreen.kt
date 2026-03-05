package br.com.fiap.axoeduc.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.axoeduc.components.CustomTopBar
import br.com.fiap.axoeduc.components.BottomMenu

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalculadoraScreenPreview() {
    CalculadoraJurosScreen(
        onProfileClick = { },
        onCursosClick = { },
        onFerramentasClick = { },
        onCertificadosClick = { },
        onCalcularClick = { _, _ -> }
    )
}

@Composable
fun CalculadoraJurosScreen(
    onProfileClick: () -> Unit,
    onCursosClick: () -> Unit,
    onFerramentasClick: () -> Unit,
    onCertificadosClick: () -> Unit,
    onCalcularClick: (String, String) -> Unit
) {

    var renda by remember { mutableStateOf("") }
    var objetivo by remember { mutableStateOf("Objetivo na Plataforma") }
    var expanded by remember { mutableStateOf(false) }

    val objetivos = listOf(
        "Empréstimo",
        "Investimento",
        "Parcelamento",
        "Financiamento"
    )

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

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Calculadora de juros",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = renda,
                onValueChange = { renda = it },
                label = { Text("Renda Mensal") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.fillMaxWidth()) {

                OutlinedTextField(
                    value = objetivo,
                    onValueChange = {},
                    label = { Text("Objetivo na Plataforma") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Selecionar",
                            modifier = Modifier.clickable { expanded = true }
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    objetivos.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                objetivo = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onCalcularClick(renda, objetivo)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8FA1E6)
                )
            ) {
                Text("Vamos Lá!")
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}