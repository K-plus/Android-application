package com.kplus.android.models.jsonobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "qty",
        "product"
})

public class CartResponse
{

    @JsonProperty("cart_id")
    private Integer cartId;
    @JsonProperty("total_items")
    private Integer totalItems;
    @JsonProperty("total_price")
    private Integer totalPrice;
    @JsonProperty("cart_lines")
    private ArrayList<CartLineResponse> cartLines;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Integer getCartId()
    {
        return cartId;
    }

    public void setCartId(Integer cartId)
    {
        this.cartId = cartId;
    }

    public Integer getTotalItems()
    {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems)
    {
        this.totalItems = totalItems;
    }

    public Integer getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public ArrayList<CartLineResponse> getCartLines()
    {
        return cartLines;
    }

    public void setCartLines(ArrayList<CartLineResponse> cartLines)
    {
        this.cartLines = cartLines;
    }

    public Map<String, Object> getAdditionalProperties()
    {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }
}
