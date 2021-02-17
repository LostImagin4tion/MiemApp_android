package ru.hse.miem.miemapp.presentation.tinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import ru.hse.miem.miemapp.R
import ru.hse.miem.miemapp.domain.repositories.IProjectRepository
import java.util.ArrayList
import javax.inject.Inject


class ProjectsActivity @Inject constructor(
    private val projectRepository: IProjectRepository
): AppCompatActivity() {
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_porjects)

        val cardStackView: CardStackView = findViewById(R.id.card_stack_view)
        manager = CardStackLayoutManager(this)
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.FREEDOM)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(false)
        manager.setSwipeableMethod(SwipeableMethod.Manual)
        manager.setOverlayInterpolator(LinearInterpolator ())
        adapter = CardStackAdapter(addList())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator ()
    }

    private fun addList(): List<ItemModel> {
        val items: ArrayList<ItemModel> = arrayListOf()
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
        items.add(ItemModel (R.drawable.sample1, "Проект #617, Программный (ДКИ)", "Мобильное приложение МИЭМ: Android",
            "Разработчик мобильного приложения (Android)", "Android, Java, Kotlin","Королев Денис"))
        items.add(ItemModel (R.drawable.sample2, "Проект #19111, Программный (ДКИ)", "Мобильное приложение МИЭМ: сервер",
            "back-end разработчик","Python, API","Королев Денис"))
        items.add(ItemModel (R.drawable.sample3, "Проект #616, Программный (ДКИ)", "Мобильное приложение МИЭМ: IOS-версия",
            "Разработчик мобильного приложения (IOS)","Swift","Федоров Тимофей"))
        items.add(ItemModel (R.drawable.sample4, "Проект #218, Программный (внешние)",
            "Разработка глоссария физических терминов для мультилингваперевода задач по физике различной сложности для иностранных слушателей подготовительных отделений",
            "Аналитик/ разработчик","C/C++, Python","Гузенкова Александра"))
        items.add(ItemModel (R.drawable.sample5, "Проект #207, Программно-аппаратный (ДЭИ)",
            "Программно-аппаратный комплекс проведения входного контроля источников вторичного электропитания ",
            "программист","C/C++, Python","Королев Павел"))
//        items.add(ItemModel (R.drawable.sample3, "Sukijah", "27", "Jonggol"))
//        items.add(ItemModel (R.drawable.sample4, "Markobar", "19", "Bandung"))
//        items.add(ItemModel (R.drawable.sample5, "Marmut", "25", "Hutan"))
//
//        items.add(ItemModel (R.drawable.sample1, "Markonah", "24", "Jember"))
//        items.add(ItemModel (R.drawable.sample2, "Marpuah", "20", "Malang"))
//        items.add(ItemModel (R.drawable.sample3, "Sukijah", "27", "Jonggol"))
//        items.add(ItemModel (R.drawable.sample4, "Markobar", "19", "Bandung"))
//        items.add(ItemModel (R.drawable.sample5, "Marmut", "25", "Hutan"))

//        val proj = projectRepository.getProjectById(617);
//        items.add(ItemModel (R.drawable.sample2, , ))

        return items
    }
}