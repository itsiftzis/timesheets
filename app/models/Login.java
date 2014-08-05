package models;

import play.data.validation.Constraints;

public class Login {

    @Constraints.Required
    public String email;
    @Constraints.Required
    public String password;
}