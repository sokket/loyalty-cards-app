package ru.oceancraft.fastcard.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding3.view.clicks
import dev.chrisbanes.insetter.applySystemWindowInsetsToMargin
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import me.dmdev.rxpm.base.PmFragment
import me.dmdev.rxpm.bindTo
import ru.oceancraft.fastcard.Dagger
import ru.oceancraft.fastcard.R
import ru.oceancraft.fastcard.databinding.FragmentBackupRestoreBinding
import ru.oceancraft.fastcard.databinding.FragmentCardsBinding
import ru.oceancraft.fastcard.presentation.BackupRestorePM
import javax.inject.Inject

class BackupRestoreFragment : PmFragment<BackupRestorePM>() {

    private var _binding: FragmentBackupRestoreBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var pm: BackupRestorePM

    override fun providePresentationModel(): BackupRestorePM = pm

    override fun onBindPresentationModel(pm: BackupRestorePM) {
        binding.backupButton.clicks() bindTo pm.backup
        pm.createFile bindTo { createFile() }

        binding.restoreButton.clicks() bindTo pm.restore
        pm.openFile bindTo { openFile() }

        pm.restoredInfo bindTo {
            Snackbar.make(binding.root, "$it items restored", Snackbar.LENGTH_LONG).show()
        }

        pm.error bindTo {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Dagger.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBackupRestoreBinding.inflate(inflater, container, false)
        binding.root.applySystemWindowInsetsToPadding(top = true)
        return binding.root
    }

    private fun createFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(Intent.EXTRA_TITLE, "backup.json")
        }
        startActivityForResult(intent, 1)
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK)
            return

        if (requestCode == 1) {
            if (data != null) {
                val uri = data.data
                if (uri != null) {
                    presentationModel.fileCreated.consumer.accept(uri)
                }
            }
        } else if (requestCode == 2) {
            if (data != null) {
                val uri = data.data
                if (uri != null) {
                    presentationModel.fileOpened.consumer.accept(uri)
                }
            }
        }
    }
}