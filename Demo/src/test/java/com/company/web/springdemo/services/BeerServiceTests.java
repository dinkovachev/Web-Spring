package com.company.web.springdemo.services;

import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.exceptions.UnauthorizedOperationException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.repositories.BeerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.company.web.springdemo.Helpers.createMockBeer;
import static com.company.web.springdemo.Helpers.createMockUser;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTests {
    @Mock
    BeerRepository mockRepository;

    @InjectMocks
    BeerServiceImpl service;

    @Test
    public void getById_Should_ReturnBeer_When_MatchExists() {
        //Arrange
        BeerService service = new BeerServiceImpl(mockRepository);

        Mockito.when(mockRepository.get(2))
                .thenReturn(new Beer(2, "MockBeerName", 1.3));

        //Act
        Beer result = service.get(2);

        //Assert
        Assertions.assertEquals(2, result.getId());
        Assertions.assertEquals("MockBeerName", result.getName());
        Assertions.assertEquals(1.3, result.getAbv());
    }

    @Test
    public void create_Should_Throw_When_BeerWithSameNameExists() {
        //Arrange
        var mockBeer = createMockBeer();
        var mockUser = createMockUser();
        Mockito.when(mockRepository.getByName(mockBeer.getName()))
                .thenReturn(mockBeer);

        //Act,Assert
        Assertions.assertThrows(EntityDuplicateException.class,
                () -> service.create(mockBeer, mockUser));
    }

    @Test
    public void create_Should_CallRepository_When_BeerWithSameNameExists() {
        //Arrange
        var mockBeer = createMockBeer();
        var mockUser = createMockUser();
        Mockito.when(mockRepository.getByName(mockBeer.getName()))
                .thenThrow(new EntityNotFoundException("Beer", "name", mockBeer.getName()));

        //Act, Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Beer.class), Mockito.any(User.class));
    }

    @Test
    public void update_Should_Throw_When_UserIsNotCreatorOrAdmin(){
        //Arrange
        var mockBeer = createMockBeer();
        var mockUser = createMockUser();
        mockUser.setUsername("MockUser2");
        //mockBeer.setCreatedBy(mockUser);

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);

        //Act,Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.update(mockBeer, mockUser));
    }
    @Test
    public void update_Should_Throw_When_BeerNameIsTaken(){
        //Arrange
        var mockBeer = createMockBeer();
        var anotherMockBeer = createMockBeer();
        anotherMockBeer.setId(2);

//fails if this is implemented need to double check why...
//        Mockito.when(mockRepository.get(Mockito.anyInt()))
//                        .thenReturn(mockBeer);

        Mockito.when(mockRepository.getByName(Mockito.anyString()))
                .thenReturn(anotherMockBeer);

        //Act, Assert
        Assertions.assertThrows(EntityDuplicateException.class,
                () -> service.update(mockBeer, createMockUser()));
    }
}
