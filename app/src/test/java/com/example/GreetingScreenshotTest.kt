package com.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import com.example.data.RiceCellEntity
import com.example.data.RiceEntry
import com.example.ui.RiceCellCard
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    composeTestRule.setContent {
      MyApplicationTheme {
        Column(modifier = Modifier.padding(16.dp)) {
          // White unselected cell
          RiceCellCard(
            cell = RiceCellEntity(id = 1, label = "1", entries = emptyList()),
            onClick = {}
          )
          // Green filled cell with multiple varieties
          RiceCellCard(
            cell = RiceCellEntity(
              id = 2,
              label = "2",
              entries = listOf(
                RiceEntry("ST", 150),
                RiceEntry("LL", 50)
              )
            ),
            onClick = {}
          )
          // Nền cell
          RiceCellCard(
            cell = RiceCellEntity(
              id = 19,
              label = "Nền",
              entries = listOf(RiceEntry("NH", 80))
            ),
            onClick = {}
          )
        }
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}

