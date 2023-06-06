package sword.sample.accompanist

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Official Doc: https://google.github.io/accompanist/pager/
 * Github: https://github.com/google/accompanist/tree/main/pager
 *
 * repository:  mavenCentral()
 * dependencies:  "com.google.accompanist:accompanist-pager:<version>"
 */
class PagerLayout {


	@Preview(showBackground = true)
	@ExperimentalPagerApi
	@Composable
	fun HorizontalPagerSample() {
		val pagerState = rememberPagerState()
		HorizontalPager(
			count = 10,
			contentPadding = PaddingValues(horizontal = 64.dp),
			state = pagerState
		) { page ->
			Text(
				text = "Page: $page",
				modifier = Modifier.fillMaxSize()
			)
		}
		
		LaunchedEffect(Unit) {
			delay(1000)
			pagerState.scrollToPage(2)
		}

	}

	@ExperimentalPagerApi
	@Composable
	fun HorizontalPagerWidthIndicators() {
		Scaffold(
			topBar = {
				TopAppBar(
					title = { Text(text = "horizontal indicator") },
					backgroundColor = MaterialTheme.colors.surface
				)
			},
			modifier = Modifier.fillMaxSize()
		) {
			Column(Modifier.fillMaxSize()) {
				val pageState = rememberPagerState()

				HorizontalPager(
					count = 10,
					state = pageState,
					contentPadding = PaddingValues(horizontal = 32.dp),
					modifier = Modifier
						.weight(1f)
						.fillMaxHeight()
				) { page ->
					PagerItem(
						page = page,
						modifier = Modifier
							.fillMaxWidth()
							.aspectRatio(1f)
					)

					HorizontalPagerIndicator(
						pagerState = pageState,
						modifier = Modifier
							.align(Alignment.CenterHorizontally)
							.padding(16.dp)
					)

					ActionsRow(
						pagerState = pageState,
						modifier = Modifier.align(Alignment.CenterHorizontally)
					)

				}
			}
		}
	}

	@ExperimentalPagerApi
	@Composable
	fun HorizontalPagerWithTab() {
		Scaffold(
			topBar = { TopAppBar(title = { Text(text = "Tab Title") }, backgroundColor = MaterialTheme.colors.surface) },
			modifier = Modifier.fillMaxSize()
		) {
			val pages = remember { listOf("Home", "Shows", "Movies", "Books", "Really long movies", "Short audiobooks") }

			Column(Modifier.fillMaxSize()) {
				val coroutineScope = rememberCoroutineScope()
				val pagerState = rememberPagerState()

				ScrollableTabRow(
					selectedTabIndex = pagerState.currentPage,
					indicator = { tabPositions ->
						//默认指示器，显示在 TabRow 的底部，Pager 和 TabRow 分割线的顶部
						//pagerTabIndicatorOffset 定义指示器如何布局，用于 TabRow（或 ScrollableTabRow）  与 HorizontalPager（或 VerticalPager） 的同步
						TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, tabPositions))
					}
				) {
					pages.forEachIndexed { index, title ->
						Tab(text = { Text(title) }, selected = pagerState.currentPage == index, onClick = {
							coroutineScope.launch {
								pagerState.animateScrollToPage(index)
							}
						})
					}
				}
				
				HorizontalPager(count = pages.size, state = pagerState, contentPadding = PaddingValues(16.dp), modifier = Modifier
					.weight(1f)
					.fillMaxWidth()) { page ->
					Card {
						Box(Modifier.fillMaxSize()) {
							Text(
								text = "Page: ${pages[page]}",
								style = MaterialTheme.typography.h4,
								modifier = Modifier.align(Alignment.Center)
							)
						}
					}
				}
			}
		}
	}

	@Composable
	fun PagerItem(page: Int, modifier: Modifier = Modifier) {
	}

	@ExperimentalPagerApi
	@Composable
	fun ActionsRow(pagerState: PagerState, modifier: Modifier = Modifier) {

	}
}
