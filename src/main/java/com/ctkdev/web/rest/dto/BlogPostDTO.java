package com.ctkdev.web.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ctkdev.domain.util.CustomDateTimeDeserializer;
import com.ctkdev.domain.util.CustomDateTimeSerializer;
import org.joda.time.DateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the BlogPost entity.
 */
public class BlogPostDTO implements Serializable {

    private String id;

    private String title;

    private String content;

    private Long user;

    private String excerpt;

    private String status;

    private String postType;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime postDate;

    private Boolean commentsAllowed;

    private String guid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }


    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }


    public DateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(DateTime postDate) {
        this.postDate = postDate;
    }


    public Boolean getCommentsAllowed() {
        return commentsAllowed;
    }

    public void setCommentsAllowed(Boolean commentsAllowed) {
        this.commentsAllowed = commentsAllowed;
    }


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BlogPostDTO blogPostDTO = (BlogPostDTO) o;

        if ( ! Objects.equals(id, blogPostDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BlogPostDTO{" +
                "id=" + id +
                ", title='" + title + "'" +
                ", content='" + content + "'" +
                ", user='" + user + "'" +
                ", excerpt='" + excerpt + "'" +
                ", status='" + status + "'" +
                ", postType='" + postType + "'" +
                ", postDate='" + postDate + "'" +
                ", commentsAllowed='" + commentsAllowed + "'" +
                ", guid='" + guid + "'" +
                '}';
    }
}
