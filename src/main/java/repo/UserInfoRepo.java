package repo;

import java.util.List;
import java.util.Locale.Category;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Game;
import model.QnA;
import model.UserInfo;

public interface UserInfoRepo extends JpaRepository<UserInfo, String> {
    // empty function to call/get the username
    UserInfo findByUsername(String username);

    UserInfo findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
