package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

import com.example.ui.theme.*

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    composeTestRule.setContent { MyApplicationTheme { Greeting("Robolectric") } }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}

@androidx.compose.runtime.Composable
fun Greeting(name: String) {
  androidx.compose.material3.Card(
    colors = androidx.compose.material3.CardDefaults.cardColors(
      containerColor = DeepGraphite
    )
  ) {
    androidx.compose.foundation.layout.Column(
      modifier = androidx.compose.foundation.layout.Modifier.padding(24.dp)
    ) {
      androidx.compose.material3.Text(
        text = "BarberFlow Verificado por $name 💈",
        color = BarberGold,
        fontSize = 18.sp,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
      )
      androidx.compose.material3.Text(
        text = "Interface modernizada para lucros de barbearias brasileiras.",
        color = CleanWhite,
        fontSize = 13.sp,
        modifier = androidx.compose.foundation.layout.Modifier.padding(top = 8.dp)
      )
    }
  }
}
