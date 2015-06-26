package com.ctkdev.web.rest;

import com.ctkdev.Application;
import com.ctkdev.domain.BlogPost;
import com.ctkdev.repository.BlogPostRepository;
import com.ctkdev.web.rest.mapper.BlogPostMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BlogPostResource REST controller.
 *
 * @see BlogPostResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BlogPostResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_CONTENT = "SAMPLE_TEXT";
    private static final String UPDATED_CONTENT = "UPDATED_TEXT";

    private static final Long DEFAULT_USER = 0L;
    private static final Long UPDATED_USER = 1L;
    private static final String DEFAULT_EXCERPT = "SAMPLE_TEXT";
    private static final String UPDATED_EXCERPT = "UPDATED_TEXT";
    private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_STATUS = "UPDATED_TEXT";
    private static final String DEFAULT_POST_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_POST_TYPE = "UPDATED_TEXT";

    private static final DateTime DEFAULT_POST_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_POST_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_POST_DATE_STR = dateTimeFormatter.print(DEFAULT_POST_DATE);

    private static final Boolean DEFAULT_COMMENTS_ALLOWED = false;
    private static final Boolean UPDATED_COMMENTS_ALLOWED = true;
    private static final String DEFAULT_GUID = "SAMPLE_TEXT";
    private static final String UPDATED_GUID = "UPDATED_TEXT";

    @Inject
    private BlogPostRepository blogPostRepository;

    @Inject
    private BlogPostMapper blogPostMapper;

    private MockMvc restBlogPostMockMvc;

    private BlogPost blogPost;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BlogPostResource blogPostResource = new BlogPostResource();
        ReflectionTestUtils.setField(blogPostResource, "blogPostRepository", blogPostRepository);
        ReflectionTestUtils.setField(blogPostResource, "blogPostMapper", blogPostMapper);
        this.restBlogPostMockMvc = MockMvcBuilders.standaloneSetup(blogPostResource).build();
    }

    @Before
    public void initTest() {
        blogPostRepository.deleteAll();
        blogPost = new BlogPost();
        blogPost.setTitle(DEFAULT_TITLE);
        blogPost.setContent(DEFAULT_CONTENT);
        blogPost.setUser(DEFAULT_USER);
        blogPost.setExcerpt(DEFAULT_EXCERPT);
        blogPost.setStatus(DEFAULT_STATUS);
        blogPost.setPostType(DEFAULT_POST_TYPE);
        blogPost.setPostDate(DEFAULT_POST_DATE);
        blogPost.setCommentsAllowed(DEFAULT_COMMENTS_ALLOWED);
        blogPost.setGuid(DEFAULT_GUID);
    }

    @Test
    public void createBlogPost() throws Exception {
        int databaseSizeBeforeCreate = blogPostRepository.findAll().size();

        // Create the BlogPost
        restBlogPostMockMvc.perform(post("/api/blogPosts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(blogPost)))
                .andExpect(status().isCreated());

        // Validate the BlogPost in the database
        List<BlogPost> blogPosts = blogPostRepository.findAll();
        assertThat(blogPosts).hasSize(databaseSizeBeforeCreate + 1);
        BlogPost testBlogPost = blogPosts.get(blogPosts.size() - 1);
        assertThat(testBlogPost.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBlogPost.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testBlogPost.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testBlogPost.getExcerpt()).isEqualTo(DEFAULT_EXCERPT);
        assertThat(testBlogPost.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBlogPost.getPostType()).isEqualTo(DEFAULT_POST_TYPE);
        assertThat(testBlogPost.getPostDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_POST_DATE);
        assertThat(testBlogPost.getCommentsAllowed()).isEqualTo(DEFAULT_COMMENTS_ALLOWED);
        assertThat(testBlogPost.getGuid()).isEqualTo(DEFAULT_GUID);
    }

    @Test
    public void getAllBlogPosts() throws Exception {
        // Initialize the database
        blogPostRepository.save(blogPost);

        // Get all the blogPosts
        restBlogPostMockMvc.perform(get("/api/blogPosts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(blogPost.getId())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.intValue())))
                .andExpect(jsonPath("$.[*].excerpt").value(hasItem(DEFAULT_EXCERPT.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].postType").value(hasItem(DEFAULT_POST_TYPE.toString())))
                .andExpect(jsonPath("$.[*].postDate").value(hasItem(DEFAULT_POST_DATE_STR)))
                .andExpect(jsonPath("$.[*].commentsAllowed").value(hasItem(DEFAULT_COMMENTS_ALLOWED.booleanValue())))
                .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID.toString())));
    }

    @Test
    public void getBlogPost() throws Exception {
        // Initialize the database
        blogPostRepository.save(blogPost);

        // Get the blogPost
        restBlogPostMockMvc.perform(get("/api/blogPosts/{id}", blogPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(blogPost.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.intValue()))
            .andExpect(jsonPath("$.excerpt").value(DEFAULT_EXCERPT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.postType").value(DEFAULT_POST_TYPE.toString()))
            .andExpect(jsonPath("$.postDate").value(DEFAULT_POST_DATE_STR))
            .andExpect(jsonPath("$.commentsAllowed").value(DEFAULT_COMMENTS_ALLOWED.booleanValue()))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID.toString()));
    }

    @Test
    public void getNonExistingBlogPost() throws Exception {
        // Get the blogPost
        restBlogPostMockMvc.perform(get("/api/blogPosts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBlogPost() throws Exception {
        // Initialize the database
        blogPostRepository.save(blogPost);

		int databaseSizeBeforeUpdate = blogPostRepository.findAll().size();

        // Update the blogPost
        blogPost.setTitle(UPDATED_TITLE);
        blogPost.setContent(UPDATED_CONTENT);
        blogPost.setUser(UPDATED_USER);
        blogPost.setExcerpt(UPDATED_EXCERPT);
        blogPost.setStatus(UPDATED_STATUS);
        blogPost.setPostType(UPDATED_POST_TYPE);
        blogPost.setPostDate(UPDATED_POST_DATE);
        blogPost.setCommentsAllowed(UPDATED_COMMENTS_ALLOWED);
        blogPost.setGuid(UPDATED_GUID);
        restBlogPostMockMvc.perform(put("/api/blogPosts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(blogPost)))
                .andExpect(status().isOk());

        // Validate the BlogPost in the database
        List<BlogPost> blogPosts = blogPostRepository.findAll();
        assertThat(blogPosts).hasSize(databaseSizeBeforeUpdate);
        BlogPost testBlogPost = blogPosts.get(blogPosts.size() - 1);
        assertThat(testBlogPost.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBlogPost.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBlogPost.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testBlogPost.getExcerpt()).isEqualTo(UPDATED_EXCERPT);
        assertThat(testBlogPost.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBlogPost.getPostType()).isEqualTo(UPDATED_POST_TYPE);
        assertThat(testBlogPost.getPostDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_POST_DATE);
        assertThat(testBlogPost.getCommentsAllowed()).isEqualTo(UPDATED_COMMENTS_ALLOWED);
        assertThat(testBlogPost.getGuid()).isEqualTo(UPDATED_GUID);
    }

    @Test
    public void deleteBlogPost() throws Exception {
        // Initialize the database
        blogPostRepository.save(blogPost);

		int databaseSizeBeforeDelete = blogPostRepository.findAll().size();

        // Get the blogPost
        restBlogPostMockMvc.perform(delete("/api/blogPosts/{id}", blogPost.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BlogPost> blogPosts = blogPostRepository.findAll();
        assertThat(blogPosts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
