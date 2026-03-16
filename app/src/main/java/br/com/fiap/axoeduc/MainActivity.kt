package br.com.fiap.axoeduc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
import br.com.fiap.axoeduc.screens.cadastro.CompletarCadastroScreen
import br.com.fiap.axoeduc.dao.AppDatabase
import br.com.fiap.axoeduc.repository.UsuarioRepository
import br.com.fiap.axoeduc.viewmodel.login.LoginViewModelFactory
import br.com.fiap.axoeduc.viewmodel.cadastro.CadastroViewModelFactory
import br.com.fiap.axoeduc.viewmodel.cadastro.CompletarCadastroViewModelFactory
import br.com.fiap.axoeduc.viewmodel.PerfilViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest

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
                        currentRoute != ScreenRoutes.COMPLETAR_CADASTRO &&
                        currentRoute != ScreenRoutes.PERFIL

                val context = LocalContext.current
                val database = AppDatabase.getDatabase(context)
                val usuarioRepository = UsuarioRepository(
                    dao = database.usuarioDao()
                )

                // UID do usuário logado (Firebase)
                var usuarioLogadoUid by remember { mutableStateOf("") }
                var fotoPerfilUri by remember { mutableStateOf<String?>(null) }
                var nomeUsuarioLogado by remember { mutableStateOf("Aluno") }

                // Observar dados do usuário logado no Room
                LaunchedEffect(usuarioLogadoUid) {
                    if (usuarioLogadoUid.isNotEmpty()) {
                        usuarioRepository.buscarPorUid(usuarioLogadoUid).collectLatest { usuario ->
                            fotoPerfilUri = usuario?.fotoPerfil
                            nomeUsuarioLogado = usuario?.nome ?: "Aluno"
                        }
                    } else {
                        fotoPerfilUri = null
                        nomeUsuarioLogado = "Aluno"
                    }
                }

                fun navegarParaPerfil() {
                    navController.navigate("perfil/$usuarioLogadoUid")
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (showBars) {
                            CustomTopBar(
                                onProfileClick = { navegarParaPerfil() },
                                fotoPerfilUri = fotoPerfilUri
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
                                    // Pegar UID do Firebase Auth e atualizar estado
                                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                                    usuarioLogadoUid = uid
                                    navController.navigate(ScreenRoutes.CURSOS) {
                                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                                    }
                                },
                                onCriarConta = { navController.navigate(ScreenRoutes.CADASTRO) },
                                onCadastroIncompleto = { uid ->
                                    usuarioLogadoUid = uid
                                    navController.navigate("completar_cadastro/$uid") {
                                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                                    }
                                },
                                viewModel = viewModel(
                                    factory = LoginViewModelFactory(usuarioRepository)
                                )
                            )
                        }

                        composable(ScreenRoutes.CADASTRO) {
                            CadastroScreen(
                                onCadastroSucesso = { uid ->
                                    usuarioLogadoUid = uid
                                    navController.navigate(ScreenRoutes.CURSOS) {
                                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                                    }
                                },
                                onVoltarLogin = { navController.popBackStack() },
                                viewModel = viewModel(
                                    factory = CadastroViewModelFactory(usuarioRepository)
                                )
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
                                    factory = CompletarCadastroViewModelFactory(
                                        usuarioUid,
                                        usuarioRepository
                                    )
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
                                    // Logout do Firebase
                                    FirebaseAuth.getInstance().signOut()
                                    usuarioLogadoUid = ""
                                    navController.navigate(ScreenRoutes.LOGIN) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                viewModel = viewModel(
                                    factory = PerfilViewModelFactory(usuarioRepository, usuarioUid)
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
            }
        }
    }
}