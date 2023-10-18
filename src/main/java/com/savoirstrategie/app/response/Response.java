package com.savoirstrategie.app.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private T data;

    private Integer code;

    private Long allRecords;

    private String message;

    private Boolean success = true;
}
