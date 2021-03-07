package ru.oceancraft.fastcard.di

import android.content.Context
import androidx.core.content.ContextCompat
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import ru.oceancraft.fastcard.R
import ru.oceancraft.fastcard.model.CardDao
import ru.oceancraft.fastcard.presentation.NewCardPM

@Module
class NewCardModule {

    @Provides
    @FragmentScope
    fun pm(router: Router, cardDao: CardDao, context : Context) =
        NewCardPM(router, cardDao, ContextCompat.getColor(context, R.color.card_default))

}