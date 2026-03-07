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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.fiap.axoeduc.ui.theme.AxoEducTheme
import br.com.fiap.axoeduc.components.BottomMenu
import br.com.fiap.axoeduc.screens.CursosScreen
import br.com.fiap.axoeduc.screens.FerramentasScreen
import br.com.fiap.axoeduc.components.CustomTopBar
import br.com.fiap.axoeduc.navigation.ScreenRoutes
import br.com.fiap.axoeduc.screens.CalculadoraJurosResultadoScreen
import br.com.fiap.axoeduc.screens.CalculadoraJurosScreen
import br.com.fiap.axoeduc.screens.CertificadosScreen
import br.com.fiap.axoeduc.screens.CofrinhoScreen
import br.com.fiap.axoeduc.screens.InvestimentosScreen
import br.com.fiap.axoeduc.screens.LoginScreen
import br.com.fiap.axoeduc.screens.cadastro.CadastroScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AxoEducTheme {
                val navController = rememberNavController()
                val navBackStackEntryState = navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntryState.value?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (currentRoute != ScreenRoutes.LOGIN && currentRoute != ScreenRoutes.CADASTRO) {
                            CustomTopBar(
                                onProfileClick = { /* TODO: Futura tela de perfil */ }
                            )
                        }
                    },
                    bottomBar = {
                        if (currentRoute != ScreenRoutes.LOGIN && currentRoute != ScreenRoutes.CADASTRO) {
                            BottomMenu(
                                onCursosClick = {
                                    navController.navigate(ScreenRoutes.CURSOS)
                                },
                                onFerramentasClick = {
                                    navController.navigate(ScreenRoutes.FERRAMENTAS)
                                },
                                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) }
                            )
                        }
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = ScreenRoutes.LOGIN,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(ScreenRoutes.LOGIN) {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate(ScreenRoutes.CURSOS) {
                                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                                    }
                                },
                                onCriarConta = {
                                    navController.navigate(ScreenRoutes.CADASTRO)
                                }
                            )
                        }

                        composable(ScreenRoutes.CADASTRO) {
                            CadastroScreen(
                                onCadastroSucesso = {
                                    navController.navigate(ScreenRoutes.CURSOS) {
                                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                                    }
                                },
                                onVoltarLogin = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(ScreenRoutes.CURSOS) {
                            CursosScreen()
                        }

                        composable(ScreenRoutes.FERRAMENTAS) {
                            FerramentasScreen(
                                onProfileClick = { /* TODO */ },
                                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) },
                                onCofrinhoClick = { navController.navigate(ScreenRoutes.COFRINHO) },
                                onCalculadoraClick = { navController.navigate(ScreenRoutes.CALCULADORA) },
                                onInvestimentosClick = { navController.navigate(ScreenRoutes.INVESTIMENTOS) }
                            )
                        }

                        composable(ScreenRoutes.COFRINHO) {
                            CofrinhoScreen(
                                onProfileClick = { /* TODO */ },
                                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) }
                            )
                        }

                        composable(ScreenRoutes.CALCULADORA) {
                            CalculadoraJurosScreen(
                                onProfileClick = { /* TODO */ },
                                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) },
                                onCalcularClick = { _, _ ->
                                    navController.navigate(ScreenRoutes.CALCULADORA_RESULTADOS)
                                }
                            )
                        }

                        composable(ScreenRoutes.CALCULADORA_RESULTADOS) {
                            CalculadoraJurosResultadoScreen(
                                onProfileClick = { /* TODO */ },
                                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) }
                            )
                        }

                        composable(ScreenRoutes.CERTIFICADOS) {
                            CertificadosScreen(
                                onProfileClick = { /* TODO */ },
                                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) }
                            )
                        }

                        composable(ScreenRoutes.INVESTIMENTOS) {
                            InvestimentosScreen(
                                onProfileClick = { /* TODO */ },
                                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) },
                                onInvestimentoClick = { /* TODO: */ }
                            )
                        }
                    }
                }
            }
        }
    }
}