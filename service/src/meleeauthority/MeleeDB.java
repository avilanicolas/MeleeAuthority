package meleeauthority;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class MeleeDB implements MeleeDAO {
    private DataSource dataSource;
    private JdbcTemplate template;

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.template = new JdbcTemplate(ds);
    }

    @Override
    public void asdf() {
        System.out.println(template.queryForList("SHOW TABLES").size());
    }

}
