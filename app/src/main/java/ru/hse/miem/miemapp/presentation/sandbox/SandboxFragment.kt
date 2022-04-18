package ru.hse.miem.miemapp.presentation.sandbox

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_sandbox.*
import kotlinx.android.synthetic.main.fragment_sandbox.filterButton
import kotlinx.android.synthetic.main.fragment_sandbox.projectsList
import kotlinx.android.synthetic.main.fragment_sandbox.searchInput
import kotlinx.android.synthetic.main.fragment_sandbox.searchLoader
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_bottom_filters.*
import kotlinx.android.synthetic.main.layout_bottom_sandbox_filters.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectInSandbox
import ru.hse.miem.miemapp.presentation.OnBackPressListener
import ru.hse.miem.miemapp.presentation.apps.AppsFragmentDirections
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import java.util.*
import javax.inject.Inject

class SandboxFragment: BaseFragment(R.layout.fragment_sandbox), SandboxView, OnBackPressListener {

    @Inject
    @InjectPresenter
    lateinit var sandboxPresenter: SandboxPresenter

    @ProvidePresenter
    fun provideSandboxPresenter() = sandboxPresenter

    private lateinit var filtersSheetBehavior: BottomSheetBehavior<View>
    private val projectsAdapter = SandboxAdapter {
        val action = SandboxFragmentDirections.actionFragmentSandboxToFragmentProject(it)
        findNavController().navigate(action)
    }
    private val filters = SandboxFilters()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        projectsList.adapter = projectsAdapter
        if(projectsAdapter.hasData) {
            searchLoader.visibility = View.GONE
            projectsList.visibility = View.VISIBLE
        }

        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterResults()
                true
            }
            else {
                false
            }
        }
        filtersSheetBehavior = BottomSheetBehavior.from(sandboxFiltersLayout)
        filtersSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        filtersSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    restoreFilters()
                }
                else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    sandboxLayoutContent.foreground = ColorDrawable(
                        resources.getColor(R.color.transparent)
                    )
                }
            }
        })

        filterButton.setOnClickListener {
            sandboxLayoutContent.foreground = ColorDrawable(
                resources.getColor(R.color.semi_transparent)
            )
            filtersSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        hideSandboxFiltersButton.setOnClickListener {
            sandboxLayoutContent.foreground = ColorDrawable(
                resources.getColor(R.color.transparent)
            )
            filtersSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        sandboxShowResultsButton.setOnClickListener {
            filterResults()
            sandboxLayoutContent.foreground = ColorDrawable(
                resources.getColor(R.color.transparent)
            )
            filtersSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        sandboxPresenter.onCreate()
    }

    override fun onBackPressed(): Boolean {
        if(filtersSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
            sandboxLayoutContent.foreground = ColorDrawable(
                resources.getColor(R.color.transparent)
            )
            filtersSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            return true
        }
        return false
    }

    private fun filterResults() {
        filters.projectType = sandboxProjectTypeSelector.selectedItemPosition
        filters.projectTypeName = sandboxProjectTypeSelector.selectedItem as String

        filters.projectState = sandboxProjectStateSelector.selectedItemPosition
        filters.projectStateName = sandboxProjectStateSelector.selectedItem as String

        filters.isAvailableVacancies = sandboxWithVacanciesCheckbox.isChecked

        (projectsList.adapter as SandboxAdapter?)?.performSearch(
            searchInput.text
                .toString()
                .lowercase(Locale.getDefault())
                .trim(),
            filters
        )
    }

    private fun restoreFilters() {
        sandboxProjectTypeSelector.setSelection(filters.projectType)
        sandboxProjectStateSelector.setSelection(filters.projectState)
        sandboxWithVacanciesCheckbox.isChecked = filters.isAvailableVacancies
    }

    override fun setupSandbox(projects: List<ProjectInSandbox>) {
        projectsAdapter.update(projects)

        val defaultFilterValue = getString(R.string.search_filters_selector_default)
        sandboxProjectTypeSelector.adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_simple,
            listOf(defaultFilterValue) + projects.map { it.type }.toSet().toList()
        )
        sandboxProjectStateSelector.adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_simple,
            listOf(defaultFilterValue) + projects.map { it.state }.toSet().toList()
        )

        restoreFilters()
        searchLoader.visibility = View.GONE
        projectsList.visibility = View.VISIBLE
    }
}