package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Login;
import models.Project;
import models.User;
import models.WorkLog;
import org.codehaus.jackson.map.ObjectMapper;
import play.*;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import static play.data.Form.form;

public class Application extends Controller {

    public static Result index() {
        return ok(views.html.index.render("welcome"));
    }

    @play.mvc.Security.Authenticated(Secured.class)
    public static Result indexUser() {
        return ok(views.html.userview.render());
    }

    public static Result indexWorkLog() {
        return ok(views.html.worklogview.render());
    }

    public static Result userEdit() {
        User user = User.userByEmail("username");
        return ok(views.html.user.render(user));
    }

    public static Result worklog(String userName) {
        List<WorkLog> workLogs = WorkLog.all();
        Logger.info("found worklogs for user " + userName + " " + workLogs.size());
        return ok(Json.toJson(workLogs));
    }

    public static Result user(String userName) {
        User user = User.userByEmail(userName);
        return ok(Json.toJson(user));
    }

    public static Result users() {
        List<User> users = User.all();
        Gson gson = new Gson();
        String json = gson.toJson(users);
        return ok(json);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result insertUser() {
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = mapper.readValue(json.toString(), User.class);
            user.create(user);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }

        return ok("inserted user");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result updateUser() {
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = mapper.readValue(json.toString(), User.class);
            user.update(user);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }

        return ok("updated user");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result deleteUser() {
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = mapper.readValue(json.toString(), User.class);
            user.deleteUser(user);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }

        return ok("deleted user");
    }


    public static Result projects() {
        List<Project> projects = Project.all();
        return ok(Json.toJson(projects  ));
    }

    public static Result project(String projectName) {
        Project project = Project.projectByName(projectName);
        return ok(Json.toJson(project));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result insertProject() {
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Project project = mapper.readValue(json.toString(), Project.class);
            project.create(project);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }

        return ok("inserted project");
    }

    public static Result workLogs() {
        List<WorkLog> workLogs = WorkLog.all();
        Gson gson = new Gson();
        String json = gson.toJson(workLogs);
        return ok(json);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result insertWorkLog() {
        User currentUser = SessionManager.get("user");
        if (currentUser == null || currentUser.getUserName().equals(""))
            return badRequest("user not logged in");
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            WorkLog workLog = mapper.readValue(json.toString(), WorkLog.class);
            workLog.setUser(currentUser.getUserName());
            WorkLog.create(workLog);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }

        return ok("inserted workLog");
    }

    public static Result login() {
        return ok(views.html.login.render(form(Login.class)));
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        Logger.debug("user logged out");
        return redirect(
                controllers.routes.Application.login()
        );
    }

    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            Logger.info("form errors " + loginForm.errors());
            return badRequest(views.html.login.render(loginForm));
        } else {
            session().clear();

            User user = User.userByEmail(loginForm.get().email);
            if (user != null && user.getPassword().equals(loginForm.get().password)) {
                SessionManager.addSession("user", user);
                return redirect(controllers.routes.Application.indexWorkLog());
            }  else
                return badRequest(views.html.login.render(loginForm));

        }
    }
}
