package br.com.fiap.axoeduc.screens.cadastro

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.axoeduc.components.inputs.ConfirmarSenhaInput
import br.com.fiap.axoeduc.components.inputs.EmailInput
import br.com.fiap.axoeduc.components.inputs.SenhaInput

@Composable
fun EtapaDadosAcesso(
    email: String,
    senha: String,
    confirmarSenha: String,
    consentimentoOpenFinance: Boolean,
    consentimentoLgpd: Boolean,
    onEmailChange: (String) -> Unit,
    onSenhaChange: (String) -> Unit,
    onConfirmarSenhaChange: (String) -> Unit,
    onConsentimentoOpenFinanceChange: (Boolean) -> Unit,
    onConsentimentoLgpdChange: (Boolean) -> Unit,
    onMostrarPolitica: () -> Unit,
    onMostrarTermos: () -> Unit,
    emailErro: String? = null,
    senhaErro: String? = null,
    confirmarSenhaErro: String? = null,
    onEmailFocusLost: (() -> Unit)? = null,
) {
    EmailInput(
        email = email,
        onValueChange = onEmailChange,
        isError = emailErro != null,
        errorMessage = emailErro,
        onFocusLost = onEmailFocusLost,
    )

    Spacer(modifier = Modifier.height(4.dp))

    SenhaInput(
        senha = senha,
        onValueChange = onSenhaChange,
        isError = senhaErro != null,
        errorMessage = senhaErro,
    )

    Spacer(modifier = Modifier.height(4.dp))

    ConfirmarSenhaInput(
        confirmarSenha = confirmarSenha,
        onValueChange = onConfirmarSenhaChange,
        isError = confirmarSenhaErro != null,
        errorMessage = confirmarSenhaErro,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = consentimentoOpenFinance,
            onCheckedChange = onConsentimentoOpenFinanceChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF3030D6),
                uncheckedColor = Color(0xCCFFFFFF),
                checkmarkColor = Color.White
            )
        )
        Text(
            text = "Autorizo a integração com Open Finance " +
                    "para receber insights personalizados.",
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Color(0xCCFFFFFF),
            modifier = Modifier.weight(1f)
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = consentimentoLgpd,
            onCheckedChange = onConsentimentoLgpdChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF3030D6),
                uncheckedColor = Color(0xCCFFFFFF),
                checkmarkColor = Color.White
            )
        )

        val linkStyle = SpanStyle(
            color = Color(0xFF7995FF),
            textDecoration = TextDecoration.Underline
        )

        val textoLgpd = buildAnnotatedString {
            withStyle(SpanStyle(color = Color(0xCCFFFFFF))) {
                append("Estou ciente e concordo com o tratamento dos meus dados pessoais para as finalidades descritas na ")
            }
            withLink(
                LinkAnnotation.Clickable(
                    tag = "politica",
                    styles = TextLinkStyles(style = linkStyle)
                ) {
                    onMostrarPolitica()
                }
            ) {
                append("Política de Privacidade")
            }
            withStyle(SpanStyle(color = Color(0xCCFFFFFF))) {
                append(" e nos ")
            }
            withLink(
                LinkAnnotation.Clickable(
                    tag = "termos",
                    styles = TextLinkStyles(style = linkStyle)
                ) {
                    onMostrarTermos()
                }
            ) {
                append("Termos de Uso")
            }
            withStyle(SpanStyle(color = Color(0xCCFFFFFF))) {
                append(".")
            }
        }

        Text(
            text = textoLgpd,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier.weight(1f)
        )
    }
}
