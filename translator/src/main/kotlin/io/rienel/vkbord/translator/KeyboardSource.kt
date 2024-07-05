package io.rienel.vkbord.translator

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CharFlow(
    private val translator: ITranslator
) : AutoCloseable {
    companion object {
        val log: Logger = LoggerFactory.getLogger(CharFlow::class.java)
    }

    private val _coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        log.error("Uncaught exception.", throwable)
    }

    private val _coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob() + _coroutineExceptionHandler)

    init {
        GlobalScreen.registerNativeHook()
    }

    val source = callbackFlow {
        val listener = object : NativeKeyListener {
            override fun nativeKeyPressed(e: NativeKeyEvent?) {
                if (e == null)
                    return

                log.info(
                    "Received event: {'keyChar'={}, 'keyCode'={}, 'rawCode'={}, 'keyText'={}}",
                    e.keyChar,
                    e.keyCode,
                    e.rawCode,
                    NativeKeyEvent.getKeyText(e.keyCode)
                )
                trySend(NativeKeyEvent.getKeyText(e.keyCode).first())
                    .onFailure { ex ->
                        log.error("Cannot send keyChar: {}", e.keyChar, ex)
                    }
            }
        }
        GlobalScreen.addNativeKeyListener(listener)

        awaitClose {
            GlobalScreen.removeNativeKeyListener(listener)
        }
    }.shareIn(_coroutineScope, SharingStarted.Eagerly, 1)
    val translated = source
        .map(Char::lowercaseChar)
        .map(translator::translate)
        .shareIn(_coroutineScope, SharingStarted.Eagerly, 1)


    override fun close() {
        _coroutineScope.cancel()
        GlobalScreen.unregisterNativeHook()
    }
}