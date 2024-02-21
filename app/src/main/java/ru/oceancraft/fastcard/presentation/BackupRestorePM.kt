package ru.oceancraft.fastcard.presentation

import android.content.Context
import android.net.Uri
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.dmdev.rxpm.PresentationModel
import me.dmdev.rxpm.action
import me.dmdev.rxpm.command
import ru.oceancraft.fastcard.model.Card
import ru.oceancraft.fastcard.model.CardDao
import java.io.FileInputStream
import java.io.FileOutputStream

class BackupRestorePM(
    private val dao: CardDao,
    private val context: Context
) : PresentationModel() {

    val backup = action<Unit>()
    val createFile = command<Unit>()
    val fileCreated = action<Uri>()

    val restore = action<Unit>()
    val openFile = command<Unit>()
    val fileOpened = action<Uri>()

    val restoredInfo = command<Int>()
    val error = command<String>()

    override fun onBind() {
        super.onBind()

        backup.observable
            .subscribe {
                createFile.consumer.accept(Unit)
            }
            .untilUnbind()

        fileCreated.observable
            .flatMapSingle { uri ->
                dao.load()
                    .map { it to uri }
                    .subscribeOn(Schedulers.io())
            }
            .flatMapSingle {
                val (cards, uri) = it
                val fileDescriptor = context.contentResolver.openFileDescriptor(uri, "w")
                Single.fromCallable {
                    val fileOutputStream = FileOutputStream(fileDescriptor!!.fileDescriptor)
                    fileOutputStream.write(
                        Json { prettyPrint = true }.encodeToString(cards).toByteArray()
                    )
                    fileOutputStream.close()
                }
                    .subscribeOn(Schedulers.io())
            }
            .subscribe()
            .untilDestroy()

        restore.observable
            .subscribe {
                openFile.consumer.accept(Unit)
            }
            .untilUnbind()

        fileOpened.observable
            .flatMapSingle { uri ->
                val fileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
                Single.fromCallable {
                    FileInputStream(fileDescriptor!!.fileDescriptor).use { fileInputStream ->
                        Json.decodeFromString<List<Card>>(
                            fileInputStream.readBytes().decodeToString()
                        )
                    }
                }
                    .subscribeOn(Schedulers.io())
            }
            .flatMapSingle {
                dao.insertAll(it)
                    .subscribeOn(Schedulers.io())
            }
            .filter { it.isNotEmpty() }
            .map { it.size }
            .doOnError {
                error.consumer.accept(it.localizedMessage ?: it.message ?: "ERROR")
            }
            .retry()
            .subscribe {
                restoredInfo.consumer.accept(it)
            }
            .untilDestroy()
    }
}