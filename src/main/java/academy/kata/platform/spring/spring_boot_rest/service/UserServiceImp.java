package academy.kata.platform.spring.spring_boot_rest.service;

import academy.kata.platform.spring.spring_boot_rest.dao.UserRepository;
import academy.kata.platform.spring.spring_boot_rest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
   private UserRepository userRepository;

   @Autowired
   public UserServiceImp(UserRepository userRepository) {
      this.userRepository = userRepository;
   }


   @Override
   public List<User> getAllUsers() {
      return userRepository.findAll();
   }

   @Override
   public User findUserById(long id) {
      return userRepository.findUserById(id);
   }

   @Override
   public User findUserByUsername(String username) {
      return userRepository.findUserByUsername(username);
   }

   @Override
   public void saveOrUpdate(User user) {
      userRepository.save(user);
   }

   @Override
   public void removeUserById(long id) {
      userRepository.deleteById(id);
   }

}
