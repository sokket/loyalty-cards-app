package ru.oceancraft.fastcard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import dev.chrisbanes.insetter.applySystemWindowInsetsToMargin
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import me.dmdev.rxpm.base.PmFragment
import me.dmdev.rxpm.bindTo
import ru.oceancraft.fastcard.Dagger
import ru.oceancraft.fastcard.databinding.FragmentCardsBinding
import ru.oceancraft.fastcard.presentation.CardsPM
import javax.inject.Inject

class CardsFragment : PmFragment<CardsPM>() {

    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var pm: CardsPM

    override fun providePresentationModel(): CardsPM = pm

    override fun onBindPresentationModel(pm: CardsPM) {
        pm.elements bindTo {
            (binding.rv.adapter as CardAdapter).update(it)
        }

        binding.addCardButton.clicks() bindTo pm.addCard

        binding.banner.addButton.clicks() bindTo pm.addCard
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Dagger.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardsBinding.inflate(inflater, container, false)
        binding.appBar.applySystemWindowInsetsToPadding(top = true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addCardButton.applySystemWindowInsetsToMargin(bottom = true)

        with(binding.rv) {
            applySystemWindowInsetsToPadding(top = true, bottom = true)
            layoutManager = LinearLayoutManager(requireContext())

            adapter = CardAdapter(
                layoutInflater,
                backupButtonListener = {
                    presentationModel.backupRestoreButton.consumer.accept(Unit)
                },
                cardListener = {
                    presentationModel.click.consumer.accept(it)
                }
            )
            addItemDecoration(RecyclerViewMargin(requireContext(), 8, 1))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}