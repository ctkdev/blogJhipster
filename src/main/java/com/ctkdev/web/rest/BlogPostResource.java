package com.ctkdev.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ctkdev.domain.BlogPost;
import com.ctkdev.repository.BlogPostRepository;
import com.ctkdev.web.rest.util.PaginationUtil;
import com.ctkdev.web.rest.dto.BlogPostDTO;
import com.ctkdev.web.rest.mapper.BlogPostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing BlogPost.
 */
@RestController
@RequestMapping("/api")
public class BlogPostResource {

    private final Logger log = LoggerFactory.getLogger(BlogPostResource.class);

    @Inject
    private BlogPostRepository blogPostRepository;

    @Inject
    private BlogPostMapper blogPostMapper;

    /**
     * POST  /blogPosts -> Create a new blogPost.
     */
    @RequestMapping(value = "/blogPosts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody BlogPostDTO blogPostDTO) throws URISyntaxException {
        log.debug("REST request to save BlogPost : {}", blogPostDTO);
        if (blogPostDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new blogPost cannot already have an ID").build();
        }
        BlogPost blogPost = blogPostMapper.blogPostDTOToBlogPost(blogPostDTO);
        blogPostRepository.save(blogPost);
        return ResponseEntity.created(new URI("/api/blogPosts/" + blogPostDTO.getId())).build();
    }

    /**
     * PUT  /blogPosts -> Updates an existing blogPost.
     */
    @RequestMapping(value = "/blogPosts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody BlogPostDTO blogPostDTO) throws URISyntaxException {
        log.debug("REST request to update BlogPost : {}", blogPostDTO);
        if (blogPostDTO.getId() == null) {
            return create(blogPostDTO);
        }
        BlogPost blogPost = blogPostMapper.blogPostDTOToBlogPost(blogPostDTO);
        blogPostRepository.save(blogPost);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /blogPosts -> get all the blogPosts.
     */
    @RequestMapping(value = "/blogPosts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<BlogPostDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<BlogPost> page = blogPostRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/blogPosts", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(blogPostMapper::blogPostToBlogPostDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /blogPosts/:id -> get the "id" blogPost.
     */
    @RequestMapping(value = "/blogPosts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BlogPostDTO> get(@PathVariable String id) {
        log.debug("REST request to get BlogPost : {}", id);
        return Optional.ofNullable(blogPostRepository.findOne(id))
            .map(blogPostMapper::blogPostToBlogPostDTO)
            .map(blogPostDTO -> new ResponseEntity<>(
                blogPostDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /blogPosts/:id -> delete the "id" blogPost.
     */
    @RequestMapping(value = "/blogPosts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete BlogPost : {}", id);
        blogPostRepository.delete(id);
    }
}
