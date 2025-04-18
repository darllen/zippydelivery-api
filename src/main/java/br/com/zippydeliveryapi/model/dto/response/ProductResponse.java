package br.com.zippydeliveryapi.model.dto.response;

public interface ProductResponse {

    Long getId();
    String getTitle();
    String getImageUrl();
    String getDescription();
    Double getPrice();

}
