package ru.oceancraft.fastcard.presentation

import com.github.terrakok.cicerone.Router
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.dmdev.rxpm.PresentationModel
import me.dmdev.rxpm.action
import me.dmdev.rxpm.state
import ru.oceancraft.fastcard.model.Card
import ru.oceancraft.fastcard.model.CardDao
import kotlin.properties.Delegates

class CardViewPM(
    private val dao: CardDao,
    private val router: Router
) : PresentationModel() {
    var cardId by Delegates.notNull<Long>()

    val card = state<Card>()
    val delete = action<Unit>()

    override fun onCreate() {
        super.onCreate()

        dao.getById(cardId)
            .subscribeOn(Schedulers.io())
            .subscribe { it ->
                card.consumer.accept(it)
            }
            .untilDestroy()

        delete.observable
            .flatMapSingle {
                dao.delete(cardId)
                    .toSingleDefault(Unit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
            .subscribe {
                router.exit()
            }
            .untilUnbind()
    }
}