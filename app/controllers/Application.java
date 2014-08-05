package controllers;

import models.Task;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {

        Logger.info("index");

        return ok(index.render("Yo  ur new application is ready."));
    }

}
