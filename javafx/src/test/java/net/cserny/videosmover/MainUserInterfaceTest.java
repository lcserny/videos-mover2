//package net.cserny.videosmover;
//
//public class MainUserInterfaceTest extends ApplicationTest {
//    private InMemoryFileSystem videoFileSystemInitializer;
//
//    @Autowired
//    private ConfigurableApplicationContext context;
//
//    @SpyBean
//    private ScanService scanService;
//    @SpyBean
//    private VideoMover videoMover;
//    @SpyBean
//    private MainController mainController;
//
//    @PostConstruct
//    public void initFilesystem() {
//        videoFileSystemInitializer = new InMemoryFileSystem();
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
//        loader.setControllerFactory(context::getBean);
//
//        stage.setScene(new Scene(loader.load()));
//        stage.setTitle(MainApplication.TITLE);
//        stage.setResizable(false);
//        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/application.png")));
//        stage.centerOnScreen();
//        stage.show();
//
//        Thread.setDefaultUncaughtExceptionHandler(context.getBean(GlobalExceptionCatcher.class));
//        context.getBean(MainStageProvider.class).setStage(stage);
//    }
//
//    @BeforeClass
//    public static void setupHeadlessMode() {
//        if (!Boolean.getBoolean("headless")) {
//            System.setProperty("testfx.robot", "glass");
//            System.setProperty("testfx.headless", "true");
//            System.setProperty("prism.order", "sw");
//            System.setProperty("prism.text", "t2k");
//            System.setProperty("java.awt.headless", "true");
//        }
//    }
//
//    @Before
//    public void setUp() throws Exception {
//        videoFileSystemInitializer.setUp();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        videoFileSystemInitializer.tearDown();
//    }
//
//    @Test
//    public void searchButton_scanService() throws Exception {
//        clickOn("#scanButton");
//        verify(scanService).scanVideos(any(String.class));
//    }
//
//    @Test
//    public void moveVideosButton_videoMover() throws Exception {
//        clickOn("#scanButton");
//        NodeQuery lookup = lookup("#tableView").lookup(".table-row-cell");
//        await().until(() -> lookup.query() != null);
//        Node movieCheckOnFirstRow = lookup.nth(0).lookup(".table-cell").nth(1).query();
//        clickOn(movieCheckOnFirstRow);
//        clickOn("#moveButton");
//        verify(videoMover).move(anyListOf(Video.class));
//    }
//
//    @Test
//    public void setDownloadsButton_setDownloads() throws Exception {
//        clickOn("#settingsPane");
//        Thread.sleep(150);
//        clickOn("#setDownloadsButton");
//        verify(mainController).setDownloadsPath(any(ActionEvent.class));
//    }
//
//    @Test
//    public void setMoviesButton_setMovies() throws Exception {
//        clickOn("#settingsPane");
//        Thread.sleep(150);
//        clickOn("#setMoviesButton");
//        verify(mainController).setMoviesPath(any(ActionEvent.class));
//    }
//
//    @Test
//    public void setTvShowsButton_setTvShows() throws Exception {
//        clickOn("#settingsPane");
//        Thread.sleep(150);
//        clickOn("#setTvShowsButton");
//        verify(mainController).setTvShowsPath(any(ActionEvent.class));
//    }
//}
