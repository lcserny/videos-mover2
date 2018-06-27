package net.cserny.videosmover

import com.sun.javafx.application.LauncherImpl
import javafx.application.Application
import javafx.application.Preloader
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import net.cserny.videosmover.controller.MainController
import net.cserny.videosmover.error.GlobalExceptionCatcher
import net.cserny.videosmover.provider.MainStageProvider
import net.cserny.videosmover.service.MessageDisplayProvider
import javax.inject.Inject

fun main(args: Array<String>) {
    LauncherImpl.launchApplication(MainApplicationKt::class.java, MainPreloaderKt::class.java, args)
}

class MainPreloaderKt : Preloader() {
    override fun start(primaryStage: Stage?) {
        val splashPane = Pane(ImageView(Image(this::class.java.getResourceAsStream("/images/loading.gif"))))
        primaryStage?.scene = Scene(splashPane, Color.TRANSPARENT)
        primaryStage?.initStyle(StageStyle.TRANSPARENT)
        primaryStage?.isAlwaysOnTop = true
        primaryStage?.centerOnScreen()
        primaryStage?.show()
    }

    override fun handleStateChangeNotification(info: StateChangeNotification?) {
        TODO("don't know how to get stage")
    }
}

class MainApplicationKt : Application() {
    val TITLE = "Downloads VideoMover"

    @Inject
    lateinit var exceptionCatcher: GlobalExceptionCatcher

    @Inject
    lateinit var stageProvider: MainStageProvider

    @Inject
    lateinit var controller: MainController

    @Inject
    lateinit var messageDisplayProvider: MessageDisplayProvider

    lateinit var parent: Parent

    private fun initContext() {
        val applicationComponent = DaggerApplicationComponent.create()
        applicationComponent.inject(this)

        Thread.setDefaultUncaughtExceptionHandler(exceptionCatcher)
    }

    override fun init() {
        initContext()

        val loader = FXMLLoader(this::class.java.getResource("/fxml/main.fxml"))
        loader.setController(controller)
        parent = loader.load()
    }

    override fun start(primaryStage: Stage?) {
        primaryStage?.scene = Scene(parent)
        primaryStage?.title = TITLE
        primaryStage?.isResizable = false
        primaryStage?.icons?.add(Image(this::class.java.getResourceAsStream("images/application.png")))
        primaryStage?.centerOnScreen()
        primaryStage?.show()
    }
}