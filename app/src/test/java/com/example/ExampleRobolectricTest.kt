package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Ô Lúa", appName)
  }

  @Test
  fun `test rice entry total price calculation`() {
    val entry = com.example.data.RiceEntry(
      variety = "ST25",
      quantity = 10,
      owner = "ANHBA",
      weightKg = 1200,
      price = 8500
    )
    assertEquals(10200000L, entry.totalPrice)
  }
}
