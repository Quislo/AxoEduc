package br.com.fiap.axoeduc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.axoeduc.ui.theme.AxoEducTheme
import br.com.fiap.axoeduc.components.BottomMenu
import br.com.fiap.axoeduc.screens.CursosScreen
import br.com.fiap.axoeduc.screens.FerramentasScreen
import br.com.fiap.axoeduc.components.CustomTopBar
import br.com.fiap.axoeduc.screens.CalculadoraJurosResultadoScreen
import br.com.fiap.axoeduc.screens.CalculadoraJurosScreen
import br.com.fiap.axoeduc.screens.CofrinhoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AxoEducTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CustomTopBar(
                            onProfileClick = { /* TODO: Futura tela de perfil */ }
                        )
                    },
                    bottomBar = {
                        BottomMenu(
                            onCursosClick = {
                                navController.navigate("cursos")
                            },
                            onFerramentasClick = {
                                navController.navigate("ferramentas")
                            },
                            onCertificadosClick = {    /* TODO: Criar navegação quando a tela existir */ }
                        )
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = "ScreenRoutes.CURSOS",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("ScreenRoutes.CURSOS") {
                            CursosScreen()
                        }

                        composable("ScreenRoutes.FERRAMENTAS") {
                            FerramentasScreen(
                                onProfileClick = { /* TODO. */ },
                                onCursosClick = { navController.navigate("ScreenRoutes.CURSOS") },
                                onFerramentasClick = { navController.navigate("ScreenRoutes.FERRAMENTAS") },
                                onCertificadosClick = { /* TODO. */ },

                                onCofrinhoClick = { navController.navigate("ScreenRoutes.COFRINHO") },
                                onCalculadoraClick = { navController.navigate("ScreenRoutes.CALCULADORA") },
                                onInvestimentosClick = { /* TODO. */ }
                            )
                        }

                        composable("cofrinho") {
                            CofrinhoScreen(
                                onProfileClick = { /* TODO. */ },
                                onCursosClick = { navController.navigate("ScreenRoutes.CURSOS") },
                                onFerramentasClick = { navController.navigate("ScreenRoutes.FERRAMENTAS") },
                                onCertificadosClick = { /* TODO. */ }
                            )
                        }

                        composable("calculadora") {
                            CalculadoraJurosScreen(
                                onProfileClick = { /* TODO */ },
                                onCursosClick = { navController.navigate("ScreenRoutes.CURSOS") },
                                onFerramentasClick = { navController.navigate("ScreenRoutes.FERRAMENTAS") },
                                onCertificadosClick = { /* TODO. */ },
                                onCalcularClick = { renda, objetivo ->

                                    navController.navigate("ScreenRoutes.CALCULADORA_RESULTADOS")
                                }
                            )
                        }

                        composable("calculadora_resultado") {
                            CalculadoraJurosResultadoScreen(
                                onProfileClick = { /* TODO */ },
                                onCursosClick = { navController.navigate("ScreenRoutes.CURSOS") },
                                onFerramentasClick = { navController.navigate("ScreenRoutes.FERRAMENTAS") },
                                onCertificadosClick = { /* TODO. */ }
                            )
                        }
                    }
                }
            }
        }
    }
}