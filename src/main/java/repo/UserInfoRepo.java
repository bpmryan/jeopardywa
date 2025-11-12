package repo;

import org.springframework.data.jpa.repository.JpaRepository;

import model.UserInfo;

public interface UserInfoRepo extends JpaRepository<UserInfo, String> {

    UserInfo save(UserInfo user);
    
}
