package br.com.fiap.axoeduc.repository


import br.com.fiap.axoeduc.model.Cofrinho

class CofrinhoRepository {

    fun getCofrinhos(): List<Cofrinho> {
        return listOf(
            Cofrinho(1, "Aluguel", 680.0, 680.0),
            Cofrinho(2, "Gás", 130.0, 90.0),
            Cofrinho(3, "Emergência", 700.0, 500.0)
        )
    }
}