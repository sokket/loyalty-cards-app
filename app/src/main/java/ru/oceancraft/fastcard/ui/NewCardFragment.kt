package ru.oceancraft.fastcard.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.google.zxing.BarcodeFormat
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.journeyapps.barcodescanner.BarcodeEncoder
import dev.chrisbanes.insetter.applySystemWindowInsetsToMargin
import me.dmdev.rxpm.base.PmFragment
import me.dmdev.rxpm.bindTo
import me.dmdev.rxpm.widget.bindTo
import ru.oceancraft.fastcard.Dagger
import ru.oceancraft.fastcard.R
import ru.oceancraft.fastcard.colorWillBeMasked
import ru.oceancraft.fastcard.databinding.FragmentNewCardBinding
import ru.oceancraft.fastcard.presentation.NewCardPM
import javax.inject.Inject
import kotlin.math.sqrt


class NewCardFragment : PmFragment<NewCardPM>() {

    private var _binding: FragmentNewCardBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var pm: NewCardPM

    override fun providePresentationModel(): NewCardPM = pm

    override fun onCreate(savedInstanceState: Bundle?) {
        Dagger.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onBindPresentationModel(pm: NewCardPM) {
        with(pm) {
            binding.pickerButton.setOnClickListener {
                ColorPickerDialog.Builder(requireContext())
                    .setTitle("Select card color")
                    .setColorListener { color, _ ->
                        pickColor.consumer.accept(color)
                    }.show()
            }

            binding.saveButton.clicks() bindTo save

            name bindTo binding.nameForm

            color bindTo {
                binding.cardView.setCardBackgroundColor(it)
                binding.barcodeData.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        if (colorWillBeMasked(it)) R.color.black else R.color.white
                    )
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val encoder = BarcodeEncoder()
        val data = arguments!!.getString(ARG_DATA)
        binding.barcodeData.text = data
        val type = arguments!!.getString(ARG_FORMAT)!!
        val format = BarcodeFormat.valueOf(type)

        presentationModel.data = data!!
        presentationModel.type = type

        if (format == BarcodeFormat.QR_CODE) {
            with(binding.barcodeImage) {
                layoutParams.height = 300.toPx().toInt()
                requestLayout()
            }
        }

        binding.barcodeImage.doOnLayout {
            println("${it.measuredWidth}x${it.measuredHeight}")
            val bitmap =
                encoder.encodeBitmap(data, format, it.measuredWidth, it.measuredHeight)
            binding.barcodeImage.setImageBitmap(bitmap)
        }

        binding.root.applySystemWindowInsetsToMargin(top = true, bottom = true)
    }

    private fun Int.toPx(): Float {
        return this * requireContext().resources.displayMetrics.density
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_FORMAT = "format"
        private const val ARG_DATA = "data"

        @JvmStatic
        fun newInstance(data: String, format: String) =
            NewCardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DATA, data)
                    putString(ARG_FORMAT, format)
                }
            }
    }
}