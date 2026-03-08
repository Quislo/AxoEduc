package br.com.fiap.axoeduc.screens.cadastro

import androidx.compose.runtime.Composable
import br.com.fiap.axoeduc.components.inputs.RendaMensalInput

@Composable
fun EtapaPerfilFinanceiro(
    rendaMensal: String,
    onRendaChange: (String) -> Unit,
    enviado: Boolean
) {
    RendaMensalInput(
        rendaMensal = rendaMensal,
        onValueChange = onRendaChange,
        enviado = enviado
    )
}
