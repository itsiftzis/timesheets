import com.google.inject.{Guice, AbstractModule}
import models.User
import play.api.Application
import play.api.GlobalSettings
import services.{SimpleUUIDGenerator, UUIDGenerator}
import play.{Logger}

/**
 * Set up the Guice injector and provide the mechanism for return objects from the dependency graph.
 */
object Global extends GlobalSettings {

  /**
   * Bind types such that whenever UUIDGenerator is required, an instance of SimpleUUIDGenerator will be used.
   */
  val injector = Guice.createInjector(new AbstractModule {
    protected def configure() {
      bind(classOf[UUIDGenerator]).to(classOf[SimpleUUIDGenerator])
    }
  })

  /**
   * Controllers must be resolved through the application context. There is a special method of GlobalSettings
   * that we can override to resolve a given controller. This resolution is required by the Play router.
   */
  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)

  override def onStart(app: Application) {
      def user = User.userByUsername("admin")
      if (user == null || user.getUserName == null) {
        Logger.info("user admin does not exist")
        User.create("admin", "admin", "admin@localhost", "admin", "admin")
        Logger.info("created admin user")
      }
      super.onStart(app)
  }
}
