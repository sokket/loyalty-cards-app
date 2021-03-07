package ru.oceancraft.fastcard

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.oceancraft.fastcard.ui.*

object Screens {
    fun cards() = FragmentScreen {
        CardsFragment()
    }

    fun addCard(value : String, format : String) = FragmentScreen {
        NewCardFragment.newInstance(value, format)
    }

    fun scan() = FragmentScreen {
        ScanFragment()
    }

    fun viewCard(cardId : Long) = FragmentScreen {
        ViewFragment.newInstance(cardId)
    }

    fun backupRestore() = FragmentScreen {
        BackupRestoreFragment()
    }
}