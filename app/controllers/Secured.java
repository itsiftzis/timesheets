package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        User user = SessionManager.get("user");
        String userName = "N/A";
        if (user != null)
            userName = user.getUserName();
        Logger.info("user logged in: " + userName);
        return user != null ? ((User)SessionManager.get("user")).getUserName() : null;
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(controllers.routes.Application.login());
    }
}