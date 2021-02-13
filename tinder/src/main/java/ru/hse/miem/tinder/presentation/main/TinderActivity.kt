package ru.hse.miem.tinder.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import ru.hse.miem.miemapp.MiemApplication
import ru.hse.miem.miemapp.presentation.search.ProjectsAdapter
import ru.hse.miem.miemapp.presentation.search.SearchFragmentDirections
import ru.hse.miem.tinder.R
import ru.hse.miem.tinder.dagger.DaggerTinderComponent
import ru.hse.miem.tinder.dagger.TinderComponent
import ru.hse.miem.tinder.presentation.cards.CardStackAdapter
import ru.hse.miem.tinder.presentation.cards.ItemModel
import java.util.*
import ru.hse.miem.miemapp.data.repositories.ProjectRepository
import ru.hse.miem.miemapp.data.api.CabinetApi


class TinderActivity : AppCompatActivity() {
//    private val projectRepository = ProjectRepository()

    private val TAG = "MainActivity"
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter

    private lateinit var tinderComponent: TinderComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyLogs", "OK")
        tinderComponent = DaggerTinderComponent.builder()
            .appComponent((application as MiemApplication).appComponent)
            .build()

        tinderComponent.inject(this)
        super.onCreate(savedInstanceState)
        Log.d("MyLogs", "OK1")
        setContentView(R.layout.activity_tinder)

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
        manager.setSwipeableMethod(SwipeableMethod.Manual)
        manager.setOverlayInterpolator(LinearInterpolator ())
        adapter = CardStackAdapter(addList())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator ()

    }

//    fun paginate() {
//        val old: List<ItemModel>  = adapter.getItems();
//        val baru: List<ItemModel> = addList()
//        val callback: CardStackCallback = CardStackCallback(old, baru);
//        val hasil: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback);
//        adapter.setItems(baru);
//        hasil.dispatchUpdatesTo(adapter);
//    }

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
        items.add(ItemModel (R.drawable.sample1, "Markonah", "24", "Jember"))
        items.add(ItemModel (R.drawable.sample2, "Marpuah", "20", "Malang"))
        items.add(ItemModel (R.drawable.sample3, "Sukijah", "27", "Jonggol"))
        items.add(ItemModel (R.drawable.sample4, "Markobar", "19", "Bandung"))
        items.add(ItemModel (R.drawable.sample5, "Marmut", "25", "Hutan"))

        items.add(ItemModel (R.drawable.sample1, "Markonah", "24", "Jember"))
        items.add(ItemModel (R.drawable.sample2, "Marpuah", "20", "Malang"))
        items.add(ItemModel (R.drawable.sample3, "Sukijah", "27", "Jonggol"))
        items.add(ItemModel (R.drawable.sample4, "Markobar", "19", "Bandung"))
        items.add(ItemModel (R.drawable.sample5, "Marmut", "25", "Hutan"))
        return items
    }

}