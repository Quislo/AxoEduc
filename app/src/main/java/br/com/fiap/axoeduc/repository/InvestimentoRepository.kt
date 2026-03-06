package br.com.fiap.axoeduc.repository

import br.com.fiap.axoeduc.R
import br.com.fiap.axoeduc.model.Investimento

class InvestimentoRepository {

    fun listarInvestimentos(): List<Investimento> {
        return listOf(

            Investimento(
                "Renda fixa",
                "CDB, CDI, CRA, LCI, LCA, LCI, LF e LODSN",
                R.drawable.ic_renda_fixa
            ),

            Investimento(
                "Tesouro direto",
                "Selic, IPCA e Prefixado",
                R.drawable.ic_tesouro
            ),

            Investimento(
                "Previdência Privada",
                "Brasilprev VGBL, Itaú Previdência PGBL",
                R.drawable.ic_previdencia
            ),

            Investimento(
                "Fundos de Investimentos",
                "Fundos de Renda Fixa, Multimercados",
                R.drawable.ic_fundos
            ),

            Investimento(
                "COE",
                "COE ligados a Ações e Fundos",
                R.drawable.ic_coe
            ),

            Investimento(
                "Criptomoedas",
                "Bitcoin, Ethereum, Chainlink",
                R.drawable.ic_crypto
            )
        )
    }
}