package br.com.fiap.axoeduc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.fiap.axoeduc.ui.theme.AxoEducTheme
import br.com.fiap.axoeduc.components.BottomMenu
import br.com.fiap.axoeduc.components.CustomTopBar
import br.com.fiap.axoeduc.navigation.ScreenRoutes
import br.com.fiap.axoeduc.screens.CursosScreen
import br.com.fiap.axoeduc.screens.FerramentasScreen
import br.com.fiap.axoeduc.screens.CalculadoraJurosResultadoScreen
import br.com.fiap.axoeduc.screens.CalculadoraJurosScreen
import br.com.fiap.axoeduc.screens.CertificadosScreen
import br.com.fiap.axoeduc.screens.CofrinhoScreen
import br.com.fiap.axoeduc.screens.InvestimentosScreen
import br.com.fiap.axoeduc.screens.LoginScreen
import br.com.fiap.axoeduc.screens.PerfilScreen
import br.com.fiap.axoeduc.screens.cadastro.CadastroScreen
import br.com.fiap.axoeduc.dao.AppDatabase
import br.com.fiap.axoeduc.repository.UsuarioRepository
import br.com.fiap.axoeduc.viewmodel.cadastro.CadastroViewModelFactory
import br.com.fiap.axoeduc.viewmodel.login.LoginViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AxoEducTheme {
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBars = currentRoute != ScreenRoutes.LOGIN &&
                        currentRoute != ScreenRoutes.CADASTRO &&
                        currentRoute != ScreenRoutes.PERFIL

                val context = LocalContext.current
                val database = AppDatabase.getDatabase(context)
                val usuarioRepository = UsuarioRepository(database.usuarioDao())

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (showBars) {
                            CustomTopBar(
                                onProfileClick = { navController.navigate(ScreenRoutes.PERFIL) }
                            )
                        }
                    },
                    bottomBar = {
                        if (showBars) {
                            BottomMenu(
                                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) }
                            )
                        }
                    }
                ) { paddingValues ->

                    NavHost(
                        navController = navController,
                        startDestination = ScreenRoutes.LOGIN,
                        modifier = Modifier.padding(paddingValues)
                    ) {

                        composable(ScreenRoutes.LOGIN) {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate(ScreenRoutes.CURSOS) {
                                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                                    }
                                },
                                onCriarConta = { navController.navigate(ScreenRoutes.CADASTRO) },
                                viewModel = viewModel(factory = LoginViewModelFactory(usuarioRepository))
                            )
                        }

                        composable(ScreenRoutes.CADASTRO) {
                            CadastroScreen(
                                onCadastroSucesso = {
                                    navController.navigate(ScreenRoutes.LOGIN) {
                                        popUpTo(ScreenRoutes.CADASTRO) { inclusive = true }
                                    }
                                },
                                onVoltarLogin = { navController.popBackStack() },
                                viewModel = viewModel(factory = CadastroViewModelFactory(usuarioRepository))
                            )
                        }

                        composable(ScreenRoutes.PERFIL) {
                            PerfilScreen(
                                onVoltarClick = { navController.popBackStack() },
                                onSairClick = {
                                    navController.navigate(ScreenRoutes.LOGIN) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(ScreenRoutes.CURSOS) {
                            CursosScreen()
                        }

                        composable(ScreenRoutes.FERRAMENTAS) {
                            FerramentasScreen(
                                onProfileClick = { navController.navigate(ScreenRoutes.PERFIL) },
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
                                onProfileClick = { navController.navigate(ScreenRoutes.PERFIL) },
                                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) }
                            )
                        }

                        composable(ScreenRoutes.CALCULADORA) {
                            CalculadoraJurosScreen(
                                onProfileClick = { navController.navigate(ScreenRoutes.PERFIL) },
                                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) },
                                onCalcularClick = { _, _ -> navController.navigate(ScreenRoutes.CALCULADORA_RESULTADOS) }
                            )
                        }

                        composable(ScreenRoutes.CALCULADORA_RESULTADOS) {
                            CalculadoraJurosResultadoScreen(
                                onProfileClick = { navController.navigate(ScreenRoutes.PERFIL) },
                                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) }
                            )
                        }

                        composable(ScreenRoutes.CERTIFICADOS) {
                            CertificadosScreen(
                                onProfileClick = { navController.navigate(ScreenRoutes.PERFIL) },
                                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) }
                            )
                        }

                        composable(ScreenRoutes.INVESTIMENTOS) {
                            InvestimentosScreen(
                                onProfileClick = { navController.navigate(ScreenRoutes.PERFIL) },
                                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) },
                                onInvestimentoClick = { /* TODO futuramente */ }
                            )
                        }
                    }
                }
            }
        }
    }
}