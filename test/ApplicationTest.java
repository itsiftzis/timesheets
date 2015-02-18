import models.Project;
import models.User;
import models.WorkLog;
import org.junit.*;
import play.Logger;
import play.test.FakeApplication;
import play.test.Helpers;
import play.twirl.api.Content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

    FakeApplication fakeApp = Helpers.fakeApplication();

    FakeApplication fakeAppWithMemoryDb = fakeApplication(inMemoryDatabase("testdb"));

    @Test
    public void findUserById() {
        running(fakeApplication(inMemoryDatabase("test")), new Runnable() {
            public void run() {
                User.create("admin", "admin", "admin", "admin", "admin");
                User user = User.userByUsername("admin");
                assertThat(user.getUserName()).isEqualTo("admin");
                assertThat(user.getLastName()).isEqualTo("admin");
            }
        });
    }

    @Test
    public void findWorkLogsPerUser() {
        running(fakeApplication(inMemoryDatabase("test")), new Runnable() {
            public void run() {
                Project project = new Project("projectname", "usa", "component", "region", 8);
                List<Project> worklogs = new ArrayList<Project>();
                worklogs.add(project);
                WorkLog workLog = new WorkLog(new Date(), 8, worklogs, "admin");
                WorkLog.create(workLog);
                List<WorkLog> worklogsPerUser = WorkLog.worklogPerUser("admin");
                assertThat(worklogsPerUser.size()).isGreaterThan(0);
                assertThat(worklogsPerUser.get(0).getUserName()).isEqualTo("admin");
            }
        });
    }

    @Test
    public void editWorkLog() {
        running(fakeApplication(inMemoryDatabase("test")), new Runnable() {
            public void run() {
                Project project = new Project("projectname", "usa", "component", "region", 8);
                List<Project> worklogs = new ArrayList<Project>();
                worklogs.add(project);
                WorkLog workLog = new WorkLog(new Date(), 8, worklogs, "admin");
                WorkLog.create(workLog);
                List<WorkLog> worklogsPerUser = WorkLog.worklogPerUser("admin");

                WorkLog fromDB = worklogsPerUser.get(0);
                Date now = new Date();
                fromDB.setDateLog(now);
                fromDB.setProjects(new ArrayList<Project>(Arrays.asList(new Project("client", "name", "component", "region", 1))));
                fromDB.setTotalHours(1);
                fromDB.setUser("seconduser");
                WorkLog.update(fromDB);

                List<WorkLog> afterUpdate = WorkLog.worklogPerUser("seconduser");
                assertThat(afterUpdate.size()).isGreaterThan(0);
                assertThat(afterUpdate.get(0).getTotalHours()).isEqualTo(1);
            }
        });
    }


}
