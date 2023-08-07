package com.example.valorantagents.ui.agents

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer


import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.valorantagents.R
import com.example.valorantagents.model.Agent
import kotlinx.coroutines.launch
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AgentListScreen(viewModel: AgentListScreenViewModel = hiltViewModel()) {

    var isPlaying by remember { mutableStateOf(true) }


    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.bg))

    val animationState by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        restartOnPlay = true
    )

    val pickerState = rememberPagerState(Int.MAX_VALUE / 2)

    val agentViewerState = rememberPagerState(Int.MAX_VALUE / 2)

    LaunchedEffect(key1 = pickerState.currentPage) {
        if (pickerState.currentPage != agentViewerState.currentPage) {
            isPlaying = false
            agentViewerState.animateScrollToPage(pickerState.currentPage)
            isPlaying = true
        }
    }

    LaunchedEffect(key1 = agentViewerState.currentPage) {
        if (agentViewerState.currentPage != pickerState.currentPage) {

            isPlaying = false
            pickerState.animateScrollToPage(agentViewerState.currentPage)
            isPlaying = true

        }
    }

    val state by viewModel.uiState.collectAsState()


    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.background
    )

    Scaffold {
        Box(modifier = Modifier.fillMaxWidth()) {

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(brush = Brush.verticalGradient(colors = colors))
            ) {

            }

            LottieAnimation(
                modifier = Modifier.alpha(0.4f),
                composition = composition, progress = { animationState },
                contentScale = ContentScale.Fit
            )


        }
        if (state.isLoading) {
            //loading state
            Box(modifier = Modifier.padding(it).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        if (!state.isLoading && state.agents.isNotEmpty()) {
            Column(modifier = Modifier
                    .padding(it)
                    .fillMaxSize()) { AgentPager(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(), listState = agentViewerState, items = state.agents
                )

                AgentsPicker(items = state.agents, pickerState)
            }

        }

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AgentPager(modifier: Modifier, listState: PagerState, items: List<Agent>) {

    val height = LocalConfiguration.current.screenHeightDp


    HorizontalPager(modifier = modifier, state = listState, pageCount = Int.MAX_VALUE) {
        val pageOffset = (listState.currentPage - it) + listState.currentPageOffsetFraction


        val alpha by animateFloatAsState(
            targetValue = if (pageOffset != 0.0f) 0.1f else 0.3f,
            animationSpec = tween(easing = LinearOutSlowInEasing),
            label = "alpha Animator",
        )


        val yPosition by animateFloatAsState(
            targetValue = if (pageOffset != 0.0f) pageOffset - (height / 4) else pageOffset,
            animationSpec = tween(200), label = ""
        )


        val backgroundSize by animateFloatAsState(
            targetValue = if (pageOffset != 0.0f) 0.8f else 1f,
            animationSpec = tween(durationMillis = 300), label = "background size"
        )

        val textAlpha by animateFloatAsState(
            targetValue = if (pageOffset != 0.0f) 0.0f else 1f,
            tween(delayMillis = 300), label = ""

        )

        val textSize by animateFloatAsState(
            targetValue = if (pageOffset != 0.0f) 0.0f else 1f, animationSpec = tween(300),
            label = ""
        )

        val boxSize by animateFloatAsState(
            targetValue = if (pageOffset != 0.0f) 0.75f else 1f,
            animationSpec = tween(300), label = ""
        )


        val index = it % items.size

        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = backgroundSize,
                    scaleY = backgroundSize,
                )
                .offset(y = yPosition.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = "${items[index].background}",
                contentDescription = "",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.alpha(alpha)

            )
        }

        Column(modifier = Modifier.fillMaxSize()) {

            Text(
                text = "${items[index].displayName?.uppercase(locale = Locale.getDefault())}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .alpha(textAlpha)
                    .graphicsLayer {
                        scaleY = textSize
                        scaleX = textSize
                    },
                style = MaterialTheme.typography.displaySmall.copy(
                    color = Color.White,
                    fontFamily = FontFamily(
                        Font(
                            R.font.valorant_font,
                            weight = FontWeight.Bold
                        )
                    ),
                    textAlign = TextAlign.Center
                ),


                )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = boxSize,
                        scaleY = boxSize
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = "${items[index].fullPortrait}",
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight

                )
            }


        }


    }


}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AgentsPicker(items: List<Agent>, listState: PagerState) {
    val scope = rememberCoroutineScope()

    val screenWidth = LocalConfiguration.current.screenWidthDp / 3

    fun onTop(index: Int) {
        scope.launch {
            listState.animateScrollToPage(index)
        }
    }


    HorizontalPager(
        pageCount = Int.MAX_VALUE,
        state = listState,
        contentPadding = PaddingValues(horizontal = screenWidth.dp)
    ) {
        val pageOffset = (listState.currentPage - it) + listState.currentPageOffsetFraction

        val boxSize = 80f
        val sizeInside by animateFloatAsState(
            targetValue = if (pageOffset == 0f) boxSize else (boxSize / 1.2).toFloat(),
            animationSpec = tween(easing = LinearOutSlowInEasing),
            label = ""
        )

        val borderColor by animateColorAsState(
            targetValue = if (pageOffset == 0f) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.background,
            animationSpec = tween(easing = LinearOutSlowInEasing), label = ""
        )

        val borderWidth by animateIntAsState(
            targetValue = if (pageOffset == 0f) 3 else 0,
            animationSpec = tween(easing = LinearOutSlowInEasing), label = ""
        )

        val index = it % items.size
        Box(
            modifier = Modifier.size(boxSize.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(sizeInside.dp)
                    .background(color = Color(0xFF183040))
                    .border(width = borderWidth.dp, color = borderColor)
                    .clickable { onTop(it) },
                contentAlignment = Alignment.Center


            ) {

                AsyncImage(
                    model = "${items[index].displayIcon}",
                    modifier = Modifier
                        .size(sizeInside.dp)
                        .alpha(if (it == listState.currentPage) 1f else 0.3f),
                    contentScale = ContentScale.Fit,
                    contentDescription = ""
                )

            }
        }

    }


}



