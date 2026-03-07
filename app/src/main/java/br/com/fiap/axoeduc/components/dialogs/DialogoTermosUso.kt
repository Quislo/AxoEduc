package br.com.fiap.axoeduc.components.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DialogoTermosUso(
    mostrar: Boolean,
    onFechar: () -> Unit
) {
    if (!mostrar) return

    AlertDialog(
        onDismissRequest = onFechar,
        title = {
            Text(
                text = "Termos de Uso",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Última atualização: 07/03/2026\n\n" +
                            "Bem-vindo à EFUB! Ao utilizar nosso aplicativo, você concorda " +
                            "com os termos descritos abaixo.\n\n" +
                            "1. Aceitação dos Termos\n" +
                            "Ao criar uma conta e utilizar o aplicativo, você declara ter lido, " +
                            "compreendido e aceito integralmente estes Termos de Uso.\n\n" +
                            "2. Descrição do Serviço\n" +
                            "A EFUB é uma plataforma de educação financeira que oferece cursos, " +
                            "ferramentas de gestão financeira e insights personalizados para " +
                            "ajudar você a gerenciar melhor suas finanças.\n\n" +
                            "3. Cadastro e Conta\n" +
                            "Você é responsável pela veracidade das informações fornecidas no cadastro " +
                            "e pela segurança da sua senha. Não compartilhe suas credenciais " +
                            "com terceiros.\n\n" +
                            "4. Uso Adequado\n" +
                            "O usuário se compromete a utilizar o aplicativo de forma ética e legal, " +
                            "não podendo: tentar acessar contas de outros usuários, " +
                            "utilizar o app para atividades ilícitas ou violar direitos de terceiros.\n\n" +
                            "5. Limitação de Responsabilidade\n" +
                            "As informações e ferramentas fornecidas têm caráter educacional " +
                            "e não constituem consultoria financeira profissional. " +
                            "Decisões financeiras são de responsabilidade exclusiva do usuário.\n\n" +
                            "6. Alterações nos Termos\n" +
                            "Reservamo-nos o direito de atualizar estes termos a qualquer momento. " +
                            "Você será notificado sobre alterações relevantes.\n\n" +
                            "7. Contato\n" +
                            "Dúvidas sobre estes termos podem ser enviadas para: contato@email.com.br.",
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onFechar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3030D6)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ok", fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}
