package ru.oceancraft.fastcard.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.doOnLayout
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import dev.chrisbanes.insetter.applySystemWindowInsetsToMargin
import me.dmdev.rxpm.base.PmFragment
import me.dmdev.rxpm.bindTo
import ru.oceancraft.fastcard.Dagger
import ru.oceancraft.fastcard.R
import ru.oceancraft.fastcard.databinding.FragmentViewBinding
import ru.oceancraft.fastcard.presentation.CardViewPM
import javax.inject.Inject

class ViewFragment : PmFragment<CardViewPM>() {

    private var _binding: FragmentViewBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var pm: CardViewPM

    override fun providePresentationModel(): CardViewPM = pm

    override fun onBindPresentationModel(pm: CardViewPM) {
        pm.card bindTo { card ->
            binding.codeData.text = card.data
            val encoder = BarcodeEncoder()
            val format = BarcodeFormat.valueOf(card.type)
            if (format == BarcodeFormat.QR_CODE) {
                with(binding.codeView) {
                    layoutParams.height = 300.toPx().toInt()
                    requestLayout()
                }
            }
            binding.codeView.doOnLayout {
                val bitmap =
                    encoder.encodeBitmap(card.data, format, it.measuredWidth, it.measuredHeight)
                binding.codeView.setImageBitmap(bitmap)
            }
        }
    }

    private fun Int.toPx(): Float {
        return this * requireContext().resources.displayMetrics.density
    }

    private fun setBrightnessParam(value: Float) {
        val window = requireActivity().window
        val attrs = window.attributes
        attrs.screenBrightness = value
        window.attributes = attrs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Dagger.inject(this)
        pm.cardId = arguments?.getLong(CARD_ID_ARG) ?: 0L
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val mode = context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        if (mode == Configuration.UI_MODE_NIGHT_YES) {
            WindowInsetsControllerCompat(requireActivity().window, requireView()).isAppearanceLightStatusBars = true
        }
        setBrightnessParam(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL)
    }

    override fun onStop() {
        super.onStop()
        val mode = context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        if (mode == Configuration.UI_MODE_NIGHT_YES) {
            WindowInsetsControllerCompat(requireActivity().window, requireView()).isAppearanceLightStatusBars = false
        }
        setBrightnessParam(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewBinding.inflate(inflater, container, false)
        with(binding.menuButton) {
            applySystemWindowInsetsToMargin(top = true)
            setOnClickListener {
                val popup = PopupMenu(requireContext(), this)
                popup.inflate(R.menu.card_menu)
                popup.gravity = Gravity.END
                popup.setOnMenuItemClickListener {
                    if (it.itemId == R.id.card_menu_delete)
                        presentationModel.delete.consumer.accept(Unit)
                    true
                }
                popup.show()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CARD_ID_ARG = "card_id"

        @JvmStatic
        fun newInstance(cardId: Long) =
            ViewFragment().apply {
                arguments = Bundle().apply {
                    putLong(CARD_ID_ARG, cardId)
                }
            }
    }
}