package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.RestaurantCategory;
import br.com.zippydeliveryapi.repository.RestaurantCategoryRepository;
import br.com.zippydeliveryapi.util.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RestaurantCategoryService {

    @Autowired
    private final RestaurantCategoryRepository restaurantCategoryRepository;

    public RestaurantCategoryService(RestaurantCategoryRepository restaurantCategoryRepository) {
        this.restaurantCategoryRepository = restaurantCategoryRepository;
    }

    @Transactional
    public RestaurantCategory save(RestaurantCategory RestaurantCategory) {
        RestaurantCategory.setEnabled(Boolean.TRUE);
        return this.restaurantCategoryRepository.save(RestaurantCategory);
    }

    public List<RestaurantCategory> findAll() {
        return this.restaurantCategoryRepository.findByEnabledTrue();
    }

    public RestaurantCategory findById(Long id) {
        return this.restaurantCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RestaurantCategory", id));
    }

    @Transactional
    public void update(Long id, RestaurantCategory novaCategoria) {
        RestaurantCategory RestaurantCategory = this.findById(id);
        RestaurantCategory.setDescription(novaCategoria.getDescription());
        this.restaurantCategoryRepository.save(RestaurantCategory);
    }

    @Transactional
    public void delete(Long id) {
        RestaurantCategory RestaurantCategory = this.findById(id);
        RestaurantCategory.setEnabled(Boolean.FALSE);
        this.restaurantCategoryRepository.save(RestaurantCategory);
    }
}