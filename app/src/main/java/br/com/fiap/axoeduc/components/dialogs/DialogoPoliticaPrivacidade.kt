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
fun DialogoPoliticaPrivacidade(
    mostrar: Boolean,
    onFechar: () -> Unit
) {
    if (!mostrar) return

    AlertDialog(
        onDismissRequest = onFechar,
        title = {
            Text(
                text = "Política de Privacidade",
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
                            "A EFUB (\"nós\") valoriza a privacidade dos seus usuários. " +
                            "Esta Política de Privacidade descreve como coletamos, usamos, " +
                            "armazenamos e protegemos suas informações pessoais.\n\n" +
                            "1. Dados Coletados\n" +
                            "Coletamos os seguintes dados pessoais: nome completo, data de nascimento, " +
                            "e-mail, renda mensal declarada e dados financeiros obtidos via Open Finance " +
                            "(quando autorizado).\n\n" +
                            "2. Finalidade do Tratamento\n" +
                            "Seus dados são utilizados para: personalizar sua experiência de aprendizado, " +
                            "oferecer conteúdos financeiros relevantes, gerar insights personalizados " +
                            "sobre seus gastos e manter a segurança da sua conta.\n\n" +
                            "3. Compartilhamento de Dados\n" +
                            "Não compartilhamos seus dados pessoais com terceiros, exceto quando " +
                            "necessário para o funcionamento do serviço (ex: provedores de infraestrutura) " +
                            "ou por determinação legal.\n\n" +
                            "4. Armazenamento e Segurança\n" +
                            "Seus dados são armazenados em servidores seguros com criptografia. " +
                            "Adotamos medidas técnicas e organizacionais para proteger suas informações " +
                            "contra acesso não autorizado, perda ou destruição.\n\n" +
                            "5. Seus Direitos (LGPD)\n" +
                            "Você tem direito a: acessar, corrigir, excluir seus dados, revogar " +
                            "consentimentos e solicitar portabilidade. Para exercer seus direitos, " +
                            "entre em contato pelo e-mail: privacidade@email.com.br.\n\n" +
                            "6. Contato\n" +
                            "Em caso de dúvidas, entre em contato com nosso Encarregado de Dados (DPO) " +
                            "pelo e-mail: dpo@email.com.br.",
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
