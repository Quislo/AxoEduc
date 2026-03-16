package br.com.fiap.axoeduc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.fiap.axoeduc.repository.UsuarioRepository
import br.com.fiap.axoeduc.screens.*
import br.com.fiap.axoeduc.screens.cadastro.*
import br.com.fiap.axoeduc.viewmodel.PerfilViewModelFactory
import br.com.fiap.axoeduc.viewmodel.cadastro.CadastroViewModelFactory
import br.com.fiap.axoeduc.viewmodel.cadastro.CompletarCadastroViewModelFactory
import br.com.fiap.axoeduc.viewmodel.login.LoginViewModelFactory
import br.com.fiap.axoeduc.viewmodel.login.LoginViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    usuarioRepository: UsuarioRepository,
    nomeUsuarioLogado: String,
    onUsuarioLogadoChange: (Int) -> Unit,
    navegarParaPerfil: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.LOGIN,
        modifier = modifier
    ) {
        composable(ScreenRoutes.LOGIN) {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(usuarioRepository)
            )

            LoginScreen(
                onLoginSuccess = {
                    loginViewModel.usuarioLogadoId?.let { id ->
                        onUsuarioLogadoChange(id)
                    }
                    navController.navigate(ScreenRoutes.CURSOS) {
                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                    }
                },
                onCriarConta = { navController.navigate(ScreenRoutes.CADASTRO) },
                onCadastroIncompleto = { id ->
                    onUsuarioLogadoChange(id)
                    navController.navigate("completar_cadastro/$id") {
                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                    }
                },
                viewModel = loginViewModel
            )
        }

        composable(ScreenRoutes.CADASTRO) {
            CadastroScreen(
                onCadastroSucesso = { usuarioId ->
                    onUsuarioLogadoChange(usuarioId)
                    navController.navigate(ScreenRoutes.CURSOS) {
                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                    }
                },
                onVoltarLogin = { navController.popBackStack() },
                viewModel = viewModel(factory = CadastroViewModelFactory(usuarioRepository))
            )
        }

        composable(
            route = ScreenRoutes.COMPLETAR_CADASTRO,
            arguments = listOf(
                navArgument("usuarioId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0

            CompletarCadastroScreen(
                onCadastroCompleto = {
                    navController.navigate(ScreenRoutes.CURSOS) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = viewModel(
                    factory = CompletarCadastroViewModelFactory(usuarioRepository, usuarioId)
                )
            )
        }

        composable(
            route = ScreenRoutes.PERFIL,
            arguments = listOf(
                navArgument("usuarioId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0

            PerfilScreen(
                onVoltarClick = { navController.popBackStack() },
                onSairClick = {
                    onUsuarioLogadoChange(0) // Zera o ID ao sair
                    navController.navigate(ScreenRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = viewModel(
                    factory = PerfilViewModelFactory(usuarioRepository, usuarioId)
                )
            )
        }

        composable(ScreenRoutes.CURSOS) {
            CursosScreen()
        }

        composable(ScreenRoutes.FERRAMENTAS) {
            FerramentasScreen(
                onProfileClick = { navegarParaPerfil() },
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
                onProfileClick = { navegarParaPerfil() },
                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) }
            )
        }

        composable(ScreenRoutes.CALCULADORA) {
            CalculadoraJurosScreen(
                onProfileClick = { navegarParaPerfil() },
                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) },
                onCalcularClick = { _, _ -> navController.navigate(ScreenRoutes.CALCULADORA_RESULTADOS) }
            )
        }

        composable(ScreenRoutes.CALCULADORA_RESULTADOS) {
            CalculadoraJurosResultadoScreen(
                onProfileClick = { navegarParaPerfil() },
                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) }
            )
        }

        composable(ScreenRoutes.CERTIFICADOS) {
            CertificadosScreen(
                nomeUsuario = nomeUsuarioLogado,
                onProfileClick = { navegarParaPerfil() },
                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) }
            )
        }

        composable(ScreenRoutes.INVESTIMENTOS) {
            InvestimentosScreen(
                onProfileClick = { navegarParaPerfil() },
                onCursosClick = { navController.navigate(ScreenRoutes.CURSOS) },
                onFerramentasClick = { navController.navigate(ScreenRoutes.FERRAMENTAS) },
                onCertificadosClick = { navController.navigate(ScreenRoutes.CERTIFICADOS) },
                onInvestimentoClick = { /* TODO futuramente */ }
            )
        }
    }
}