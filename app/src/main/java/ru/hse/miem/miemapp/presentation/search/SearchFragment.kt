package ru.hse.miem.miemapp.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_bottom_filters.*
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectInSearch
import ru.hse.miem.miemapp.domain.entities.UserGitStatistics
import ru.hse.miem.miemapp.presentation.OnBackPressListener
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import java.util.*
import javax.inject.Inject

class SearchFragment : BaseFragment(R.layout.fragment_search), SearchView, OnBackPressListener {

    @Inject
    @InjectPresenter
    lateinit var searchPresenter: SearchPresenter

    @ProvidePresenter
    fun provideSearchPresenter() = searchPresenter

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val projectsAdapter = ProjectsAdapter {
        val action = SearchFragmentDirections.actionFragmentSearchToFragmentProject(it)
        findNavController().navigate(action)
    }

    private val filters = SearchFilters()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        projectsList.adapter = projectsAdapter
        if (projectsAdapter.hasData) {
            searchLoader.visibility = View.GONE
            projectsList.visibility = View.VISIBLE
        }

        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterResults()
                true
            } else {
                false
            }
        }
        bottomSheetBehavior = BottomSheetBehavior.from(filtersLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    restoreFilters()
                }
            }
        })

        filterButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        showResultsButton.setOnClickListener {
            filterResults()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        tinderButton.setOnClickListener {
            findNavController().navigate(R.id.fragmentTinder)
        }

        searchPresenter.onCreate()
    }

    override fun onBackPressed(): Boolean {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            return true
        }
        return false

    }

    private fun filterResults() {
        filters.projectType = projectTypeSelector.selectedItemPosition
        filters.projectTypeName = projectTypeSelector.selectedItem as String

        filters.projectState = projectStateSelector.selectedItemPosition
        filters.projectStateName = projectStateSelector.selectedItem as String

        filters.isAvailableVacancies = withVacanciesCheckbox.isChecked

        (projectsList.adapter as ProjectsAdapter?)?.performSearch(
            searchInput.text
                .toString()
                .lowercase(Locale.getDefault())
                .trim(),
            filters
        )
    }

    private fun restoreFilters() {
        projectTypeSelector.setSelection(filters.projectType)
        projectStateSelector.setSelection(filters.projectState)
        withVacanciesCheckbox.isChecked = filters.isAvailableVacancies
    }

    override fun setupProjects(projects: List<ProjectInSearch>) {
        projectsAdapter.update(projects)

        val defaultFilterValue = getString(R.string.search_filters_selector_default)
        projectTypeSelector.adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_simple,
            listOf(defaultFilterValue) + projects.map { it.type }.toSet().toList()
        )
        projectStateSelector.adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_simple,
            listOf(defaultFilterValue) + projects.map { it.state }.toSet().toList()
        )

        restoreFilters()
        searchLoader.visibility = View.GONE
        projectsList.visibility = View.VISIBLE
    }
}