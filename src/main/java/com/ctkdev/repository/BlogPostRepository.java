package com.ctkdev.repository;

import com.ctkdev.domain.BlogPost;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the BlogPost entity.
 */
public interface BlogPostRepository extends MongoRepository<BlogPost,String> {

}
