package Model;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String firstName;
    private String surname;
    private int age;
    private String username;


    public User(String email, String password, String firstName, String surname, int age, String username /*, boolean admin*/) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
    }


        public void setUsername (String username){
            this.username = username;
        }

        public String getUsername () {
            return username;
        }

        public String getEmail () {
            return email;
        }

        public String getPassword () {
            return password;
        }

        public String getFirstName () {
            return firstName;
        }

        public String getSurname () {
            return surname;
        }

        public int getAge () {
            return age;
        }

        public void setEmail (String email){
            this.email = email;
        }

        public void setPassword (String password){
            this.password = password;
        }

        public void setFirstName (String firstName){
            this.firstName = firstName;
        }

        public void setSurname (String surname){
            this.surname = surname;
        }

        public void setAge ( int age){
            this.age = age;
        }

        public boolean equals (User user){
            return (this.age == user.age && this.firstName.equals(user.firstName));
        }

}
