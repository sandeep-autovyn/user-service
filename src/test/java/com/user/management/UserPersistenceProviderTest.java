//package com.user.management;
//
//import com.user.management.entity.User;
//import com.user.management.persistence.UserPersistenceProvider;
//import com.user.management.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class UserPersistenceProviderTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserPersistenceProvider userPersistenceProvider;
//
//    private User user;
//    private Pageable pageable;
//
//    @BeforeEach
//    void setUp() {
//        user = new User(1, "sandeep_sharma", "sandeep", "sandeep@example.com", "Doe", "testphoto.com");
//        pageable = mock(Pageable.class);
//    }
//
//    @Test
//    void testGetAllUsers() {
//        // Prepare mock data
//        Page mockPage = mock(Page.class);
//        when(userRepository.findAll(pageable)).thenReturn(mockPage);
//        Page<User> result = userPersistenceProvider.getAllUsers(pageable);
//        assertNotNull(result);
//        verify(userRepository).findAll(pageable);
//    }
//
//    @Test
//    void testGetUserByUserName_UserExists() {
//        // Prepare mock data
//        Optional<User> mockUser = Optional.of(user);
//        when(userRepository.findByUserName("sandeep_sharma")).thenReturn(mockUser);
//        Optional<User> result = userPersistenceProvider.getUserByUserName("sandeep_sharma");
//        assertTrue(result.isPresent());
//        assertEquals("sandeep_sharma", result.get().getUserName());
//        verify(userRepository).findByUserName("sandeep_sharma");
//    }
//
//    @Test
//    void testGetUserByUserName_UserDoesNotExist() {
//
//        when(userRepository.findByUserName("non_existent_user")).thenReturn(Optional.empty());
//        Optional<User> result = userPersistenceProvider.getUserByUserName("non_existent_user");
//        assertFalse(result.isPresent());
//        verify(userRepository).findByUserName("non_existent_user");
//    }
//
//    @Test
//    void testCreateUser() {
//        when(userRepository.save(user)).thenReturn(user);
//        User result = userPersistenceProvider.createUser(user);
//        assertNotNull(result);
//        assertEquals("sandeep_sharma", result.getUserName());
//        verify(userRepository).save(user);
//    }
//
//    @Test
//    void testDeleteUser() {
//        userPersistenceProvider.deleteUser(user);
//        verify(userRepository).delete(user);
//    }
//}
