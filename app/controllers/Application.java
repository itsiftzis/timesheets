package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import org.codehaus.jackson.map.ObjectMapper;
import play.*;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
        return ok(views.html.userview.render(showAdminLogo()));
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

    public static Result recentWorklogs(Integer count) {
        User user = SessionManager.get("user");
        List<WorkLog> workLogs = WorkLog.worklogPerUser(user.getUserName(), count);
        Logger.info("found recent worklogs for user " + user.getUserName() + " " + workLogs.size());

        if (count == -1)
            return ok(Json.toJson(workLogs));
        else {
            int counter = 0;
            List<WorkLog> limitedWorklogs = new ArrayList<WorkLog>();
            List<Project> uniqueprojects = new ArrayList<Project>();
            int found = 0;
            for (WorkLog wl : workLogs) {
                for (Project pr : wl.getProjects()) {
                    if (!uniqueprojects.contains(pr) && (found < count)) {
                        uniqueprojects.add(pr);
                        found++;
                    }
                }
            }
            WorkLog finalWl = new WorkLog();
            finalWl.setProjects(uniqueprojects);
            limitedWorklogs.add(finalWl);
            return ok(Json.toJson(limitedWorklogs));
        }
    }

    public static Result allRecentWorklogs(Integer count) {
        List<WorkLog> workLogs = WorkLog.recentWorklogs(count);
        Logger.info("found all recent worklogs " + count + " " + workLogs.size());
        if (count == -1)
            return ok(Json.toJson(workLogs));
        else {
            List<WorkLog> limitedWorklogs = new ArrayList<WorkLog>();
            List<Project> uniqueprojects = new ArrayList<Project>();
            int found = 0;
            for (WorkLog wl : workLogs) {
                for (Project pr : wl.getProjects()) {
                    if (!uniqueprojects.contains(pr) && (found < count)) {
                        uniqueprojects.add(pr);
                        found++;
                    }
                }
            }
            WorkLog finalWl = new WorkLog();
            finalWl.setProjects(uniqueprojects);
            limitedWorklogs.add(finalWl);
            return ok(Json.toJson(limitedWorklogs));
        }
    }

    private static boolean containsPr(List<WorkLog> limitedWorklogs, Project pr) {
        boolean contains = false;
        for (WorkLog lwl:limitedWorklogs) {
            if (lwl.getProjects().contains(pr)) {
                contains = true;
                break;
            }

            else {
                Logger.debug("Adding to " + lwl.getProjects() + " pr " + pr);
            }
        }
        return contains;
    }


    public static Result frequentWorklogs(Integer count) {
        User user = SessionManager.get("user");
        List<WorkLog> workLogs = WorkLog.frequentWorklogs(count, user.getUserName());
        Logger.info("found worklogs " + count + " " + workLogs.size());
        return ok(Json.toJson(workLogs));
    }

    public static Result teamFrequentWorklogs(Integer count) {
        List<WorkLog> workLogs = WorkLog.frequentWorklogs(count, "all");
        Logger.info("found worklogs " + count + " " + workLogs.size());
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

    @BodyParser.Of(BodyParser.Json.class)
    public static Result deleteProjectClient() {
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProjectClient project = mapper.readValue(json.toString(), ProjectClient.class);
            ProjectClient.deleteProjectClient(project);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }
        return ok("deleted project client");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result deleteProjectName() {
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProjectName project = mapper.readValue(json.toString(), ProjectName.class);
            ProjectName.delete(project);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }
        return ok("deleted project name");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result deleteProjectComponent() {
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProjectComponent project = mapper.readValue(json.toString(), ProjectComponent.class);
            ProjectComponent.delete(project);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }
        return ok("deleted project comp");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result fetchNamesForClient() {
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProjectClient project = mapper.readValue(json.toString(), ProjectClient.class);
            List<ProjectName> projects = ProjectName.findByClient(project.getClient());

            Gson gson = new Gson();
            String jsonr = gson.toJson(projects);
            return ok(jsonr);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result fetchComponentsForName() {
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProjectName project = mapper.readValue(json.toString(), ProjectName.class);
            List<ProjectComponent> projects = ProjectComponent.findByNameClient(project.getName(), project.getClient());

            Gson gson = new Gson();
            String jsonr = gson.toJson(projects);
            return ok(jsonr);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }
    }

    public static Result projects() {
        return ok(views.html.projectview.render(showAdminLogo()));
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

    @BodyParser.Of(BodyParser.Json.class)
    public static Result insertProjectClient() {
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProjectClient project = mapper.readValue(json.toString(), ProjectClient.class);
            ProjectClient.create(project);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }

        return ok("inserted project client");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result insertProjectComponent() {
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProjectComponent project = mapper.readValue(json.toString(), ProjectComponent.class);
            ProjectComponent.create(project);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }

        return ok("inserted project client");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result insertProjectName() {
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProjectName project = mapper.readValue(json.toString(), ProjectName.class);
            ProjectName.create(project);
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }

        return ok("inserted project name");
    }

    @play.mvc.Security.Authenticated(Secured.class)
    public static Result workLogs() {
        User user = SessionManager.get("user");
        if (user == null || user.getUserName().equals(""))
            return badRequest("user not logged in");
        List<WorkLog> workLogs = WorkLog.worklogPerUser(user.getUserName(), -1);
        Gson gson = new Gson();
        String json = gson.toJson(workLogs);
        return ok(json);
    }

    public static Result missinghours() {
        return ok(views.html.missinghoursview.render(showAdminLogo()));
    }

    @play.mvc.Security.Authenticated(Secured.class)
    public static Result allWorkLogs() {
        List<WorkLog> workLogs = WorkLog.all();
        Gson gson = new Gson();
        String json = gson.toJson(workLogs);
        return ok(json);
    }

    @play.mvc.Security.Authenticated(Secured.class)
    public static Result projectclients() {
        List<ProjectClient> projectClients = ProjectClient.all();
        Gson gson = new Gson();
        String json = gson.toJson(projectClients);
        return ok(json);
    }

    @play.mvc.Security.Authenticated(Secured.class)
    public static Result projectnames() {
        List<ProjectName> projectNames = ProjectName.all();
        Gson gson = new Gson();
        String json = gson.toJson(projectNames);
        return ok(json);
    }

    @play.mvc.Security.Authenticated(Secured.class)
    public static Result projectcomponents() {
        List<ProjectComponent> projectComponents = ProjectComponent.all();
        Gson gson = new Gson();
        String json = gson.toJson(projectComponents);
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

    @BodyParser.Of(BodyParser.Json.class)
    public static Result batchWorkLog() {
        User currentUser = SessionManager.get("user");
        if (currentUser == null || currentUser.getUserName().equals(""))
            return badRequest("user not logged in");
        JsonNode json = request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            BatchWorkLog workLog = mapper.readValue(json.toString(), BatchWorkLog.class);
            long millis = workLog.getDateLogTo().getTime() - workLog.getDateLogFrom().getTime();
            long days = TimeUnit.DAYS.convert(millis, TimeUnit.MILLISECONDS);
            int iDays = (int)days + 1;
            Logger.info("inserting worklogs for " + iDays + " period");
            for (int i=0; i<iDays; i++) {
                WorkLog workLogBatch = new WorkLog();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(workLog.getDateLogFrom());
                calendar.add(Calendar.DAY_OF_MONTH, i);
                calendar.set(Calendar.HOUR, 18);
                workLogBatch.setDateLog(calendar.getTime());
                workLogBatch.setProjects(workLog.getProjects());
                workLogBatch.setTotalHours(workLog.getTotalHours());
                workLogBatch.setUser(currentUser.getUserName());
                if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && calendar.get(Calendar.DAY_OF_WEEK)
                        != Calendar.SUNDAY)
                    WorkLog.create(workLogBatch);
            }
        } catch (IOException e) {
            Logger.error("Error parsing json ", e);
            return badRequest("error parsing json");
        }

        return ok("inserted workLog");
    }

    public static Result fetchTotalMissingHours() {
        User loggedUser = SessionManager.get("user");
        if (loggedUser == null || loggedUser.getUserName().equals(""))
            return notFound("logged user not found");

        List<WorkLog> missingHourLogs = WorkLog.fetchMissingHourWlogs(loggedUser.getUserName());

        Gson gson = new Gson();
        String json = gson.toJson(missingHourLogs);
        return ok(json);
    }

    public static Result fetchTotalHoursPerMonth(String date) throws ParseException {
        User user = SessionManager.get("user");
        String userName = user == null ? "" : user.getUserName();
        double totalMissingHours = 0;
        List<WorkLog> workLog;
        if (date != null && !date.equals("all")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(sdf.parse(date));
            startDate.set(Calendar.DAY_OF_MONTH, 0);
            startDate.add(Calendar.HOUR_OF_DAY, 23);
            startDate.add(Calendar.MINUTE, 59);
            startDate.add(Calendar.SECOND, 59);

            Calendar endDate = Calendar.getInstance();
            endDate.setTime(sdf.parse(date));
            endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DATE));
            endDate.add(Calendar.HOUR_OF_DAY, 23);
            endDate.add(Calendar.MINUTE, 59);
            endDate.add(Calendar.SECOND, 59);

            workLog = WorkLog.getWorkLogsPerMonth(userName, startDate.getTime(), endDate.getTime());

            totalMissingHours = calculateMissingHours(workLog, startDate, endDate);
        } else {
            workLog = WorkLog.getWorkLogs(userName);
            totalMissingHours = calculateMissingHours(workLog, null, null);
        }

        String result = "{\"totalHours\":" + (totalMissingHours) + "}";
        return ok(result);
    }

    private static int calculateMissingWlHours(List<WorkLog> workLog) {
        Date startDate = workLog.get(workLog.size()-1).getDateLog();
        Date endDate = workLog.get(0).getDateLog();
        return 0;
    }

    private static double calculateMissingHours(List<WorkLog> workLog, Calendar startDate, Calendar endDate) {
        if (startDate == null && endDate == null) {
            Map<Date, List<WorkLog>> groupedWorkLogs = new HashMap<Date, List<WorkLog>>();
            Calendar wlCalendar = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            for (WorkLog wl : workLog) {
                wlCalendar.setTime(wl.getDateLog());
                calendar.set(wlCalendar.get(Calendar.YEAR), wlCalendar.get(Calendar.MONTH), wlCalendar.get(Calendar.DATE));

                if (!groupedWorkLogs.containsKey(calendar.getTime())) {
                    List<WorkLog> innerWl = new ArrayList<WorkLog>();
                    innerWl.add(wl);
                    groupedWorkLogs.put(calendar.getTime(), innerWl);
                } else {
                    groupedWorkLogs.get(calendar.getTime()).add(wl);
                }
            }
            double totalMissingHours = 0;
            for (Map.Entry<Date, List<WorkLog>> entry : groupedWorkLogs.entrySet()) {
                double dailyHours = 0;
                for (WorkLog wl : entry.getValue()) {
                    for (Project pr : wl.getProjects()) {
                        dailyHours += pr.getHours();
                    }
                }
                if (dailyHours < 8)
                    totalMissingHours += (8 - dailyHours);
            }
            return totalMissingHours;
        } else {
            Map<Integer, List<WorkLog>> dateGroupedWorklogs = groupWLByDate(workLog, startDate, endDate);
            float totalMonthMissingHours = 0;
            for (Map.Entry<Integer, List<WorkLog>> entry:dateGroupedWorklogs.entrySet()) {
                float counter = 0;
                for (WorkLog wl:entry.getValue()) {
                    for (Project pr:wl.getProjects()) {
                        counter += pr.getHours();
                    }
                }
                if (counter < 8)
                    totalMonthMissingHours += 8-counter;
                }
            return totalMonthMissingHours;
        }
    }

    private static Map<Integer, List<WorkLog>> groupWLByDate(List<WorkLog> workLog, Calendar startDate, Calendar endDate) {
        long diff = (endDate.getTimeInMillis()-startDate.getTimeInMillis())/(24*60*60*1000) + 1;
        Map<Integer, List<WorkLog>> grouped = new HashMap<>();
        for (int i=0; i<diff; i++) {
            startDate.add(Calendar.DATE, 1);
            if (startDate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
                grouped.put(new Integer(startDate.get(Calendar.DATE)), new ArrayList<WorkLog>());

        }
        for (WorkLog wl:workLog) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(wl.getDateLog());
            int day = cal.get(Calendar.DATE);
            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                grouped.get(day).add(wl);
            }
        }
        return grouped;
    }

    private static int calculateAdditionalHours(WorkLog workLog) {
        Date d1 = Calendar.getInstance().getTime();
        Date d2 = workLog.getDateLog();

        long diff = d1.getTime() - d2.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        int daysdiff = (int) diffDays;
        return daysdiff > 0 ? daysdiff : 0;
    }

    public static Result downloadCsv(String user, String period) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        StringBuffer stringBuffer = new StringBuffer();

        try {
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();

            if (period==null || period.equals("undefined")) {
                startDate.set(Calendar.YEAR, 2014);
                startDate.set(Calendar.MONTH, 1);

            } else {

                Date parsedDate = sdf.parse(period);
                startDate = Calendar.getInstance();
                startDate.setTime(parsedDate);
                startDate.set(Calendar.DAY_OF_MONTH, 0);
                startDate.add(Calendar.HOUR_OF_DAY, 23);
                startDate.add(Calendar.MINUTE, 59);
                startDate.add(Calendar.SECOND, 59);

                endDate = Calendar.getInstance();
                endDate.setTime(parsedDate);
                endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DATE));
                endDate.add(Calendar.HOUR_OF_DAY, 23);
                endDate.add(Calendar.MINUTE, 59);
                endDate.add(Calendar.SECOND, 59);
            }

            if (user==null || user.equals("") || user.equals("undefined"))
                user = "all";

            Logger.debug("User: " + user + " Start date:" + startDate.getTime() + " End date:" + endDate.getTime());

            List<WorkLog> workLogs = WorkLog.getReport(startDate.getTime(), endDate.getTime(), user);
            stringBuffer.append("username,").append("date,").append("Project Client,").append("Project Name,")
                    .append("Project Component,").append("Hours,").append("Description").append("\n");
            for (WorkLog worklog:workLogs) {
                stringBuffer.append("\"").append(worklog.getUserName()).append("\"").append(",");
                stringBuffer.append("\"").append(formatDate(worklog.getDateLog())).append("\"").append(",");
                int line=0;
                for (Project project:worklog.getProjects()) {
                    if (line == 0)
                        stringBuffer.append("\"").append(project.getClient()).append("\"").append(",").append("\"").
                                append(project.getName()).append("\"").append(",").append("\"")
                                .append(project.getComponent()).append("\"").append(",").append("\"")
                                .append(project.getHours()).append("\"").append(",").append("\"").append(project.getRegion()).append("\"").append("\n");
                    else
                        stringBuffer.append("\"").append(worklog.getUserName()).append("\"").append(",").append("\"").append(formatDate(worklog.getDateLog())).
                                append("\"").append(",").append("\"").append(project.getClient()).append("\"").append(",").append("\"").
                                append(project.getName()).append("\"").append(",").append("\"").append(project.getComponent()).append("\"").
                                append(",").append("\"").append(project.getHours()).append("\"").append(",").append("\"").append(project.getRegion()).append("\"").append("\n");
                    line++;
                }
            }

        } catch (ParseException e) {
            Logger.error("error parsing date", e);
        }
        response().setContentType("text/csv; charset=utf-8");
        response().setHeader("Content-Disposition", "attachment; filename=report_" + user +"_" + period +".csv");
        return ok(stringBuffer.toString()).as("text/csv");
    }

    private static String formatDate(Date dateLog) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
        return sdf.format(dateLog).toString();
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
            loginForm.reject("Enter user name and password");
            Logger.info("form errors " + loginForm.errors());
            return badRequest(views.html.login.render(loginForm));
        } else {
            session().clear();

            User user = User.userByUsername(loginForm.get().userName);
            if (user != null && user.getPassword() != null && user.getPassword().equals(loginForm.get().password)) {
                SessionManager.addSession("user", user);
                return redirect(controllers.routes.Application.indexWorkLog());
            } else {
                if (user.getUserName() == null)
                    loginForm.reject("Unknown User");
                else if (user.getPassword() != null && !user.getPassword().equals(loginForm.get().password))
                    loginForm.reject("Wrong Password for user " + user.getUserName());
                else
                    loginForm.reject("Wrong user name or password");
                return badRequest(views.html.login.render(loginForm));
            }

        }
    }
}
