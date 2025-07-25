package aom.finsplit.finsplit.entities;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;

    @Size(min = 3, max = 10)
    @NotNull
    private String userName;

    @NotNull
    @Size(min = 6)
    private String email;

    @Size(min = 6)
    @NotNull
    private String password;

    @Min(0)
    private int walletBalance;

    public User() {
    }

    public User(String userName, String password, String email, String walletBalance) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.walletBalance = Integer.parseInt(walletBalance);
    }

    public long getId() {
        return this.id;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public int getWalletBalance() {
        return this.walletBalance;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setWalletBalance(int walletBalance) {
        this.walletBalance = walletBalance;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }
}
