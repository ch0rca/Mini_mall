package com.mini_mall.service;

import com.mini_mall.dto.UserDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    UserService userService = new UserService();

    // 회원가입 
    @Test
    void 회원가입_성공() {
        UserDTO user = new UserDTO();
        user.setLoginId("testuser");
        user.setPassword("test1234");
        user.setName("테스트유저");

        boolean result = userService.register(user);
        assertTrue(result); 
    }

    // 중복 아이디 
    @Test
    void 중복_아이디_가입_실패() {
        UserDTO user = new UserDTO();
        user.setLoginId("user1"); 
        user.setPassword("pass1");
        user.setName("김철수");

        boolean result = userService.register(user);
        assertFalse(result); // false면 성공
    }

    // 로그인 성공 
    @Test
    void 로그인_성공() {
        UserDTO result = userService.login("user1", "pass1");

        assertNotNull(result);           // null 아니면 성공
        assertEquals("USER", result.getRole()); // role 확인
        assertEquals("김철수", result.getName()); // 이름 확인
    }

    // 로그인 실패 
    @Test
    void 로그인_실패_틀린비밀번호() {
        UserDTO result = userService.login("user1", "wrongpassword");
        assertNull(result); // null이면 성공 
        }
}