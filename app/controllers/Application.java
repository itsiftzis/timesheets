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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static play.data.Form.form;

public class Application extends Controller {

    public static Result index() {
        return ok(views.html.index.render("welcome"));
    }

    @play.mvc.Security.Authenticated(Secured.class)
    public static Result totallogs() {
        return ok(views.html.totallogsview.render(showAdminLogo()));
    }

    @play.mvc.Security.Authenticated(Secured.class)
    public static Result indexUser() {
        return ok(views.html.userview.render());
    }

    private static String showAdminLogo() {
        User user = SessionManager.get("user");
        String display = user.getUserName().equals("admin") ? "inline" : "none";
        return display;
    }

    @play.mvc.Security.Authenticated(Secured.class)
    public static Result indexWorkLog() {
        return ok(views.html.worklogview.render(showAdminLogo()));
    }

    public static Result userEdit() {
        User user = User.userByUsername("username");
        return ok(views.html.user.render(user));
    }

    public static Result worklog(String userName) {
        List<WorkLog> workLogs = WorkLog.all();
        Logger.info("found worklogs for user " + userName + " " + workLogs.size());
        return ok(Json.toJson(workLogs));
    }

    public static Result user(String userName) {
        User user = User.userByUsername(userName);
        return ok(Json.toJson(user));
    }

    @play.mvc.Security.Authenticated(Secured.class)
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
    public static Result serveJson() {
        JsonNode json = request().body().asJson();
        return ok(json);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result updateWorkLog() {
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            WorkLog worklog = mapper.readValue(json.toString(), WorkLog.class);
            WorkLog.update(worklog);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }

        return ok("updated worklog");
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

    @BodyParser.Of(BodyParser.Json.class)
    public static Result deleteWorkLog() {
        JsonNode json = request().body().asJson();
        String value = ((JsonNode)json.elements().next()).textValue();
        WorkLog.delete(value);
        return ok("deleted worklog");
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

    @play.mvc.Security.Authenticated(Secured.class)
    public static Result workLogs() {
        User user = SessionManager.get("user");
        if (user == null || user.getUserName().equals(""))
            return badRequest("user not logged in");
        List<WorkLog> workLogs = WorkLog.worklogPerUser(user.getUserName());
        Gson gson = new Gson();
        String json = gson.toJson(workLogs);
        return ok(json);
    }

    @play.mvc.Security.Authenticated(Secured.class)
    public static Result allWorkLogs() {
        List<WorkLog> workLogs = WorkLog.all();
        Gson gson = new Gson();
        String json = gson.toJson(workLogs);
        return ok(json);
    }

    public static Result fetchWorklog(String id) {
        WorkLog workLog = WorkLog.fetchWorklog(id);
        Gson gson = new Gson();
        String json = gson.toJson(workLog);
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

    public static Result downloadCsv(String user, String period) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        StringBuffer stringBuffer = new StringBuffer();
        try {
            Date parsedDate = sdf.parse(period);
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(parsedDate);
            startDate.set(Calendar.DAY_OF_MONTH, 0);

            Calendar endDate = Calendar.getInstance();
            endDate.setTime(parsedDate);
            endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DATE));

            if (user==null || user.equals("") || user.equals("undefined"))
                user = "all";

            Logger.debug("User: " + user + " Start date:" + startDate.getTime() + " End date:" + endDate.getTime());

            List<WorkLog> workLogs = WorkLog.getReport(startDate.getTime(), endDate.getTime(), user);
            stringBuffer.append("username,").append("date,").append("totalHours,").append("Project Client,").append("Project Name,")
                    .append("Project Component,").append("Hours,").append("Region").append("\n");
            for (WorkLog worklog:workLogs) {
                stringBuffer.append(worklog.getUserName()).append(",");
                stringBuffer.append(worklog.getDateLog()).append(",");
                stringBuffer.append(worklog.getTotalHours()).append(",");
                for (Project project:worklog.getProjects()) {
                    stringBuffer.append("\n");
                    stringBuffer.append(",").append(",").append(",").append(project.getClient()).append(",");
                    stringBuffer.append(project.getName()).append(",");
                    stringBuffer.append(project.getComponent()).append(",");
                    stringBuffer.append(project.getHours()).append(",");
                    stringBuffer.append(project.getRegion());
                }
                stringBuffer.append("\n");
            }

        } catch (ParseException e) {
            Logger.error("error parsing date", e);
        }
        response().setContentType("text/csv; charset=utf-8");
        response().setHeader("Content-Disposition", "attachment; filename=report_" + user +"_" + period +".csv");
        return ok(stringBuffer.toString()).as("text/csv");
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

            User user = User.userByUsername(loginForm.get().userName);
            if (user != null && user.getPassword().equals(loginForm.get().password)) {
                SessionManager.addSession("user", user);
                return redirect(controllers.routes.Application.indexWorkLog());
            }  else
                return badRequest(views.html.login.render(loginForm));

        }
    }
}
