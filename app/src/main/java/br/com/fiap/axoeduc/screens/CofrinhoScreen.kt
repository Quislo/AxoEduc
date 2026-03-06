package br.com.fiap.axoeduc.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        onCertificadosClick = {},
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

    val cofrinhos = viewModel.cofrinhos



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.cofrinho_screen),
            contentDescription = "Cofrinho",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Cofrinho",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AcaoCofrinho("+", "Criar reserva")
            AcaoCofrinho("💰", "Depositar")
            AcaoCofrinho("↩", "Retirar")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Seus cofrinhos",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(cofrinhos) { cofrinho ->
                CofrinhoItem(cofrinho)
            }
        }
    }
}


@Composable
fun CofrinhoItem(cofrinho: Cofrinho) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE6E6E6)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = cofrinho.nome,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Meta: R$ %.2f".format(cofrinho.meta),
                    fontSize = 12.sp
                )
            }

            Text(
                text = "R$ %.2f".format(cofrinho.valorAtual),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AcaoCofrinho(icone: String, texto: String) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color = Color(0xFFD0D9F7),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(2.dp)
            .width(100.dp)
            .clickable { }
    ) {

        Text(
            text = icone,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = texto,
            fontSize = 12.sp
        )
    }
}