package com.example.readfile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.readfile.ui.theme.ReadfileTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReadfileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ReadFile()
                }
            }
        }
    }
}

@Composable
fun ReadFile() {
    val url = " https://users.metropolia.fi/~jarkkov/koe.txt"
    var fileContent by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                loading = true
                runBlocking {
                    launch(Dispatchers.IO) {
                        fileContent = downloadFile(url)
                        loading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Read File")
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (loading) {
            CircularProgressIndicator(color = Color.Black)
        } else {
            Text(
                text = fileContent,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

private fun downloadFile(url: String): String {

     val connection = URL(url).openConnection()
    val inputStream = connection.getInputStream()
    val reader = BufferedReader(InputStreamReader(inputStream))
    val content = StringBuilder()

    var line: String?
    while (reader.readLine().also { line = it } != null) {
        content.append(line).append('\n')
    }

    reader.close()
    inputStream.close()

    return content.toString()
}