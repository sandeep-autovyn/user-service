package com.user.management;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.management.entity.RandomUser;
import com.user.management.entity.RandomUserApiData;
import com.user.management.entity.User;
import com.user.management.exception.UserNameAlreadyExists;
import com.user.management.exception.UserNotFoundException;
import com.user.management.persistence.UserPersistenceService;
import com.user.management.service.RandomUserGenerationService;
import com.user.management.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserPersistenceService userPersistenceService;

    @Mock
    private RandomUserGenerationService randomUserGenerationService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private RandomUser randomUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1, "sandeep_sharma", "sandeep", "sandeep@example.com", "Doe", "testphoto.com");
        randomUser = new RandomUser();
    }

    @Test
    void testGetAllUsers() {
        Page<User> mockPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        when(userPersistenceService.getAllUsers(pageable)).thenReturn(mockPage);

        Page<User> result = userService.getAllUsers(pageable);

        assertNotNull(result);
        verify(userPersistenceService).getAllUsers(pageable);
    }

    @Test
    void testGetUserByUserName_UserExists() {
        when(userPersistenceService.getUserByUserName("sandeep_sharma")).thenReturn(Optional.of(user));

        User result = userService.getUserByUserName("sandeep_sharma");

        assertNotNull(result);
        assertEquals("sandeep_sharma", result.getUserName());
        verify(userPersistenceService).getUserByUserName("sandeep_sharma");
    }

    @Test
    void testGetUserByUserName_UserDoesNotExist() {
        when(userPersistenceService.getUserByUserName("non_existent_user")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByUserName("non_existent_user"));
        verify(userPersistenceService).getUserByUserName("non_existent_user");
    }

    @Test
    void testCreateUser_UserNameAlreadyExists() {
        when(userPersistenceService.getUserByUserName("sandeep_sharma")).thenReturn(Optional.of(user));

        assertThrows(UserNameAlreadyExists.class, () -> userService.createUser(user));
        verify(userPersistenceService).getUserByUserName("sandeep_sharma");
    }

    @Test
    void testCreateUser_Success() {
        when(userPersistenceService.getUserByUserName("sandeep_sharma")).thenReturn(Optional.empty());
        when(userPersistenceService.createUser(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("sandeep_sharma", result.getUserName());
        verify(userPersistenceService).getUserByUserName("sandeep_sharma");
        verify(userPersistenceService).createUser(user);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userPersistenceService.getUserByUserName("non_existent_user")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(user, "non_existent_user"));
        verify(userPersistenceService).getUserByUserName("non_existent_user");
    }

    @Test
    void testUpdateUser_UserNameAlreadyExists() {
        User existingUser = new User(2, "existing_user", "existing", "existing@example.com", "Doe", "testphoto.com");
        when(userPersistenceService.getUserByUserName("sandeep_sharma")).thenReturn(Optional.of(user));
        when(userPersistenceService.getUserByUserName("existing_user")).thenReturn(Optional.of(existingUser));

        assertThrows(UserNameAlreadyExists.class, () -> userService.updateUser(existingUser, "sandeep_sharma"));
        verify(userPersistenceService).getUserByUserName("sandeep_sharma");
        verify(userPersistenceService).getUserByUserName("existing_user");
    }



    @Test
    void testDeleteUser_UserNotFound() {
        when(userPersistenceService.getUserByUserName("non_existent_user")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser("non_existent_user"));
        verify(userPersistenceService).getUserByUserName("non_existent_user");
    }

    @Test
    void testDeleteUser_Success() {
        when(userPersistenceService.getUserByUserName("sandeep_sharma")).thenReturn(Optional.of(user));

        userService.deleteUser("sandeep_sharma");

        verify(userPersistenceService).getUserByUserName("sandeep_sharma");
        verify(userPersistenceService).deleteUser(user);
    }

    @Test
    void testGetRandomUsers() throws JsonProcessingException {
        RandomUserApiData randomUserApiData = prepareData();
        when(randomUserGenerationService.generateRandomUsers(anyInt())).thenReturn(randomUserApiData);

        List<RandomUser> result = userService.getRandomUsers(10);

        assertNotNull(result);
        assertEquals(10, result.size());
        verify(randomUserGenerationService).generateRandomUsers(10);
    }

    @Test
    void testGetRandomUsersTree() throws JsonProcessingException {
        RandomUserApiData randomUserApiData = prepareData();
        when(randomUserGenerationService.generateRandomUsers(anyInt())).thenReturn(randomUserApiData);

        Map<String, Map<String, Map<String, List<RandomUser>>>> result = userService.getRandomUsersTree(20);

        assertNotNull(result);
        verify(randomUserGenerationService).generateRandomUsers(20);
    }

    private RandomUserApiData prepareData() throws JsonProcessingException {

        String json = "{\"results\":[{\"gender\":\"male\",\"name\":{\"title\":\"Mr\",\"first\":\"Ray\",\"last\":\"Henderson\"},\"location\":{\"street\":{\"number\":726,\"name\":\"Ormond Quay\"},\"city\":\"Sallins\",\"state\":\"Meath\",\"country\":\"Ireland\",\"postcode\":78739,\"coordinates\":{\"latitude\":\"-0.5686\",\"longitude\":\"-150.1030\"},\"timezone\":{\"offset\":\"+7:00\",\"description\":\"Bangkok, Hanoi, Jakarta\"}},\"email\":\"ray.henderson@example.com\",\"login\":{\"uuid\":\"c6d19dac-d150-4d06-aa98-faa66592e132\",\"username\":\"organiclion983\",\"password\":\"nevada\",\"salt\":\"Yevd3bzk\",\"md5\":\"a20f55b0e43e5c7a074817b9ccea21ce\",\"sha1\":\"beabca3c7b3c831b546048e6e54591128cc9b8cd\",\"sha256\":\"124bd95d08c05f359b96c24c748190bc2d0d3be0cec58c7cef491068e8f56b61\"},\"dob\":{\"date\":\"1981-04-21T08:23:49.439Z\",\"age\":43},\"registered\":{\"date\":\"2016-01-07T15:51:29.959Z\",\"age\":9},\"phone\":\"031-351-9121\",\"cell\":\"081-118-9936\",\"id\":{\"name\":\"PPS\",\"value\":\"7485431T\"},\"picture\":{\"large\":\"https://randomuser.me/api/portraits/men/43.jpg\",\"medium\":\"https://randomuser.me/api/portraits/med/men/43.jpg\",\"thumbnail\":\"https://randomuser.me/api/portraits/thumb/men/43.jpg\"},\"nat\":\"IE\"},{\"gender\":\"male\",\"name\":{\"title\":\"Mr\",\"first\":\"Kadir\",\"last\":\"Korol\"},\"location\":{\"street\":{\"number\":6117,\"name\":\"Anafartalar Cd\"},\"city\":\"Bilecik\",\"state\":\"Şanlıurfa\",\"country\":\"Turkey\",\"postcode\":57294,\"coordinates\":{\"latitude\":\"-60.4982\",\"longitude\":\"-112.0473\"},\"timezone\":{\"offset\":\"+9:30\",\"description\":\"Adelaide, Darwin\"}},\"email\":\"kadir.korol@example.com\",\"login\":{\"uuid\":\"def5ac7d-9652-4e77-8e91-2c8edb3631f4\",\"username\":\"greenbird268\",\"password\":\"doug\",\"salt\":\"5YToApg8\",\"md5\":\"ca49bba296f34e3410fb448b364579f6\",\"sha1\":\"c4c960f83283255c44c3fdae9c6c68d76db9c0bd\",\"sha256\":\"732ca7b3d241f503df06bfd16791adce8f3d03747f64f812c1978d5aaf2491e0\"},\"dob\":{\"date\":\"1995-07-13T01:44:40.577Z\",\"age\":29},\"registered\":{\"date\":\"2016-01-29T05:48:32.104Z\",\"age\":8},\"phone\":\"(214)-280-7277\",\"cell\":\"(523)-197-0269\",\"id\":{\"name\":\"\",\"value\":null},\"picture\":{\"large\":\"https://randomuser.me/api/portraits/men/63.jpg\",\"medium\":\"https://randomuser.me/api/portraits/med/men/63.jpg\",\"thumbnail\":\"https://randomuser.me/api/portraits/thumb/men/63.jpg\"},\"nat\":\"TR\"},{\"gender\":\"female\",\"name\":{\"title\":\"Mrs\",\"first\":\"Åsne\",\"last\":\"Tandstad\"},\"location\":{\"street\":{\"number\":6994,\"name\":\"Professor Dahls gate\"},\"city\":\"Røra Stasjon\",\"state\":\"Vest-Agder\",\"country\":\"Norway\",\"postcode\":\"5052\",\"coordinates\":{\"latitude\":\"-25.3408\",\"longitude\":\"-153.4331\"},\"timezone\":{\"offset\":\"+4:00\",\"description\":\"Abu Dhabi, Muscat, Baku, Tbilisi\"}},\"email\":\"asne.tandstad@example.com\",\"login\":{\"uuid\":\"3eaa3263-e1c4-4cb8-852e-c04afbbfd8ab\",\"username\":\"tinycat792\",\"password\":\"discover\",\"salt\":\"7QEwEYRs\",\"md5\":\"196ca8421c3643b9f89a7d77de939fbd\",\"sha1\":\"72796de5a1c9ff6ebb9f468e0f54795a8d8f75d3\",\"sha256\":\"5f1d387bf0b0ab3515baa69a0f7e25d4807c9ac7b0a5f4da9211a254c7e20283\"},\"dob\":{\"date\":\"1954-05-16T08:15:43.123Z\",\"age\":70},\"registered\":{\"date\":\"2004-12-22T10:19:59.434Z\",\"age\":20},\"phone\":\"53740595\",\"cell\":\"46088825\",\"id\":{\"name\":\"FN\",\"value\":\"16055411836\"},\"picture\":{\"large\":\"https://randomuser.me/api/portraits/women/46.jpg\",\"medium\":\"https://randomuser.me/api/portraits/med/women/46.jpg\",\"thumbnail\":\"https://randomuser.me/api/portraits/thumb/women/46.jpg\"},\"nat\":\"NO\"},{\"gender\":\"male\",\"name\":{\"title\":\"Mr\",\"first\":\"Girish\",\"last\":\"Shet\"},\"location\":{\"street\":{\"number\":4816,\"name\":\"China Bazaar Rd\"},\"city\":\"Dhule\",\"state\":\"Tamil Nadu\",\"country\":\"India\",\"postcode\":53669,\"coordinates\":{\"latitude\":\"32.0723\",\"longitude\":\"55.3145\"},\"timezone\":{\"offset\":\"-4:00\",\"description\":\"Atlantic Time (Canada), Caracas, La Paz\"}},\"email\":\"girish.shet@example.com\",\"login\":{\"uuid\":\"eefa757d-b44e-42c2-a775-b709e81a1dce\",\"username\":\"crazybear539\",\"password\":\"darkside\",\"salt\":\"gK6EzL2q\",\"md5\":\"dd23569beb75c147693d8a6ceda9737f\",\"sha1\":\"bc2fbb91844a99a2871786d5d51d74b5be39b867\",\"sha256\":\"2e2498a15d18110f7d8e84d4c80cb20722b95c1fe54ede7e38ec46039f6b84a4\"},\"dob\":{\"date\":\"1975-07-04T19:10:32.985Z\",\"age\":49},\"registered\":{\"date\":\"2020-10-05T12:44:30.244Z\",\"age\":4},\"phone\":\"8762598339\",\"cell\":\"9751127892\",\"id\":{\"name\":\"UIDAI\",\"value\":\"655247863588\"},\"picture\":{\"large\":\"https://randomuser.me/api/portraits/men/74.jpg\",\"medium\":\"https://randomuser.me/api/portraits/med/men/74.jpg\",\"thumbnail\":\"https://randomuser.me/api/portraits/thumb/men/74.jpg\"},\"nat\":\"IN\"},{\"gender\":\"female\",\"name\":{\"title\":\"Mrs\",\"first\":\"Abitha\",\"last\":\"Belligatti\"},\"location\":{\"street\":{\"number\":9772,\"name\":\"Lamington Rd\"},\"city\":\"Firozabad\",\"state\":\"Daman and Diu\",\"country\":\"India\",\"postcode\":24106,\"coordinates\":{\"latitude\":\"-36.2962\",\"longitude\":\"0.9515\"},\"timezone\":{\"offset\":\"-11:00\",\"description\":\"Midway Island, Samoa\"}},\"email\":\"abitha.belligatti@example.com\",\"login\":{\"uuid\":\"c8846d47-24e7-419e-b69d-444078ebaba4\",\"username\":\"silverduck608\",\"password\":\"roberto\",\"salt\":\"sCxwRbZb\",\"md5\":\"5eb0ccdda30edc9fd36213e28008afbc\",\"sha1\":\"b78a303a3a7d6db1708e1844181ed1de0a3da960\",\"sha256\":\"eddca98ce4fe1d864008e5d1d41345a7d5cc9f2c561e698cd4da0219aadb0395\"},\"dob\":{\"date\":\"1967-05-07T09:26:10.068Z\",\"age\":57},\"registered\":{\"date\":\"2004-12-05T10:56:37.914Z\",\"age\":20},\"phone\":\"9869735591\",\"cell\":\"7117925544\",\"id\":{\"name\":\"UIDAI\",\"value\":\"444260443808\"},\"picture\":{\"large\":\"https://randomuser.me/api/portraits/women/81.jpg\",\"medium\":\"https://randomuser.me/api/portraits/med/women/81.jpg\",\"thumbnail\":\"https://randomuser.me/api/portraits/thumb/women/81.jpg\"},\"nat\":\"IN\"},{\"gender\":\"female\",\"name\":{\"title\":\"Ms\",\"first\":\"Linnea\",\"last\":\"Manni\"},\"location\":{\"street\":{\"number\":8077,\"name\":\"Hatanpään Valtatie\"},\"city\":\"Lemi\",\"state\":\"Ostrobothnia\",\"country\":\"Finland\",\"postcode\":46294,\"coordinates\":{\"latitude\":\"7.3080\",\"longitude\":\"176.1852\"},\"timezone\":{\"offset\":\"+3:00\",\"description\":\"Baghdad, Riyadh, Moscow, St. Petersburg\"}},\"email\":\"linnea.manni@example.com\",\"login\":{\"uuid\":\"e65ffb92-13cc-4f4a-8837-cea59cd8a02a\",\"username\":\"happyduck324\",\"password\":\"cutlass\",\"salt\":\"XjvmUJxF\",\"md5\":\"cc8a8fd1a9f723f2c8b4e39005096e79\",\"sha1\":\"ebd1696fa057d9bfb66171908810ee00ea9706b0\",\"sha256\":\"600cab787a3489c9a3c9da4f4f61ac8c72505314d2f09134a590aa483f26f517\"},\"dob\":{\"date\":\"1986-09-02T01:20:38.378Z\",\"age\":38},\"registered\":{\"date\":\"2014-06-07T11:24:48.803Z\",\"age\":10},\"phone\":\"09-680-404\",\"cell\":\"040-449-84-99\",\"id\":{\"name\":\"HETU\",\"value\":\"NaNNA734undefined\"},\"picture\":{\"large\":\"https://randomuser.me/api/portraits/women/52.jpg\",\"medium\":\"https://randomuser.me/api/portraits/med/women/52.jpg\",\"thumbnail\":\"https://randomuser.me/api/portraits/thumb/women/52.jpg\"},\"nat\":\"FI\"},{\"gender\":\"female\",\"name\":{\"title\":\"Miss\",\"first\":\"Charline\",\"last\":\"Lefevre\"},\"location\":{\"street\":{\"number\":2341,\"name\":\"Esplanade du 9 Novembre 1989\"},\"city\":\"Nancy\",\"state\":\"Essonne\",\"country\":\"France\",\"postcode\":65924,\"coordinates\":{\"latitude\":\"-78.4546\",\"longitude\":\"171.8157\"},\"timezone\":{\"offset\":\"-5:00\",\"description\":\"Eastern Time (US & Canada), Bogota, Lima\"}},\"email\":\"charline.lefevre@example.com\",\"login\":{\"uuid\":\"64424755-b0d0-4cc3-962d-cd9fc588f242\",\"username\":\"ticklishpeacock784\",\"password\":\"morton\",\"salt\":\"nrPO2ITQ\",\"md5\":\"e8fe5e7b1bd38e71aee21b531142dc15\",\"sha1\":\"f226b9d7281877e4fcea48990786b771d7a95dd4\",\"sha256\":\"a6c62268e7f2ff80cf37c72cf10b26620b59eff650861134fe3204ad93a04032\"},\"dob\":{\"date\":\"1972-04-04T09:12:10.352Z\",\"age\":52},\"registered\":{\"date\":\"2012-08-20T00:17:08.815Z\",\"age\":12},\"phone\":\"04-71-90-19-85\",\"cell\":\"06-30-53-69-61\",\"id\":{\"name\":\"INSEE\",\"value\":\"2720348760051 15\"},\"picture\":{\"large\":\"https://randomuser.me/api/portraits/women/26.jpg\",\"medium\":\"https://randomuser.me/api/portraits/med/women/26.jpg\",\"thumbnail\":\"https://randomuser.me/api/portraits/thumb/women/26.jpg\"},\"nat\":\"FR\"},{\"gender\":\"female\",\"name\":{\"title\":\"Miss\",\"first\":\"Meral\",\"last\":\"Erberk\"},\"location\":{\"street\":{\"number\":5793,\"name\":\"Atatürk Sk\"},\"city\":\"Van\",\"state\":\"Sivas\",\"country\":\"Turkey\",\"postcode\":28890,\"coordinates\":{\"latitude\":\"-54.6108\",\"longitude\":\"84.2901\"},\"timezone\":{\"offset\":\"+5:45\",\"description\":\"Kathmandu\"}},\"email\":\"meral.erberk@example.com\",\"login\":{\"uuid\":\"f3887677-63a1-4776-90d5-e79f8c2786c2\",\"username\":\"purplepanda929\",\"password\":\"ning\",\"salt\":\"9KeFzyXS\",\"md5\":\"e8b3061305c3ed45173aa83a74014b55\",\"sha1\":\"c116c5c3a16477415006d8fd77cfc930311408d9\",\"sha256\":\"abd2c64978cf17c40ec9fa6b72c93ed9e3ee8867be173c1c44cfbca0799f8939\"},\"dob\":{\"date\":\"1949-05-05T06:39:58.348Z\",\"age\":75},\"registered\":{\"date\":\"2012-02-29T18:54:02.192Z\",\"age\":12},\"phone\":\"(079)-114-1946\",\"cell\":\"(934)-984-7733\",\"id\":{\"name\":\"\",\"value\":null},\"picture\":{\"large\":\"https://randomuser.me/api/portraits/women/8.jpg\",\"medium\":\"https://randomuser.me/api/portraits/med/women/8.jpg\",\"thumbnail\":\"https://randomuser.me/api/portraits/thumb/women/8.jpg\"},\"nat\":\"TR\"},{\"gender\":\"male\",\"name\":{\"title\":\"Mr\",\"first\":\"Octavio\",\"last\":\"Domínguez\"},\"location\":{\"street\":{\"number\":3700,\"name\":\"Corredor Rumania\"},\"city\":\"Ciudad Apodaca\",\"state\":\"Quintana Roo\",\"country\":\"Mexico\",\"postcode\":25296,\"coordinates\":{\"latitude\":\"-28.9972\",\"longitude\":\"-106.9287\"},\"timezone\":{\"offset\":\"+6:00\",\"description\":\"Almaty, Dhaka, Colombo\"}},\"email\":\"octavio.dominguez@example.com\",\"login\":{\"uuid\":\"b9684633-b98f-4bd0-b060-77ed7ffb6768\",\"username\":\"organicpanda725\",\"password\":\"hotdog\",\"salt\":\"BV7C8udi\",\"md5\":\"3083441e944c675160c70edc0edf8a77\",\"sha1\":\"7ce289bd5af18c1870318d42629979f3dad9b85c\",\"sha256\":\"be48f90ae9346d3d2fca1b62e1507cc1f79da5024e327ef1cd065d1d7da85aba\"},\"dob\":{\"date\":\"1962-02-26T00:53:41.105Z\",\"age\":62},\"registered\":{\"date\":\"2019-07-03T13:34:24.877Z\",\"age\":5},\"phone\":\"(631) 791 2017\",\"cell\":\"(698) 796 4966\",\"id\":{\"name\":\"NSS\",\"value\":\"87 84 19 7759 7\"},\"picture\":{\"large\":\"https://randomuser.me/api/portraits/men/58.jpg\",\"medium\":\"https://randomuser.me/api/portraits/med/men/58.jpg\",\"thumbnail\":\"https://randomuser.me/api/portraits/thumb/men/58.jpg\"},\"nat\":\"MX\"},{\"gender\":\"female\",\"name\":{\"title\":\"Ms\",\"first\":\"Ida\",\"last\":\"Christiansen\"},\"location\":{\"street\":{\"number\":9753,\"name\":\"Vester Alle\"},\"city\":\"Jerslev Sj\",\"state\":\"Nordjylland\",\"country\":\"Denmark\",\"postcode\":74972,\"coordinates\":{\"latitude\":\"-57.3346\",\"longitude\":\"12.4378\"},\"timezone\":{\"offset\":\"+2:00\",\"description\":\"Kaliningrad, South Africa\"}},\"email\":\"ida.christiansen@example.com\",\"login\":{\"uuid\":\"33328493-4aad-49ba-9dbc-7849c2f11ed8\",\"username\":\"redbird844\",\"password\":\"shiloh\",\"salt\":\"68BOeApq\",\"md5\":\"354741fc591ccb45e8b73d96d341d59e\",\"sha1\":\"acab323e7920b30099cea414ac1d60ed42d1a378\",\"sha256\":\"6b8c10ed6ada88487fab14ed4dff5522dfdf2dcbdf17f71f6a7950c0831eeacf\"},\"dob\":{\"date\":\"1956-05-03T16:09:04.734Z\",\"age\":68},\"registered\":{\"date\":\"2020-11-26T01:59:46.470Z\",\"age\":4},\"phone\":\"97315559\",\"cell\":\"81164173\",\"id\":{\"name\":\"CPR\",\"value\":\"030556-3878\"},\"picture\":{\"large\":\"https://randomuser.me/api/portraits/women/6.jpg\",\"medium\":\"https://randomuser.me/api/portraits/med/women/6.jpg\",\"thumbnail\":\"https://randomuser.me/api/portraits/thumb/women/6.jpg\"},\"nat\":\"DK\"}],\"info\":{\"seed\":\"77edcc7590c7ae77\",\"results\":10,\"page\":1,\"version\":\"1.4\"}}";
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, RandomUserApiData.class);


    }
}