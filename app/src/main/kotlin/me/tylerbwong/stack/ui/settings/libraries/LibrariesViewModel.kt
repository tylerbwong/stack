package me.tylerbwong.stack.ui.settings.libraries

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mikepenz.aboutlibraries.Libs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.toHtml

data class LibraryItem(
    val name: String,
    val author: String,
    val licenseText: String,
)

class LibrariesViewModel : BaseViewModel() {
    val libraries: LiveData<List<LibraryItem>>
        get() = mutableLibraries
    private val mutableLibraries = MutableLiveData<List<LibraryItem>>()

    fun fetchLibraries(context: Context) {
        launchRequest {
            val libraries = withContext(Dispatchers.IO) {
                Libs(context).libraries
                    .filter {
                        it.isOpenSource && it.licenses?.isNotEmpty() == true &&
                                it.licenses?.joinToString("\n") { license ->
                                    license.licenseShortDescription.toHtml().toString()
                                }?.isNotEmpty() == true
                    }
                    .map {
                        LibraryItem(
                            name = it.libraryName,
                            author = it.author,
                            licenseText = it.licenses?.joinToString("\n") { license ->
                                license.licenseShortDescription.toHtml().toString()
                            } ?: ""
                        )
                    }
            }
            mutableLibraries.value = libraries
        }
    }
}
