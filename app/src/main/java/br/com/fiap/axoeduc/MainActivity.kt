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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AxoEducTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomMenu(
                            onCursosClick = {
                                navController.navigate("cursos")
                            },
                            onFerramentasClick = { /* TODO: Criar navegação quando a tela existir */ },
                            onCertificadosClick = { /* TODO: Criar navegação quando a tela existir */ }
                        )
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = "cursos",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("cursos") {
                            CursosScreen()
                        }
                    }
                }
            }
        }
    }
}