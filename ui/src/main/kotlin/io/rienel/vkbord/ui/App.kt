package io.rienel.vkbord.ui

import io.rienel.vkbord.translator.CharFlow
import io.rienel.vkbord.translator.ITranslator
import io.rienel.vkbord.ui.view.MainForm
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.JLabel

class App(
    private val version: String = "DEV"
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(App::class.java)
    }

    private val _coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        log.error("Uncaught exception.", throwable)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob() + _coroutineExceptionHandler)

    private val mainForm = MainForm("vkbord-${version}")
    private val charFlow: CharFlow by inject(CharFlow::class.java)


    fun start() {
        log.info("Starting application")
        with(mainForm) {
            setSize(600, 400)
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            addWindowListener(object : WindowAdapter() {
                override fun windowClosed(e: WindowEvent?) {
                    onClose()
                }
            })

            val pressed = JLabel("?")
            pressed.setBounds(10, 10, 100, 100)
            coroutineScope.launch {
                charFlow.source.collect {
                    log.info("Received source char: {}", it)
                    pressed.text = it.toString()
                }
            }
            val result = JLabel("?")
            coroutineScope.launch {
                charFlow.translated.collect {
                    log.info("Received translation: {}", it)
                    result.text = it.toString()
                }
            }
            result.setBounds(10, 110, 100, 100)
            add(pressed)
            add(result)

            isVisible = true
        }
        log.info("Application started.")
    }

    private fun onClose() {
        log.info("Closing application")
        coroutineScope.cancel()
        charFlow.close()
        log.info("Application closed")
    }
}