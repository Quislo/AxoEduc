package br.com.fiap.axoeduc.screens.cadastro

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.fiap.axoeduc.components.inputs.DataNascimentoInput
import br.com.fiap.axoeduc.components.inputs.NomeInput

@Composable
fun EtapaDadosPessoais(
    nome: String,
    dataNascimento: String,
    onNomeChange: (String) -> Unit,
    onDataChange: (String) -> Unit,
    enviado: Boolean
) {
    NomeInput(
        nome = nome,
        onValueChange = onNomeChange,
        enviado = enviado
    )

    Spacer(modifier = Modifier.height(4.dp))

    DataNascimentoInput(
        dataNascimento = dataNascimento,
        onValueChange = onDataChange,
        enviado = enviado
    )
}
