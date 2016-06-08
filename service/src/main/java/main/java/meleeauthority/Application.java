package main.java.meleeauthority;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class Application {

    private static MeleeDB meleeDB;

    public static void main(String[] args) {
        ApplicationContext context =
            new ClassPathXmlApplicationContext("file:src/main/resources/META-INF/beans.xml");

        meleeDB = (MeleeDB) context.getBean("meleeJDBCTemplate");

        SpringApplication.run(Application.class, args);
    }

    public static MeleeDB getDB() {
        return meleeDB;
    }
}
