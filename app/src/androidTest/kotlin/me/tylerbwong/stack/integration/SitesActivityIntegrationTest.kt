package me.tylerbwong.stack.integration

import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaEditTextInteractions.writeTo
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.settings.sites.SitesActivity
import org.junit.Test

class SitesActivityIntegrationTest : BaseIntegrationTest<SitesActivity>(SitesActivity::class.java) {
    @Test
    fun testFilters() {
        listOf(
            R.string.all_sites,
            R.string.main_sites,
            R.string.meta_sites
        ).forEach {
            clickOn(R.id.filter)
            clickOn(it)
        }
    }

    @Test
    fun testSearch() {
        clickOn(R.id.search)
        writeTo(androidx.appcompat.R.id.search_src_text, "Stack")
        waitForRequest(waitTimeMillis = 1_000L)
        clickOn(androidx.appcompat.R.id.search_close_btn)
    }
}
