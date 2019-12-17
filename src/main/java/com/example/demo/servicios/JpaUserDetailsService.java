package com.example.demo.servicios;

import com.example.demo.modelos.dao.IUsuarioDao;
import com.example.demo.modelos.entidad.Role;
import com.example.demo.modelos.entidad.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IUsuarioDao usuarioDao;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByUsername(username);

        if (usuario==null) {
            logger.error("No existe el usuario " + username);
            throw new UsernameNotFoundException("el usuario " + username + " no existe");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role: usuario.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
            logger.info(role.getAuthority());
        }

        if (authorities.isEmpty()) {
            logger.error("El usuario " + username + " no tiene roles");
            throw new UsernameNotFoundException("el usuario " + username + " no tiene roles asignados");
        }
        return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
    }
}
