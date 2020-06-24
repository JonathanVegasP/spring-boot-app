package com.vegasdevelopments.app.repositories;

import com.vegasdevelopments.app.models.App;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AppRepository extends PagingAndSortingRepository<App,Long> {
}
