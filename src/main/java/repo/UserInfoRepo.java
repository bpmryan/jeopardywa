package repo;

import org.springframework.data.jpa.repository.JpaRepository;

import model.UserInfo;

public interface UserInfoRepo extends JpaRepository<UserInfo, String> {
    // empty function to call/get the username
    UserInfo findMyUsername(String username);
    
}
