package me.tylerbwong.stack.ui.settings.sites

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.text.clearSpans
import androidx.core.text.set
import androidx.core.text.toSpannable
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import coil.ImageLoader
import coil.request.LoadRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.showSnackbar
import me.tylerbwong.stack.ui.utils.toHtml
import javax.inject.Inject

@AndroidEntryPoint
class SitesFragment : PreferenceFragmentCompat(), SearchView.OnQueryTextListener {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val viewModel by viewModels<SitesViewModel>()

    private var searchView: SearchView? = null

    private var snackbar: Snackbar? = null

    private var searchCatalog = listOf<Preference>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        if (savedInstanceState == null) {
            addPreferencesFromResource(R.xml.site_settings)
        }
    }

    @SuppressLint("DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.snackbar.observe(viewLifecycleOwner) {
            if (it != null) {
                view.post {
                    snackbar = view.showSnackbar(
                        R.string.network_error,
                        duration = Snackbar.LENGTH_LONG
                    )
                }
            } else {
                snackbar?.dismiss()
            }
        }
        viewModel.logOutState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SiteLogOutResult.SiteLogOutSuccess -> changeSite(state.siteParameter)
                else -> Unit // No-op
            }
        }
        viewModel.sites.observe(viewLifecycleOwner) { sites ->
            searchCatalog = emptyList()
            searchCatalog = sites.map { site ->
                Preference(requireContext()).apply {
                    title = site.name.toHtml()
                    summary = site.audience.capitalize().toHtml()
                    setOnPreferenceClickListener {
                        if (viewModel.isAuthenticated) {
                            MaterialAlertDialogBuilder(requireContext())
                                .setBackground(
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.default_dialog_bg
                                    )
                                )
                                .setTitle(R.string.log_out_title)
                                .setMessage(R.string.log_out_site_switch)
                                .setPositiveButton(R.string.log_out) { _, _ -> viewModel.logOut(site.parameter) }
                                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                                .create()
                                .show()
                        } else {
                            changeSite(site.parameter)
                        }
                        true
                    }
                    val request = LoadRequest.Builder(requireContext())
                        .crossfade(true)
                        .data(site.iconUrl)
                        .target { icon = it }
                        .build()
                    imageLoader.execute(request)
                }
            }
            refreshPreferences(searchCatalog)
        }
        viewModel.fetchSites()
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentQuery?.let { searchView?.setQuery(it, true) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (menu.findItem(R.id.search) == null) {
            inflater.inflate(R.menu.menu_sites, menu)
        }

        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        // No-op
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val query = newText ?: return false
        val queryWithoutWhitespaces = query.replace(whitespaceRegex, "")
        refreshPreferences(
            searchCatalog.filter { preference ->
                val containsTitle = preference.title
                    .replace(whitespaceRegex, "")
                    .contains(queryWithoutWhitespaces, ignoreCase = true)
                val containsSummary = preference.summary
                    .replace(whitespaceRegex, "")
                    .contains(queryWithoutWhitespaces, ignoreCase = true)
                containsTitle || containsSummary
            },
            query
        )
        return true
    }

    private fun Spannable.highlightSpan(query: String): Spannable {
        val startIndex = indexOf(query, startIndex = 0, ignoreCase = true)
        val endIndex = startIndex + query.length
        if (startIndex >= 0 && endIndex <= length) {
            this[startIndex, endIndex] = StyleSpan(Typeface.BOLD)
        }
        return this
    }

    private fun refreshPreferences(catalog: List<Preference>, query: String? = null) {
        preferenceScreen?.removeAll()
        catalog.forEach { preference ->
            (preference.title as? Spannable)?.clearSpans()
            (preference.summary as? Spannable)?.clearSpans()
            if (query != null) {
                preference.title = preference.title.toSpannable().highlightSpan(query)
                val summary = preference.summary.toSpannable().highlightSpan(query)
                preference.summary = ""
                preference.summary = summary
            }
            preferenceScreen?.addPreference(preference)
        }
    }

    private fun changeSite(siteParameter: String) {
        viewModel.changeSite(siteParameter)
        requireActivity().finish()
    }

    companion object {
        private val whitespaceRegex = "\\s".toRegex()
    }
}
