package com.example.restapi.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
public class User {

    private Long id;
    @Size(min=2, message = "최솟값은 2 이상 이어야 합니다.")
    private String name;
    @Past
    private Date date;
}
