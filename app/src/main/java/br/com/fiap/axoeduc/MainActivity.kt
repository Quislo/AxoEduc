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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.fiap.axoeduc.ui.theme.AxoEducTheme
import br.com.fiap.axoeduc.components.BottomMenu
import br.com.fiap.axoeduc.components.CustomTopBar
import br.com.fiap.axoeduc.navigation.ScreenRoutes
import br.com.fiap.axoeduc.navigation.AppNavigation
import br.com.fiap.axoeduc.dao.AppDatabase
import br.com.fiap.axoeduc.repository.UsuarioRepository
import androidx.compose.ui.platform.LocalContext
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
                    dao = database.usuarioDao(),
                    credencialEmailDao = database.credencialEmailDao(),
                    credencialGoogleDao = database.credencialGoogleDao()
                )

                var usuarioLogadoId by remember { mutableIntStateOf(0) }
                var fotoPerfilUri by remember { mutableStateOf<String?>(null) }
                var nomeUsuarioLogado by remember { mutableStateOf("Aluno") }

                LaunchedEffect(usuarioLogadoId) {
                    if (usuarioLogadoId > 0) {
                        usuarioRepository.buscarPorId(usuarioLogadoId).collectLatest { usuario ->
                            fotoPerfilUri = usuario?.fotoPerfil
                            nomeUsuarioLogado = usuario?.nome ?: "Aluno"
                        }
                    } else {
                        fotoPerfilUri = null
                        nomeUsuarioLogado = "Aluno"
                    }
                }

                fun navegarParaPerfil() {
                    navController.navigate("perfil/$usuarioLogadoId")
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

                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues),
                        usuarioRepository = usuarioRepository,
                        nomeUsuarioLogado = nomeUsuarioLogado,
                        onUsuarioLogadoChange = { id -> usuarioLogadoId = id },
                        navegarParaPerfil = { navegarParaPerfil() }
                    )

                }
            }
        }
    }
}