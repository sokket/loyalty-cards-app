package ru.oceancraft.fastcard.presentation

import com.github.terrakok.cicerone.Router
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.dmdev.rxpm.PresentationModel
import me.dmdev.rxpm.action
import me.dmdev.rxpm.state
import me.dmdev.rxpm.widget.inputControl
import ru.oceancraft.fastcard.Screens
import ru.oceancraft.fastcard.model.Card
import ru.oceancraft.fastcard.model.CardDao

class NewCardPM(
    private val router: Router,
    private val dao: CardDao,
    defaultColor: Int
) : PresentationModel() {
    lateinit var type: String
    lateinit var data: String

    val color = state(defaultColor)
    val pickColor = action<Int>()
    val save = action<Unit>()
    val name = inputControl()

    override fun onBind() {
        super.onBind()

        pickColor.observable
            .subscribe {
                color.consumer.accept(it)
            }
            .untilDestroy()

        save.observable
            .flatMapSingle {
                dao.insert(
                    Card(
                        name = name.text.value,
                        color = color.value,
                        type = type,
                        data = data
                    )
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
            .subscribe {
                router.exit()
            }
            .untilDestroy()

    }
}