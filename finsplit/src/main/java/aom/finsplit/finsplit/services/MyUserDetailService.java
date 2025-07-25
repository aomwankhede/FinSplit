package aom.finsplit.finsplit.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import aom.finsplit.finsplit.entities.User;


@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<User> optionUser = userService.getUserFromID(Long.parseLong(id));
        return optionUser.get();
    }

}
