package models;

import play.data.validation.Constraints;

public class Login {

    @Constraints.Required(message = "please enter your user name")
    public String userName;
    @Constraints.Required(message = "please enter your password")
    public String password;
}