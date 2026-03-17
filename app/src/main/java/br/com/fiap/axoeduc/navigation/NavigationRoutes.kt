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
import br.com.fiap.axoeduc.screens.CalculadoraJurosResultadoScreen
import br.com.fiap.axoeduc.screens.CalculadoraJurosScreen
import br.com.fiap.axoeduc.screens.CertificadosScreen
import br.com.fiap.axoeduc.screens.CofrinhoScreen
import br.com.fiap.axoeduc.screens.CursosScreen
import br.com.fiap.axoeduc.screens.EsqueciSenhaScreen
import br.com.fiap.axoeduc.screens.FerramentasScreen
import br.com.fiap.axoeduc.screens.InvestimentosScreen
import br.com.fiap.axoeduc.screens.LoginScreen
import br.com.fiap.axoeduc.screens.PerfilScreen
import br.com.fiap.axoeduc.screens.cadastro.CadastroScreen
import br.com.fiap.axoeduc.screens.cadastro.CompletarCadastroScreen
import br.com.fiap.axoeduc.viewmodel.PerfilViewModelFactory
import br.com.fiap.axoeduc.viewmodel.cadastro.CadastroViewModelFactory
import br.com.fiap.axoeduc.viewmodel.cadastro.CompletarCadastroViewModelFactory
import br.com.fiap.axoeduc.viewmodel.login.LoginViewModel
import br.com.fiap.axoeduc.viewmodel.login.LoginViewModelFactory
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    usuarioRepository: UsuarioRepository,
    nomeUsuarioLogado: String,
    onUsuarioLogadoChange: (String) -> Unit,
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
                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    onUsuarioLogadoChange(uid)

                    navController.navigate(ScreenRoutes.CURSOS) {
                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                    }
                },
                onCriarConta = { navController.navigate(ScreenRoutes.CADASTRO) },
                onEsqueciSenha = { navController.navigate(ScreenRoutes.ESQUECI_SENHA) },
                onCadastroIncompleto = { id ->
                    onUsuarioLogadoChange(id.toString())
                    navController.navigate("completar_cadastro/$id") {
                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                    }
                },
                viewModel = loginViewModel
            )
        }

        composable(ScreenRoutes.CADASTRO) {
            CadastroScreen(
                onCadastroSucesso = { id ->
                    onUsuarioLogadoChange(id.toString())
                    navController.navigate(ScreenRoutes.CURSOS) {
                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                    }
                },
                onVoltarLogin = { navController.popBackStack() },
                viewModel = viewModel(factory = CadastroViewModelFactory(usuarioRepository))
            )
        }

        composable(ScreenRoutes.ESQUECI_SENHA) {
            EsqueciSenhaScreen(
                onVoltarLogin = { navController.popBackStack() }
            )
        }

        composable(
            route = ScreenRoutes.COMPLETAR_CADASTRO,
            arguments = listOf(
                navArgument("usuarioUid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val usuarioUid = backStackEntry.arguments?.getString("usuarioUid") ?: ""

            CompletarCadastroScreen(
                onCadastroCompleto = {
                    navController.navigate(ScreenRoutes.CURSOS) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = viewModel(
                    factory = CompletarCadastroViewModelFactory(usuarioUid, usuarioRepository)
                )
            )
        }

        composable(
            route = ScreenRoutes.PERFIL,
            arguments = listOf(
                navArgument("usuarioUid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val usuarioUid = backStackEntry.arguments?.getString("usuarioUid") ?: ""

            PerfilScreen(
                onVoltarClick = { navController.popBackStack() },
                onSairClick = {
                    FirebaseAuth.getInstance().signOut()
                    onUsuarioLogadoChange("")
                    navController.navigate(ScreenRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = viewModel(
                    factory = PerfilViewModelFactory(usuarioRepository, usuarioUid)
                )
            )
        }

        composable(ScreenRoutes.CURSOS) { CursosScreen() }

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