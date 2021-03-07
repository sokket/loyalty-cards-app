package ru.oceancraft.fastcard.presentation

import com.github.terrakok.cicerone.Router
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.dmdev.rxpm.PresentationModel
import me.dmdev.rxpm.action
import me.dmdev.rxpm.state
import ru.oceancraft.fastcard.model.Card
import ru.oceancraft.fastcard.model.CardDao
import ru.oceancraft.fastcard.Screens

class CardsPM(
    private val cardDao: CardDao,
    private val router: Router
) : PresentationModel() {

    val backupRestoreButton = action<Unit>()
    val elements = state<List<Card>>(emptyList())
    val addCard = action<Unit>()
    val click = action<Long>()
    val delete = action<Int>()

    override fun onBind() {
        super.onBind()

        delete.observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                cardDao.delete(elements.value[it].id)
            }
            .untilDestroy()

        cardDao.load()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                elements.accept(it)
            }
            .untilDestroy()

        addCard.observable
            .subscribe {
                router.navigateTo(Screens.scan())
            }
            .untilUnbind()

        click.observable
            .subscribe {
                router.navigateTo(Screens.viewCard(it))
            }
            .untilUnbind()

        backupRestoreButton.observable
            .subscribe {
                router.navigateTo(Screens.backupRestore())
            }
            .untilUnbind()
    }

}