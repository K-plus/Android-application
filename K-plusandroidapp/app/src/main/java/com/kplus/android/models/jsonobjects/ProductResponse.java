package com.kplus.android.models.jsonobjects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "price",
        "ean",
        "stock"
})
public class ProductResponse
{

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("ean")
    private String ean;
    @JsonProperty("stock")
    private Integer stock;
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
     * @return The name
     */
    @JsonProperty("name")
    public String getName()
    {
        return name;
    }

    /**
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return The price
     */
    @JsonProperty("price")
    public Integer getPrice()
    {
        return price;
    }

    /**
     * @param price The price
     */
    @JsonProperty("price")
    public void setPrice(Integer price)
    {
        this.price = price;
    }

    /**
     * @return The ean
     */
    @JsonProperty("ean")
    public String getEan()
    {
        return ean;
    }

    /**
     * @param ean The ean
     */
    @JsonProperty("ean")
    public void setEan(String ean)
    {
        this.ean = ean;
    }

    /**
     * @return The stock
     */
    @JsonProperty("stock")
    public Integer getStock()
    {
        return stock;
    }

    /**
     * @param stock The stock
     */
    @JsonProperty("stock")
    public void setStock(Integer stock)
    {
        this.stock = stock;
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