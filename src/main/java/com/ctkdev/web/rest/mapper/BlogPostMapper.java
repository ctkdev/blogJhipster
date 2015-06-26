package com.ctkdev.web.rest.mapper;

import com.ctkdev.domain.*;
import com.ctkdev.web.rest.dto.BlogPostDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BlogPost and its DTO BlogPostDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BlogPostMapper {

    BlogPostDTO blogPostToBlogPostDTO(BlogPost blogPost);

    BlogPost blogPostDTOToBlogPost(BlogPostDTO blogPostDTO);
}
