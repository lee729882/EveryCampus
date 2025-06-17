package com.everycampus.init;

import com.everycampus.entity.User;
import com.everycampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        // ⭐️ role이 null인 사용자들 role = "USER"로 초기화
        userRepository.findAll().stream()
                .filter(user -> user.getRole() == null)
                .forEach(user -> {
                    user.setRole("USER");
                    userRepository.save(user);
                });

        // ⭐️ 관리자 계정이 없다면 생성
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password("admin")  // ⚠️ 실제 운영에서는 반드시 암호화 필요
                    .email("admin@everycampus.com")
                    .school("관리자학교")  // 아무거나 채워야 함
                    .role("ADMIN")
                    .build();

            userRepository.save(admin);
            System.out.println("✅ 관리자 계정(admin/admin) 자동 생성 완료");
        }
    }
}
