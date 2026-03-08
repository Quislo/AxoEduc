package br.com.fiap.axoeduc.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun IndicadorProgresso(
    etapaAtual: Int,
    totalEtapas: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until totalEtapas) {
            val ativo = i <= etapaAtual

            val largura by animateDpAsState(
                targetValue = if (i == etapaAtual) 32.dp else 12.dp,
                animationSpec = tween(durationMillis = 300),
                label = "largura"
            )

            val cor by animateColorAsState(
                targetValue = if (ativo) Color(0xFFFFFFFF) else Color(0x40FFFFFF),
                animationSpec = tween(durationMillis = 300),
                label = "cor"
            )

            Box(
                modifier = Modifier
                    .width(largura)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(cor)
            )
        }
    }
}
