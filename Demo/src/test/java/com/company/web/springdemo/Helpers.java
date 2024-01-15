package com.company.web.springdemo;

import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.Style;
import com.company.web.springdemo.models.User;

import java.util.Set;

public class Helpers {

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("mock@user.com");
        mockUser.setUsername("mock@user.com");
        mockUser.setLastName("mock@user.com");
        mockUser.setPassword("mock@user.com");
        mockUser.setFirstName("mock@user.com");
        mockUser.setAdmin(true);
        return mockUser;
    }

    public static Beer createMockBeer(){
        var mockBeer = new Beer();
        mockBeer.setId(1);
        mockBeer.setName("TestBeer");
        mockBeer.setCreatedBy(createMockUser());
        mockBeer.setStyle(createMockStyle());
        return mockBeer;
    }
//    public static boolean createMockRole(){
//        var mockRole = new Role();
//        mockRole.setId(1);
//        mockRole.setName("User");
//        return mockRole;
//    }
    public static Style createMockStyle(){
        var mockStyle = new Style();
        mockStyle.setId(1);
        mockStyle.setName("Ale");
        return mockStyle;
    }
}
