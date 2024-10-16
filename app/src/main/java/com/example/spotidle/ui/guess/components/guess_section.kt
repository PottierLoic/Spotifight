package com.example.spotidle.ui.guess.components

import GameViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotidle.GameState

@Composable
fun GuessSection(
    modifier: Modifier = Modifier,
    correctGuessName: String,
    onGuessSubmit: (String) -> Unit = {},
    toGuess: String,
    gameViewModel: GameViewModel,
    onHintClick: (Int) -> Unit = {},
    suggestions: List<String>
) {
    var inputText by remember { mutableStateOf("") }
    var guessState by remember { mutableStateOf(listOf(Color.Gray, Color.Gray, Color.Gray, Color.Gray)) }

    LaunchedEffect(gameViewModel.attempts, gameViewModel.gameState) {
        guessState = List(4) { index ->
            when (gameViewModel.gameState) {
                GameState.WIN -> {
                    when {
                        index < gameViewModel.attempts -> Color(0xFFbf4e4e)
                        index == gameViewModel.attempts -> Color(0xFF00C853)
                        else -> Color.Gray
                    }
                }
                GameState.LOOSE -> {
                    if (index < gameViewModel.attempts) Color(0xFFbf4e4e) else Color.Gray
                }
                GameState.PLAYING -> {
                    when {
                        index < gameViewModel.attempts -> Color(0xFFbf4e4e)
                        index == gameViewModel.attempts -> Color(0xFFa19847)
                        else -> Color.Gray
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (i in 1..4) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = guessState[i - 1],
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable { onHintClick(i - 1) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "$i", fontSize = 16.sp, color = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (gameViewModel.gameState) {
            GameState.WIN -> {
                Text(
                    text = "Congratulations, you won!",
                    color = Color.White
                )
            }
            GameState.LOOSE -> {
                Text(
                    text = "Game Over! You lost. It was $correctGuessName...",
                    color = Color.White
                )
            }
            GameState.PLAYING -> {
                Spacer(modifier = Modifier.height(16.dp))
                GuessInputField(
                    label = "Enter the $toGuess name",
                    inputText = inputText,
                    onInputChange = { inputText = it },
                    onGuessSubmit = {
                        if (inputText.isNotBlank()) {
                            onGuessSubmit(inputText)
                            gameViewModel.guesses.add(inputText)
                            inputText = ""
                        }
                    },
                    suggestions = suggestions
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Previous Guesses:", color = Color.White)
        gameViewModel.guesses.reversed().forEach { guess ->
            val isCorrect = guess.equals(correctGuessName, ignoreCase = true)
            val backgroundColor = if (isCorrect) Color(0xFF00C853) else Color(0xFFbf4e4e)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(backgroundColor)
                    .padding(8.dp)
            ) {
                Text(text = guess, color = Color.White)
            }
        }
    }
}
