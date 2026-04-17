package com.socialmedia.backend.config;

import com.socialmedia.backend.entity.Role;
import com.socialmedia.backend.entity.User;
import com.socialmedia.backend.entity.Friend;
import com.socialmedia.backend.repository.RoleRepository;
import com.socialmedia.backend.repository.UserRepository;
import com.socialmedia.backend.repository.FriendRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.init.create-test-user:true}")
    private boolean createTestUser;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, 
                          FriendRepository friendRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.findByRoleName("USER").isEmpty()) {
            Role r = new Role();
            r.setRoleName("USER");
            roleRepository.save(r);
        }
        if (roleRepository.findByRoleName("ADMIN").isEmpty()) {
            Role r = new Role();
            r.setRoleName("ADMIN");
            roleRepository.save(r);
        }

        // Update existing users without avatar to have default avatar
        userRepository.findAll().forEach(user -> {
            if (user.getAvatarUrl() == null || user.getAvatarUrl().isEmpty()) {
                user.setAvatarUrl("/images/avatars/default_avatar.png");
                userRepository.save(user);
            }
        });

        if (createTestUser) {
            boolean exists = userRepository.findByUserName("test").isPresent() || userRepository.findByEmail("test@example.com").isPresent();
            if (!exists) {
                Role userRole = roleRepository.findByRoleName("USER").orElse(null);
                User u = new User();
                u.setUserName("test");
                u.setEmail("test@example.com");
                u.setPassWord(passwordEncoder.encode("password"));
                u.setStatus(true);
                u.setCreatedAt(LocalDateTime.now());
                u.setRole(userRole);
                u.setAvatarUrl("/images/avatars/default_avatar.png");
                userRepository.save(u);
            }

            // Create more test users for friend testing
            String[] testUsers = {"alice", "bob", "charlie", "diana"};
            for (String username : testUsers) {
                if (userRepository.findByUserName(username).isEmpty()) {
                    Role userRole = roleRepository.findByRoleName("USER").orElse(null);
                    User u = new User();
                    u.setUserName(username);
                    u.setEmail(username + "@example.com");
                    u.setPassWord(passwordEncoder.encode("password"));
                    u.setStatus(true);
                    u.setCreatedAt(LocalDateTime.now());
                    u.setRole(userRole);
                    u.setAvatarUrl("/images/avatars/default_avatar.png");
                    userRepository.save(u);
                }
            }

            // Create test friend relationships
            User test = userRepository.findByUserName("test").orElse(null);
            User alice = userRepository.findByUserName("alice").orElse(null);
            User bob = userRepository.findByUserName("bob").orElse(null);
            User charlie = userRepository.findByUserName("charlie").orElse(null);
            
            if (test != null && alice != null) {
                // Check if friendship already exists
                if (friendRepository.findFriendship(test.getUserId(), alice.getUserId()).isEmpty()) {
                    // Create accepted friendship (test <-> alice)
                    Friend f1 = new Friend();
                    f1.setUser(test);
                    f1.setFriendUser(alice);
                    f1.setStatus(1); // ACCEPTED
                    friendRepository.save(f1);
                }
            }

            if (test != null && bob != null) {
                if (friendRepository.findFriendship(test.getUserId(), bob.getUserId()).isEmpty()) {
                    // Create pending request (bob -> test)
                    Friend f2 = new Friend();
                    f2.setUser(bob);
                    f2.setFriendUser(test);
                    f2.setStatus(0); // PENDING
                    friendRepository.save(f2);
                }
            }

            if (alice != null && charlie != null) {
                if (friendRepository.findFriendship(alice.getUserId(), charlie.getUserId()).isEmpty()) {
                    // Create accepted friendship (alice <-> charlie)
                    Friend f3 = new Friend();
                    f3.setUser(alice);
                    f3.setFriendUser(charlie);
                    f3.setStatus(1); // ACCEPTED
                    friendRepository.save(f3);
                }
            }
        }
    }
}