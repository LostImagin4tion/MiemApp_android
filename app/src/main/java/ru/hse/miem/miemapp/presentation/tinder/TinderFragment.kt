package ru.hse.miem.miemapp.presentation.tinder

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_project.*
import kotlinx.android.synthetic.main.fragment_tinder.*
import kotlinx.android.synthetic.main.item_cards.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.entities.ProjectExtended
import ru.hse.miem.miemapp.presentation.base.BaseFragment
import ru.hse.miem.miemapp.presentation.project.*
import java.util.ArrayList
import javax.inject.Inject

class TinderFragment : BaseFragment(R.layout.fragment_tinder), ProjectView {

    private lateinit var adapter: CardStackAdapter
    private lateinit var manager: CardStackLayoutManager

    companion object {
        fun newInstance() = TinderFragment()
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: TinderPresenter

    @ProvidePresenter
    fun provideProjectPresenter() = presenter

    private val args: ProjectFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as MiemApplication).appComponent.inject(this)
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_tinder, container, false)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.onCreate(617)
//        projectEmail.setOnClickListener {
//            startActivity(
//                Intent.createChooser(
//                    Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", projectEmail.text.toString(), null)),
//                    getString(R.string.send_mail_dialog_title)
//                )
//            )
//        }
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        presenter = ViewModelProvider(this).get(TinderPresenter::class.java)
//
//    }

//    override fun setupProject(project: ProjectExtended) {
//
//    }

    override fun setupProject(project: ProjectExtended) = project.run {
//        projectType.text = getString(R.string.project_type_and_number).format(number, type, source)
//        projectName.text = name
//        projectState.text = state
//        projectState.setBackgroundResource(if (isActive) R.drawable.project_badge_active_bg else R.drawable.project_badge_inactive_bg)
//        projectEmail.text = email

        if (project.isActive){
            Log.d("TinderMyLogs", "Active")
        }

        val items: ArrayList<ItemModel> = arrayListOf()

        items.add(ItemModel (R.drawable.sample1, project.type, project.name,
            "Разработчик мобильного приложения (Android)", "Android, Java, Kotlin","Королев Денис"))

        adapter = CardStackAdapter(items)

        val cardStackView: CardStackView = card_stack_view
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator ()


//        item_type.text = getString(R.string.project_type_and_number).format(number, type, source)
//        item_name.text = name
//
//
//        val imageRegex = Regex("!\\[.+]\\(data:image/.+\\)")
//        projectObjective.text = objective.replace(imageRegex, getString(R.string.projected_embedded_image_error))
//        projectAnnotation.text = annotation.replace(imageRegex, getString(R.string.projected_embedded_image_error))
//
//        membersList.adapter = MembersAdapter(members) { id, isTeacher ->
//            val action = ProjectFragmentDirections.actionFragmentProjectToFragmentProfile(id, isTeacher)
//            findNavController().navigate(action)
//        }
//
//        if (links.isNotEmpty()) {
//            linksList.adapter = LinksAdapter(links)
//            linksNoData.visibility = View.GONE
//            linksList.visibility = View.VISIBLE
//        }
//
//        if (vacancies.isNotEmpty()) {
//            vacanciesList.adapter = VacanciesAdapter(vacancies)
//            vacanciesNoData.visibility = View.GONE
//            vacanciesList.visibility = View.VISIBLE
//        }
//
//        projectOpenInBrowserButton.setOnClickListener {
//            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
//        }
//
//        projectLoader.visibility = View.GONE
//        projectContent.visibility = View.VISIBLE
    }

//    private fun addList(): List<ItemModel> {
//        val items: ArrayList<ItemModel> = arrayListOf()
//        val project = ProjectsAdapter {
//            val action = SearchFragmentDirections.actionFragmentSearchToFragmentProject(it)
//        }
//        var projects = project.getProjects()
//        project.update(projects)
//        projects = project.getProjects()
//        Log.d("MyLogs", projects.size.toString())
//        for (n in 1..projects.size){
//            Log.d("MyLogs", "OK2")
//            items.add(ItemModel(R.drawable.sample1, projects[n].name, projects[n].name, projects[n].name))
//        }
//        items.add(ItemModel (R.drawable.sample1, "Проект #617, Программный (ДКИ)", "Мобильное приложение МИЭМ: Android",
//            "Разработчик мобильного приложения (Android)", "Android, Java, Kotlin","Королев Денис"))
//        items.add(ItemModel (R.drawable.sample2, "Проект #19111, Программный (ДКИ)", "Мобильное приложение МИЭМ: сервер",
//            "back-end разработчик","Python, API","Королев Денис"))
//        items.add(ItemModel (R.drawable.sample3, "Проект #616, Программный (ДКИ)", "Мобильное приложение МИЭМ: IOS-версия",
//            "Разработчик мобильного приложения (IOS)","Swift","Федоров Тимофей"))
//        items.add(ItemModel (R.drawable.sample4, "Проект #218, Программный (внешние)",
//            "Разработка глоссария физических терминов для мультилингваперевода задач по физике различной сложности для иностранных слушателей подготовительных отделений",
//            "Аналитик/ разработчик","C/C++, Python","Гузенкова Александра"))
//        items.add(ItemModel (R.drawable.sample5, "Проект #207, Программно-аппаратный (ДЭИ)",
//            "Программно-аппаратный комплекс проведения входного контроля источников вторичного электропитания ",
//            "программист","C/C++, Python","Королев Павел"))
//        items.add(ItemModel (R.drawable.sample3, "Sukijah", "27", "Jonggol"))
//        items.add(ItemModel (R.drawable.sample4, "Markobar", "19", "Bandung"))
//        items.add(ItemModel (R.drawable.sample5, "Marmut", "25", "Hutan"))
//
//        items.add(ItemModel (R.drawable.sample1, "Markonah", "24", "Jember"))
//        items.add(ItemModel (R.drawable.sample2, "Marpuah", "20", "Malang"))
//        items.add(ItemModel (R.drawable.sample3, "Sukijah", "27", "Jonggol"))
//        items.add(ItemModel (R.drawable.sample4, "Markobar", "19", "Bandung"))
//        items.add(ItemModel (R.drawable.sample5, "Marmut", "25", "Hutan"))
//        return items
//    }

}