package com.kplus.android.models.jsonobjects;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "firstname",
        "lastname",
        "email",
        "password",
        "status",
        "avatar",
        "createdAt"
})
public class UserResponse
{

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("lastname")
    private String lastname;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("status")
    private String status;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("createdAt")
    private Date createdAt;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The id
     */
    @JsonProperty("id")
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return The firstname
     */
    @JsonProperty("firstname")
    public String getFirstname()
    {
        return firstname;
    }

    /**
     * @param firstname The firstname
     */
    @JsonProperty("firstname")
    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    /**
     * @return The lastname
     */
    @JsonProperty("lastname")
    public String getLastname()
    {
        return lastname;
    }

    /**
     * @param lastname The lastname
     */
    @JsonProperty("lastname")
    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    /**
     * @return The email
     */
    @JsonProperty("email")
    public String getEmail()
    {
        return email;
    }

    /**
     * @param email The email
     */
    @JsonProperty("email")
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * @return The password
     */
    @JsonProperty("password")
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password The password
     */
    @JsonProperty("password")
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * @return The status
     */
    @JsonProperty("status")
    public String getStatus()
    {
        return status;
    }

    /**
     * @param status The status
     */
    @JsonProperty("status")
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * @return The avatar
     */
    @JsonProperty("avatar")
    public String getAvatar()
    {
        return avatar;
    }

    /**
     * @param avatar The avatar
     */
    @JsonProperty("avatar")
    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    /**
     * @return The createdAt
     */
    @JsonProperty("createdAt")
    public Date getCreatedAt()
    {
        return createdAt;
    }

    /**
     * @param createdAt The createdAt
     */
    @JsonProperty("createdAt")
    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties()
    {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value)
    {
        this.additionalProperties.put(name, value);
    }
}

