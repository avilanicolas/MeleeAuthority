package meleeauthority;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext context =
            new ClassPathXmlApplicationContext("file:src/main/resources/META-INF/beans.xml");

        MeleeDB meleeDB =
            (MeleeDB) context.getBean("meleeJDBCTemplate");

        meleeDB.asdf();

        SpringApplication.run(Application.class, args);
    }
}
