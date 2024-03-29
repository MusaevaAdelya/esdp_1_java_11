package com.esdp.demo_esdp.service;

import com.esdp.demo_esdp.entity.Category;
import com.esdp.demo_esdp.entity.Favorites;
import com.esdp.demo_esdp.entity.Product;
import com.esdp.demo_esdp.entity.User;
import com.esdp.demo_esdp.enums.ProductStatus;
import com.esdp.demo_esdp.exception.ProductNotFoundException;
import com.esdp.demo_esdp.repositories.FavoritesRepository;
import com.esdp.demo_esdp.repositories.ImagesRepository;
import com.esdp.demo_esdp.repositories.ProductRepository;
import com.esdp.demo_esdp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class FavoritesServiceTest {

    private final long ID = 1L;

    @Autowired
    FavoritesService favoritesService;

    @MockBean
    FavoritesRepository favoritesRepository;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ImagesRepository imagesRepository;

    @MockBean
    ImagesService imagesService;


    @Test
    void getUsersFav() {
        var user = mock(User.class);
        when(user.getEmail()).thenReturn("test@test.kg");
        var category = mock(Category.class);
        var product = mock(Product.class);
        when(product.getId()).thenReturn(ID);
        when(product.getCategory()).thenReturn(category);
        when(product.getCategory().getName()).thenReturn("test name");
        when(product.getUp()).thenReturn(LocalDateTime.now());
        when(favoritesRepository.getFavProducts(user.getEmail(),ProductStatus.ACCEPTED)).thenReturn(List.of(product));
        var image  = "test/path";
        when(imagesService.getImagesPathsByProductId(product.getId())).thenReturn(List.of(image));
        var  favorites = favoritesService.getUsersFav(user.getEmail());
        assertNotNull(favorites);
        verify(imagesService).getImagesPathsByProductId(product.getId());
        verify(favoritesRepository).getFavProducts(user.getEmail(),ProductStatus.ACCEPTED);
    }

    @Test
    void addToFavorites() throws ProductNotFoundException {
        var user = mock(User.class);
        when(userRepository.findByEmail("test@test.kg")).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn("test@test.kg");
        var product = mock(Product.class);
        when(product.getId()).thenReturn(ID);
        when(productRepository.findById(ID)).thenReturn(Optional.of(product));
        var isAdded = favoritesService.addToFavorites(user.getEmail(),product.getId());
        assertTrue(isAdded);
        verify(userRepository).findByEmail(user.getEmail());
        verify(productRepository).findById(product.getId());

    }

    @Test
    void removeFromFavorites() throws ProductNotFoundException {
        var user = mock(User.class);
        when(userRepository.findByEmail("test@test.kg")).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn("test@test.kg");
        var product = mock(Product.class);
        when(product.getId()).thenReturn(ID);
        when(productRepository.findById(ID)).thenReturn(Optional.of(product));
        var favorite  = mock(Favorites.class);
        when(favoritesRepository.findByUserAndProduct(user,product)).thenReturn(Optional.of(favorite));

        var isRemoved = favoritesService.removeFromFavorites(user.getEmail(),product.getId());
        assertTrue(isRemoved);
        verify(userRepository).findByEmail(user.getEmail());
        verify(productRepository).findById(product.getId());
        verify(favoritesRepository).delete(favorite);
    }


    @Test
    void deleteFavoritesByProductId() {
        var product = mock(Product.class);
        when(product.getId()).thenReturn(ID);
        var favorites = mock(Favorites.class);
        when(favoritesRepository.getFavoritesByProductId(product.getId())).thenReturn(List.of(favorites));

        var isDeleted = favoritesService.deleteFavoritesByProductId(product.getId());
        assertTrue(isDeleted);
    }

    @Test
    void handleFavorites() {
    }
}