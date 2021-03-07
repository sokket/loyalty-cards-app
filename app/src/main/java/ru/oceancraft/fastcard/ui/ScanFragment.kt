package ru.oceancraft.fastcard.ui

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import dev.chrisbanes.insetter.applySystemWindowInsetsToMargin
import ru.oceancraft.fastcard.Dagger
import ru.oceancraft.fastcard.Screens
import ru.oceancraft.fastcard.databinding.FragmentScanBinding
import javax.inject.Inject

class ScanFragment : Fragment() {

    @Inject
    lateinit var router : Router

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private val callback = BarcodeCallback {
        router.replaceScreen(Screens.addCard(it.text, it.barcodeFormat.toString()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Dagger.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        requestPermissions(arrayOf(Manifest.permission.CAMERA), 144)
        with(binding.barcodeView) {
            decoderFactory = DefaultDecoderFactory(BarcodeFormat.values().asList())
            decodeContinuous(callback)
        }
        binding.root.applySystemWindowInsetsToMargin(top = true, bottom = true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeView.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}