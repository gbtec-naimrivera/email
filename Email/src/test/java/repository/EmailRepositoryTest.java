package repository;

import com.example.email.EmailApplication;
import com.example.email.entity.Email;
import com.example.email.entity.EmailCC;
import com.example.email.entity.EmailStateEnum;
import com.example.email.entity.EmailTo;
import com.example.email.repositories.EmailDao;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;

import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;


import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = EmailApplication.class)
class EmailRepositoryTest {

    @Autowired
    EmailDao emailDao;

    private static PostgreSQLContainer sqlContainer = new PostgreSQLContainer("postgres:10.7");

    static {

        sqlContainer.start();
    }

    @BeforeEach
    void setUp(){

        List<EmailTo> emailToList = Arrays.asList(
                new EmailTo(null, null, "marcus@gbtec.com"),
                new EmailTo(null, null, "alex@gbtec.com")
        );

        List<EmailCC> emailCCList = Arrays.asList(
                new EmailCC(null, null, "daniel@gbtec.com"),
                new EmailCC(null, null, "pablo@gbtec.com")
        );

        Email email1 = Email.builder()


                .emailId(1L)
                .emailFrom("naim@gbtec.com")
                .emailBody("Body of email 1")
                .state(EmailStateEnum.DRAFT)
                .emailTo(emailToList)
                .emailCC(emailCCList)
                .build();

        emailCCList.forEach(emailCC -> emailCC.setEmail(email1));
        emailToList.forEach(emailTo -> emailTo.setEmail(email1));

        emailDao.save(email1);
    }

    @Test
    void testFindPostByState(){
        List<Email> emailList = emailDao.findByState(EmailStateEnum.DRAFT);
        assertEquals(emailList.get(0).getEmailFrom(), "naim@gbtec.com");
        assertEquals(emailList.size(), 1);
    }

    @Test
    void testfindByEmailFrom(){

        List<Email> emailList = emailDao.findByEmailFrom("naim@gbtec.com");
        assertThat(emailList).isNotNull();

    }
}
