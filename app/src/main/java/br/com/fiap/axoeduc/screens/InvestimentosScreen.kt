package br.com.fiap.axoeduc.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.axoeduc.R
import br.com.fiap.axoeduc.model.Investimento
import br.com.fiap.axoeduc.viewmodel.InvestimentoViewModel


@Composable
fun InvestimentosScreen(
    onProfileClick: () -> Unit,
    onCursosClick: () -> Unit,
    onFerramentasClick: () -> Unit,
    onCertificadosClick: () -> Unit,
    onInvestimentoClick: (Investimento) -> Unit
) {

    val viewModel: InvestimentoViewModel = viewModel()


    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.investimentos_screen),
                contentDescription = "Investimentos",
                modifier = Modifier.size(80.dp)
            )

            Text(
                text = "Investimentos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(viewModel.investimentos) { investimento ->

                CardInvestimento(
                    investimento = investimento,
                    onClick = { onInvestimentoClick(investimento) }
                )
            }
        }
    }
}


@Composable
fun CardInvestimento(
    investimento: Investimento,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            Image(
                painter = painterResource(investimento.icone),
                contentDescription = investimento.titulo,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = investimento.titulo,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = investimento.descricao,
                fontSize = 12.sp
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InvestimentosScreenPreview() {

    InvestimentosScreen(
        onProfileClick = {},
        onCursosClick = {},
        onFerramentasClick = {},
        onCertificadosClick = {},
        onInvestimentoClick = {}
    )
}